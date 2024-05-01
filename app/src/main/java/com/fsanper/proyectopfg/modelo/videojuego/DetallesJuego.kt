package com.fsanper.proyectopfg.modelo.videojuego

import com.google.gson.annotations.SerializedName

data class DetallesJuego(
    @SerializedName("name")
    val nombre: String,
    @SerializedName("description")
    val descripcion: String,
    @SerializedName("released")
    val lanzamiento: String,
    /*@SerializedName("platforms")
    val plataforma: List<Plataforma>,*/
    @SerializedName("background_image")
    val imagen: String
)

/*data class Plataforma(

)*/