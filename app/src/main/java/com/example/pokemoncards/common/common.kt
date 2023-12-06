package com.example.pokemoncards.common

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokemoncards.PokemonCardsApp
import com.example.pokemoncards.R
import com.example.pokemoncards.Viewmodel.PokemonViewModel
import com.example.pokemoncards.api.PokemonApi
import com.example.pokemoncards.data.Data
import com.example.pokemoncards.destinations.CardDetailDestination
import com.example.pokemoncards.destinations.LoginScreenDestination
import com.example.pokemoncards.destinations.SearchScreenDestination
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Composable
fun CardList(destinationsNavigator: DestinationsNavigator){
    val viewModel = viewModel{ PokemonViewModel() }
    if (viewModel.isLoading)
    {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else {
        if (viewModel.cards.isEmpty()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                contentAlignment = Alignment.Center) {
                Text(text = "No Results",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
        else{
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
                //contentPadding = PaddingValues(10.dp)
            )
            {
                items(viewModel.cards) { card ->
                    Card (
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            AsyncImage(
                                model = card.images.small,
                                modifier = Modifier
                                    //.weight(0.8f)
                                    .padding(4.dp)
                                    .clickable() {
                                        destinationsNavigator.navigate(
                                            CardDetailDestination(
                                                1,
                                                card
                                            )
                                        )
                                    },
                                contentDescription = card.id,
                                alignment = Alignment.Center
                            )
                            Text(card.set.name,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                            if(PokemonCardsApp.isLoginSuccessful)
                                FavoriteIcon(card, modifier = Modifier.padding(3.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val viewModel = viewModel{ PokemonViewModel() }
    var query by remember { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()

    TextField(
        value = query,
        onValueChange = {query = it},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text("Search Cards by Name")
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions (onSearch = {
            viewModel.isLoading = true
            coroutine.launch{
                val result = PokemonApi.getCard(query)
                if (result != null)
                    viewModel.cards = result.data
                else
                    viewModel.cards = emptyList()
                viewModel.isLoading = false
            }
            focusManager.clearFocus()
        }),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Composable
fun BottomNavigation(modifier: Modifier = Modifier, destinationsNavigator: DestinationsNavigator) {
    val viewModel = viewModel{ PokemonViewModel() }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        if(!PokemonCardsApp.isLoginSuccessful){
            NavigationBarItem(
                icon = {Icon(imageVector = Icons.Default.Login, contentDescription = null)},
                label = { Text(stringResource(R.string.desc_login)) },
                selected = true,
                onClick = { destinationsNavigator.navigate(LoginScreenDestination)}
            )
        }
        else {
            NavigationBarItem(
                icon = {Icon(imageVector = Icons.Default.Search, contentDescription = null)},
                label = { Text(stringResource(R.string.desc_search)) },
                selected = false,
                onClick = {destinationsNavigator.navigate(LoginScreenDestination)}
            )

            NavigationBarItem(
                icon = {Icon(imageVector = Icons.Default.Favorite, contentDescription = null)},
                label = { Text(stringResource(R.string.desc_favorities)) },
                selected = false,
                onClick = {viewModel.Favourites()}
            )

            NavigationBarItem(
                icon = {Icon(imageVector = Icons.Default.Logout, contentDescription = null)},
                label = { Text(stringResource(R.string.desc_logout)) },
                selected = false,
                onClick =
                {
                    PokemonCardsApp.isLoginSuccessful = false
                    destinationsNavigator.navigate(SearchScreenDestination)
                }
            )
        }
    }
}

@Composable
fun FavoriteIcon(card: Data, modifier: Modifier = Modifier) {
    var isFavorite by remember { mutableStateOf(false) }
    val context = LocalContext.current
    // Retrieve and check card marked as favorite card
    val db = Firebase.firestore
    db.collection("favorites").document(card.id + PokemonCardsApp.currentUserId).get()
        .addOnSuccessListener{document->
            if (document != null){
                val cardid = document.data?.get("id")
                isFavorite = (cardid == card.id)
            }
        }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {

                    // Remove the record from database
                    if (isFavorite) {

                        db.collection("favorites").document(card.id + PokemonCardsApp.currentUserId).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Remove success", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener{
                                    exception -> Toast.makeText(context, "Remove failure", Toast.LENGTH_SHORT).show()
                            }
                    }else{
                        // Add a record to database
                        db.collection("favorites").document(card.id + PokemonCardsApp.currentUserId).set(card)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Insert success", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener{
                                    exception -> Toast.makeText(context, "Insert failure", Toast.LENGTH_SHORT).show()
                            }
                    }
                    isFavorite = !isFavorite
                }
            ) {
                Icon(
                    painter = if (isFavorite) {
                        painterResource(id = R.drawable.baseline_favorite_24)
                    } else {
                        painterResource(id = R.drawable.baseline_favorite_border_24)
                    },
                    contentDescription = "Favorite Icon"
                )
            }
        }
    }
}