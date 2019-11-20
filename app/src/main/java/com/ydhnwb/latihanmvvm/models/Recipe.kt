package com.ydhnwb.latihanmvvm.models

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("content") var content : String? = null
)