package com.note.mynote.data.models

import kotlin.random.Random

data class Note(
    var id: Int = Random.nextInt(0,1000000),
    var title: String? = "",
    var description: String? = "",
    var time: String? = "",
    var date: String? = ""
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$title$description",
            "$title $description",
            "${title?.first()} ${description?.first()}"
        )

        return matchingCombinations.any { it.contains(query,ignoreCase = true) }
    }
}
