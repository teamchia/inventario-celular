package com.tuapp.inventario.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tuapp.inventario.ui.screens.AgregarEditarScreen
import com.tuapp.inventario.ui.screens.DetalleScreen
import com.tuapp.inventario.ui.screens.InventarioScreen
import com.tuapp.inventario.viewmodel.RepuestoViewModel

object Rutas {
    const val INVENTARIO = "inventario"
    const val AGREGAR = "agregar"
    const val EDITAR = "editar/{repuestoId}"
    const val DETALLE = "detalle/{repuestoId}"
    fun editarRuta(id: Int) = "editar/$id"
    fun detalleRuta(id: Int) = "detalle/$id"
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: RepuestoViewModel) {
    NavHost(navController = navController, startDestination = Rutas.INVENTARIO) {
        composable(Rutas.INVENTARIO) {
            InventarioScreen(
                viewModel = viewModel,
                onAgregar = { navController.navigate(Rutas.AGREGAR) },
                onVerDetalle = { navController.navigate(Rutas.detalleRuta(it)) }
            )
        }
        composable(Rutas.AGREGAR) {
            AgregarEditarScreen(
                viewModel = viewModel,
                repuestoId = null,
                onGuardar = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.EDITAR,
            arguments = listOf(navArgument("repuestoId") { type = NavType.IntType })
        ) { backStackEntry ->
            AgregarEditarScreen(
                viewModel = viewModel,
                repuestoId = backStackEntry.arguments?.getInt("repuestoId"),
                onGuardar = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("repuestoId") { type = NavType.IntType })
        ) { backStackEntry ->
            DetalleScreen(
                viewModel = viewModel,
                repuestoId = backStackEntry.arguments?.getInt("repuestoId") ?: 0,
                onEditar = { navController.navigate(Rutas.editarRuta(it)) },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
