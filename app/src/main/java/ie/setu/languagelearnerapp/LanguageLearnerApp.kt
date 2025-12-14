package ie.setu.languagelearnerapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ie.setu.languagelearnerapp.view.AddWordActivity
import ie.setu.languagelearnerapp.view.WordListActivity
import timber.log.Timber

class LanguageLearnerApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_language_learner)

        Timber.plant(Timber.DebugTree())
        Timber.i("LanguageLearner Activity started...")
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnList = findViewById<Button>(R.id.btnList)

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddWordActivity::class.java))
        }

        btnList.setOnClickListener {
            startActivity(Intent(this, WordListActivity::class.java))
        }
    }
}