package com.tuapp.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TipoRepuesto(val label: String) {
    PANTALLA("Pantalla"),
    BATERIA("Batería"),
    CAMARA("Cámara"),
    FLEX("Flex"),
    TAPA("Tapa"),
    PIN_CARGA("Pin de carga"),
    PARLANTE("Parlante"),
    MICROFONO("Micrófono"),
    OTRO("Otro")
}

enum class EstadoRepuesto(val label: String) {
    NUEVO("Nuevo"),
    USADO("Usado"),
    DEFECTUOSO("Defectuoso"),
    SIN_PROBAR("Sin probar"),
    PROBADO("Probado")
}

@Entity(tableName = "repuestos")
data class Repuesto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipo: String,
    val modelo: String,
    val marca: String,
    val cantidad: Int,
    val estado: String,
    val observacion: String = "",
    val stockMinimo: Int = 2,
    val fechaActualizacion: Long = System.currentTimeMillis()
)
