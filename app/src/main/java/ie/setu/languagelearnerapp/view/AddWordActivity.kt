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
import ie.setu.languagelearnerapp.model.WordModel
import android.widget.Toast
import ie.setu.languagelearnerapp.R.*
import ie.setu.languagelearnerapp.repository.WordRepository


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

        WordRepository.load(this)
        btnSave.setOnClickListener {
            val w = etWord.text.toString().trim()
            val t = etTranslation.text.toString().trim()
            val lg = spinnerLanguage.selectedItem.toString()
            val lv = pickerLevel.value.toString()

            var ok = true
            if (w.isEmpty()) {
                etWord.error = "Required"
                Toast.makeText(this, "Word is required", Toast.LENGTH_SHORT).show()
                ok = false
            }
            if (t.isEmpty()) {
                etTranslation.error = "Required"
                Toast.makeText(this, "Translation is required", Toast.LENGTH_SHORT).show()
                ok = false
                         }

            if (ok) {
                WordRepository.addWord(this, WordModel(w, t, lg, lv, date = System.currentTimeMillis().toString()))
                Snackbar.make(it, "Word added successfully!", Snackbar.LENGTH_SHORT).show()
                finish()
            }
        }

        btnCancel.setOnClickListener { finish() }
    }
}

