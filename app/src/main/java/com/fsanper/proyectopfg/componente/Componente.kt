package com.fsanper.proyectopfg.componente

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.fsanper.proyectopfg.modelo.videojuego.VideoJuegosLista

@Composable
fun CardJuego(
    juego: VideoJuegosLista,
    onClick: () -> Unit
){
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .shadow(40.dp)
            .clickable { onClick }
    ){
        Column {
            InicioImagen(imagen = juego.imagen)
        }
    }
}

@Composable
fun InicioImagen(
    imagen: String
){
    val imagen = rememberImagePainter(data = imagen)

    Image(
        painter = imagen,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}