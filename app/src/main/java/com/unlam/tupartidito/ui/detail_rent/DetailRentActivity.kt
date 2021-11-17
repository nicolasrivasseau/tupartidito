package com.unlam.tupartidito.ui.detail_rent

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.toast
import com.unlam.tupartidito.data.model.club.Club
import com.unlam.tupartidito.data.model.user.Rent
import com.unlam.tupartidito.ui.detail_rent.ui.theme.TuPartiditoTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailRentActivity : ComponentActivity() {
    private val viewModel: DetailRentActivityViewModel by viewModels()
    private var isReserved: Boolean = false
    private var isCanceled: Boolean = false
    private var itsMine: Boolean = false

    private var locationLatLong: HashMap<String, Double?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idRent = intent.getStringExtra(Constants.DATA_ID_RENT)!!
        val idClub = intent.getStringExtra(Constants.DATA_ID_CLUB)!!
        isReserved = intent.getBooleanExtra(Constants.RENT_IS_RESERVED, false)
        isCanceled = intent.getBooleanExtra(Constants.RENT_IS_CANCELED, false)
        itsMine = intent.getBooleanExtra(Constants.RENT_IS_MINE, false)

        setContent {
            TuPartiditoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    RentScreen(idRent, idClub, isReserved)
                }
            }
        }
    }

    @Composable
    fun RentScreen(idRent: String, idClub: String, isReserved: Boolean) {
        val state = viewModel.state.observeAsState()
        viewModel.isCanceled.observe(this,{
            if(it != "") {
                toast(it, 1)
            }
        })
        viewModel.isCreated.observe(this,{
            if(it != "") {
                toast(it,1)
            }
        })
        viewModel.isMine.observe(this,{
            itsMine = it
        })
        viewModel.setUsername(this)
        viewModel.setIdClub(idClub)
        viewModel.setUsernameAndIdRent(idRent)
        viewModel.getRentUser(idRent)

        when (state.value) {
            is DetailRentActivityViewModel.State.Loading -> LoadingScreen()
            is DetailRentActivityViewModel.State.Success -> RentDetail(
                state as MutableState<DetailRentActivityViewModel.State.Success>
            )
        }
    }

    @Composable
    fun RentDetail(
        state: MutableState<DetailRentActivityViewModel.State.Success>
    ) {
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
        dataClub: Club
    ) {
        val reserved = remember { mutableStateOf(isReserved) }
        val username = viewModel.username
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
                    .fillMaxHeight().padding(0.dp, 0.dp, 5.dp,50.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ButtonRent(textContent = "COMO LLEGAR") { howToGet() }
                ButtonRent(textContent = "COMPARTIR") { sharedWhatsapp(dataRent = dataRent) }
                if (reserved.value ) {
                    if(viewModel.isMine.value!!){
                        ButtonRent(textContent = "CANCELAR RESERVA",colors = ButtonDefaults.buttonColors(backgroundColor =MaterialTheme.colors.error)){
                            viewModel.cancelRent(idRent = dataRent.id_rent!!, dataRent.id_club!!,username)
                            reserved.value = false
                        }
                    }
                } else {
                    ButtonRent(textContent = "RESERVAR"){
                        viewModel.createRent(idRent = dataRent.id_rent!!,idCLub = dataRent.id_club!!,idUser = username,location = dataRent.location,price = dataRent.price,slot = dataRent.slot)
                        reserved.value = true
                    }
                }
            }
    }

    private fun sharedWhatsapp(dataRent: Rent) {
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

    private fun howToGet() {
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
    fun ButtonRent(
        textContent: String,
        colors: ButtonColors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onPrimary),
        onClicked: () -> Unit
    ) {
        Button(
            colors = colors,
            onClick = { onClicked() },
            modifier = Modifier.padding(all = Dp(10F)),
            enabled = true,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = textContent, color = Color.White)
        }
    }
}