# ============================================
# netease-music-player SDK 混淆规则
# consumer-rules.pro - 会自动合并到使用方的混淆配置中
# ============================================

# ---------- 基础保持规则 ----------
# 保持SDK公开API不被混淆
-keep public class com.smart.neteasemusic.sdk.player.** { *; }

# 保持GlobalPlayManager(核心播放管理器)
-keep class com.smart.neteasemusic.sdk.player.GlobalPlayManager { *; }

# 保持所有Bean类(数据类)不被混淆
-keep class com.smart.neteasemusic.sdk.player.bean.** { *; }
-keep class com.smart.neteasemusic.sdk.data.bean.MusicInfo { *; }

# 保持所有带有 @Keep 注解的类
-keep @androidx.annotation.Keep class * {*;}
-keepclassmembers class * { @androidx.annotation.Keep *; }

# ---------- Room 数据库 ----------
-keep class com.smart.neteasemusic.sdk.player.repository.** { *; }
-keep class com.smart.neteasemusic.sdk.player.repository.dao.** { *; }

# Room 核心
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Room Entity 和 DAO
-keep @androidx.room.Entity class * { *; }
-keepclassmembers @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep @androidx.room.Database class * { *; }
-keep @androidx.room.TypeConverter class * { *; }
-keep @androidx.room.TypeConverters class * { *; }

# Room 生成的实现类
-keep class **_Impl { *; }
-keep class **_Factory { *; }

# ---------- ExoPlayer / Media3 ----------
-keep class androidx.media3.** { *; }
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn androidx.media3.**
-dontwarn com.google.android.exoplayer2.**

# ---------- Moshi ----------
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

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

# ---------- Flow/LiveData ----------
-keep class kotlinx.coroutines.flow.** { *; }
-keepclassmembers class kotlinx.coroutines.flow.** { *; }

# ---------- Parcelable ----------
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
