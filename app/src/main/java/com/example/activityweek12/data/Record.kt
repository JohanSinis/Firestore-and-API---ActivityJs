package com.example.activityweek12.data

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("email")
    val email: String
)

data class ApiResponse(
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("nombre")
    val nombre: String? = null,
    
    @SerializedName("email")
    val email: String? = null
)

