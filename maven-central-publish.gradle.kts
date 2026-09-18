// smart-maven-publish.gradle.kts

import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

var signingKeyId = ""//签名的密钥后8位
var signingPassword = ""//签名设置的密码
var secretKeyRingFile = ""//生成的secring.gpg文件目录
var ossrhUsername = ""//sonatype用户名
var ossrhPassword = "" //sonatype密码

val localProperties: File = project.rootProject.file("local.properties")

if (localProperties.exists()) {
    println("Found secret props file, loading props")
    val properties = Properties()

    InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
        properties.load(reader)
    }
    signingKeyId = properties.getProperty("signing.keyId")
    signingPassword = properties.getProperty("signing.password")
    secretKeyRingFile = properties.getProperty("signing.secretKeyRingFile")
    ossrhUsername = properties.getProperty("ossrhUsername")
    ossrhPassword = properties.getProperty("ossrhPassword")

} else {
    println("No props file, loading env vars")
}

gradle.taskGraph.whenReady {
    if (allTasks.any { it is Sign }) {

        allprojects {
            extra["signing.keyId"] = signingKeyId
            extra["signing.secretKeyRingFile"] = secretKeyRingFile
            extra["signing.password"] = signingPassword
        }
    }
}

// 抽取公共的 repositories 配置，避免重复
fun RepositoryHandler.configureNexus() {
    maven {
        name = "Sonatype"
        url = uri("${rootProject.buildDir}/staging-deploy")
//        url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
//        credentials {
//            username = ossrhUsername
//            password = ossrhPassword
//        }
    }
}

fun MavenPom.configurePom(projectName: String, projectDescription: String?, smartUser: String, smartEmail: String) {
    name.set(projectName)
    description.set(projectDescription)
    url.set("https://github.com/longdw/netease-music-sdk.git")
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
    scm {
        connection.set("scm:git:https://github.com/longdw/netease-music-sdk.git")
        developerConnection.set("scm:git:ssh://github.com/longdw/netease-music-sdk.git")
        url.set("https://github.com/longdw/netease-music-sdk")
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
    val projectDescription = "Android SDK for Netease Music integration"

    val smartVersion = findProperty("SMART_VERSION") as String
    val smartGroupId = findProperty("SMART_GROUP_ID") as String
    val smartUser = findProperty("SMART_USER") as String
    val smartEmail = findProperty("SMART_EMAIL") as String

    when {
        isSdkModule -> {
            apply(plugin = "com.android.library")
            apply(plugin = "maven-publish")
            apply(plugin = "signing")

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
                        }
                    }
                    repositories { configureNexus() }
                }

                // 2. 配置签名逻辑
                configure<SigningExtension> {
                    val publication = extensions.getByType<PublishingExtension>().publications["maven"]
                    sign(publication)
                }
            }
        }

        isAarModule -> {
            apply(plugin = "maven-publish")
            apply(plugin = "signing")

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
                                configurePom(projectName, projectDescription, smartUser, smartEmail)
                            }
                        }
                    }
                    repositories { configureNexus() }
                }

                // 2. 配置签名逻辑
                configure<SigningExtension> {
                    val publication = extensions.getByType<PublishingExtension>().publications["maven"]
                    sign(publication)
                }
            }
        }
    }
}