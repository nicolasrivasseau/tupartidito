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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.ui.detail_rent.ui.theme.TuPartiditoTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailRentActivity : ComponentActivity() {
    private lateinit var myPreferences: SharedPreferences
    private lateinit var isReserved: MutableState<Boolean>
    private val viewModel: DetailRentActivityViewModel by viewModels()

    private var locationLatLong: HashMap<String, Double?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
        val idRent = intent.getStringExtra(Constants.DATA_ID_RENT)!!
        val idClub = intent.getStringExtra(Constants.DATA_ID_CLUB)!!
        val isReserved = intent.getBooleanExtra(Constants.RENT_IS_RESERVED, false)

        setContent {
            TuPartiditoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    //RentsScreenContent()
                    RentScreen(idRent, idClub, isReserved)
                }
            }
        }
    }

    @Composable
    fun RentScreen(idRent: String, idClub: String, isReserved: Boolean) {
        val state = viewModel.state.observeAsState()
        val isCreated = viewModel.isCreated.observeAsState()
        val username = myPreferences.getString("user", "")
        viewModel.setIdClub(idClub)
        viewModel.setUsernameAndIdRent(username.toString(), idRent)

        when (state.value) {
            is DetailRentActivityViewModel.State.Loading -> LoadingScreen()
            is DetailRentActivityViewModel.State.Success -> RentDetail(state as MutableState<DetailRentActivityViewModel.State.Success>)
        }
    }

    @Composable
    fun RentDetail(state: MutableState<DetailRentActivityViewModel.State.Success>) {
        val club = state.value.club
        val rent = state.value.rent
        rent!!.location = club!!.location
        locationLatLong?.put("Latitude", club?.latitude)
        locationLatLong?.put("Longitude", club?.longitude)
        DetailRent(
            dataRent = rent,
            dataClub = club
        )
    }

    @Composable
    fun DetailRent(
        dataRent: Rent,
        dataClub: Club,
    ) {
        Column(
            modifier = Modifier
                .size(520.dp, 500.dp)
                .background(MaterialTheme.colors.onSecondary)
                .border(0.5.dp, Color.Black),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImageClub(dataClub.url_image.toString())
            TextRent(
                message = "Datos de la reserva #${dataRent.id_rent}",
                textStyle = MaterialTheme.typography.h6
            )
            TextRent(message = "Cancha: ${dataRent?.id_club}")
            TextRent(message = "Ubicacion: ${dataClub?.location}")
            TextRent(message = "Precio: $${dataRent?.price}")
            TextRent(message = "Horario: ${dataRent?.slot}")

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ButtonRent("COMO LLEGAR") { howToGet() }
            ButtonRent("COMPARTIR") { sharedWhatsapp(dataRent = dataRent) }
        }
    }

    private fun sharedWhatsapp(dataRent:Rent) {
        try {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Esta es una invitacion a jugar un partidito en el club ${dataRent.id_club} a las ${dataRent.slot}. Direccion: ${dataRent.location}"
                )
                type = "text/plain"
                setPackage("com.whatsapp")
                startActivity(this)
            }
        } catch (ignored: ActivityNotFoundException) {
            Toast.makeText(this, "No se encontro app", Toast.LENGTH_LONG).show()
        }
    }

    private fun howToGet(){
        val intentUriNavigation = Uri.parse(
            "google.navigation:q=${locationLatLong?.get("Latitude")},${
                locationLatLong?.get("Longitude")
            }"
        )
        Intent(Intent.ACTION_VIEW, intentUriNavigation).apply {
            setPackage("com.google.android.apps.maps")
            startActivity(this)
        }
    }

    @Composable
    fun ImageClub(url: String) {
        Image(
            painter = rememberImagePainter(url),
            contentDescription = "Imagen de cancha",
            modifier = Modifier.size(520.dp, 230.dp)
        )
    }

    @Composable
    fun TextRent(
        message: String,
        textStyle: TextStyle = MaterialTheme.typography.subtitle1
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(2.dp),
            style = textStyle
        )
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
    fun ButtonRent(textContent: String, onClicked: () -> Unit) {
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onPrimary),
            onClick = { onClicked() },
            modifier = Modifier.padding(all = Dp(10F)),
            enabled = true,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = textContent, color = Color.White)
        }
    }

    @Composable
    fun RentsScreenContent() {

//        val viewModel = viewModels<DetailRentActivityViewModel>().value
//        val state = viewModel.state.observeAsState()
//        val isCreated = viewModel.isCreated.observeAsState("")
//
//        val username = myPreferences.getString("user", "")
//        viewModel.setUsernameAndIdRent(username!!, idRent)
//        viewModel.setIdClub(idClub)
//        isReserved = remember { mutableStateOf(isVisible) }
//
//        when (state.value) {
//            is DetailRentActivityViewModel.State.Loading -> LoadingScreen()
//            is DetailRentActivityViewModel.State.Success -> {
//
//                val rent = (state.value as DetailRentActivityViewModel.State.Success).rent
//                if (rent != null) {
//                    rent.id_club?.let { viewModel.setIdClub(it) }
//                }
//                val club = (state.value as DetailRentActivityViewModel.State.Success).club
//
//                locationLatLong?.put("Latitude", club?.latitude)
//                locationLatLong?.put("Longitude", club?.longitude)
//                rent?.id_club = club?.id
//                rent?.location = club?.location
//                RotationPortrait(
//                    data = rent!!,
//                    isReserved = isReserved.value,
//                    locationLatLong = locationLatLong,
//                    viewModel = viewModel,
//                    myPreferences = myPreferences,
//                    reservedRent = {
//                        viewModel.createRent(
//                            idRent = rent.id_rent!!,
//                            idCLub = idClub,
//                            idUser = username,
//                            location = rent.location,
//                            price = rent.price,
//                            slot = rent.slot
//                        )
//                    },
//                    isCreated = isCreated.value
//                )
//            }
//        }
    }

    @Composable
    fun ClubContent(
        data: Rent,
        isReserved: Boolean,
        locationLatLong: HashMap<String, Double?>?,
        viewModel: DetailRentActivityViewModel,
        myPreferences: SharedPreferences,
        reservedRent: () -> Unit,
        isCreated: String?
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
                    text = "Ubicacion: ${data?.location}",
                    modifier = Modifier
                        .padding(2.dp)
                        .width(320.dp),
                    style = MaterialTheme.typography.subtitle1
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
                ButtonsContent(
                    data,
                    isReserved!!,
                    locationLatLong,
                    viewModel,
                    myPreferences,
                    reservedRent
                )
            }
        }
        if (isCreated != "") {
            ButtonsContent(
                data = data,
                isReserved = true,
                locationLatLong = locationLatLong,
                viewModel = viewModel,
                myPreferences = myPreferences,
                reservedRent
            )
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
            val context = LocalContext.current

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
                        val resultadoo =
                            viewModel.cancelRent(data.id_rent!!, data.id_club!!, user!!)
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
                    onClick = {
                        reservedRent()
                    },
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
        reservedRent: () -> Unit,
        isCreated: String?
    ) {
        val configuration = LocalConfiguration.current
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                ClubContent(
                    data,
                    isReserved,
                    locationLatLong,
                    viewModel,
                    myPreferences,
                    reservedRent,
                    isCreated
                )
            }
            else -> {
                ClubContent(
                    data,
                    isReserved,
                    locationLatLong,
                    viewModel,
                    myPreferences,
                    reservedRent,
                    isCreated
                )
            }
        }
    }
}