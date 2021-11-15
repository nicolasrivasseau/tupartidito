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
import androidx.compose.runtime.livedata.observeAsState
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.ui.detail_rent.ui.theme.TuPartiditoTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailRentActivity : ComponentActivity() {
    private lateinit var myPreferences: SharedPreferences

    private val viewModel: DetailRentActivityViewModel by viewModels()
    private var locationLatLong: HashMap<String,Double?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)


        setContent {
            TuPartiditoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    RentsScreenContent()
                }

            }
        }

    }


    @Composable
    fun LoadingScreen() {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            CircularProgressIndicator(
                Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
            )
        }
    }

    @Composable
    fun RentsScreenContent() {

        val viewModel = viewModels<DetailRentActivityViewModel>().value
        val state = viewModel.state.observeAsState()
        val idRent = intent.getStringExtra(Constants.RENT_DATA)!!
        val isVisible = intent.getBooleanExtra(Constants.RENT_IS_RESERVED,false)
        val username = myPreferences.getString("user","")
        viewModel.setUsernameAndIdRent(username!!,idRent)

        when (state.value) {
            is DetailRentActivityViewModel.State.Loading -> LoadingScreen()
            is DetailRentActivityViewModel.State.Success -> {

                val rent = (state.value as DetailRentActivityViewModel.State.Success).rent
                if (rent != null) {
                    rent.id_club?.let { viewModel.setIdClub(it) }
                }
                val club = (state.value as DetailRentActivityViewModel.State.Success).club

                locationLatLong?.put("Latitude",club?.latitude)
                locationLatLong?.put("Longitude",club?.longitude)
                RotationPortrait(
                    data = rent!!,
                    isReserved = isVisible,
                    locationLatLong = locationLatLong,
                    viewModel = viewModel,
                    myPreferences = myPreferences,
                    reservedRent = { viewModel.reservedRent(username,rent.id_club,rent.id_rent)}
                )
            }
        }
    }

    @Composable
    fun ClubContent(
        data: Rent,
        isReserved: Boolean,
        locationLatLong: HashMap<String, Double?>?,
        viewModel: DetailRentActivityViewModel,
        myPreferences: SharedPreferences,
        reservedRent: () -> Unit
    ) {
        Column() {
            Column(
                //modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.verdeFondo),
                modifier = Modifier
                    .size(520.dp, 500.dp)
                    .background(MaterialTheme.colors.onSecondary)
                    .border(0.5.dp, Color.Black),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Image(
                    painter = rememberImagePainter("https://1.bp.blogspot.com/_Jb7HkNAV7oI/TTC5rXiJ67I/AAAAAAAAACA/EtCvfJQED40/s1600/cancha.jpg"),
                    contentDescription = "Imagen de cancha",
                    modifier = Modifier.size(520.dp, 230.dp)

                )
                Text(
                    text = "Datos de la reserva #${data?.id_rent}",
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = "Cancha: ${data?.id_club}",
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Ubicacion: ${data?.location}", modifier = Modifier
                        .padding(2.dp)
                        .width(320.dp), style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Precio: $${data?.price}",
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "Horario: ${data?.slot}",
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Column() {
                //val isVisible = isVisible.toBoolean()
                ButtonsContent(data, isReserved!!, locationLatLong, viewModel, myPreferences,reservedRent)
            }
        }
    }

    @Composable
    fun ButtonsContent(
        data: Rent,
        isReserved: Boolean,
        locationLatLong: HashMap<String, Double?>?,
        viewModel: DetailRentActivityViewModel,
        myPreferences: SharedPreferences,
        reservedRent: () -> Unit
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
                                    "Esta es una invitacion a jugar un partidito en el club ${data.id_club} a las ${data.slot}. Direccion: ${data.location}"
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
                        val intentUriNavigation = Uri.parse(
                            "google.navigation:q=${locationLatLong?.get("Latitude")},${
                                locationLatLong?.get("Longitude")
                            }"
                        )
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
            if (isReserved) {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                    onClick = {
                        //logica cancelar reserva

                        val user = myPreferences.getString("user", "")
                        Log.d("cancelar", "DetailRentActivity call cancelrent $user")
                        val resultadoo = viewModel.cancelRent(data.id_rent!!, data.id_club!!, user!!)
                    },
                    modifier = Modifier.padding(all = Dp(10F)),
                    enabled = true,
                    shape = MaterialTheme.shapes.medium,
                )
                {
                    Text(text = "Cancelar reserva", color = Color.White)
                }
            } else {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onSecondary),
                    onClick = { reservedRent() },
                    modifier = Modifier.padding(all = Dp(10F)),
                    enabled = true,
                    shape = MaterialTheme.shapes.medium,
                )
                {
                    Text(text = "Realizar reserva", color = Color.White)
                }
            }
        }

    }


    @Composable
    fun RotationPortrait(
        data: Rent,
        isReserved: Boolean,
        locationLatLong: HashMap<String, Double?>?,
        viewModel: DetailRentActivityViewModel,
        myPreferences: SharedPreferences,
        reservedRent: () -> Unit
    ) {
        val configuration = LocalConfiguration.current
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                ClubContent(data, isReserved, locationLatLong, viewModel, myPreferences,reservedRent)
            }
            else -> {
                ClubContent(data, isReserved, locationLatLong, viewModel, myPreferences,reservedRent)
            }
        }
    }
}