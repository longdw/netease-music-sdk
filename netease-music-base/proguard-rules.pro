## 保持SDK公开API不被混淆
-keep public class com.smart.neteasemusic.sdk.base.** {
    public protected *;
}

# 优化时不警告
-dontnote com.smart.neteasemusic.sdk.base.**

-dontwarn java.lang.invoke.StringConcatFactory