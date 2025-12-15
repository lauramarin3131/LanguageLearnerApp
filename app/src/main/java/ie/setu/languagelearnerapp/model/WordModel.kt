package ie.setu.languagelearnerapp.model

import java.io.Serializable
import java.util.UUID
data class WordModel (
    val id: String = UUID.randomUUID().toString(),
    var word: String = "",
    var translation: String = "",
    var language: String = "",
    var level: String = "",
    var date: String = "",
    var isFavorite: Boolean = false,
    var imageUri: String? = null
): Serializable
