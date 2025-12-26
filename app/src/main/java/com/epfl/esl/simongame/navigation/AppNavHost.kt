package com.epfl.esl.simongame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.epfl.esl.simongame.auth.AuthState
import com.epfl.esl.simongame.auth.AuthViewModel
import com.epfl.esl.simongame.data.UserRepository
import com.epfl.esl.simongame.di.LocalAppContainer
import com.epfl.esl.simongame.home.HomeViewModel
import com.epfl.esl.simongame.screens.HomeScreen
import com.epfl.esl.simongame.screens.MultiGameScreen
import com.epfl.esl.simongame.screens.ProfileScreen
import com.epfl.esl.simongame.screens.RegisterScreen
import com.epfl.esl.simongame.screens.SoloGameScreen
import com.epfl.esl.simongame.screens.SoloMotionUiState
import com.epfl.esl.simongame.screens.SoloPhase
import com.epfl.esl.simongame.screens.SoloResultScreen
import com.epfl.esl.simongame.solo_mode.SoloGameViewModel
import com.epfl.esl.simongame.solo_mode.SoloGameViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epfl.esl.simongame.friends.FriendSearchViewModel
import com.epfl.esl.simongame.friends.FriendSearchViewModelFactory
import com.epfl.esl.simongame.screens.FriendSearchScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.epfl.esl.simongame.friends.FriendsListViewModel
import com.epfl.esl.simongame.friends.FriendsListViewModelFactory
import com.epfl.esl.simongame.screens.FriendsListScreen



@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val container = LocalAppContainer.current

    val authVm: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(container.userRepository)
    )
    val authState by authVm.state.collectAsState()

    when (authState) {
        AuthState.Loading -> {
            // Optional: splash screen UI
        }

        AuthState.Unregistered -> {
            RegisterScreen(
                onRegister = { username, email ->
                    authVm.register(username, email)
                }
            )
        }

        AuthState.Registered -> {
            val homeVm: HomeViewModel = viewModel()
            val homeUi by homeVm.uiState.collectAsState()

            NavHost(
                navController = navController,
                startDestination = AppDestinations.HOME,
                modifier = modifier
            ) {

                composable(AppDestinations.HOME) {
                    HomeScreen(
                        onPlaySoloClick = { navController.navigate(AppDestinations.SOLO_GAME) },
                        onPlayVersusClick = { navController.navigate(AppDestinations.MULTI_GAME) },
                        onProfileClick = { navController.navigate(AppDestinations.PROFILE) }
                    )
                }

                composable(AppDestinations.MULTI_GAME) {
                    MultiGameScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(AppDestinations.SOLO_GAME) {
                    val soloVm: SoloGameViewModel = viewModel(
                        factory = SoloGameViewModelFactory(
                            wearConnectionRepository = container.wearConnectionRepository,
                            motionWindowRepository = container.motionWindowRepository,
                            phoneWearClient = container.phoneWearClient,
                            predictor = container.motionToMovePredictor
                        )
                    )
                    val soloState by soloVm.uiState.collectAsState()

                    val phase = when {
                        !soloState.isWatchConnected -> SoloPhase.WaitingWatch
                        soloState.isGameOver -> SoloPhase.GameOver
                        soloState.isCapturing -> SoloPhase.Checking
                        else -> SoloPhase.YourTurn
                    }

                    val uiForScreen = SoloMotionUiState(
                        phase = phase,
                        isWatchConnected = soloState.isWatchConnected,
                        round = soloState.round.coerceAtLeast(1),
                        score = soloState.score,
                        mistakes = soloState.mistakes,
                        timeLeftSec = soloState.timeLeftSec,
                        instruction = soloState.instructionText.ifBlank { "GET READY" }
                    )

                    // Navigate to results when game ends
                    LaunchedEffect(soloState.gameResult) {
                        val r = soloState.gameResult ?: return@LaunchedEffect
                        homeVm.onSoloGameFinished(r)

                        navController.navigate(
                            AppDestinations.soloResultRoute(
                                score = r.finalScore,
                                rounds = r.totalRounds,
                                mistakes = r.mistakes
                            )
                        )
                    }

                    SoloGameScreen(
                        ui = uiForScreen,
                        onBack = { navController.popBackStack() },
                        onPause = { /* TODO if you want */ },
                        onRestart = { soloVm.startGame() },
                        onCalibrate = { /* TODO later */ }
                    )
                }

                composable(AppDestinations.FRIEND_SEARCH) {
                    val container = LocalAppContainer.current

                    val vm: FriendSearchViewModel = viewModel(
                        factory = FriendSearchViewModelFactory(container.userRepository)
                    )
                    val ui by vm.uiState.collectAsState()

                    FriendSearchScreen(
                        ui = ui,
                        onBack = { navController.popBackStack() },
                        onQueryChange = vm::onQueryChange,
                        onAddFriend = vm::addFriend,
                    )
                }


                composable(
                    route = "${AppDestinations.SOLO_RESULT}/{score}/{rounds}/{mistakes}",
                    arguments = listOf(
                        navArgument("score") { defaultValue = 0 },
                        navArgument("rounds") { defaultValue = 0 },
                        navArgument("mistakes") { defaultValue = 0 }
                    )
                ) { backStackEntry ->
                    val score = backStackEntry.arguments?.getInt("score") ?: 0
                    val rounds = backStackEntry.arguments?.getInt("rounds") ?: 0
                    val mistakes = backStackEntry.arguments?.getInt("mistakes") ?: 0

                    SoloResultScreen(
                        score = score,
                        rounds = rounds,
                        mistakes = mistakes,
                        onPlayAgain = { navController.popBackStack() },  // go back to SoloGame screen
                        onBackHome = {
                            navController.popBackStack(AppDestinations.HOME, inclusive = false)
                        }
                    )
                }

                composable(AppDestinations.PROFILE) {
                    ProfileScreen(
                        username = "Player 1",
                        soloStats = homeUi,
                        onBack = { navController.popBackStack() },
                        onFindFriends = { navController.navigate(AppDestinations.FRIEND_SEARCH) },
                        onLogout = { authVm.logout() },
                        onFriendsList = { navController.navigate(AppDestinations.FRIENDS_LIST) }
                    )
                }

                composable(AppDestinations.FRIENDS_LIST) {
                    val container = LocalAppContainer.current

                    val vm: FriendsListViewModel = viewModel(
                        factory = FriendsListViewModelFactory(container.userRepository)
                    )
                    val ui by vm.uiState.collectAsState()

                    FriendsListScreen(
                        ui = ui,
                        onBack = { navController.popBackStack() }
                    )
                }

            }
        }
    }
}

private class AuthViewModelFactory(
    private val repo: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}
