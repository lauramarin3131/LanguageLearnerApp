package ie.setu.languagelearnerapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.adapter.WordAdapter
import com.google.android.material.snackbar.Snackbar
import ie.setu.languagelearnerapp.repository.WordRepository
import android.content.Intent
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar

class WordListActivity : AppCompatActivity() {

    private lateinit var adapter: WordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbarWordList)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Word List"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        WordRepository.load(this)
        title = "Word List"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewWords)

        WordRepository.load(this)
        adapter = WordAdapter(
            WordRepository.getAll().toMutableList(),
            onDeleteClick = { position ->
                WordRepository.deleteWord(this, position)
                adapter.updateList(WordRepository.getAll())
                Snackbar.make(
                    findViewById(R.id.recyclerViewWords),
                    "Word deleted",
                    Snackbar.LENGTH_SHORT
                ).show() },
            onEditClick = { word ->
                val intent = Intent(this, EditWordActivity::class.java)
                intent.putExtra("word", word)
                startActivity(intent)
            },
            onFavoriteClick = { word ->
                WordRepository.toggleFavorite(this, word)
                adapter.updateList(WordRepository.getAll())
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val spinnerLanguage = findViewById<Spinner>(R.id.spinnerLanguageFilter)
        val checkBoxFavorites = findViewById<CheckBox>(R.id.checkBoxFavorites)
        checkBoxFavorites.setOnCheckedChangeListener { _, _ -> applyFilters() }

        val languages =
            listOf("All", "English 🇬🇧", "Spanish 🇪🇸", "French 🇫🇷", "German 🇩🇪", "Italian 🇮🇹")
        spinnerLanguage.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val pickerLevel = findViewById<NumberPicker>(R.id.pickerLevelFilter)
        pickerLevel.minValue = 0
        pickerLevel.maxValue = 5
        pickerLevel.value = 0
        pickerLevel.setOnValueChangedListener { _, _, _ -> applyFilters() }

        val spinnerSort = findViewById<Spinner>(R.id.spinnerSort)
        val sortOptions = listOf("Alphabetical", "Level", "Date Added")
        spinnerSort.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sortList(sortOptions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilters() {
        val spinnerLanguage = findViewById<Spinner>(R.id.spinnerLanguageFilter)
        val pickerLevel = findViewById<NumberPicker>(R.id.pickerLevelFilter)
        val checkBoxFavorites = findViewById<CheckBox>(R.id.checkBoxFavorites)

        val lang = spinnerLanguage.selectedItem.toString()
        val lvl = pickerLevel.value
        val showFavOnly = checkBoxFavorites.isChecked
        val filtered = WordRepository.getAll().filter {
            (lang == "All" || it.language == lang) &&
                    (lvl == 0 || it.level.toInt() == lvl)&&
                    (!showFavOnly || it.isFavorite)
        }
        adapter.updateList(filtered.toMutableList())
    }

    private fun sortList(option: String) {
        val currentList = adapter.words
        val sorted = when (option) {
            "Alphabetical" -> currentList.sortedBy { it.word }
            "Level" -> currentList.sortedBy { it.level.toInt() }
            "Date Added" -> currentList.sortedBy { it.date }
            else -> currentList
        }
        adapter.updateList(sorted.toMutableList())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_word_list, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = WordRepository.getAll().filter {
                    it.word.contains(newText.orEmpty(), ignoreCase = true) ||
                            it.translation.contains(newText.orEmpty(), ignoreCase = true)
                }
                adapter.updateList(filtered.toMutableList())
                return true
            }
        })
        return true
    }

    private fun updateList() {
        adapter.updateList(WordRepository.getAll().toMutableList())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        adapter.updateList(WordRepository.getAll().toMutableList())
    }

}


