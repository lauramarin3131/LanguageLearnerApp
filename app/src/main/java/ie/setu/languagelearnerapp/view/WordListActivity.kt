package ie.setu.languagelearnerapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.adapter.WordAdapter

class WordListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        title = "Word List"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewWords)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WordAdapter(AddWordActivity.words)
    }
}
