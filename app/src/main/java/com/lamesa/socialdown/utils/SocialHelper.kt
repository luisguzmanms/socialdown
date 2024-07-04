package com.lamesa.socialdown.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.webkit.URLUtil
import com.lamesa.socialdown.R
import com.lamesa.socialdown.app.SDApp
import com.lamesa.socialdown.app.SDApp.Context.resources
import com.lamesa.socialdown.data.remote.APIHelper
import com.lamesa.socialdown.domain.model.api.ModelMediaDataExtracted
import com.lamesa.socialdown.downloader.DownloaderHelper
import com.lamesa.socialdown.utils.Constansts.Code.attemptsCode
import com.lamesa.socialdown.utils.Constansts.SHARE_APP_REQUEST_CODE
import com.lamesa.socialdown.utils.DialogXUtils.NotificationX.showError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Random
import java.util.TimeZone
import java.util.UUID

object SocialHelper {

    enum class MediaType(var type: String, var icon: Int) {
        REEL("REEL", R.drawable.ic_reel),
        STORY("STORY", R.drawable.ic_story),
        VIDEO("VIDEO", R.drawable.ic_video),
        POST("POST", R.drawable.ic_image),
        IGTV("IGTV", R.drawable.ic_video),
        IMAGE("IMAGE", R.drawable.ic_image),
        NONE("", 0)
    }

    enum class AppDownloader(
        val app: String,
        val mediaSupported: String,
        val background: Int,
        val iconOn: Int,
        val iconOff: Int
    ) {
        FACEBOOK(
            "FACEBOOK", resources.getString(R.string.desc_facebook),
            R.drawable.gradiant_facebook,
            R.drawable.ic_facebook,
            R.drawable.ic_facebook_off
        ),
        INSTAGRAM(
            "INSTAGRAM",
            resources.getString(R.string.desc_instagram),
            R.drawable.gradiant_instagram,
            R.drawable.ic_instagram,
            R.drawable.ic_instagram_off
        ),
        TIKTOK(
            "TIKTOK",
            resources.getString(R.string.desc_tiktok),
            R.drawable.gradiant_tiktok,
            R.drawable.ic_tiktok,
            R.drawable.ic_tiktok_off
        ),
    }

    internal fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        showError(context.getString(R.string.text_NoInternetConnection))
        return false
    }

    fun checkAppTypeUrl(url: String): APIHelper.AppApi {

        val urlsTiktok = arrayListOf<String>()
        urlsTiktok.add("https://www.tiktok.com/")
        urlsTiktok.add("https://vm.tiktok.com/")
        urlsTiktok.add("https://www.vm.tiktok.com/")
        urlsTiktok.add("https://vt.tiktok.com/")
        urlsTiktok.add("https://www.vt.tiktok.com/")
        urlsTiktok.add("tiktok.com")

        val urlsInstagram = arrayListOf<String>()
        urlsInstagram.add("https://www.instagram.com/")
        urlsInstagram.add("https://instagram.com/")

        val urlsFacebook = arrayListOf<String>()
        urlsFacebook.add("https://fb.watch/")
        urlsFacebook.add("https://www.facebook.com/watch?v=")
        urlsFacebook.add("https://www.facebook.com/reel/")
        urlsFacebook.add("https://facebook.com/watch?v=")
        urlsFacebook.add("https://www.facebook.com/")

        for (x in urlsTiktok) {
            if (url.contains(x)) {
                return APIHelper.AppApi.TIKTOK
            }
        }

        for (x in urlsInstagram) {
            if (url.contains(x)) {
                return APIHelper.AppApi.INSTAGRAM
            }
        }

        for (x in urlsFacebook) {
            if (url.contains(x)) {
                return APIHelper.AppApi.FACEBOOK
            }
        }

        return APIHelper.AppApi.NONE
    }

    fun checkTypeMedia(url: String): MediaType {

        if (url.contains("/tv/")) return MediaType.IGTV
        else if (url.contains("/reel/")) return MediaType.REEL
        else if (url.contains("/s/")) return MediaType.STORY
        else if (url.contains("/stories/")) return MediaType.STORY
        else if (url.contains("/p/")) return MediaType.POST
        else if (
            url.contains("/watch") ||
            url.contains("tiktok.com") ||
            url.contains("/fb.watch/") ||
            url.contains("/video/") ||
            url.contains("/videos/")
        )
            return MediaType.VIDEO

        return MediaType.NONE
    }

    /**
     * Algunos enlaces de instagram contienes una cadena de texto "?igshid" que provoa error en la APi
     */
    internal fun removeIgshid(url: String): String {
        // Separa la cadena por el carácter "?" y toma la primera parte de la lista resultante
        val parts = url.split("?")
        return parts[0]
    }

    internal fun randomString(): String {
        val id = UUID.randomUUID().toString().substring(0, 8)
        return (id)
    }

    internal fun checkExtensionFile(url: String?): DownloaderHelper.ExtensionFile {
        if (url?.contains("jpg") == true || url?.contains("jpeg") == true) {
            return DownloaderHelper.ExtensionFile.JPG
        } else if (url?.contains("mp4") == true) {
            return DownloaderHelper.ExtensionFile.MP4
        }
        return DownloaderHelper.ExtensionFile.MP4
    }

    internal fun chooseRandomString(input: String): String {
        // Divide la cadena de texto por el separador ","
        val strings = input.split(",")
        // Selecciona un elemento aleatorio de la lista
        return strings[Random().nextInt(strings.size)].trim()
    }

    internal fun searchByLink(context: Context, queryLink: String) {
        if (isOnline(context)) {
            if (URLUtil.isNetworkUrl(queryLink)) {
                when (checkAppTypeUrl(queryLink)) {
                    APIHelper.AppApi.TIKTOK -> APIHelper.executeApi(
                        context,
                        APIHelper.AppApi.TIKTOK, queryLink
                    )
                    APIHelper.AppApi.INSTAGRAM -> APIHelper.executeApi(
                        context,
                        APIHelper.AppApi.INSTAGRAM, queryLink
                    )
                    APIHelper.AppApi.FACEBOOK -> APIHelper.executeApi(
                        context,
                        APIHelper.AppApi.FACEBOOK, queryLink
                    )
                    else -> showError(context.getString(R.string.text_linkNoSupported) + " : " + queryLink)
                }
            } else {
                showError(context.getString(R.string.text_linkNotValid) + " : $queryLink")
                SDAd().showInterAd(context)
            }
        } else {
            showError(context.getString(R.string.text_NoInternetConnection))
        }
    }

    fun shareApp(context: Activity) {
        val incentive = context.getString(R.string.text_shareApp)
        val downloadLink = "https://rebrand.ly/shareSocialDown"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "$incentive\n$downloadLink")
        context.startActivityForResult(
            Intent.createChooser(shareIntent, "Share SocialDown"),
            SHARE_APP_REQUEST_CODE
        )
    }

    // detectar tipo de error e intentar nuevamente hasta 3 veces
    fun checkCodeResponse(context: Context, dataExtracted: ModelMediaDataExtracted) {
        if (attemptsCode < 5) {
            searchByLink(context, dataExtracted.queryLink!!)
            attemptsCode += 1
        } else if (attemptsCode > 5) {
            showError("Plese try again later --Error code: ${dataExtracted.codeResponse}")
            attemptsCode = 0
        }
    }

    fun updateDailyDownloadsCount(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            val defaultTimeZoneID = TimeZone.getDefault().id
            val currentDateTime = WorldTimeUtil.fetchCurrentDateTime(defaultTimeZoneID)

            if (currentDateTime != null) {
                val currentDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
                val lastDownloadDay = SDApp.Context.tinyDB.getInt(Constansts.TinyDB.TB_LAST_DOWNLOAD_DAY_KEY)

                if (currentDay != lastDownloadDay) {
                    SDApp.Context.tinyDB.putInt(Constansts.TinyDB.TB_DOWNLOADS_COUNT_KEY, 1)
                    SDApp.Context.tinyDB.putInt(Constansts.TinyDB.TB_LAST_DOWNLOAD_DAY_KEY, currentDay)
                } else {
                    val currentCount = SDApp.Context.tinyDB.getInt(Constansts.TinyDB.TB_DOWNLOADS_COUNT_KEY)

                    if (currentCount >= Constansts.TinyDB.TB_MAX_DOWNLOADS_PER_DAY) {
                        DialogXUtils().dialogLimitDownloads(context = context)
                    } else {
                        SDApp.Context.tinyDB.putInt(Constansts.TinyDB.TB_DOWNLOADS_COUNT_KEY, currentCount + 1)
                    }
                }
            }
        }
    }

}
