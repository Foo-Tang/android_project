package com.example.pokemoncards

import android.app.Application
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokemonCardsApp : Application(){


    companion object {
        var isLoginSuccessful: Boolean = false
        lateinit var currentUserId: String
        lateinit var windowsize: WindowSizeClass
    }
}