package com.lamesa.socialdown.domain.repository

import androidx.appcompat.app.AppCompatActivity
import com.lamesa.socialdown.data.remote.APIHelper
import com.lamesa.socialdown.data.remote.APIHelper.AppApi
import com.lamesa.socialdown.data.remote.RetrofitHelper
import com.lamesa.socialdown.data.remote.api.APIInsta
import com.lamesa.socialdown.data.remote.api.APIInsta.APIs.Maatootz
import com.lamesa.socialdown.domain.model.api.ModelApi
import com.lamesa.socialdown.domain.model.api.ModelMediaDataExtracted
import com.lamesa.socialdown.domain.response.instagram.InstaResMaatootz
import com.lamesa.socialdown.downloader.DownloaderHelper
import com.lamesa.socialdown.usecase.GetDataApiByIdUseCase
import com.lamesa.socialdown.utils.DialogXUtils.LoadingX
import com.lamesa.socialdown.utils.DialogXUtils.NotificationX.showError
import com.lamesa.socialdown.utils.DialogXUtils.ToastX
import com.lamesa.socialdown.utils.SDAd
import com.lamesa.socialdown.utils.SDAnalytics
import com.lamesa.socialdown.utils.SocialHelper
import com.lamesa.socialdown.utils.SocialHelper.chooseRandomString
import com.lamesa.socialdown.utils.SocialHelper.isOnline
import com.lamesa.socialdown.utils.SocialHelper.searchByLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketException

class InstagramRepository(private val context: AppCompatActivity) {

    private val appInstagram = AppApi.INSTAGRAM.id
    private val dataExtracted = ModelMediaDataExtracted()
    private var dataApi = ModelApi()
    private val downloaderHelper = DownloaderHelper(context)

    /**
     * Se ejecuta la Api Maatootz con el link [queryLink].
     */
    internal fun executeApiMaatootz(queryLink: String) {
        LoadingX.showLoading()
        // Obtener datos de la Api Maatootz del listado de APIs y pasarlos a dataApi
        GetDataApiByIdUseCase().getApiById(appInstagram, Maatootz.id)
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
        var dataKey = chooseRandomString(dataApi.key!!)
        if (isOnline(context)) {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val call: Response<InstaResMaatootz> =
                        RetrofitHelper
                            .getRetrofit(dataApi.baseUrl!!)
                            .create(APIInsta::class.java)
                            .getDataMaatootz(
                                dataKey,
                                "${dataApi.queryPath}$queryLink"
                            )

                    val resBody: InstaResMaatootz? = call.body()
                    context.runOnUiThread {
                        if (call.isSuccessful) {
                            LoadingX.hideLoading()

                            // Convertir links en un array (esto para post tipo Carrusel o Stories de Instagram)
                            val linkAsArray: List<String> =
                                resBody?.linkDownloadMedia.toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(" ", "")
                                    .split(",")

                            dataExtracted.app = appInstagram
                            dataExtracted.queryLink = queryLink
                            dataExtracted.mediaType = SocialHelper.checkTypeMedia(queryLink).type
                            dataExtracted.linksToDownload = linkAsArray
                            dataExtracted.title = resBody?.title.toString()
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            // Se valida codeResponse de la respuesta y contenido obtenido
                            downloaderHelper.checkToDownload(dataExtracted)

                        } else {
                            LoadingX.hideLoading()
                            /**  Se envia datos de error a Analytics */
                            println(" videos Info call.errorBody \n" + call.errorBody())
                            println(" videos Info call.raw \n" + call.raw())
                            println(("videos Info call.body:: " + call.body()))

                            dataExtracted.app = appInstagram
                            dataExtracted.queryLink = queryLink
                            dataExtracted.codeResponse = call.code().toString()
                            dataExtracted.mediaType = SocialHelper.checkTypeMedia(queryLink).type
                            dataExtracted.body = call.body().toString()
                            dataExtracted.raw = call.raw().toString()
                            dataExtracted.key = dataKey

                            when (dataExtracted.codeResponse) {
                                APIHelper.CodeApi.C_500.code.toString() -> showError(APIHelper.CodeApi.C_500.desc + " : $queryLink").showLong()
                                APIHelper.CodeApi.C_403.code.toString() -> searchByLink(
                                    context,
                                    queryLink
                                )
                                APIHelper.CodeApi.C_429.code.toString() -> searchByLink(
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
                showError("Please try again. ${e.message}")
            }
        }

    }
}

