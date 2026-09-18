# ============================================
# iotsdk-extension SDK 混淆规则
# consumer-rules.pro - 会自动合并到使用方的混淆配置中
# ============================================

# ---------- 基础保持规则 ----------
# 保持SDK公开API不被混淆
-keep public class com.netease.android.extension.** { *; }

# 保持所有带有 @Keep 注解的类
-keep @androidx.annotation.Keep class * {*;}
-keepclassmembers class * { @androidx.annotation.Keep *; }

# ---------- Kotlin 相关 ----------
-keepattributes *Annotation*, InnerClasses

# Kotlin 协程相关
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ---------- 枚举类 ----------
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ---------- Serializable ----------
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
