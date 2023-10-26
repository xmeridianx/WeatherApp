package com.example.weatherapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.DialogManager
import com.example.weatherapp.viewmodel.MainViewModel
import com.example.weatherapp.adapters.WeatherAdapter
import com.example.weatherapp.adapters.WeatherModel
import com.example.weatherapp.databinding.FragmentBinding
import com.example.weatherapp.isPermissionGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.squareup.picasso.Picasso
import org.json.JSONObject


class Fragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding.recyclerViewTemperatureDays.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        binding.recyclerViewTemperatureDays.adapter = adapter
        viewModel.liveDataList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        updateCurrentCard()
        binding.buttonCityChange.setOnClickListener {
            DialogManager.searchByCity(requireContext(), object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    name?.let { it1 -> requestWeatherData(it1) }
                }
            })
        }
    }
    override fun onResume() {
        super.onResume()
        checkLocation()
    }
    private fun isLocationEnabled(): Boolean{
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    private fun checkLocation(){
        if (isLocationEnabled()){
            getLocation()
        }else{
            DialogManager.locationDialog(requireContext(), object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }
    private fun getLocation(){
        if (!isLocationEnabled()){
            return
        }
        val cancellationToken = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationToken.token ).
        addOnCompleteListener{
            requestWeatherData("${it.result.latitude},${it.result.longitude}")
        }
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


