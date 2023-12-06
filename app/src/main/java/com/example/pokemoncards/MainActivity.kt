package com.example.pokemoncards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.example.pokemoncards.destinations.SearchScreenDestination
import com.example.pokemoncards.ui.theme.PokemonCardsTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContent {
                PokemonCardsTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        DestinationsNavHost(navGraph = NavGraphs.root)
                    }

                }
            }
        }
    }

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
){
    navigator.navigate(SearchScreenDestination)
}


//@Destination
//@Composable
//fun SearchScreen(
//    destinationsNavigator: DestinationsNavigator
//){
//     val background = if(PokemonCardsApp.isLoginSuccessful)
//                 MaterialTheme.colorScheme.primaryContainer
//                 else MaterialTheme.colorScheme.background
//
//    Scaffold (
//        topBar = {SearchBar()},
//          bottomBar = {BottomNavigation(destinationsNavigator = destinationsNavigator)}
//    ){ innerPadding ->
//        Box(modifier = Modifier
//            .padding(innerPadding)
//            .background(color = background)){
//            CardList( destinationsNavigator = destinationsNavigator)}
//    }
//}

//@Composable
//fun CardList(destinationsNavigator: DestinationsNavigator){
//    val viewModel = viewModel{ PokemonViewModel() }
//    if (viewModel.isLoading)
//        {
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//                contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//    else {
//        if (viewModel.cards.isEmpty()) {
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//                contentAlignment = Alignment.Center) {
//                Text(text = "No Results",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 24.sp
//                    )
//            }
//        }
//        else{
//        LazyVerticalGrid(
//            columns = GridCells.Adaptive(100.dp),
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//            horizontalArrangement = Arrangement.spacedBy(10.dp),
//            modifier = Modifier.fillMaxSize()
//            //contentPadding = PaddingValues(10.dp)
//        )
//        {
//            items(viewModel.cards) { card ->
//                Card (
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceVariant
//                    ),
//                    modifier = Modifier.fillMaxSize(),
//                ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                        ) {
//                            AsyncImage(
//                                model = card.images.small,
//                                modifier = Modifier
//                                    //.weight(0.8f)
//                                    .padding(4.dp)
//                                    .clickable() {
//                                        destinationsNavigator.navigate(
//                                            CardDetailDestination(
//                                                1,
//                                                card
//                                            )
//                                        )
//                                    },
//                                contentDescription = card.id,
//                                alignment = Alignment.Center
//                            )
//                            Text(card.set.name,
//                                textAlign = TextAlign.Center,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis)
//                            if(PokemonCardsApp.isLoginSuccessful)
//                                FavoriteIcon(card, modifier = Modifier.padding(3.dp))
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun SearchBar(
//    modifier: Modifier = Modifier
//) {
//    val focusManager = LocalFocusManager.current
//    val viewModel = viewModel{ PokemonViewModel() }
//    var query by remember { mutableStateOf("") }
//    val coroutine = rememberCoroutineScope()
//
//    TextField(
//        value = query,
//        onValueChange = {query = it},
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = null
//            )
//        },
//        colors = TextFieldDefaults.colors(
//            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
//            focusedContainerColor = MaterialTheme.colorScheme.surface
//        ),
//        placeholder = {
//            Text("Search Cards by Name")
//        },
//        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//        keyboardActions = KeyboardActions (onSearch = {
//            viewModel.isLoading = true
//            coroutine.launch{
//                val result = PokemonApi.getCard(query)
//                if (result != null)
//                    viewModel.cards = result.data
//                else
//                    viewModel.cards = emptyList()
//                viewModel.isLoading = false
//            }
//            focusManager.clearFocus()
//        }),
//        modifier = modifier
//            .fillMaxWidth()
//            .heightIn(min = 56.dp)
//    )
//}
//
//@Composable
//fun BottomNavigation(modifier: Modifier = Modifier, destinationsNavigator: DestinationsNavigator) {
//    val viewModel = viewModel{ PokemonViewModel() }
//
//    NavigationBar(
//        containerColor = MaterialTheme.colorScheme.surfaceVariant,
//        modifier = modifier
//    ) {
//        if(!PokemonCardsApp.isLoginSuccessful){
//            NavigationBarItem(
//                icon = {Icon(imageVector = Icons.Default.Login, contentDescription = null)},
//                label = {Text(stringResource(R.string.desc_login))},
//                selected = true,
//                onClick = { destinationsNavigator.navigate(LoginScreenDestination)}
//            )
//        }
//        else {
//            NavigationBarItem(
//                icon = {Icon(imageVector = Icons.Default.Search, contentDescription = null)},
//                label = {Text(stringResource(R.string.desc_search))},
//                selected = false,
//                onClick = {destinationsNavigator.navigate(LoginScreenDestination)}
//            )
//
//            NavigationBarItem(
//                icon = {Icon(imageVector = Icons.Default.Favorite, contentDescription = null)},
//                label = {Text(stringResource(R.string.desc_favorities))},
//                selected = false,
//                onClick = {viewModel.Favourites()}
//            )
//
//            NavigationBarItem(
//                icon = {Icon(imageVector = Icons.Default.Logout, contentDescription = null)},
//                label = {Text(stringResource(R.string.desc_logout))},
//                selected = false,
//                onClick =
//                    {
//                        PokemonCardsApp.isLoginSuccessful = false
//                        destinationsNavigator.navigate(SearchScreenDestination)
//                    }
//            )
//        }
//    }
//}
