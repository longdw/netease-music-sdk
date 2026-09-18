package com.smart.neteasemusic.sdk.data.bean

/**
 * @author david.long 2026/6/1 13:52
 */
data class ArtistZoneDTO(
    val areaTabList: List<AreaTabDTO>,
    val bandTabList: List<BandTabDTO>
)

data class AreaTabDTO(
    val name: String,
    val tab: String
)

data class BandTabDTO(
    val name: String,
    val tab: String
)

