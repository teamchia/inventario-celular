package com.tuapp.inventario.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuapp.inventario.data.Repuesto

@Composable
fun RepuestoCard(repuesto: Repuesto, onClick: () -> Unit) {
    val stockBajo = repuesto.cantidad <= repuesto.stockMinimo
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (stockBajo) Color(0xFFFFF3E0)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${repuesto.tipo} - ${repuesto.marca}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = repuesto.modelo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                EstadoBadge(estado = repuesto.estado)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (stockBajo) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Stock bajo",
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${repuesto.cantidad}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (stockBajo) Color(0xFFE65100)
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(text = "unidades", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}
