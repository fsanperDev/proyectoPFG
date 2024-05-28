package com.fsanper.proyectopfg.modelo.usuario

/**
 * Data class que representa un usuario.
 * @param userId Identificador único asociado al usuario, por ejemplo, en la autenticación.
 * @param user Nombre de usuario.
 * @param name Nombre completo del usuario.
 * @param email Dirección de correo electrónico del usuario.
 * @param password Contraseña del usuario.
 */
data class Usuario(
    val userId: String?,    // Identificador único asociado al usuario, por ejemplo, en la autenticación
    val user: String?,      // Nombre de usuario
    val name: String?,      // Nombre completo del usuario
    val email: String?,     // Dirección de correo electrónico del usuario
    val password: String?   // Contraseña del usuario
)
