package ie.setu.languagelearnerapp.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ie.setu.languagelearnerapp.model.WordModel
import timber.log.Timber

object WordRepository {

    private const val FILE_NAME = "app_words.json"
    private var words = mutableListOf<WordModel>()
    private val gson = Gson()

    fun load(context: Context) {
        try {
            val file = context.getFileStreamPath(FILE_NAME)
            if (file.exists()) {
                val json = context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() }
                val type = object : TypeToken<MutableList<WordModel>>() {}.type
                words = gson.fromJson(json, type) ?: mutableListOf()
                words = words.map {
                    if (it.id.isBlank()) it.copy(id = java.util.UUID.randomUUID().toString()) else it
                }.toMutableList()

                save(context)

                Timber.Forest.i("Words loaded from file: ${words.size} items")
            } else {
                Timber.Forest.i("No previous data found, starting fresh.")
            }
        } catch (e: Exception) {
            Timber.Forest.e("Error loading words: ${e.message}")
        }
    }


    private fun save(context: Context) {
        try {
            val json = gson.toJson(words)
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
            Timber.Forest.i("Words saved to file: ${words.size} items")
        } catch (e: Exception) {
            Timber.Forest.e("Error saving words: ${e.message}")
        }
    }

    fun getAll(): List<WordModel> = words

    fun addWord(context: Context, word: WordModel, imageUri: String?) {

        if (word.word.isBlank() || word.translation.isBlank()) {
             Timber.Forest.w("Attempted to add invalid word")
             return
        }
        if (words.any { it.word.equals(word.word, ignoreCase = true) }) {
             Timber.Forest.w("Duplicate word: ${word.word}")
             return
        }
        val wordWithImage = word.copy(imageUri = imageUri)
        words.add(wordWithImage)
        save(context)
        Timber.Forest.i("Word added: ${word.word}")
    }
    fun updateWord(context: Context, updated: WordModel) {
        val index = words.indexOfFirst { it.id == updated.id }
        if (index == -1) return

        if (updated.word.isBlank() || updated.translation.isBlank()) return

        val duplicate = words.any {
            it.id != updated.id && it.word.equals(updated.word, ignoreCase = true)
        }
        if (duplicate) return

        words[index] = updated
        save(context)
    }
    fun toggleFavorite(context: Context, id: String) {
        val index = words.indexOfFirst { it.id == id }
        if (index == -1) return

        words[index].isFavorite = !words[index].isFavorite
        save(context)
        Timber.Forest.i("Favorite toggled for: ${words[index].word}")
    }
    fun deleteWord(context: Context, id:String) {
        val index = words.indexOfFirst { it.id == id }
        if (index == -1) return

        words.removeAt(index)
        save(context)
    }

    fun sortByLevel() {
        words.sortBy { it.level }
        Timber.Forest.i("Words sorted by level")
    }
}