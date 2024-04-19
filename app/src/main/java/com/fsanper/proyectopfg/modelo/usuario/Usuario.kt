package com.fsanper.proyectopfg.modelo.usuario

data class Usuario(
    val id: String?,        // Identificador único del usuario en el sistema.
    val userId: String?,    // Identificador único asociado al usuario, por ejemplo, en la autenticación.
    val user: String?,      // Nombre de usuario.
    val name: String?,      // Nombre completo del usuario.
    val email: String?,     // Dirección de correo electrónico del usuario.
    val password: String?   // Contraseña del usuario.
)
