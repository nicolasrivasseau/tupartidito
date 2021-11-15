package com.unlam.tupartidito.ui.detail_rent

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.ui.detail_rent.ui.theme.TuPartiditoTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailRentActivityBU : ComponentActivity() {
    private lateinit var myPreferences : SharedPreferences

    private val viewModel: DetailRentActivityViewModelBU by viewModels()
    private var locationLatLong: ArrayList<Double?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        with(viewModel){
            observe(clubData){ club ->
                locationLatLong?.add(club.latitude)
                locationLatLong?.add(club.longitude)
                setContent {
                    TuPartiditoTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colors.background) {
                            val data = intent.getStringArrayExtra("data")!!
                            val isVisible = intent.getStringExtra("isVisible" )
                            //Datos(data, isVisible, locationLatLong)
                            RotationPortrait(data, isVisible, locationLatLong, viewModel, myPreferences)
                        }
                    }
                }
            }
            getClubData(intent.getStringArrayExtra("data")!!.get(1))

        }


    }
}

@Composable
fun Datos(
    datos: Array<String>, isVisible: String?, locationLatLong: ArrayList<Double?>?
    , viewModel: DetailRentActivityViewModelBU, myPreferences: SharedPreferences) {
    Column() {
        Column(
            //modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.verdeFondo),
            modifier = Modifier
                .size(520.dp, 500.dp)
                .background(MaterialTheme.colors.onSecondary)
                .border(0.5.dp, Color.Black),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ){
            Image(
                painter = rememberImagePainter("https://1.bp.blogspot.com/_Jb7HkNAV7oI/TTC5rXiJ67I/AAAAAAAAACA/EtCvfJQED40/s1600/cancha.jpg"),
                contentDescription = "Imagen de cancha",
                modifier = Modifier.size(520.dp, 230.dp)

            )
            Text(text = "Datos de la reserva #${datos.get(0)}", modifier = Modifier.padding(2.dp), style = MaterialTheme.typography.h6)
            Text(text = "Cancha: ${datos.get(1)}", modifier = Modifier.padding(2.dp) , style = MaterialTheme.typography.subtitle1)
            Text(text = "Ubicacion: ${datos.get(2)}", modifier = Modifier.padding(2.dp).width(320.dp) , style = MaterialTheme.typography.subtitle1)
            Text(text = "Precio: $${datos.get(3)}", modifier = Modifier.padding(2.dp), style = MaterialTheme.typography.subtitle1)
            Text(text = "Horario: ${datos.get(4)}", modifier = Modifier.padding(2.dp), style = MaterialTheme.typography.subtitle1)
        }
        Column(){
            //val isVisible = isVisible.toBoolean()
            ButtonsContent(datos, isVisible!!, locationLatLong, viewModel, myPreferences, datos)
        }
    }
}
@Composable
fun ButtonsContent(
    datos: Array<String>, isVisible: String, locationLatLong: ArrayList<Double?>?
    , viewModel: DetailRentActivityViewModelBU, myPreferences: SharedPreferences, datosR: Array<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row() {
            val context = LocalContext.current
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onPrimary),
                onClick = {
                    try {
                        Intent(Intent.ACTION_SEND).apply {
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Esta es una invitacion a jugar un partidito en el club ${datos[0]} a las ${datos[4]}. Direccion: ${datos[2]}"
                            )
                            setType("text/plain")
                            setPackage("com.whatsapp")
                            context.startActivity(this)
                        }
                    } catch (ignored: ActivityNotFoundException) {
                        Toast.makeText(context, "No se encontro app", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.padding(all = Dp(10F)),
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            )
            {
                Text(text = "Compartir", color = Color.White)
            }

            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onSecondary),
                onClick = {
                    val intentUriNavigation = Uri.parse("google.navigation:q=${locationLatLong?.get(0)},${locationLatLong?.get(1)}")
                    Intent(Intent.ACTION_VIEW, intentUriNavigation).apply {
                        setPackage("com.google.android.apps.maps")
                        context.startActivity(this)
                    }
                },
                modifier = Modifier.padding(all = Dp(10F)),
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            )
            {
                Text(text = "Como llegar", color = Color.White)
            }
        }
        if(isVisible == "EsMia"){
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                onClick = {
                    //logica cancelar reserva

                    val user = myPreferences.getString("user","")
                    Log.d("cancelar", "DetailRentActivity call cancelrent $user")
                    val resultadoo = viewModel.cancelRent(datosR[0], datosR[1],user!!)
                },
                modifier = Modifier.padding(all = Dp(10F)),
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            )
            {
                Text(text = "Cancelar reserva", color = Color.White)
            }
        } else if(isVisible != "NoEsMia"){
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onSecondary),
                onClick = {
                },
                modifier = Modifier.padding(all = Dp(10F)),
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            )
            {
                Text(text = "Realizar reserva", color = Color.White)
            }
        }
        //var rating: Float = 5.0f
        //RatingBar(value = rating,
        //    ratingBarStyle = RatingBarStyle.HighLighted, onValueChange = {
        //        rating = it
        //    }) {
        //    Log.d("TAG", "onRatingChanged: $it")
        //}
    }

}


@Composable
fun RotationPortrait(datos: Array<String>, isVisible: String?,locationLatLong: ArrayList<Double?>?
                     ,viewModel: DetailRentActivityViewModelBU ,myPreferences : SharedPreferences
){
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Datos(datos, isVisible, locationLatLong, viewModel, myPreferences)
        }
        else -> {
            Datos(datos, isVisible, locationLatLong, viewModel, myPreferences)
        }
    }
}
/*
@Composable
fun RatingBar(){
    RatingBar(value = rating,
        ratingBarStyle =
        )
}*/