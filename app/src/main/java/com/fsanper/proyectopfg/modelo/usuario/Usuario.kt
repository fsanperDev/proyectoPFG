package com.fsanper.proyectopfg.modelo.usuario

data class Usuario(
    val id: String?,        // Identificador único del usuario en el sistema.
    val userId: String?,    // Identificador único asociado al usuario, por ejemplo, en la autenticación.
    val user: String?,      // Nombre de usuario.
    val name: String?,      // Nombre completo del usuario.
    val email: String?,     // Dirección de correo electrónico del usuario.
    val password: String?   // Contraseña del usuario.
) {
    /**
     * Convierte la instancia de la clase User a un MutableMap<String, String?>.
     * @param salt: Valor aleatorio utilizado para mejorar la seguridad del hash de la contraseña.
     * @return MutableMap que representa los pares clave-valor de las propiedades del usuario.
     */
    fun toMap(salt: String): MutableMap<String, String?> {
        return mutableMapOf(
            "user_Id" to this.userId,
            "user" to this.user,
            "name" to this.name,
            "email" to this.email,
            "password" to this.password,
            "salt" to salt
        )
    }
}
