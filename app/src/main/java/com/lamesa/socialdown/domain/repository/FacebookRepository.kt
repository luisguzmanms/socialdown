package com.lamesa.socialdown.domain.repository

import androidx.appcompat.app.AppCompatActivity
import com.lamesa.socialdown.data.remote.APIHelper
import com.lamesa.socialdown.data.remote.RetrofitHelper
import com.lamesa.socialdown.data.remote.api.APIFacebook
import com.lamesa.socialdown.domain.model.api.ModelApi
import com.lamesa.socialdown.domain.model.api.ModelMediaDataExtracted
import com.lamesa.socialdown.domain.response.facebook.FaceResCrashBash
import com.lamesa.socialdown.domain.response.facebook.FaceResVikar
import com.lamesa.socialdown.downloader.DownloaderHelper
import com.lamesa.socialdown.usecase.GetDataApiByIdUseCase
import com.lamesa.socialdown.utils.DialogXUtils.*
import com.lamesa.socialdown.utils.SDAd
import com.lamesa.socialdown.utils.SDAnalytics
import com.lamesa.socialdown.utils.SocialHelper
import com.lamesa.socialdown.utils.SocialHelper.chooseRandomString
import com.lamesa.socialdown.utils.SocialHelper.isOnline
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketException

/** Created by luis Mesa on 07/08/22 */
class FacebookRepository(private val context: AppCompatActivity) {

    private val appFacebook = APIHelper.AppApi.FACEBOOK.id
    private var dataApi = ModelApi()
    private val dataExtracted = ModelMediaDataExtracted()
    private val downloaderHelper = DownloaderHelper(context)

    /** Se ejecuta la Api crashbash con el link [queryLink]. */
    internal fun executeApiCrashbash(queryLink: String) {
        LoadingX.showLoading()
        // Obtener datos de la Api Crashbash del listado de APIs y pasarlos a dataApi
        GetDataApiByIdUseCase().getApiById(appFacebook, APIFacebook.APIs.Crashbash.id)
            .observe(context) {
                if (it != null) {
                    dataApi = it
                    apiCrashbash(queryLink)
                } else {
                    LoadingX.hideLoading()
                    ToastX.showError("dataApi == null")
                }
            }
    }

    private fun apiCrashbash(queryLink: String) {
        val dataKey = chooseRandomString(dataApi.key!!)
        if (isOnline(context)) {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val call: Response<FaceResCrashBash> =
                        RetrofitHelper.getRetrofit(dataApi.baseUrl!!)
                            .create(APIFacebook::class.java)
                            .getDataCrashBash(dataKey, "${dataApi.queryPath}$queryLink")
                    val resBody: FaceResCrashBash? = call.body()

                    context.runOnUiThread() {
                        if (call.isSuccessful) {
                            LoadingX.hideLoading()
                            dataExtracted.app = appFacebook
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType =
                                SocialHelper.checkTypeMedia(queryLink).toString()
                            // Se asigna datos obtenidos de call al modelo ModelMediaDataExtracted
                            dataExtracted.linksToDownload =
                                arrayListOf(
                                    resBody?.body?.videoFHD ?: ""
                                        .trim()
                                        .replace("[", "")
                                        .replace("]", "")
                                )
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            // Se valida codeResponse de la respuesta y contenido obtenido
                            downloaderHelper.checkToDownload(dataExtracted)

                        } else {
                            LoadingX.hideLoading()
                            /** Se envia datos de error a Analytics */
                            Logger.d("!call.isSuccessful > call.body:: " + call.body())
                            Logger.d("!call.isSuccessful > call.code:: " + call.code())
                            Logger.d("!call.isSuccessful > call.raw:: " + call.raw())

                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            when (dataExtracted.codeResponse) {
                                APIHelper.CodeApi.C_500.code.toString() -> NotificationX.showError(
                                    APIHelper.CodeApi.C_500.desc + " : $queryLink"
                                ).showLong()
                                APIHelper.CodeApi.C_403.code.toString() -> SocialHelper.searchByLink(
                                    context,
                                    queryLink
                                )
                                APIHelper.CodeApi.C_429.code.toString() -> SocialHelper.searchByLink(
                                    context,
                                    queryLink
                                )
                                else -> ToastX.showWarning("Error code: ${dataExtracted.codeResponse}")
                            }

                            SDAnalytics().eventErrorApiData(dataExtracted)
                            SDAd().showInterAd(context)
                        }
                    }
                }
            } catch (e: SocketException) {
                NotificationX.showError("Please try again. ${e.message}")
            }
        }

    }

    /** Se ejecuta la Api Vikas con el link [queryLink]. */
    internal fun executeApiVikas(queryLink: String) {
        LoadingX.showLoading()
        // Obtener datos de la Api Vikas del listado de APIs y pasarlos a dataApi
        GetDataApiByIdUseCase().getApiById(APIHelper.AppApi.FACEBOOK.id, APIFacebook.APIs.Vikas.id)
            .observe(context) {
                if (it != null) {
                    dataApi = it
                    apiVikas(queryLink)
                } else {
                    LoadingX.hideLoading()
                    ToastX.showError("dataApi == null")
                }
            }
    }

    private fun apiVikas(queryLink: String) {
        val dataKey = chooseRandomString(dataApi.key!!)
        if (isOnline(context)) {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val call: Response<FaceResVikar> =
                        RetrofitHelper.getRetrofit(dataApi.baseUrl!!)
                            .create(APIFacebook::class.java)
                            .getDataVikas(dataKey, "${dataApi.queryPath}$queryLink")
                    val resBody: FaceResVikar? = call.body()

                    context.runOnUiThread {
                        if (call.isSuccessful) {
                            LoadingX.hideLoading()
                            dataExtracted.app = appFacebook
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType =
                                SocialHelper.checkTypeMedia(queryLink).toString()
                            // Se asigna datos obtenidos de call al modelo dataExtracted
                            dataExtracted.linksToDownload = arrayListOf(
                                resBody?.body?.videoFHD ?: ""
                                    .trim()
                                    .replace("[", "")
                                    .replace("]", "")
                            )
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey


                            // Se valida codeResponse de la respuesta y contenido obtenido
                            downloaderHelper.checkToDownload(dataExtracted)

                        } else {
                            LoadingX.hideLoading()
                            /** Se envia datos de error a Analytics */
                            println(" videos Info call.errorBody \n" + call.errorBody())
                            println(" videos Info call.raw \n" + call.raw())
                            println(("videos Info call.body:: " + call.body()))

                            dataExtracted.app = appFacebook
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType =
                                SocialHelper.checkTypeMedia(queryLink).toString()
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            when (dataExtracted.codeResponse) {
                                APIHelper.CodeApi.C_500.code.toString() -> NotificationX.showError(
                                    APIHelper.CodeApi.C_500.desc + " : $queryLink"
                                ).showLong()
                                APIHelper.CodeApi.C_403.code.toString() -> SocialHelper.searchByLink(
                                    context,
                                    queryLink
                                )
                                APIHelper.CodeApi.C_429.code.toString() -> SocialHelper.searchByLink(
                                    context,
                                    queryLink
                                )
                                else -> ToastX.showWarning("Error code: ${dataExtracted.codeResponse}")
                            }

                            SDAnalytics().eventErrorApiData(dataExtracted)
                            SDAd().showInterAd(context)
                        }
                    }
                }
            } catch (e: SocketException) {
                NotificationX.showError("Please try again. ${e.message}")
            }
        }

    }

}


