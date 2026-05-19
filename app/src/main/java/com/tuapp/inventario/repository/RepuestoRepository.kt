package com.tuapp.inventario.repository

import com.tuapp.inventario.data.Repuesto
import com.tuapp.inventario.data.RepuestoDao
import kotlinx.coroutines.flow.Flow

class RepuestoRepository(private val dao: RepuestoDao) {
    fun obtenerTodos(): Flow<List<Repuesto>> = dao.obtenerTodos()
    fun buscar(texto: String): Flow<List<Repuesto>> = dao.buscar(texto)
    fun filtrarPorEstado(estado: String): Flow<List<Repuesto>> = dao.filtrarPorEstado(estado)
    fun stockBajo(): Flow<List<Repuesto>> = dao.stockBajo()
    suspend fun obtenerPorId(id: Int): Repuesto? = dao.obtenerPorId(id)
    suspend fun insertar(repuesto: Repuesto) = dao.insertar(repuesto)
    suspend fun actualizar(repuesto: Repuesto) = dao.actualizar(repuesto)
    suspend fun eliminar(repuesto: Repuesto) = dao.eliminar(repuesto)
}
