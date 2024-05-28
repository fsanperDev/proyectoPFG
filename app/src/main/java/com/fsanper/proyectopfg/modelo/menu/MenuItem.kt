package com.fsanper.proyectopfg.modelo.menu

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class que representa un elemento del menú.
 * @param title Título de la opción del menú.
 * @param icon Icono que se muestra junto al título en el menú.
 */
data class MenuItem(
    val title: String,      // Título de la opción del menú
    val icon: ImageVector   // Icono que se muestra en el menú
)