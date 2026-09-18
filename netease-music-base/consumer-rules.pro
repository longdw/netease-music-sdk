# ============================================
# netease-music-base SDK 混淆规则
# consumer-rules.pro - 会自动合并到使用方的混淆配置中
# ============================================

# ---------- 基础保持规则 ----------
# 保持SDK公开API不被混淆
-keep public class com.smart.neteasemusic.sdk.base.** { *; }

# 保持所有Bean类(数据类)不被混淆，确保Moshi序列化正常
-keep class com.smart.neteasemusic.sdk.base.bean.** { *; }
-keep class com.squareup.moshi.** { *; }
-keep class com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
-keep class com.squareup.moshi.kotlin.reflect.*KotlinJsonAdapter
-keep class com.squareup.moshi.kotlin.reflect.*KotlinJsonAdapter$*
-dontwarn com.squareup.moshi.**

# 保持SDK初始化回调
-keep class com.smart.neteasemusic.sdk.base.SdkInitCallback { *; }
-keep interface com.smart.neteasemusic.sdk.base.SdkInitCallback { *; }

# 保持所有带有 @Keep 注解的类
-keep @androidx.annotation.Keep class * {*;}
-keepclassmembers class * { @androidx.annotation.Keep *; }

# 保持所有带有 @JsonClass 注解的类(Moshi)
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepclassmembers @com.squareup.moshi.JsonClass class * { *; }

# 保持所有带有 @Json 注解的字段
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}

# ---------- Kotlin 相关 ----------
# 保持Kotlin元数据
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# Kotlin 协程相关
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ---------- 网络相关 ----------
# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Retrofit (如果使用)
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# ---------- MMKV ----------
-keep class com.tencent.mmkv.** { *; }
-dontwarn com.tencent.mmkv.**

# ---------- Logger ----------
-keep class com.smart.logger.** { *; }
-dontwarn com.smart.logger.**

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

-dontwarn java.lang.invoke.StringConcatFactory
