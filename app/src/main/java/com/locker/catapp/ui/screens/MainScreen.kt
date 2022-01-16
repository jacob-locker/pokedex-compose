package com.locker.catapp.ui.screens

import android.graphics.ComposeShader
import android.graphics.PorterDuff
import android.os.SystemClock
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.size.SizeResolver
import coil.transform.CircleCropTransformation
import com.locker.catapp.MainViewModel
import com.locker.catapp.model.Pokemon
import com.locker.catapp.model.Type
import com.locker.catapp.ui.theme.PokeAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val MAIN_SCREEN = "main"
var IMAGE_SIZE = IntSize.Zero

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MainScreen(viewModel: MainViewModel, onPokemonClicked: (Pokemon) -> Unit) {
    val searchState = viewModel.pokemonSearchFlow.collectAsLazyPagingItems()
    val hasPokemonState = viewModel.hasPokemonFlow.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchView(viewModel = viewModel)

        val loading = rememberSaveable { mutableStateOf(false) }
        if (searchState.loadState.refresh is LoadState.Loading) {
            LaunchedEffect(SystemClock.elapsedRealtime()) {
                delay(200)
                loading.value = true
            }
        } else {
            loading.value = false
        }

        AnimatedVisibility(visible = loading.value) {
            LoadScreen()
        }

        AnimatedVisibility(
            visible = !loading.value && searchState.loadState.refresh is LoadState.Error || !hasPokemonState.value) {
            ErrorScreen(searchState.loadState.refresh , hasPokemonState.value)
        }

        AnimatedVisibility(
            visible = !loading.value && hasPokemonState.value) {
            MainScreen(
                viewModel = viewModel,
                searchState,
                onPokemonClicked = onPokemonClicked
            )
        }
    }
}

@Composable
fun SearchView(viewModel: MainViewModel) {
    val text = rememberSaveable { mutableStateOf("") }
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
}

@Composable
fun ErrorScreen(errorState: LoadState, hasPokemonState: Boolean) {
    val errorMsg = when {
        errorState is LoadState.Error -> errorState.error.message.toString()
        !hasPokemonState -> "Could not find that Pokemon!"
        else -> "Something went wrong!"
    }
    Text(text = errorMsg)
}

@ExperimentalAnimationApi
@Composable
fun LoadScreen() {
    CircularProgressIndicator()
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    searchList: LazyPagingItems<Pokemon>,
    onPokemonClicked: (Pokemon) -> Unit
) {
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(searchList.itemCount) {
            BigPokemonListItem(pokemon = searchList[it]!!)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun BigPokemonListItem(pokemon: Pokemon) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Box(
            Modifier.wrapContentSize()
        ) {
            PokemonImage(
                imageUrl = pokemon.sprites.additionalSprites?.artwork?.frontDefaultUrl
                    ?: "https://static.wikia.nocookie.net/pokemontowerdefense/images/c/ce/Missingno_image.png/revision/latest?cb=20180809204127",
                pokemon.types,
                modifier = Modifier
            )
            Text(
                text = pokemon.name.capitalize(Locale.current),
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomCenter),
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "#${pokemon.id}",
                color = Color(red = 0x44, green = 0x44, blue = 0x44, alpha = 0x44),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun PokemonImage(
    imageUrl: String,
    pokemonTypes: List<Type>,
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val typeGradient = pokemonTypes.toGradient(LocalContext.current)
    val gradient = Brush.verticalGradient(
        colors = typeGradient
            .applyAlphaGradient(listOf(0f, 0.9f, 1f)),
        startY = sizeImage.height.toFloat() / 1.5f,  // 1/3
        endY = sizeImage.height.toFloat()
    )

    Box(
        modifier = Modifier
            .height(128.dp)
            .fillMaxWidth()
            .onGloballyPositioned { sizeImage = it.size }
            .background(color = typeGradient[0])
    ) {
        val painter = rememberImagePainter(imageUrl, builder = {
            transformations(CircleCropTransformation())
            scale(Scale.FIT)
        })

        Image(
            painter = painter,
            contentDescription = "Pokemon Appearance Image",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(bottom = 0.dp),
            contentScale = ContentScale.Fit
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {}

        if (painter.state is ImagePainter.State.Loading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
        }
    }
}

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PreviewPokemonListItem() {
    PokeAppTheme {
        //PokemonListItem(pokemon = Pokemon.BULBASAUR, onPokemonClicked = {})
        BigPokemonListItem(pokemon = Pokemon.BULBASAUR)
    }
}