package com.lamesa.socialdown.domain.response.tiktok

import com.google.gson.annotations.SerializedName

/** Created by luis Mesa on 09/08/22 */
data class TiktokResYi005(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("msg") var msg: String? = null,
    @SerializedName("processed_time") var processedTime: Double? = null,
    @SerializedName("data") var data: Data? = Data()
)

data class Data(
    @SerializedName("aweme_id") var awemeId: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("region") var region: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("cover") var cover: String? = null,
    @SerializedName("origin_cover") var originCover: String? = null,
    @SerializedName("play") var linkDownloadMp4: String? = null,
    @SerializedName("wmplay") var wmplay: String? = null,
    @SerializedName("music") var linkDownloadMp3: String? = null,
    @SerializedName("music_info") var musicInfo: MusicInfo = MusicInfo(),
    @SerializedName("play_count") var playCount: Int? = null,
    @SerializedName("digg_count") var diggCount: Int? = null,
    @SerializedName("comment_count") var commentCount: Int? = null,
    @SerializedName("share_count") var shareCount: Int? = null,
    @SerializedName("download_count") var downloadCount: Int? = null,
    @SerializedName("create_time") var createTime: Int? = null,
    @SerializedName("author") var author: Author = Author()
)

data class MusicInfo(
    @SerializedName("id") var id: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("play") var play: String? = null,
    @SerializedName("cover") var cover: String? = null,
    @SerializedName("author") var author: String? = null,
    @SerializedName("original") var original: Boolean? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("album") var album: String? = null
)

data class Author(
    @SerializedName("id") var id: String? = null,
    @SerializedName("unique_id") var uniqueId: String? = null,
    @SerializedName("nickname") var nickname: String? = null,
    @SerializedName("avatar") var avatar: String? = null
)