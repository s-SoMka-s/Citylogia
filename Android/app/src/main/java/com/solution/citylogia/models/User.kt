package com.solution.citylogia.models

import com.google.gson.annotations.SerializedName

data class User(
        val id: Long = 0,

        @SerializedName("Name")
        val name: String = "",

        val surname: String = "",
        val avatar: Photo = Photo(),
)
