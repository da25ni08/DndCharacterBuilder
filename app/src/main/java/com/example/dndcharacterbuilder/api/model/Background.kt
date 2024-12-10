package com.example.dndcharacterbuilder.api.model

data class Background(
    val index: String,
    val name: String,
    val url: String
) {
    override fun toString(): String {
        return name
    }
}

data class BackgroundsResponse(
    val count: Int,
    val results: List<Background>
)