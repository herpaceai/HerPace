package com.herpace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.herpace.data.remote.ApiResult
import com.herpace.data.repository.AuthTokenProvider
import com.herpace.domain.repository.ProfileRepository
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import com.herpace.presentation.common.LoadingIndicator
import com.herpace.presentation.navigation.BottomNavBar
import com.herpace.presentation.navigation.NavGraph
import com.herpace.presentation.navigation.Screen
import com.herpace.presentation.navigation.bottomNavItems
import com.herpace.presentation.theme.HerPaceTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authTokenProvider: AuthTokenProvider

    @Inject
    lateinit var profileRepository: ProfileRepository

    private var startDestination by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        determineStartDestination()

        setContent {
            HerPaceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val destination = startDestination
                    if (destination == null) {
                        LoadingIndicator(modifier = Modifier.padding(innerPadding))
                    } else {
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val showBottomBar by remember {
                            derivedStateOf {
                                val currentRoute = navBackStackEntry?.destination?.route
                                currentRoute in bottomNavItems.map { it.route }
                            }
                        }

                        Scaffold(
                            modifier = Modifier.padding(innerPadding),
                            bottomBar = {
                                if (showBottomBar) {
                                    BottomNavBar(navController = navController)
                                }
                            }
                        ) { innerScaffoldPadding ->
                            NavGraph(
                                navController = navController,
                                startDestination = destination,
                                modifier = Modifier.padding(innerScaffoldPadding)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun determineStartDestination() {
        if (!authTokenProvider.isLoggedIn()) {
            startDestination = Screen.Login.route
            return
        }

        lifecycleScope.launch {
            val result = profileRepository.getProfile()
            startDestination = when {
                result is ApiResult.Success && result.data != null -> Screen.Dashboard.route
                result is ApiResult.Success && result.data == null -> Screen.Onboarding.route
                else -> Screen.Dashboard.route
            }
        }
    }
}
