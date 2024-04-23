package com.fsanper.proyectopfg.modelo.videojuego

import com.google.gson.annotations.SerializedName

data class VideoJuego(
    @SerializedName("counts")
    val total: Int,
    @SerializedName("results")
    val listaVideojuegos: List<VideoJuegosLista>
)

data class VideoJuegosLista(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val nombre: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("released")
    val released: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("reviews_text_count")
    val reviews: String,
    @SerializedName("metacritic")
    val metacritic: String,
    @SerializedName("background_image")
    val imagen: String,
)