package com.epfl.esl.simongame

import android.app.Application
import com.epfl.esl.simongame.di.AppContainer

class SimonGameApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
