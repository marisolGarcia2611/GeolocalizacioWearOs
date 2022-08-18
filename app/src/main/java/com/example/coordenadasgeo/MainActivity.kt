package com.example.coordenadasgeo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.coordenadasgeo.databinding.ActivityMainBinding
import com.google.android.gms.common.api.Response
import org.json.JSONObject

class MainActivity : Activity() {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private var latitude: Double? = null
    private var longitude: Double? = null

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var botonsit = binding.obtenerUbi
        botonsit.setOnClickListener {
            getLocation()
        }
    }
    private fun getLocation() {
        Toast.makeText(this, "Obteniendo Coordenadas", Toast.LENGTH_SHORT).show()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000000,0f, LocationListener {
            var latitudd = binding.latView
            var longiutd = binding.lngView
            //ENVIAR CON VOLLEY A API
            this.latitude = it.latitude
            this.longitude = it.longitude
            Peticion(it.latitude,it.longitude)
            //SOLO ESAS DOS VARIABLES DE ARRIBA

            latitudd.text = "Latitud: "+it.latitude.toString()
            longiutd.text = "Longitud: "+it.longitude.toString()

            Log.d("LATITUD", "${it.latitude.toString()}")
            Log.d("LONGITUD", "${it.longitude.toString()}")

        })
    }

    private fun Peticion(latitud:Double,longitud:Double){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.254.208:8000/api/insert"

        var lat=latitud.toString()
        var lon=longitud.toString()

        val JsonObject = JSONObject()
        JsonObject.put("latitud",lat)
        JsonObject.put("longitud",lon)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,url,JsonObject,
            { response ->
                Toast.makeText(this,response.toString(),Toast.LENGTH_SHORT).show()
                Log.e("-----------------data-------------------", response.toString())
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                Log.e("----------------error------------------", error.toString())
                error.printStackTrace()
            }
        )

            queue.add(jsonObjectRequest)

    }

//    private fun volley(latitud:Double,longitud:Double){
//        val queue = Volley.newRequestQueue(this)
//        val url = "http://192.168.80.217:8000/api/insert"
//
//        val JsonObject = JSONObject()
//        JsonObject.put("latitud",latitud)
//        JsonObject.put("longitud",longitud)
//
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.POST,url,JsonObject,
//            com.android.volley.Response.Listener { response ->
//                Toast.makeText(this,response.toString(),Toast.LENGTH_SHORT).show()
//                Log.e("data", response.toString())
//            },
//            com.android.volley.Response.ErrorListener { error ->
//                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
//                Log.e("error", error.toString())
//                error.printStackTrace()
//            }
//        )
//
//        queue.add(jsonObjectRequest)
//    }
}