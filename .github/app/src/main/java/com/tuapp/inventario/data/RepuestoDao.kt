package com.tuapp.inventario.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepuestoDao {

    @Query("SELECT * FROM repuestos ORDER BY modelo ASC")
    fun obtenerTodos(): Flow<List<Repuesto>>

    @Query("""
        SELECT * FROM repuestos 
        WHERE modelo LIKE '%' || :busqueda || '%' 
        OR marca LIKE '%' || :busqueda || '%'
        OR tipo LIKE '%' || :busqueda || '%'
        ORDER BY modelo ASC
    """)
    fun buscar(busqueda: String): Flow<List<Repuesto>>

    @Query("SELECT * FROM repuestos WHERE estado = :estado ORDER BY modelo ASC")
    fun filtrarPorEstado(estado: String): Flow<List<Repuesto>>

    @Query("SELECT * FROM repuestos WHERE cantidad <= stockMinimo")
    fun stockBajo(): Flow<List<Repuesto>>

    @Query("SELECT * FROM repuestos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Repuesto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(repuesto: Repuesto)

    @Update
    suspend fun actualizar(repuesto: Repuesto)

    @Delete
    suspend fun eliminar(repuesto: Repuesto)
}
