package com.tuapp.inventario.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tuapp.inventario.data.Repuesto
import com.tuapp.inventario.repository.RepuestoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class InventarioUiState(
    val repuestos: List<Repuesto> = emptyList(),
    val busqueda: String = "",
    val mostrarStockBajo: Boolean = false
)

class RepuestoViewModel(private val repository: RepuestoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(InventarioUiState())
    val uiState: StateFlow<InventarioUiState> = _uiState.asStateFlow()

    private val _repuestoSeleccionado = MutableStateFlow<Repuesto?>(null)
    val repuestoSeleccionado: StateFlow<Repuesto?> = _repuestoSeleccionado.asStateFlow()

    init { cargarRepuestos() }

    private fun cargarRepuestos() {
        viewModelScope.launch {
            repository.obtenerTodos().collect { lista ->
                _uiState.update { it.copy(repuestos = lista) }
            }
        }
    }

    fun buscar(texto: String) {
        _uiState.update { it.copy(busqueda = texto) }
        viewModelScope.launch {
            if (texto.isBlank()) {
                repository.obtenerTodos().collect { lista ->
                    _uiState.update { it.copy(repuestos = lista) }
                }
            } else {
                repository.buscar(texto).collect { lista ->
                    _uiState.update { it.copy(repuestos = lista) }
                }
            }
        }
    }

    fun mostrarStockBajo() {
        viewModelScope.launch {
            repository.stockBajo().collect { lista ->
                _uiState.update { it.copy(repuestos = lista, mostrarStockBajo = true) }
            }
        }
    }

    fun mostrarTodos() {
        _uiState.update { it.copy(mostrarStockBajo = false) }
        cargarRepuestos()
    }

    fun seleccionarRepuesto(repuesto: Repuesto?) {
        _repuestoSeleccionado.value = repuesto
    }

    fun agregarRepuesto(
        tipo: String, modelo: String, marca: String,
        cantidad: Int, estado: String,
        observacion: String, stockMinimo: Int
    ) {
        viewModelScope.launch {
            repository.insertar(
                Repuesto(
                    tipo = tipo, modelo = modelo, marca = marca,
                    cantidad = cantidad, estado = estado,
                    observacion = observacion, stockMinimo = stockMinimo
                )
            )
        }
    }

    fun actualizarRepuesto(repuesto: Repuesto) {
        viewModelScope.launch {
            repository.actualizar(
                repuesto.copy(fechaActualizacion = System.currentTimeMillis())
            )
        }
    }

    fun eliminarRepuesto(repuesto: Repuesto) {
        viewModelScope.launch { repository.eliminar(repuesto) }
    }

    fun descontarStock(repuesto: Repuesto, cantidad: Int) {
        viewModelScope.launch {
            repository.actualizar(
                repuesto.copy(cantidad = (repuesto.cantidad - cantidad).coerceAtLeast(0))
            )
        }
    }

    fun agregarStock(repuesto: Repuesto, cantidad: Int) {
        viewModelScope.launch {
            repository.actualizar(repuesto.copy(cantidad = repuesto.cantidad + cantidad))
        }
    }
}

class RepuestoViewModelFactory(
    private val repository: RepuestoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepuestoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepuestoViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
