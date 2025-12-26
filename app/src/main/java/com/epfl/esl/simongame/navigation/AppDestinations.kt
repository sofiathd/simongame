package com.epfl.esl.simongame.navigation

object AppDestinations {
    const val HOME = "home"
    const val SOLO_GAME = "solo_game"
    const val SOLO_RESULT = "solo_result"
    const val PROFILE = "profile"
    const val MULTI_GAME = "multi_game"
    const val REGISTER = "register"
    const val FRIEND_SEARCH = "friend_search"
    const val FRIENDS_LIST = "friends_list"


    fun soloResultRoute(score: Int, rounds: Int, mistakes: Int) =
        "$SOLO_RESULT/$score/$rounds/$mistakes"
}
