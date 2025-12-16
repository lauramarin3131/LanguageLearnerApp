package ie.setu.languagelearnerapp.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.model.WordModel
import ie.setu.languagelearnerapp.repository.WordRepository

class EditWordActivity : AppCompatActivity() {

    private lateinit var oldWord: WordModel
    private var selectedImageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
        WordRepository.load(this)

        val btnAddImage = findViewById<Button>(R.id.btnAddImage)
        btnAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1001)
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            finish()
        }

        title = "Edit Word"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etWord = findViewById<EditText>(R.id.etWord)
        val etTranslation = findViewById<EditText>(R.id.etTranslation)
        val spinnerLanguage = findViewById<Spinner>(R.id.spinnerLanguage)
        val pickerLevel = findViewById<NumberPicker>(R.id.pickerLevel)
        pickerLevel.minValue = 1
        pickerLevel.maxValue = 5

        val btnSave = findViewById<Button>(R.id.btnSaveWord)

        val languages = listOf("English 🇬🇧", "Spanish 🇪🇸", "French 🇫🇷", "German 🇩🇪", "Italian 🇮🇹")
        spinnerLanguage.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)

        val incoming = intent.getSerializableExtra("word") as? WordModel
        if (incoming == null) {
            finish()
            return
        }
        oldWord = incoming

        etWord.setText(oldWord.word)
        etTranslation.setText(oldWord.translation)
        val langIndex = languages.indexOf(oldWord.language).coerceAtLeast(0)
        spinnerLanguage.setSelection(langIndex)

        pickerLevel.value = oldWord.level.toIntOrNull() ?: 1

        btnSave.setOnClickListener {
            val newWordText = etWord.text.toString().trim()
            val newTranslationText = etTranslation.text.toString().trim()

            if (newWordText.isBlank()) {
                etWord.error = "Required"
                return@setOnClickListener
            }
            if (newTranslationText.isBlank()) {
                etTranslation.error = "Required"
                return@setOnClickListener
            }

            val duplicate = WordRepository.getAll().any {
                it.id != oldWord.id && it.word.equals(newWordText, ignoreCase = true)
            }
            if (duplicate) {
                etWord.error = "Word already exists"
                return@setOnClickListener
            }
            val updated = oldWord.copy(
                word = etWord.text.toString().trim(),
                translation = etTranslation.text.toString().trim(),
                language = spinnerLanguage.selectedItem.toString(),
                level = pickerLevel.value.toString(),
                imageUri = selectedImageUri ?: oldWord.imageUri
            )
            WordRepository.updateWord(this,  updated)
            Snackbar.make(it, "Word updated!", Snackbar.LENGTH_SHORT).show()
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data?.toString()
        }
    }

}
