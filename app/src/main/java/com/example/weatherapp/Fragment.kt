package com.example.weatherapp

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.adapters.WeatherAdapter
import com.example.weatherapp.adapters.WeatherModel
import com.example.weatherapp.databinding.FragmentBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject


class Fragment : Fragment() {
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentBinding
    private lateinit var adapter: WeatherAdapter
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBinding.inflate(inflater, container,false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        binding.recyclerViewTemperatureDays.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        binding.recyclerViewTemperatureDays.adapter = adapter
        viewModel.liveDataList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        //

        updateCurrentCard()
        requestWeatherData("Moscow")



        /*
        val list = listOf(WeatherModel("kazan", "12:00", "11", "22", "33", "22"),
            WeatherModel("kazan", "13:00", "11", "22", "33", "22"),
            WeatherModel("kazan", "14:00", "11", "22", "33", "22"))
        adapter.submitList(list)

         */
    }

    private fun updateCurrentCard(){
        viewModel.liveData.observe(viewLifecycleOwner){
        binding.textViewCity.text = it.city
            binding.textViewTemperature.text = "${it.temperature} °C"
            Picasso.get().load("https:" + it.imageUrl).into(binding.imageViewCondition)

        }
    }


    private fun permissionListener(){
        launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity, "permission is$it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission(){
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionListener()
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun requestWeatherData(city: String){
        val url = "http://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "7" +
                "&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result-> parseWeatherData(result)
            },
            {
                error-> Log.d("MyLog", "Error: $error")
            }
        )
        queue.add(request)
    }
    private fun parseWeatherData(result: String){
        val mainObject = JSONObject(result)
        val list = parseDays(mainObject)

        parseCurrentData(mainObject, list[0])
    }

    private fun parseDays(mainObject: JSONObject): List<WeatherModel>{
        val list = ArrayList<WeatherModel>()
        val daysArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val name = mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()){
            val day = daysArray[i] as JSONObject
            val item = WeatherModel(
                name,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                "",
                day.getJSONObject("day").getString("maxtemp_c"),
                day.getJSONObject("day").getString("mintemp_c")
            )
            list.add(item)
        }
        viewModel.liveDataList.value = list
        return list
    }

    private fun parseCurrentData(mainObject: JSONObject, weatherItem: WeatherModel){
        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            mainObject.getJSONObject("current").getString("temp_c"),
            weatherItem.maxTemperature,
            weatherItem.minTemperature
        )
        viewModel.liveData.value = item
        Log.d("MyLog", "city: ${item.city}")

    }

    companion object {

        @JvmStatic
        fun newInstance() = Fragment()
    }
}


