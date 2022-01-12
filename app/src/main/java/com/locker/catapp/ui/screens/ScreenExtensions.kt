package com.locker.catapp.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import androidx.core.content.res.ResourcesCompat
import com.locker.catapp.R
import com.locker.catapp.isNightMode
import com.locker.catapp.model.Type
import com.locker.catapp.uiMode

fun List<Type>.toGradient(context: Context) : List<Color> {
    val gradientList = when (get(0).info.name) {
        "bug" ->
            listOf(context.getColorExt(R.color.typeBugLight),
                context.getColorExt(R.color.typeBug),
                context.getColorExt(R.color.typeBugDark))
        "dark" ->
            listOf(context.getColorExt(R.color.typeDarkLight),
                context.getColorExt(R.color.typeDark),
                context.getColorExt(R.color.typeDarkDark))
        "dragon" ->
            listOf(context.getColorExt(R.color.typeDragonLight),
                context.getColorExt(R.color.typeDragon),
                context.getColorExt(R.color.typeDragonDark))
        "electric" ->
            listOf(context.getColorExt(R.color.typeElectricLight),
                context.getColorExt(R.color.typeElectric),
                context.getColorExt(R.color.typeElectricDark))
        "fairy" ->
            listOf(context.getColorExt(R.color.typeFairyLight),
                context.getColorExt(R.color.typeFairy),
                context.getColorExt(R.color.typeFairyDark))
        "fighting" ->
            listOf(context.getColorExt(R.color.typeFightingLight),
                context.getColorExt(R.color.typeFighting),
                context.getColorExt(R.color.typeFightingDark))
        "fire" ->
            listOf(context.getColorExt(R.color.typeFireLight),
                context.getColorExt(R.color.typeFire),
                context.getColorExt(R.color.typeFireDark))
        "flying" ->
            listOf(context.getColorExt(R.color.typeFlyingLight),
                context.getColorExt(R.color.typeFlying),
                context.getColorExt(R.color.typeFlyingDark))
        "ghost" ->
            listOf(context.getColorExt(R.color.typeGhostLight),
                context.getColorExt(R.color.typeGhost),
                context.getColorExt(R.color.typeGhostDark))
        "grass" ->
            listOf(context.getColorExt(R.color.typeGrassLight),
                context.getColorExt(R.color.typeGrass),
                context.getColorExt(R.color.typeGrassDark))
        "ground" ->
            listOf(context.getColorExt(R.color.typeGroundLight),
                context.getColorExt(R.color.typeGround),
                context.getColorExt(R.color.typeGroundDark))
        "ice" ->
            listOf(context.getColorExt(R.color.typeIceLight),
                context.getColorExt(R.color.typeIce),
                context.getColorExt(R.color.typeIceDark))
        "normal" ->
            listOf(context.getColorExt(R.color.typeNormalLight),
                context.getColorExt(R.color.typeNormal),
                context.getColorExt(R.color.typeNormalDark))
        "poison" ->
            listOf(context.getColorExt(R.color.typePoisonLight),
                context.getColorExt(R.color.typePoison),
                context.getColorExt(R.color.typePoisonDark))
        "psychic" ->
            listOf(context.getColorExt(R.color.typePsychicLight),
                context.getColorExt(R.color.typePsychic),
                context.getColorExt(R.color.typePsychicDark))
        "rock" ->
            listOf(context.getColorExt(R.color.typeRockLight),
                context.getColorExt(R.color.typeRock),
                context.getColorExt(R.color.typeRockDark))
        "steel" ->
            listOf(context.getColorExt(R.color.typeSteelLight),
                context.getColorExt(R.color.typeSteel),
                context.getColorExt(R.color.typeSteelDark))
        "water" ->
            listOf(context.getColorExt(R.color.typeWaterLight),
                context.getColorExt(R.color.typeWater),
                context.getColorExt(R.color.typeWaterDark))
        else ->
            listOf(context.getColorExt(R.color.typeNormalLight),
                context.getColorExt(R.color.typeNormal),
                context.getColorExt(R.color.typeNormalDark))
    }

    return if (context.isNightMode()) gradientList else gradientList.reversed()
}

fun Context.getColorExt(@ColorRes resId: Int) =
    Color(ResourcesCompat.getColor(resources, resId, theme))
