package com.tuapp.inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tuapp.inventario.data.EstadoRepuesto
import com.tuapp.inventario.data.TipoRepuesto
import com.tuapp.inventario.viewmodel.RepuestoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarEditarScreen(
    viewModel: RepuestoViewModel,
    repuestoId: Int?,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    val repuestoExistente by viewModel.repuestoSeleccionado.collectAsState()
    var tipo by remember { mutableStateOf(TipoRepuesto.PANTALLA.label) }
    var modelo by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("1") }
    var estado by remember { mutableStateOf(EstadoRepuesto.NUEVO.label) }
    var observacion by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("2") }
    var expandirTipo by remember { mutableStateOf(false) }
    var expandirEstado by remember { mutableStateOf(false) }
    var modeloError by remember { mutableStateOf(false) }
    var marcaError by remember { mutableStateOf(false) }
    val esEdicion = repuestoId != null

    LaunchedEffect(repuestoExistente) {
        repuestoExistente?.let { r ->
            tipo = r.tipo; modelo = r.modelo; marca = r.marca
            cantidad = r.cantidad.toString(); estado = r.estado
            observacion = r.observacion; stockMinimo = r.stockMinimo.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (esEdicion) "Editar repuesto" else "Agregar repuesto", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Tipo de repuesto", fontWeight = FontWeight.SemiBold)
            ExposedDropdownMenuBox(expanded = expandirTipo, onExpandedChange = { expandirTipo = it }) {
                OutlinedTextField(
                    value = tipo, onValueChange = {}, readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirTipo) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = expandirTipo, onDismissRequest = { expandirTipo = false }) {
                    TipoRepuesto.values().forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t.label) },
                            onClick = { tipo = t.label; expandirTipo = false }
                        )
                    }
                }
            }

            Text("Marca", fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it; marcaError = false },
                placeholder = { Text("Ej: Samsung, iPhone, Xiaomi...") },
                isError = marcaError,
                supportingText = { if (marcaError) Text("Campo obligatorio") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Modelo", fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it; modeloError = false },
                placeholder = { Text("Ej: Galaxy A14, iPhone 11...") },
                isError = modeloError,
                supportingText = { if (modeloError) Text("Campo obligatorio") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Estado", fontWeight = FontWeight.SemiBold)
            ExposedDropdownMenuBox(expanded = expandirEstado, onExpandedChange = { expandirEstado = it }) {
                OutlinedTextField(
                    value = estado, onValueChange = {}, readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirEstado) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = expandirEstado, onDismissRequest = { expandirEstado = false }) {
                    EstadoRepuesto.values().forEach { e ->
                        DropdownMenuItem(
                            text = { Text(e.label) },
                            onClick = { estado = e.label; expandirEstado = false }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Cantidad", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { if (it.all { c -> c.isDigit() }) cantidad = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Stock mínimo", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = stockMinimo,
                        onValueChange = { if (it.all { c -> c.isDigit() }) stockMinimo = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Text("Observación", fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = observacion,
                onValueChange = { observacion = it },
                placeholder = { Text("Notas adicionales...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    modeloError = modelo.isBlank()
                    marcaError = marca.isBlank()
                    if (!modeloError && !marcaError) {
                        if (esEdicion && repuestoExistente != null) {
                            viewModel.actualizarRepuesto(
                                repuestoExistente!!.copy(
                                    tipo = tipo, modelo = modelo, marca = marca,
                                    cantidad = cantidad.toIntOrNull() ?: 0,
                                    estado = estado, observacion = observacion,
                                    stockMinimo = stockMinimo.toIntOrNull() ?: 2
                                )
                            )
                        } else {
                            viewModel.agregarRepuesto(
                                tipo = tipo, modelo = modelo, marca = marca,
                                cantidad = cantidad.toIntOrNull() ?: 0,
                                estado = estado, observacion = observacion,
                                stockMinimo = stockMinimo.toIntOrNull() ?: 2
                            )
                        }
                        onGuardar()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(if (esEdicion) "Guardar cambios" else "Agregar repuesto", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Cancelar") }
        }
    }
}
