package ie.setu.languagelearnerapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.adapter.WordAdapter
import com.google.android.material.snackbar.Snackbar
import ie.setu.languagelearnerapp.repository.WordRepository

class WordListActivity : AppCompatActivity() {
    private lateinit var repository: WordRepository
    private lateinit var adapter: WordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        WordRepository.load(this)
        title = "Word List"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewWords)
        adapter = WordAdapter(WordRepository.getAll().toMutableList()) { position ->
            WordRepository.deleteWord(this, position)
            adapter.updateList(WordRepository.getAll())
            Snackbar.make(recyclerView, "Word deleted", Snackbar.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
