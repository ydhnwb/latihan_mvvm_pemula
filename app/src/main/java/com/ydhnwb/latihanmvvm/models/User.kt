package com.ydhnwb.latihanmvvm.models

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("api_token") var api_token : String? = null
)