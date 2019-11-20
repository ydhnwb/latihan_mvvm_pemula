package com.ydhnwb.latihanmvvm.utils

import com.google.gson.annotations.SerializedName

data class WrappedResponse<T>(
    @SerializedName("messsage") var message : String? = null,
    @SerializedName("status") var status : String? = null,
    @SerializedName("data") var data : T? = null
)

data class WrappedListResponse<T>(
    @SerializedName("messsage") var message : String? = null,
    @SerializedName("status") var status : String? = null,
    @SerializedName("data") var data : List<T>? = null
)