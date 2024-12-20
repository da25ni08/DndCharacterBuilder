package com.example.dndcharacterbuilder.api.model

data class Class(
    val index: String,
    val name: String,
    val url: String
) {
    override fun toString(): String {
        return name
    }
}

data class ClassesResponse(
    val count: Int,
    val results: List<Class>
)