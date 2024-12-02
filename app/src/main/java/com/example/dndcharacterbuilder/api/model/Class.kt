package com.example.dndcharacterbuilder.api.model

data class Class(
    val index: String,
    val name: String,
    val url: String
)

data class ClassesResponse(
    val count: Int,
    val results: List<Class>
)