#include <jni.h>
#include <string>
#include <vector>

namespace {

    const std::vector<std::string>& authorizedModels() {
        static const std::vector<std::string> models = {
                "HY11_PHEV",
                "HS11_P",
                "HY11_CN",
                "E371",
                "HY11",
                "sdk_gphone64_arm64"
        };
        return models;
    }

    const std::vector<std::string>& authorizedPackages() {
        static const std::vector<std::string> packages = {
                "com.smart.dhu.netease",
                "com.geely.auto.music"
        };
        return packages;
    }

    const std::vector<std::string>& authorizedExpiredPackages() {
        static const std::vector<std::string> packages = {
                "com.geely.auto.music"
        };
        return packages;
    }

    // 截止日期：2026-09-01 00:00:00
    // 使用 tm 结构体构造这个时间点，再转成 time_t 用于比较
    bool isBeforeDeadline() {
        std::tm deadlineTm = {};
        deadlineTm.tm_year = 2026 - 1900; // tm_year 是从1900年开始计数
        deadlineTm.tm_mon = 9 - 1;        // tm_mon 是从0开始计数，9月即索引8
        deadlineTm.tm_mday = 1;
        deadlineTm.tm_hour = 0;
        deadlineTm.tm_min = 0;
        deadlineTm.tm_sec = 0;

        std::time_t deadlineTime = std::mktime(&deadlineTm);
        std::time_t nowTime = std::time(nullptr);

        // mktime 失败会返回 -1，做个兜底，异常情况直接判定为不在有效期内
        if (deadlineTime == static_cast<std::time_t>(-1) ||
            nowTime == static_cast<std::time_t>(-1)) {
            return false;
        }

        return nowTime < deadlineTime;
    }

} // namespace

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_smart_neteasemusic_sdk_base_common_SdkAuthorized_nativeIsAuthorizedModel(JNIEnv *env, jobject /* this */, jstring model) {
    if (model == nullptr) return JNI_FALSE;

    const char *cModel = env->GetStringUTFChars(model, nullptr);
    std::string modelStr(cModel);
    env->ReleaseStringUTFChars(model, cModel);

    for (const auto &m : authorizedModels()) {
        if (m == modelStr) {
            return JNI_TRUE;
        }
    }
    return JNI_FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_smart_neteasemusic_sdk_base_common_SdkAuthorized_nativeIsAuthorizedPackage(JNIEnv *env, jobject /* this */, jstring packageName) {
    if (packageName == nullptr) return JNI_FALSE;

    const char *cPkg = env->GetStringUTFChars(packageName, nullptr);
    std::string pkgStr(cPkg);
    env->ReleaseStringUTFChars(packageName, cPkg);

    for (const auto &p : authorizedPackages()) {
        if (p == pkgStr) {
            return JNI_TRUE;
        }
    }
    return JNI_FALSE;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_smart_neteasemusic_sdk_base_common_SdkAuthorized_nativeIsWithinValidPeriod(JNIEnv *env, jobject /* this */, jstring packageName) {

    const char *cPkg = env->GetStringUTFChars(packageName, nullptr);
    std::string pkgStr(cPkg);
    env->ReleaseStringUTFChars(packageName, cPkg);

    bool needCheckDate = false;
    for (const auto &p : authorizedExpiredPackages()) {
        if (p == pkgStr) {
            needCheckDate = true;
            break;
        }
    }

    if (!needCheckDate) {
        return JNI_TRUE;
    }

    return isBeforeDeadline() ? JNI_TRUE : JNI_FALSE;
}