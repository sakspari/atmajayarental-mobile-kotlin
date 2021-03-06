package com.example.atmajayarental

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atmajayarental.data.api.model.AuthResponse
import com.example.atmajayarental.data.userpreferences.UserPreferencesImpl
import com.example.atmajayarental.ui.auth.AuthScreen
import com.example.atmajayarental.ui.home.HomeScreen
import com.example.atmajayarental.ui.home.customer.CustomerHomeScreen
import com.example.atmajayarental.ui.home.driver.DriverHomeScreen
import com.example.atmajayarental.ui.home.manager.ManagerHomeScreen
import com.example.atmajayarental.ui.home.profil.ProfilScreen
import com.example.atmajayarental.ui.laporan.LaporanScreen
import com.example.atmajayarental.ui.promo.MobilScreen
import com.example.atmajayarental.ui.promo.PromoScreen
import com.example.atmajayarental.ui.theme.AtmaJayaRentalTheme
import com.example.atmajayarental.ui.transaksi.TransaksiScreen
import com.example.atmajayarental.util.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferences: UserPreferencesImpl


    var authResponse: MutableLiveData<AuthResponse> = MutableLiveData()
    var startRoute by mutableStateOf(Routes.AUTH)
        private set

//    suspend fun getUserLogin(){
//        userPreferences.getUserLogin().collect {
//            authResponse.postValue(it)
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferences = UserPreferencesImpl(this)
        CoroutineScope(Dispatchers.IO).launch {
            userPreferences.getUserLogin().collect {
                authResponse.postValue(it)
                if (it.user != null) {
                    when (it.user.level) {
                        "CUSTOMER" -> startRoute = Routes.HOME_CUSTOMER
                        "DRIVER" -> startRoute = Routes.HOME_DRIVER
                        else -> startRoute = Routes.HOME_MANAGER
                    }
                }
//                    startRoute = Routes.HOME
            }
        }

        setContent {
            AtmaJayaRentalTheme {
                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    NavHost(navController = navController, startDestination = startRoute) {

                        composable(Routes.AUTH) {
                            AuthScreen(onNavigate = {
                                navController.popBackStack(route = Routes.AUTH, inclusive = true)
                                navController.navigate(it.route) {
                                    launchSingleTop = true
//                                    navController.clearBackStack(navController.currentDestination?.id!!)
                                }
                            })

                        }
                        composable(Routes.HOME) {
                            HomeScreen()
                        }
                        composable(Routes.HOME_CUSTOMER) {
                            CustomerHomeScreen(
                                onNavigate = {
                                    navController.navigate(it.route)
                                },
                                onLogout = {
                                    try {
                                        navController.popBackStack(
                                            route = Routes.HOME_CUSTOMER,
                                            inclusive = true
                                        )
                                        navController.navigate(route = Routes.AUTH)
                                    } catch (e: CancellationException) {
                                        throw e
                                    }
                                }
                            )
                        }
                        composable(Routes.HOME_DRIVER) {
                            DriverHomeScreen(onNavigate = {
                                navController.navigate(it.route)
                            },
                                onLogout = {
                                    try {
                                        navController.popBackStack(
                                            route = Routes.HOME_DRIVER,
                                            inclusive = true
                                        )
                                        navController.navigate(route = Routes.AUTH)
                                    } catch (e: CancellationException) {
                                        throw e
                                    }
                                }
                            )
                        }
                        composable(Routes.HOME_MANAGER) {
                            ManagerHomeScreen(onNavigate = {
                                navController.navigate(it.route)
                            },
                                onLogout = {
                                    try {
                                        navController.popBackStack(
                                            route = Routes.HOME_MANAGER,
                                            inclusive = true
                                        )
                                        navController.navigate(route = Routes.AUTH)
                                    } catch (e: CancellationException) {
                                        throw e
                                    }
                                }
                            )
                        }
                        composable(Routes.PROMO) {
                            PromoScreen(
                                onPopBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Routes.PROFIL) {
                            ProfilScreen(
                                onPopBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Routes.MOBIL) {
                            MobilScreen(
                                onPopBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Routes.TRANSAKSI) {
                            TransaksiScreen(
                                onPopBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Routes.LAPORAN_PENYEWAAN_MOBIL) {
                            LaporanScreen(
                                onPopBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AtmaJayaRentalTheme {
        Greeting("Android")
    }
}