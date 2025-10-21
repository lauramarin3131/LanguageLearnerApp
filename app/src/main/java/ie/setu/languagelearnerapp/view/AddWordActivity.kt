package ie.setu.languagelearnerapp.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.model.WordModel

class AddWordActivity : AppCompatActivity() {
    companion object {
        val words = ArrayList<WordModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_word)

        // edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom)
            insets
        }
        title = "Add Word"

        val etWord = findViewById<EditText>(R.id.etWord)
        val etTranslation = findViewById<EditText>(R.id.etTranslation)
        val etLanguage = findViewById<EditText>(R.id.etLanguage)
        val etLevel = findViewById<EditText>(R.id.etLevel)
        val btnSave = findViewById<Button>(R.id.btnSaveWord)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnSave.setOnClickListener {
            val w = etWord.text.toString().trim()
            val t = etTranslation.text.toString().trim()
            val lg = etLanguage.text.toString().trim()
            val lv = etLevel.text.toString().trim()

            var ok = true
            if (w.isEmpty()) { etWord.error = "Required"; ok = false }
            if (t.isEmpty()) { etTranslation.error = "Required"; ok = false }

            if (ok) {
                words.add(WordModel(w, t, lg, lv))
                finish() // back to previous screen
            }
        }

        btnCancel.setOnClickListener { finish() }
    }
}

