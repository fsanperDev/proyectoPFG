package com.fsanper.proyectopfg.modelo.videojuego

import com.google.gson.annotations.SerializedName

data class Genero(
    @SerializedName("results")
    val listaGenero: List<GeneroList>
)

data class GeneroList(
    @SerializedName("slug")
    val slug: String
)