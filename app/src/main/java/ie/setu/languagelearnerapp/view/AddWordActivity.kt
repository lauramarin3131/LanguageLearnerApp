package ie.setu.languagelearnerapp.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.model.WordModel
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ie.setu.languagelearnerapp.R.*


class AddWordActivity : AppCompatActivity() {
    companion object {
        val words = ArrayList<WordModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_add_word)

        // edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id.main)) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom)
            insets
        }
        title = "Add Word"

        val etWord = findViewById<EditText>(id.etWord)
        val etTranslation = findViewById<EditText>(id.etTranslation)
        val spinnerLanguage = findViewById<Spinner>(id.spinnerLanguage)
        val pickerLevel = findViewById<NumberPicker>(id.pickerLevel)
        val btnSave = findViewById<Button>(id.btnSaveWord)
        val btnCancel = findViewById<Button>(id.btnCancel)
        val languages = listOf("English 🇬🇧", "Spanish 🇪🇸", "French 🇫🇷", "German 🇩🇪", "Italian 🇮🇹")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter
        pickerLevel.minValue = 1
        pickerLevel.maxValue = 5
        pickerLevel.value = 1

        loadWords()
        btnSave.setOnClickListener {
            val w = etWord.text.toString().trim()
            val t = etTranslation.text.toString().trim()
            val lg = spinnerLanguage.selectedItem.toString()
            val lv = pickerLevel.value.toString()

            var ok = true
            if (w.isEmpty()) { etWord.error = "Required"; ok = false }
            if (t.isEmpty()) { etTranslation.error = "Required"; ok = false }

            if (ok) {
                words.add(WordModel(w, t, lg, lv))
                saveWords()
                Snackbar.make(it, "Word added successfully!", Snackbar.LENGTH_SHORT).show()
                finish()
            }
        }

        btnCancel.setOnClickListener { finish() }
    }
    private fun saveWords() {
        val sharedPref = getSharedPreferences("LanguageLearnerPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val json = Gson().toJson(words)
        editor.putString("words", json)
        editor.apply()
    }
    private fun loadWords() {
        val sharedPref = getSharedPreferences("LanguageLearnerPrefs", Context.MODE_PRIVATE)
        val json = sharedPref.getString("words", null)
        if (json != null) {
            val type = object : TypeToken<ArrayList<WordModel>>() {}.type
            val savedWords: ArrayList<WordModel> = Gson().fromJson(json, type)
            words.clear()
            words.addAll(savedWords)
        }
    }
}

