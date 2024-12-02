package com.example.dndcharacterbuilder.api.model


import java.io.Serializable

data class Item(
    val name: String,
    val url: String
) : Serializable

data class ItemResponse(
    val count: Int,
    val results: List<Item>
)