package com.example.dndcharacterbuilder.api.model

data class Race(
    val index: String,
    val name: String,
    val url: String
) {
    override fun toString(): String {
        return name
    }
}

data class RacesResponse(
    val count: Int,
    val results: List<Race>
)
