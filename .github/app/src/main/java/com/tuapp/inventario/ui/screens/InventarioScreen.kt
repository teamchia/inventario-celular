package com.tuapp.inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tuapp.inventario.ui.components.RepuestoCard
import com.tuapp.inventario.viewmodel.RepuestoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    viewModel: RepuestoViewModel,
    onAgregar: () -> Unit,
    onVerDetalle: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📱 Inventario", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {
                        if (uiState.mostrarStockBajo) viewModel.mostrarTodos()
                        else viewModel.mostrarStockBajo()
                    }) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "Stock bajo",
                            tint = if (uiState.mostrarStockBajo) Color(0xFFFF9800) else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregar,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            OutlinedTextField(
                value = uiState.busqueda,
                onValueChange = { viewModel.buscar(it) },
                placeholder = { Text("Buscar por modelo, marca o tipo...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (uiState.busqueda.isNotBlank()) {
                        IconButton(onClick = { viewModel.buscar("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                singleLine = true
            )

            Text(
                text = "${uiState.repuestos.size} repuestos",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (uiState.repuestos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "📦", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No hay repuestos", color = Color.Gray)
                        Text("Toca + para agregar uno", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                    items(items = uiState.repuestos, key = { it.id }) { repuesto ->
                        RepuestoCard(
                            repuesto = repuesto,
                            onClick = { onVerDetalle(repuesto.id) }
                        )
                    }
                }
            }
        }
    }
}
