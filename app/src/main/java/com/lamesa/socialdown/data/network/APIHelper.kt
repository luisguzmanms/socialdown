package com.lamesa.socialdown.data.network

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.kongzue.dialogx.dialogs.PopTip
import com.lamesa.socialdown.data.network.api.APIFacebook
import com.lamesa.socialdown.data.network.api.APIInsta
import com.lamesa.socialdown.data.network.api.APITiktok
import com.lamesa.socialdown.domain.repository.FacebookRepository
import com.lamesa.socialdown.domain.repository.InstagramRepository
import com.lamesa.socialdown.domain.repository.TiktokRepository
import com.lamesa.socialdown.otros.SocialHelper
import com.lamesa.socialdown.otros.SocialHelper.checkTypeMedia
import com.lamesa.socialdown.utils.DialogUtils
import java.util.*

/** Created by luis Mesa on 09/08/22 */
object APIHelper {

    enum class AppApi { TIKTOK, INSTAGRAM, FACEBOOK, NONE }

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
        DialogUtils.Toast.showSuccess(nunApiRandom.toString())
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
        getInstagramData(context, firstApiDefault, queryLink)
    }

    private fun getInstagramData(context: Context, api: String, queryLink: String) {
        val intagramRepository = InstagramRepository(context as AppCompatActivity)
        when (api) {
            APIInsta.APIs.Maatootz.toString() -> intagramRepository.executeApimaatootz(queryLink)
        }
    }

    // endregion

    // region ----------------------- APIHelper Facebook -----------------------

    private fun executeFacebook(context: Context, queryLink: String) {
        // Api con la cual obtener la data
        val firstApiDefault = APIFacebook.APIs.Crashbash.toString()
        // Si es un reels se utiliza la Api Vikas
        if (checkTypeMedia(queryLink) == SocialHelper.MediaType.REEL) {
            PopTip.show("Es un reel").iconWarning()
            getFacebookData(context, APIFacebook.APIs.Vikas.toString(), queryLink)
        } else {
            getFacebookData(context, firstApiDefault, queryLink)
        }

    }

    private fun getFacebookData(context: Context, api: String, queryLink: String) {
        val facebookRepository = FacebookRepository(context as AppCompatActivity)

        when (api) {
            APIFacebook.APIs.Crashbash.toString() -> facebookRepository.executeApicrashbash(queryLink)
            APIFacebook.APIs.Vikas.toString() -> facebookRepository.executeApivikas(queryLink)
        }
    }

    // endregion

}
