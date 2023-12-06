package com.example.pokemoncards.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pokemoncards.common.BottomNavigation
import com.example.pokemoncards.common.CardList
import com.example.pokemoncards.PokemonCardsApp
import com.example.pokemoncards.common.SearchBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SearchScreen(
    destinationsNavigator: DestinationsNavigator
){
    val background = if(PokemonCardsApp.isLoginSuccessful)
        MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.background

    Scaffold (
        topBar = { SearchBar() },
        bottomBar = { BottomNavigation(destinationsNavigator = destinationsNavigator) }
    ){ innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .background(color = background)){
            CardList( destinationsNavigator = destinationsNavigator)
        }
    }
}