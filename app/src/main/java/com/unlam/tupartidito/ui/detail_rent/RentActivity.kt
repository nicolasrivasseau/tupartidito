package com.unlam.tupartidito.ui.detail_rent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.unlam.tupartidito.ui.detail_rent.ui.theme.TuPartiditoTheme

class RentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TuPartiditoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val id_rent = intent.getStringExtra("id_rent")
                    val data = intent.getStringArrayExtra("data")!!
                    Datos("Id de reserva: ${data.get(0)}, " +
                            "Ubicacion: ${data.get(1)}, Precio: ${data.get(3)}, Horario: ${data.get(4)}")
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
@Composable
fun Datos(datos: String) {
    Text(text = "$datos")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TuPartiditoTheme {
        Greeting("Android")
    }
}