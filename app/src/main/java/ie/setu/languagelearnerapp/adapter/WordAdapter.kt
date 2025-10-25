package ie.setu.languagelearnerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.model.WordModel

class WordAdapter(
        private var words: MutableList<WordModel>,
        private val onDeleteClick: (Int) -> Unit
    ) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    fun updateList(newWords: List<WordModel>) {
        words.clear()
        words.addAll(newWords)
        notifyDataSetChanged()
    }
    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWord: TextView = itemView.findViewById(R.id.tvWord)
        val tvTranslation: TextView = itemView.findViewById(R.id.tvTranslation)
        val tvLanguage: TextView = itemView.findViewById(R.id.tvLanguage)
        val tvLevel: TextView = itemView.findViewById(R.id.tvLevel)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.tvWord.text = word.word
        holder.tvTranslation.text = word.translation
        holder.tvLanguage.text = word.language
        holder.tvLevel.text = word.level
        holder.btnDelete.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int = words.size
}
