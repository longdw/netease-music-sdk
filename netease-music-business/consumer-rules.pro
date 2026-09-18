# ============================================
# netease-music-business SDK 混淆规则
# consumer-rules.pro - 会自动合并到使用方的混淆配置中
# ============================================

# ---------- 基础保持规则 ----------
# 保持SDK公开API不被混淆
-keep public class com.smart.neteasemusic.business.** { *; }

# 保持NeSdk入口类
-keep class com.smart.neteasemusic.business.NeSdk { *; }

# 保持所有带有 @Keep 注解的类
-keep @androidx.annotation.Keep class * {*;}
-keepclassmembers class * { @androidx.annotation.Keep *; }

# ---------- ViewModel ----------
-keep class com.smart.neteasemusic.business.base.** { *; }
-keep class com.smart.neteasemusic.business.viewmodel.** { *; }

# 保持ViewModel类
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }

# ---------- 预加载管理 ----------
-keep class com.smart.neteasemusic.business.preload.** { *; }

# ---------- Moshi ----------
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

# ---------- Kotlin 相关 ----------
-keepattributes *Annotation*, InnerClasses

# Kotlin 协程相关
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ---------- Lifecycle ----------
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

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

# ---------- Flow/LiveData ----------
-keep class kotlinx.coroutines.flow.** { *; }
-keepclassmembers class kotlinx.coroutines.flow.** { *; }
-keep class androidx.lifecycle.LiveData { *; }
