package com.locker.catapp.model

import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("stat") val type: StatType
) {
}

data class StatType(@SerializedName("name") val name: String)