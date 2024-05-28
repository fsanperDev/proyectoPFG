package com.fsanper.proyectopfg.modelo.comentario

/**
 * Data class que representa un comentario.
 * @param idComentario Identificador único del comentario.
 * @param nombreJuego Nombre del juego asociado al comentario.
 * @param comentario Contenido del comentario.
 * @param usuario Nombre del usuario que realizó el comentario.
 * @param idCorreo Dirección de correo electrónico del usuario que realizó el comentario.
 */
data class Comentario(
    var idComentario: Long = 0,      // Identificador único del comentario
    var nombreJuego: String = "",    // Nombre del juego
    var comentario: String = "",     // Texto del comentario
    var usuario: String = "",        // Nombre del usuario
    var idCorreo: String = ""        // Representa el UserId del correo
)
