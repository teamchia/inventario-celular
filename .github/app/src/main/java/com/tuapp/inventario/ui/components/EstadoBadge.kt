package com.tuapp.inventario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstadoBadge(estado: String) {
    val color = when (estado) {
        "Nuevo"      -> Color(0xFF4CAF50)
        "Usado"      -> Color(0xFF2196F3)
        "Defectuoso" -> Color(0xFFF44336)
        "Sin probar" -> Color(0xFFFF9800)
        "Probado"    -> Color(0xFF9C27B0)
        else         -> Color(0xFF607D8B)
    }
    Text(
        text = estado,
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}
