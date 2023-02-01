package com.lamesa.socialdown.data.remote

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.lamesa.socialdown.data.remote.api.APIFacebook
import com.lamesa.socialdown.data.remote.api.APIInsta
import com.lamesa.socialdown.data.remote.api.APITiktok
import com.lamesa.socialdown.domain.repository.FacebookRepository
import com.lamesa.socialdown.domain.repository.InstagramRepository
import com.lamesa.socialdown.domain.repository.TiktokRepository
import com.lamesa.socialdown.utils.SocialHelper.removeIgshid
import java.util.*

/** Created by luis Mesa on 09/08/22 */
object APIHelper {

    enum class AppApi(val id: String) {
        TIKTOK("tiktok"),
        INSTAGRAM("instagram"),
        FACEBOOK("facebook"),
        NONE("none")
    }

    enum class CodeApi(val code: Int, val desc: String) {
        C_200(200, "success"),
        C_429(429, "MONTHLY quota for Requests"),
        C_404(404, "Please try again later.\nLink is roung / private or there is unkown error!!\n"),
        C_403(403, "Please try again later.\nLink is roung / private or there is unkown error!!\n"),
        C_501(501, "Please try again later.\nLink is roung / private or there is unkown error!!\n"),
        C_502(502, "Please try again later.\nLink is roung / private or there is unkown error!!\n"),
        C_504(504, "Please try again later.\nLink is roung / private or there is unkown error!!\n"),
        C_500(500, "Please try again later.\nLink is roung / private or there is unkown error!!\n")
    }

    fun executeApi(context: Context, appApi: AppApi, queryLink: String) {
        when (appApi) {
            AppApi.FACEBOOK -> executeFacebook(context, queryLink)
            AppApi.TIKTOK -> executeTiktok(context, queryLink)
            AppApi.INSTAGRAM -> executeInstagram(context, queryLink)
            else -> {}
        }
    }

    // region ----------------------- APIHelper Tikok -----------------------

    private fun executeTiktok(context: Context, queryLink: String) {
        // Ejecutar api random del listado de APIs
        val nunApiRandom = Random().nextInt(APITiktok.APIs.values().size)
        val firstApiDefault = APITiktok.APIs.values()[nunApiRandom].toString()
        getTiktokData(context, firstApiDefault, queryLink)
    }

    private fun getTiktokData(context: Context, api: String, queryLink: String) {
        val tiktokProvider = TiktokRepository(context as AppCompatActivity)
        when (api) {
            APITiktok.APIs.Maatootz.toString() -> tiktokProvider.executeApiMaatootz(queryLink)
            APITiktok.APIs.Maatootz2.toString() -> tiktokProvider.executeApiMaatootz2(queryLink)
            APITiktok.APIs.Yi005.toString() -> tiktokProvider.executeApiYi005(queryLink)
        }
    }

    // endregion

    // region ----------------------- APIHelper Instagram -----------------------

    private fun executeInstagram(context: Context, queryLink: String) {
        // Api con la cual obtener la data
        val firstApiDefault = APIInsta.APIs.Maatootz.toString()
        getInstagramData(context, firstApiDefault, removeIgshid(queryLink))
    }

    private fun getInstagramData(context: Context, api: String, queryLink: String) {
        val intagramRepository = InstagramRepository(context as AppCompatActivity)
        when (api) {
            APIInsta.APIs.Maatootz.toString() -> intagramRepository.executeApiMaatootz(queryLink)
        }
    }

    // endregion

    // region ----------------------- APIHelper Facebook -----------------------

    private fun executeFacebook(context: Context, queryLink: String) {
        // Api con la cual obtener la data
        val firstApiDefault = APIFacebook.APIs.Vikas.id
        getFacebookData(context, firstApiDefault, queryLink)
    }

    private fun getFacebookData(context: Context, api: String, queryLink: String) {
        val facebookRepository = FacebookRepository(context as AppCompatActivity)
        when (api) {
            APIFacebook.APIs.Crashbash.id -> facebookRepository.executeApiCrashbash(
                queryLink
            )
            APIFacebook.APIs.Vikas.id -> facebookRepository.executeApiVikas(queryLink)
        }
    }
    // endregion


}
