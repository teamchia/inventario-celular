package com.tuapp.inventario.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresClaro = lightColorScheme(
    primary = Color(0xFF1565C0),
    secondary = Color(0xFF0288D1),
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF1565C0)
)

@Composable
fun InventarioTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresClaro, content = content)
}
