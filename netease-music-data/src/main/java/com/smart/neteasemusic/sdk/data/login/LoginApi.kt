package com.smart.neteasemusic.sdk.data.login

import com.smart.neteasemusic.sdk.base.network.ApiResult
import com.smart.neteasemusic.sdk.base.oauth.OAuthConst
import com.smart.neteasemusic.sdk.data.bean.QRCodeDTO
import com.smart.neteasemusic.sdk.data.bean.QRCodeStatusDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author david.long 2026/1/9 15:45
 */
interface LoginApi {

    /**
     * 获取登录二维码
     * @param type - 固定传2
     * expiredKey - 固定传300
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/user/oauth2/qrcodekey/get/v2")
    suspend fun fetchLoginQRCode(
        @Field("type") type: Int = 2,
        @Field("expiredKey") expiredKey: String = "300"
    ): ApiResult<QRCodeDTO>

    /**
     * 轮询获取二维码状态
     * @param key - 二维码的uniKey
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/oauth2/device/login/qrcode/get")
    suspend fun fetchQRCodeStatus(
        @Field("key") key: String,
        @Field("clientId") clientId: String = OAuthConst.appId
    ): ApiResult<QRCodeStatusDTO>
}