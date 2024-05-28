package com.fsanper.proyectopfg.modelo.videojuego

import com.google.gson.annotations.SerializedName

/**
 * Data class que representa la respuesta que contiene una lista de géneros.
 * @param listaGenero Lista de géneros obtenida de la respuesta.
 */
data class Genero(
    @SerializedName("results")
    val listaGenero: List<GeneroList> // Lista de géneros obtenida de la respuesta
)

/**
 * Data class que representa un género individual en la lista de géneros.
 * @param slug Identificador único o "slug" del género.
 */
data class GeneroList(
    @SerializedName("slug")
    val slug: String // Identificador único o "slug" del género
)