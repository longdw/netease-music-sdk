// smart-maven-publish.gradle.kts

import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

// 抽取公共的 repositories 配置，避免重复
fun RepositoryHandler.configureNexus(smartUrl: String, nexusUsername: String, nexusPassword: String) {
    maven {
        url = uri(smartUrl)
        credentials {
            username = nexusUsername
            password = nexusPassword
        }
    }
}

fun MavenPom.configurePom(projectName: String, projectDescription: String?, smartUser: String, smartEmail: String) {
    name.set(projectName)
    description.set(projectDescription)
    url.set("http://gerrit-sejv.smartchina.com.cn/smart/apps/netease-music-sdk")
    licenses {
        license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        }
    }
    developers {
        developer {
            name.set(smartUser)
            email.set(smartEmail)
        }
    }
}

subprojects {
    val isSdkModule = name.startsWith("netease-") || name.startsWith("iotsdk-")
    val isAarModule = name.endsWith("-aar")

    // 防止同一模块被两套配置同时命中
    check(!(isSdkModule && isAarModule)) {
        "Module '$name' matches both sdk and aar publishing rules, please rename it."
    }

    val projectName = name
    val projectDescription = description

    val smartVersion = findProperty("SMART_VERSION") as String
    val smartGroupId = findProperty("SMART_GROUP_ID") as String
    val smartUser = findProperty("SMART_USER") as String
    val smartEmail = findProperty("SMART_EMAIL") as String
    val smartUrl = findProperty("SMART_URL") as String
    val nexusUsername = findProperty("NEXUS_USERNAME") as String
    val nexusPassword = findProperty("NEXUS_PASSWORD") as String

    when {
        isSdkModule -> {
            apply(plugin = "com.android.library")
            apply(plugin = "maven-publish")

//            val javadocJar = tasks.register<Jar>("javadocJar") {
//                archiveClassifier.set("javadoc")
//                dependsOn("dokkaGenerateJavadoc")
//                from(layout.buildDirectory.dir("dokka/javadoc"))
//            }

            afterEvaluate {

                // 在外部脚本中，使用 configure<PublishingExtension> 代替原生的 publishing {} 块
                configure<PublishingExtension> {
                    publications {
                        create<MavenPublication>("maven") {
                            from(components["release"])
                            groupId = smartGroupId
                            artifactId = projectName
                            version = smartVersion
                            pom {
                                configurePom(projectName, projectDescription, smartUser, smartEmail)
                            }

//                            // 附加 javadoc jar，方便 IDE 和 Maven 仓库展示文档
//                            artifact(javadocJar)
                        }
                    }
                    repositories { configureNexus(smartUrl, nexusUsername, nexusPassword) }
                }
            }
        }

        isAarModule -> {
            apply(plugin = "maven-publish")

            afterEvaluate {
                // 容错：找到唯一的 aar 文件，否则给出明确错误
                val aarFiles = fileTree(projectDir) { include("*.aar") }.files
                check(aarFiles.size == 1) {
                    "Module '$name' should contain exactly 1 aar file, but found ${aarFiles.size}."
                }

                configure<PublishingExtension> {
                    publications {
                        create<MavenPublication>("maven") {
                            artifact(aarFiles.single())
                            groupId = smartGroupId
                            artifactId = projectName
                            version = smartVersion
                            pom {
                                packaging = "aar"
                                configurePom(projectName, projectDescription, smartUser, smartEmail)
                            }
                        }
                    }
                    repositories { configureNexus(smartUrl, nexusUsername, nexusPassword) }
                }
            }
        }
    }
}