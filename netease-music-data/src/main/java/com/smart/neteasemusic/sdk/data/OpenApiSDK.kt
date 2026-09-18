package com.smart.neteasemusic.sdk.data

import com.smart.neteasemusic.sdk.data.album.AlbumApiImpl
import com.smart.neteasemusic.sdk.data.artist.ArtistApiImpl
import com.smart.neteasemusic.sdk.data.fm.FmApiImpl
import com.smart.neteasemusic.sdk.data.hifi.HiFiApiImpl
import com.smart.neteasemusic.sdk.data.history.HistoryApiImpl
import com.smart.neteasemusic.sdk.data.login.LoginApiImpl
import com.smart.neteasemusic.sdk.data.playlist.PlaylistApiImpl
import com.smart.neteasemusic.sdk.data.podcast.PodcastApiImpl
import com.smart.neteasemusic.sdk.data.purchased.PurchasedApiImpl
import com.smart.neteasemusic.sdk.data.recommend.RecommendApiImpl
import com.smart.neteasemusic.sdk.data.search.SearchApiImpl
import com.smart.neteasemusic.sdk.data.sing.FreeSingApiImpl
import com.smart.neteasemusic.sdk.data.song.SongApiImpl
import com.smart.neteasemusic.sdk.data.user.UserApiImpl

/**
 * @author david.long 2026/1/13 18:58
 */
object OpenApiSDK {

    private val userApiImpl by lazy {
        UserApiImpl()
    }

    private val recommendApiImpl by lazy {
        RecommendApiImpl()
    }

    private val loginApiImpl by lazy {
        LoginApiImpl()
    }

    private val searchApiImpl by lazy {
        SearchApiImpl()
    }

    private val playlistApiImpl by lazy {
        PlaylistApiImpl()
    }
    private val podcastApiImpl by lazy {
        PodcastApiImpl()
    }

    private val songApiImpl by lazy {
        SongApiImpl()
    }

    private val hifiApiImpl by lazy {
        HiFiApiImpl()
    }

    private val albumApiImpl by lazy {
        AlbumApiImpl()
    }

    private val historyApiImpl by lazy {
        HistoryApiImpl()
    }

    private val artistApiImpl by lazy {
        ArtistApiImpl()
    }
    private val purchasedApiImpl by lazy {
        PurchasedApiImpl()
    }

    fun getUserApi(): UserApiImpl {
        return userApiImpl
    }
    fun getPurchasedApi(): PurchasedApiImpl{
        return purchasedApiImpl
    }

    fun getRecommendApi(): RecommendApiImpl {
        return recommendApiImpl
    }

    fun getLoginApi(): LoginApiImpl {
        return loginApiImpl
    }

    fun getSearchApi():SearchApiImpl {
        return searchApiImpl
    }

    fun getPlaylistApi(): PlaylistApiImpl {
        return playlistApiImpl
    }

    fun getSongApi(): SongApiImpl {
        return songApiImpl
    }

    fun getPodcastApi(): PodcastApiImpl {
        return podcastApiImpl
    }

    fun getFmApi(): FmApiImpl {
        return FmApiImpl()
    }

    fun getHiFiApi(): HiFiApiImpl{
        return hifiApiImpl
    }

    fun getSingApi(): FreeSingApiImpl{
        return FreeSingApiImpl()
    }

    fun getAlbumApi(): AlbumApiImpl {
        return albumApiImpl
    }

    fun getHistoryApi(): HistoryApiImpl {
        return historyApiImpl
    }

    fun getArtistApi(): ArtistApiImpl {
        return artistApiImpl
    }
}