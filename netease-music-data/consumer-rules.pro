# ============================================
# netease-music-data SDK 混淆规则
# consumer-rules.pro - 会自动合并到使用方的混淆配置中
# ============================================

# ---------- 基础保持规则 ----------
# 保持SDK公开API不被混淆
-keep public class com.smart.neteasemusic.sdk.data.** { *; }

# 保持所有Bean类(数据类)不被混淆，确保Moshi序列化正常
-keep class com.smart.neteasemusic.sdk.data.bean.** { *; }

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

# ---------- Moshi ----------
-keep class com.squareup.moshi.** { *; }
-keep class com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
-keep class com.squareup.moshi.kotlin.reflect.*KotlinJsonAdapter
-keep class com.squareup.moshi.kotlin.reflect.*KotlinJsonAdapter$*
-dontwarn com.squareup.moshi.**

# ---------- Kotlin 相关 ----------
-keepattributes *Annotation*, InnerClasses

# Kotlin 协程相关
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ---------- ZXing 二维码 ----------
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

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
