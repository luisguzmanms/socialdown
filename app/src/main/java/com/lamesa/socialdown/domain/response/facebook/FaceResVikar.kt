import com.google.gson.annotations.SerializedName

/** Created by luis Mesa on 09/08/22 */
data class FaceResVikar(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null,
    @SerializedName("links") var links: Links? = Links()
)

data class Links(
    @SerializedName("Download Low  Quality") var DownloadLowQuality: String? = null,
    @SerializedName("Download High Quality") var DownloadHighQuality: String? = null
)
