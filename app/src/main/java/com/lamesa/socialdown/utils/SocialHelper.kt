package com.lamesa.socialdown.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.lamesa.socialdown.R
import com.lamesa.socialdown.data.remote.APIHelper
import java.util.*

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

    enum class AppTypeIconOn(var icon: Int) {
        INSTAGRAM(R.drawable.ic_instagram),
        FACEBOOK(R.drawable.ic_facebook),
        TIKTOK(R.drawable.ic_tiktok),
        NONE(0)
    }

    enum class AppTypeIconOff(var icon: Int) {
        INSTAGRAM(R.drawable.ic_instagram_off),
        FACEBOOK(R.drawable.ic_facebook_off),
        TIKTOK(R.drawable.ic_tiktok_off)
    }

    fun isOnline(context: Context): Boolean {
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
        else if (url.contains("/p/")) return MediaType.POST
        else if (
            url.contains("/watch") ||
            url.contains("tiktok.com") ||
            url.contains("/fb.watch/") ||
            url.contains("/video/")
        )
            return MediaType.VIDEO

        return MediaType.NONE
    }

    internal fun randomString(): String {
        val id = UUID.randomUUID().toString().substring(0, 8)
        return (id)
    }
}
