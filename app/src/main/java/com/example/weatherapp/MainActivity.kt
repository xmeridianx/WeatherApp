package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.databinding.ActivityMainBinding
import org.json.JSONObject


const val API_KEY = "cf6d9a1dfdbe4917842192231232409"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container, Fragment()).commit()
    }
}
       /*
       1e9041e5-d36f-43f5-ae5f-2beb178ea172  - яндекс api

       binding.buttonGet.setOnClickListener {
            getResult("Kazan")
        }

    }
//http://api.weatherapi.com/v1/current.json?key=&q=Kazan&aqi=no
    private fun getResult(city: String) {
        val url = "https://api.weatherapi.com/v1/current.json" +
                "?key=$API_KEY&q=$city&aqi=no"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {

                    response ->
                val obj = JSONObject(response)
                val temp = obj.getJSONObject("current")
                Log.d("Log", "${temp.getString("temp_c")}")
            },
            {
                Log.d("Log", "Erroorrr")
            }
        )
        queue.add(stringRequest)

    }

        */

        /*
        call.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
                    // Обработка успешного ответа
                    val weatherData = response.body()
                    // Отобразите данные о погоде или выполняйте другие действия
                } else {
                    // Обработка ошибки, если запрос не удался
                    // В response.errorBody() можно получить информацию об ошибке
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                // Обработка ошибки, если запрос не был выполнен (например, проблемы с сетью)
            }
        })

        val weatherData = response.body()
        if (weatherData != null) {
            temperatureTextView.text = "${weatherData.temperature}°C"
            humidityTextView.text = "${weatherData.humidity}%"
            descriptionTextView.text = weatherData.description
            // Загрузите иконку погоды по URL
            // Вы можете использовать библиотеку Picasso или Glide для этой цели
            // Пример для Picasso:
            // Picasso.get().load(weatherData.iconUrl).into(iconImageView)
        } else {
            // Обработайте случаи ошибок или отсутствия данных
        }
    }

         */
