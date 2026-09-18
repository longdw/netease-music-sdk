# ============================================
# netease-music-player 模块自身构建混淆规则
# proguard-rules.pro - SDK自身构建时使用
# ============================================

# 继承consumer-rules.pro的所有规则
# (通过build.gradle.kts中的consumerProguardFiles自动合并)

## 保持SDK公开API不被混淆
-keep public class com.smart.neteasemusic.sdk.player.** {
    public protected *;
}

# 优化时不警告
-dontnote com.smart.neteasemusic.sdk.player.**
