package com.locker.catapp.model

import com.google.gson.annotations.SerializedName

data class Sprites(
    @SerializedName("back_default") val backDefaultUrl: String,
    @SerializedName("back_female") val backFemaleUrl: String,
    @SerializedName("back_shiny") val backShinyUrl: String,
    @SerializedName("back_shiny_female") val backShinyFemaleUrl: String,
    @SerializedName("front_default") val frontDefaultUrl: String?,
    @SerializedName("front_female") val frontFemaleUrl: String,
    @SerializedName("front_shiny") val frontShinyUrl: String,
    @SerializedName("front_shiny_female") val frontShinyFemaleUrl: String,
    @SerializedName("other") val additionalSprites: AdditionalSprites?
)

data class AdditionalSprites(
    @SerializedName("official-artwork") val artwork: OfficialArtwork?
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontDefaultUrl: String?
)