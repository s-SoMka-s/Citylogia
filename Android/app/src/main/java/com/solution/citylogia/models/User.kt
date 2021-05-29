package com.solution.citylogia.models

data class User(
        val id: Long = 0,
        val name: String = "",
        val surname: String = "",
        val avatar: Photo = Photo(),
)
