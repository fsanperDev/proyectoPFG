package com.fsanper.proyectopfg.modelo.videojuego

import com.google.gson.annotations.SerializedName

data class DetallesJuego(
    @SerializedName("name")
    val nombre: String,
    @SerializedName("description")
    val descripcion: String,
    @SerializedName("released")
    val lanzamiento: String,
    @SerializedName("background_image")
    val imagen: String,
    @SerializedName("platforms")
    val plataformas: List<Plataforma> // Lista de plataformas del juego
)

data class Plataforma(
    @SerializedName("platform")
    val plataforma: PlatformaDetalle
)

data class PlatformaDetalle(
    @SerializedName("name")
    val nombre: String
)