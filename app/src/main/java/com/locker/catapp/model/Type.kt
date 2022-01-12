package com.locker.catapp.model

import com.google.gson.annotations.SerializedName

data class Type(@SerializedName("type") val info: TypeInfo) {
}

data class TypeInfo(@SerializedName("name") val name: String)