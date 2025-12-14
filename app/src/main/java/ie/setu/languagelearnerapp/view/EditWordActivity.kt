package ie.setu.languagelearnerapp.view

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.model.WordModel
import ie.setu.languagelearnerapp.repository.WordRepository

class EditWordActivity : AppCompatActivity() {

    private lateinit var oldWord: WordModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        title = "Edit Word"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etWord = findViewById<EditText>(R.id.etWord)
        val etTranslation = findViewById<EditText>(R.id.etTranslation)
        val spinnerLanguage = findViewById<Spinner>(R.id.spinnerLanguage)
        val pickerLevel = findViewById<NumberPicker>(R.id.pickerLevel)
        val btnSave = findViewById<Button>(R.id.btnSaveWord)

        val languages = listOf("English 🇬🇧", "Spanish 🇪🇸", "French 🇫🇷", "German 🇩🇪", "Italian 🇮🇹")
        spinnerLanguage.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)

        oldWord = intent.getSerializableExtra("word") as WordModel
        if (oldWord == null) {
            finish()
            return
        }
        etWord.setText(oldWord.word)
        etTranslation.setText(oldWord.translation)
        spinnerLanguage.setSelection(languages.indexOf(oldWord.language))
        pickerLevel.value = oldWord.level.toInt()

        btnSave.setOnClickListener {
            val duplicate = WordRepository.getAll().any {
                it.word.equals(etWord.text.toString(), ignoreCase = true) && it != oldWord
            }
            if (duplicate) {
                etWord.error = "Word already exists"
                return@setOnClickListener
            }
            val updated = WordModel(
                etWord.text.toString(),
                etTranslation.text.toString(),
                spinnerLanguage.selectedItem.toString(),
                pickerLevel.value.toString()
            )
            WordRepository.updateWord(this, oldWord, updated)
            Snackbar.make(it, "Word updated!", Snackbar.LENGTH_SHORT).show()
            finish()
        }
    }
}
