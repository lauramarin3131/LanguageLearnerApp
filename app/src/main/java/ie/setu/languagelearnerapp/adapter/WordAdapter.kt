package ie.setu.languagelearnerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ie.setu.languagelearnerapp.R
import ie.setu.languagelearnerapp.model.WordModel
import android.widget.ImageButton
import androidx.core.content.ContextCompat

class WordAdapter(
        var words: MutableList<WordModel>,
        private val onDeleteClick: (Int) -> Unit,
        private val onEditClick: (WordModel) -> Unit,
        private val onFavoriteClick: (WordModel) -> Unit
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
        val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
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
        holder.itemView.setOnClickListener { onEditClick(word) }
        holder.btnFavorite.setImageResource(
            if (word.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_border
        )
        holder.btnFavorite.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.colorAccent))
        holder.btnFavorite.setOnClickListener {
            onFavoriteClick(word)
        }
    }
        override fun getItemCount(): Int = words.size



}
