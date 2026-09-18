package com.smart.neteasemusic.business.recorder

import com.smart.neteasemusic.sdk.base.network.ApiResult
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 数据回传接口
 * @author david.long 2026/4/21 14:03
 */
interface DataRecorderApi {

    /**
     * 开始播放数据回传
     * @param id 当前资源id, 例如：歌曲的id，声音的id或者encVoiceId
     * @param action 行为，startplay:开始播放
     * @param bitrate 码率，单位/kbps。song传实际播放码率，128：标准，320：极高，999：无损，1999：hires，高清臻音：3999，超清母带：4999，沉浸环绕声：4099，杜比全景声：2999，臻音全景声（vivid）：5999，播客固定传128
     * @param file 文件类型回传枚举值：0：下载文件， 1：本地文件， 2：缓存文件， 3：云盘文件， 4：线上文件。不清楚填4
     * @param type 类型，song：歌曲, dj：播客
     * @param startLogTime 开始播放时的时间戳，单位：毫秒
     * @param alg 算法使用，私人漫游、每日推荐和场景音乐已返回该字段，产生播放时需透传alg（其余场景可参考下方，也可不传）
     * @param sourceId 播放来源资源ID（最近常听必传）
     * @param sourceType 播放来源资源类型（最近常听必传）
     * @param categoryId 播客categoryId
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/play/data/record")
    suspend fun recordStart(
        @Field("id") id: String,
        @Field("action") action: String = "startplay",
        @Field("bitrate") bitrate: Int,
        @Field("file") file: Int = 4,
        @Field("type") type: String = "song",
        @Field("startLogTime") startLogTime: Long,
        @Field("alg") alg: String? = null,
        @Field("sourceId") sourceId: String? = null,
        @Field("sourceType") sourceType: String? = null,
        @Field("categoryId") categoryId: String? = null,
    ): ApiResult<Any>

    /**
     * 结束播放数据回传
     * @param time 播放时长，单位：秒， 播放完成或者中止播放时的时长，按照实际播放时长统计，不包含暂停，不能超过歌曲总时长
     * @param end 结束方式回传枚举值：playend：正常结束（歌曲播放到结尾自然结束；系统自动触发上报）；interrupt：第三方APP打断（第三方应用抢占音频焦点；系统通知或电话等打断播放）； exception：错误（网络错误导致播放失败；歌曲文件损坏；其他技术异常）； ui: 用户切歌（用户主动切换到下一首/上一首；用户切换播放列表；用户点击指定歌曲播放；用户暂停播放）。不清楚填playend
     */
    @FormUrlEncoded
    @POST("/openapi/music/basic/play/data/record")
    suspend fun recordEnd(
        @Field("id") id: String,
        @Field("action") action: String = "play",
        @Field("bitrate") bitrate: Int,
        @Field("file") file: Int = 4,
        @Field("type") type: String = "song",
        @Field("startLogTime") startLogTime: Long,
        @Field("time") time: Double? = null,
        @Field("end") end: String? = null,
        @Field("alg") alg: String? = null,
        @Field("sourceId") sourceId: String? = null,
        @Field("sourceType") sourceType: String? = null,
        @Field("categoryId") categoryId: String? = null,
    ): ApiResult<Any>
}