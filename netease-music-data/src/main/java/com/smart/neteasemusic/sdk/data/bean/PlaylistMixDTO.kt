package com.smart.neteasemusic.sdk.data.bean

/**
 * 日推mix
 * @author david.long 2026/4/27 15:53
 */
data class PlaylistMixResultDTO(
    val blocks: List<PlaylistMixDTO>?
)

data class PlaylistMixDTO(
    val title: String?,
    val subTitle: String?,
    val imageInfo: ImageInfoDTO?,
    val resourceId: String?,
    val resourceType: String?,
    val extData: ExtDataDTO?
)

data class ImageInfoDTO(
    //图片地址
    val url: String?,
    //附加在图上的图标
    val onImageIconUrl: String?
)

data class ExtDataDTO(
    val playlist: String?,
    val songId: String?,
    val tagId: String?,
    val categoryId: String?
)