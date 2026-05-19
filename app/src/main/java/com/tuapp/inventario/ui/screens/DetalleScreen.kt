package com.tuapp.inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuapp.inventario.ui.components.EstadoBadge
import com.tuapp.inventario.viewmodel.RepuestoViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: RepuestoViewModel,
    repuestoId: Int,
    onEditar: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val repuesto by viewModel.repuestoSeleccionado.collectAsState()
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var mostrarDialogoStock by remember { mutableStateOf(false) }
    var esDescontar by remember { mutableStateOf(true) }
    var cantidadMovimiento by remember { mutableStateOf("1") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { repuesto?.let { onEditar(it.id) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { mostrarDialogoEliminar = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        repuesto?.let { r ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(r.tipo, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        Text(r.modelo, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text(r.marca, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        EstadoBadge(estado = r.estado)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (r.cantidad <= r.stockMinimo) Color(0xFFFFF3E0)
                        else MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Cantidad en stock", color = Color.Gray)
                            Text("${r.cantidad} unidades", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                            if (r.cantidad <= r.stockMinimo) {
                                Text("⚠️ Stock bajo (mínimo: ${r.stockMinimo})", color = Color(0xFFE65100), fontSize = 12.sp)
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(
                                onClick = { esDescontar = false; mostrarDialogoStock = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) { Icon(Icons.Default.Add, contentDescription = "Agregar") }
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = { esDescontar = true; mostrarDialogoStock = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                            ) { Icon(Icons.Default.Remove, contentDescription = "Descontar") }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (r.observacion.isNotBlank()) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Observación", fontWeight = FontWeight.SemiBold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(r.observacion)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                Text("Actualizado: ${formato.format(Date(r.fechaActualizacion))}", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("¿Eliminar repuesto?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    repuesto?.let { viewModel.eliminarRepuesto(it) }
                    mostrarDialogoEliminar = false
                    onVolver()
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) { Text("Cancelar") }
            }
        )
    }

    if (mostrarDialogoStock) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoStock = false },
            title = { Text(if (esDescontar) "Descontar stock" else "Agregar stock") },
            text = {
                OutlinedTextField(
                    value = cantidadMovimiento,
                    onValueChange = { if (it.all { c -> c.isDigit() }) cantidadMovimiento = it },
                    label = { Text("Cantidad") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    repuesto?.let { r ->
                        val cant = cantidadMovimiento.toIntOrNull() ?: 1
                        if (esDescontar) viewModel.descontarStock(r, cant)
                        else viewModel.agregarStock(r, cant)
                    }
                    mostrarDialogoStock = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoStock = false }) { Text("Cancelar") }
            }
        )
    }
}
