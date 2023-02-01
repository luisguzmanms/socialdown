package com.lamesa.socialdown.domain.repository

import androidx.appcompat.app.AppCompatActivity
import com.lamesa.socialdown.data.remote.APIHelper
import com.lamesa.socialdown.data.remote.RetrofitHelper
import com.lamesa.socialdown.data.remote.api.APITiktok
import com.lamesa.socialdown.domain.model.api.ModelApi
import com.lamesa.socialdown.domain.model.api.ModelMediaDataExtracted
import com.lamesa.socialdown.domain.response.tiktok.TikTokResMaatootz
import com.lamesa.socialdown.domain.response.tiktok.TikTokResMaatootz2
import com.lamesa.socialdown.domain.response.tiktok.TiktokResYi005
import com.lamesa.socialdown.downloader.DownloaderHelper
import com.lamesa.socialdown.usecase.GetDataApiByIdUseCase
import com.lamesa.socialdown.utils.DialogXUtils.LoadingX
import com.lamesa.socialdown.utils.DialogXUtils.NotificationX.showError
import com.lamesa.socialdown.utils.DialogXUtils.ToastX
import com.lamesa.socialdown.utils.SDAd
import com.lamesa.socialdown.utils.SDAnalytics
import com.lamesa.socialdown.utils.SocialHelper.checkCodeResponse
import com.lamesa.socialdown.utils.SocialHelper.checkTypeMedia
import com.lamesa.socialdown.utils.SocialHelper.chooseRandomString
import com.lamesa.socialdown.utils.SocialHelper.isOnline
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketException
import javax.inject.Inject

class TiktokRepository @Inject
constructor(
    private val context: AppCompatActivity,
) {

    private val appTiktok: String = APIHelper.AppApi.TIKTOK.id
    private var dataApi = ModelApi()
    private val dataExtracted = ModelMediaDataExtracted()
    private val downloaderHelper = DownloaderHelper(context)

    /** Se ejecuta la Api yi005 con el link [queryLink]. */
    internal fun executeApiYi005(queryLink: String) {
        LoadingX.showLoading()
        // Obtener datos de la Api Crashbash del listado de APIs y pasarlos a dataApi
        GetDataApiByIdUseCase().getApiById(appTiktok, APITiktok.APIs.Yi005.id)
            .observe(context) {
                if (it != null) {
                    dataApi = it
                    apiYi005(queryLink)
                } else {
                    LoadingX.hideLoading()
                    ToastX.showError("dataApi == null")
                }
            }

    }

    private fun apiYi005(queryLink: String) {
        var dataKey = chooseRandomString(dataApi.key!!)
        if (isOnline(context)) {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val call: Response<TiktokResYi005> =
                        RetrofitHelper.getRetrofit(dataApi.baseUrl!!)
                            .create(APITiktok::class.java)
                            .getDataYi005(dataKey, "${dataApi.queryPath}$queryLink")
                    val resBody: TiktokResYi005? = call.body()

                    context.runOnUiThread {
                        if (call.isSuccessful) {
                            LoadingX.hideLoading()

                            dataExtracted.app = appTiktok
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType = checkTypeMedia(queryLink).toString()
                            //region Se asigna datos obtenidos de call al modelo dataExtracted
                            dataExtracted.linksToDownload =
                                listOf(resBody?.data?.linkDownloadMp4 ?: "")
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey
                            //endregion

                            // Se valida codeResponse de la respuesta y contenido obtenido
                            downloaderHelper.checkToDownload(dataExtracted)

                        } else {
                            /*
                            if (call.code().toString() == "429") {
                                // "You have exceeded the DAILY quota for Requests on your current plan,
                                // BASIC. Upgrade your plan at
                                // https://rapidapi.com/yi005/api/tiktok-download-without-watermark"
                            }
                             */
                            LoadingX.hideLoading()
                            Logger.i("Logger videos is call.body:: " + call.body().toString())
                            Logger.i("Logger videos is call.code:: " + call.code())
                            Logger.i("Logger videos is call.raw:: " + call.raw())
                            /** Se envia datos de error a Analytics */
                            dataExtracted.app = appTiktok
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType = checkTypeMedia(queryLink).toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.key = dataKey

                            // detectar tipo de error e intentar nuevamente hasta 3 veces
                            checkCodeResponse(context, dataExtracted)

                            SDAnalytics().eventErrorApiData(dataExtracted)
                            SDAd().showInterAd(context)
                        }
                    }
                }
            } catch (e: SocketException) {
                showError("Please try again. ${e.message}")
            }
        }

    }

    /** Se ejecuta la Api maatootz con el link [queryLink]. */
    internal fun executeApiMaatootz(queryLink: String) {
        LoadingX.showLoading()
        // Obtener datos de la Api Maatootz del listado de APIs y pasarlos a dataApi
        GetDataApiByIdUseCase().getApiById(appTiktok, APITiktok.APIs.Maatootz.id)
            .observe(context) {
                if (it != null) {
                    dataApi = it
                    apiMaatootz(queryLink)
                } else {
                    LoadingX.hideLoading()
                    ToastX.showError("dataApi == null")
                }
            }
    }

    private fun apiMaatootz(queryLink: String) {
        val dataKey = chooseRandomString(dataApi.key!!)
        if (isOnline(context)) {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val call: Response<TikTokResMaatootz> =
                        RetrofitHelper.getRetrofit(dataApi.baseUrl!!)
                            .create(APITiktok::class.java)
                            .getDataMaatootz(dataKey, "${dataApi.queryPath}$queryLink")
                    val resBody: TikTokResMaatootz? = call.body()
                    context.runOnUiThread {
                        if (call.isSuccessful) {
                            LoadingX.hideLoading()

                            dataExtracted.app = appTiktok
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType = checkTypeMedia(queryLink).toString()
                            // Se asigna datos obtenidos de call al modelo dataExtracted
                            dataExtracted.linksToDownload =
                                listOf(resBody?.linkDownloadMp4?.get(0) ?: "")
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            // Se valida codeResponse de la respuesta y contenido obtenido
                            downloaderHelper.checkToDownload(dataExtracted)

                        } else {
                            LoadingX.hideLoading()

                            dataExtracted.app = appTiktok
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType = checkTypeMedia(queryLink).toString()
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            // detectar tipo de error e intentar nuevamente hasta 3 veces
                            checkCodeResponse(context, dataExtracted)

                            /** Se envia datos de error a Analytics */
                            SDAnalytics().eventErrorApiData(dataExtracted)
                            SDAd().showInterAd(context)
                        }
                    }
                }
            } catch (e: SocketException) {
                showError("Please try again. ${e.message}")
            }
        }

    }

    /** Se ejecuta la Api maatootz2 con el link [queryLink]. */
    internal fun executeApiMaatootz2(queryLink: String) {
        LoadingX.showLoading()
        // Obtener datos de la Api Maatootz del listado de APIs y pasarlos a dataApi
        GetDataApiByIdUseCase().getApiById(appTiktok, APITiktok.APIs.Maatootz2.id)
            .observe(context) {
                if (it != null) {
                    dataApi = it
                    apiMaatootz2(queryLink)
                } else {
                    LoadingX.hideLoading()
                    ToastX.showError("dataApiTiktok == null")
                }
            }
    }

    private fun apiMaatootz2(queryLink: String) {
        val dataKey = chooseRandomString(dataApi.key!!)
        if (isOnline(context)) {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val call: Response<TikTokResMaatootz2> =
                        RetrofitHelper.getRetrofit(dataApi.baseUrl!!)
                            .create(APITiktok::class.java)
                            .getDataMaatootz2(dataKey, "${dataApi.queryPath}$queryLink")
                    val resBody: TikTokResMaatootz2? = call.body()

                    context.runOnUiThread {
                        if (call.isSuccessful) {
                            LoadingX.hideLoading()

                            dataExtracted.app = appTiktok
                            dataExtracted.mediaType = checkTypeMedia(queryLink).type
                            dataExtracted.queryLink = queryLink
                            // Se asigna datos obtenidos de call al modelo dataExtracted
                            dataExtracted.linksToDownload =
                                listOf(resBody?.linkDownloadMp4?.get(0) ?: "")
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            // Se valida codeResponse de la respuesta y contenido obtenido
                            downloaderHelper.checkToDownload(dataExtracted)

                        } else {
                            LoadingX.hideLoading()
                            dataExtracted.app = appTiktok
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType = checkTypeMedia(queryLink).toString()
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            // detectar tipo de error e intentar nuevamente hasta 3 veces
                            checkCodeResponse(context, dataExtracted)

                            /** Se envia datos de error a Analytics */
                            SDAnalytics().eventErrorApiData(dataExtracted)
                            SDAd().showInterAd(context)
                        }
                    }
                }
            } catch (e: SocketException) {
                showError("Please try again. ${e.message}")
            }
        }

    }

}
