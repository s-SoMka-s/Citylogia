package com.solution.citylogia.models

import kotlinx.serialization.Serializable

@Serializable
data class PlaceType(
        val id: Long = 0,
        val name: String = "Архитектура",
) {
    companion object Factory {
        private var lastId: Long = -1;

        fun makeType(): PlaceType {
            lastId++;

            return PlaceType(id = lastId);
        }
    }
}