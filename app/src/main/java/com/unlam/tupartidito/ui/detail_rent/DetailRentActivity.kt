package com.unlam.tupartidito.ui.detail_rent

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.unlam.tupartidito.R
import com.unlam.tupartidito.ui.detail_rent.ui.theme.TuPartiditoTheme
import com.unlam.tupartidito.ui.detail_rent.ui.theme.VerdeCompartir
import com.unlam.tupartidito.ui.detail_rent.ui.theme.VerdeFondo

class RentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TuPartiditoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val data = intent.getStringArrayExtra("data")!!
                    Datos(data)

                }
            }
        }

    }
}

@Composable
fun Greeting(name: String) {
    Column(){
        Text(text = "Hello $name!")
        Text(text = "Hello $name!")
        Text(text = "Hello $name!")
    }
}
@Composable
fun Datos(datos: Array<String>) {
    Column() {



    Column(
        //modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.verdeFondo),
        modifier = Modifier
            .size(520.dp, 500.dp)
            .background(MaterialTheme.colors.onSecondary),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
        MyButton(datos)
        }
    }
}
@Composable
fun MyButton(datos: Array<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
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
            border = BorderStroke(width = 0.5.dp, brush = SolidColor(Color.Black)),
            shape = MaterialTheme.shapes.medium,
        )
        {
            Text(text = "Compartir", color = Color.White)
        }
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
            onClick = {
                      //logica cancelar reserva
            },
            modifier = Modifier.padding(all = Dp(10F)),
            enabled = true,
            border = BorderStroke(width = 0.5.dp, brush = SolidColor(Color.Black)),
            shape = MaterialTheme.shapes.medium,
        )
        {
            Text(text = "Cancelar reserva", color = Color.White)
        }
    }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TuPartiditoTheme {
        Greeting("Android")
    }
}