package com.fsanper.proyectopfg.modelo.videojuego

import com.google.gson.annotations.SerializedName

data class VideoJuego(
    @SerializedName("results")
    val listaVideojuegos: List<VideoJuegosLista>
)

data class VideoJuegosLista(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val nombre: String,
    @SerializedName("released")
    val released: String,
    @SerializedName("metacritic")
    val metacritic: String,
    @SerializedName("background_image")
    val imagen: String,
)