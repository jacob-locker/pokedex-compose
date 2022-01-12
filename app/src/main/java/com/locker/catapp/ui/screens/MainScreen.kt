package com.locker.catapp.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.asFlow
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.transform.CircleCropTransformation
import com.locker.catapp.MainViewModel
import com.locker.catapp.R
import com.locker.catapp.model.Pokemon
import com.locker.catapp.ui.AutoCompleteBox
import com.locker.catapp.ui.theme.PokeAppTheme

const val MAIN_SCREEN = "main"

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MainScreen(viewModel: MainViewModel, onPokemonClicked: (Pokemon) -> Unit) {
    val state =
        viewModel.pokemonList.collectAsLazyPagingItems()
    val searchState = viewModel.pokemonSearchLiveData.asFlow().collectAsLazyPagingItems()
    MainScreen(viewModel, state, searchState, onPokemonClicked)

    val loadingState = viewModel.isLoading.asFlow().collectAsState(initial = true)
    LoadingScreen(loadingState.value)
}

@ExperimentalAnimationApi
@Composable
fun LoadingScreen(isLoading: Boolean) {
    AnimatedVisibility(visible = isLoading) {
        CircularProgressIndicator()
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    pokemonList: LazyPagingItems<Pokemon>,
    searchList: LazyPagingItems<Pokemon>,
    onPokemonClicked: (Pokemon) -> Unit
) {
    val text = rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

            TextField(value = text.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = MaterialTheme.colors.primary) },
                onValueChange = {
                    text.value = it
                    viewModel.searchPokemon(it)
                }
            )


        AnimatedVisibility(visible = text.value.isNotEmpty()) {
            LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                items(searchList.itemCount) {
                    PokemonListItem(pokemon = searchList[it]!!)
                }
            }
        }
        AnimatedVisibility(visible = text.value.isEmpty()) {
            LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                items(pokemonList.itemCount) {
//            PokemonListItem(pokemon = pokemonList[it], onPokemonClicked)
                    PokemonListItem(pokemon = pokemonList[it]!!)
                }
            }
        }
    }
//        TextField(value = text.value, onValueChange = {
//            text.value = it
//            viewModel.searchPokemon(it)
//        } )

}

@Composable
fun PokemonSearchItem(pokemon: Pokemon) {

}

@ExperimentalCoilApi
@Composable
fun PokemonListItem(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Box(
            Modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            pokemon.types.toGradient(LocalContext.current),
                            0F,
                            500F
                        )
                    )
            ) {}
            PokemonImage(
                imageUrl = pokemon.sprites.frontDefaultUrl
                    ?: "https://static.wikia.nocookie.net/pokemontowerdefense/images/c/ce/Missingno_image.png/revision/latest?cb=20180809204127",
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.TopCenter)
                    .padding(PaddingValues(bottom = 16.dp))
            )
            Text(
                text = pokemon.name,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomCenter),
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun PokemonImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        val painter = rememberImagePainter(imageUrl, builder = {
            transformations(CircleCropTransformation())
        })

        Image(
            painter = painter,
            contentDescription = "Pokemon Appearance Image",
            modifier = modifier
        )

        if (painter.state is ImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}

//@Composable
//fun SearchView(state: MutableState<TextFieldValue>) {
//    TextField(
//        value = state.value,
//        onValueChange = { value ->
//            state.value = value
//        },
//        modifier = Modifier
//            .fillMaxWidth(),
//        style = MaterialTheme.typography.h5,
//        leadingIcon = {
//            Icon(
//                Icons.Default.Search,
//                contentDescription = "",
//                modifier = Modifier
//                    .padding(15.dp)
//                    .size(24.dp)
//            )
//        },
//        trailingIcon = {
//            if (state.value != TextFieldValue("")) {
//                IconButton(
//                    onClick = {
//                        state.value =
//                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
//                    }
//                ) {
//                    Icon(
//                        Icons.Default.Close,
//                        contentDescription = "",
//                        modifier = Modifier
//                            .padding(15.dp)
//                            .size(24.dp)
//                    )
//                }
//            }
//        },
//        singleLine = true,
//        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
//        colors = TextFieldDefaults.textFieldColors(
//            textColor = Color.White,
//            cursorColor = Color.White,
//            leadingIconColor = Color.White,
//            trailingIconColor = Color.White,
//            backgroundColor = colorResource(id = R.color.design_default_color_primary),
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            disabledIndicatorColor = Color.Transparent
//        )
//    )
//}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewPokemonListItem() {
    PokeAppTheme {
        //PokemonListItem(pokemon = Pokemon.BULBASAUR, onPokemonClicked = {})
        PokemonListItem(pokemon = Pokemon.BULBASAUR)
    }
}