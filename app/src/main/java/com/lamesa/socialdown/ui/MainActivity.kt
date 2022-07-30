package com.lamesa.socialdown.ui

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lamesa.socialdown.adapter.TiktokAdapter
import com.lamesa.socialdown.api.APITiktok
import com.lamesa.socialdown.databinding.ActivityMainBinding
import com.lamesa.socialdown.model.tiktok.TiktokResYi005
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : TiktokAdapter
    private val listVideoExported = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svGetMedia.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = TiktokAdapter(listVideoExported)
        binding.rvLinks.layoutManager = LinearLayoutManager(this)
        binding.rvLinks.adapter = adapter
    }

    private fun getRetrofit():Retrofit{

        return Retrofit.Builder()
            .baseUrl("https://tiktok-download-without-watermark.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    private fun searchByLink(queryLink:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call:Response<TiktokResYi005> = getRetrofit().create(APITiktok::class.java).getLinkVideoYi005("8a310e002fmsh7b0a3a11dfb8f16p1159c1jsnf5580def1147","analysis?url=$queryLink")
            val videoLink : TiktokResYi005? = call.body()
            runOnUiThread{
                if(call.isSuccessful){
                    val videos : String = videoLink?.data?.play ?: ""
                    listVideoExported.clear()
                    listVideoExported.add(videos)
                    adapter.notifyDataSetChanged()
                    if(videos.isEmpty()){

                    }

                    println(("videos is call.body:: " + call.body()) ?: "nohayerror")

                    // https://www.tiktok.com/@luisguzmanms/video/6840184669732932866?is_copy_url=1&is_from_webapp=v1
                    // }else{
                    println("Info \n"+call.errorBody())
                }
            }
        }
    }

    private fun showError(error:String) {
        Toast.makeText(this,error,Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            searchByLink(query.toLowerCase())
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }


}