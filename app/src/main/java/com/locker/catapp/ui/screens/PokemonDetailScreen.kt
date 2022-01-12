package com.locker.catapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.locker.catapp.model.Pokemon
import com.locker.catapp.ui.theme.PokeAppTheme

const val POKEMON_DETAILS = "pokemonDetails"
const val POKEMON_DETAIL_KEY = "pokemonDetailsKey"

@Composable
fun PokemonDetailScreen(pokemon: Pokemon, onBack: () -> Unit) {
    Column {
        //BackHandler(onBack = onBack)
        DetailTopAppBar(pokemon = pokemon, onBack = onBack)
        Text(text = pokemon.name)
    }
}

@Composable
fun DetailTopAppBar(pokemon: Pokemon, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(pokemon.name.capitalize(Locale.current)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokeAppTheme {
        PokemonDetailScreen(pokemon = Pokemon.BULBASAUR) {}
    }
}