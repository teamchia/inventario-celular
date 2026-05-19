package com.tuapp.inventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.tuapp.inventario.data.AppDatabase
import com.tuapp.inventario.navigation.NavGraph
import com.tuapp.inventario.repository.RepuestoRepository
import com.tuapp.inventario.ui.theme.InventarioTheme
import com.tuapp.inventario.viewmodel.RepuestoViewModel
import com.tuapp.inventario.viewmodel.RepuestoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = RepuestoRepository(database.repuestoDao())
        val factory = RepuestoViewModelFactory(repository)
        setContent {
            InventarioTheme {
                val navController = rememberNavController()
                val viewModel: RepuestoViewModel = viewModel(factory = factory)
                NavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
