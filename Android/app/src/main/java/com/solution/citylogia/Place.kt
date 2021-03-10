package com.solution.citylogia

import kotlinx.serialization.Serializable

@Serializable
data class Place(val id: Long, val name: String, val description: String, val mark: Long, val typeId: Long)

@Serializable
data class Places(val elements: List<Place>);