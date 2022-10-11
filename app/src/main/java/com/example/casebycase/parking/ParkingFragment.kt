package com.example.casebycase.parking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.casebycase.R
import com.example.casebycase.databinding.FragmentParkingBinding
import com.example.casebycase.park.ParkResponse
import com.example.casebycase.park.ParkService
import com.example.casebycase.park.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.math.roundToInt

class ParkingFragment: Fragment(R.layout.fragment_parking) {
    private var parkingBinding: FragmentParkingBinding?=null
    private val binding get() = parkingBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        parkingBinding = FragmentParkingBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //공기압 받아오기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://parkbomin.iptime.org:18000")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ParkService::class.java)!!

        val callGetTH = service.PS()

        callGetTH.enqueue(object : Callback<ParkResponse>{
            override fun onResponse(call: Call<ParkResponse>, response: Response<ParkResponse>){
                Log.d("Parking", response.toString())
                response.body().let {
                    Log.d("Parking", it.toString())
                    binding.pressure.text = it?.MESSAGE?.get(0)?.pressure.toString()
                    binding.lat.text = it?.MESSAGE?.get(0)?.lat.toString()
                    binding.lng.text = it?.MESSAGE?.get(0)?.lng.toString()
                }

            }
            override fun onFailure(call: Call<ParkResponse>, t: Throwable) {
                Log.e("Parking", t.toString())
            }
        })
        //공기압 받아오기 끝

        //
        val retrofitW: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicew = retrofitW.create(WeatherService::class.java)!!

        val callGetW = servicew.getCurrentWeatherData("37", "126", "1681d3f8a577fed2061198d014e9d4eb")

        var Celsius:Double = 0.0

            callGetW.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    Log.d("Weather", response.toString())
                    response.body().let {
                        Log.d("Weather", it.toString())
                        binding.weather.text = it?.weather?.get(0)?.main.toString()
                        if(it?.main?.temp != null){
                            Celsius = (it.main.temp)
                        }
                        binding.temp.text = (Celsius - 273).roundToInt().toString() + "℃"
                        binding.hum.text = it?.main?.humidity.toString() + "%"
                    }

                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("Weather", t.toString())
                }
            })

    }
}

interface WeatherService {
    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon")lon: String,
        @Query("appid")appid: String
    ):Call<WeatherResponse>
}

