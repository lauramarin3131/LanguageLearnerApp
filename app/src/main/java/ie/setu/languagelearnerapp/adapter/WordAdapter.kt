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
import android.speech.tts.TextToSpeech
import java.util.Locale
import android.widget.ImageView
import android.net.Uri
import android.widget.Toast

class WordAdapter(
        var words: MutableList<WordModel>,
        private val onDeleteClick: (String) -> Unit,
        private val onEditClick: (WordModel) -> Unit,
        private val onFavoriteClick: (String) -> Unit
    ) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private var tts: TextToSpeech? = null

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
        val btnSpeak: ImageButton = itemView.findViewById(R.id.btnSpeak)
        val imgWord: ImageView = itemView.findViewById(R.id.imgWord)
        val btnViewImage: Button = itemView.findViewById(R.id.btnViewImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        if (tts == null) {
            tts = TextToSpeech(parent.context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                }
            }
        }
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


        holder.btnSpeak.setOnClickListener {
            tts?.speak(word.word, TextToSpeech.QUEUE_FLUSH, null, word.id)
        }



        holder.btnFavorite.setImageResource(
            if (word.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_border
        )
        holder.btnFavorite.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.colorAccent))
        holder.btnFavorite.setOnClickListener {
            onFavoriteClick(word.id)
        }
        holder.itemView.setOnClickListener {
            onEditClick(word)
        }
        holder.btnEdit.setOnClickListener { onEditClick(word) }
        holder.btnDelete.setOnClickListener {
            onDeleteClick(word.id)
        }
        holder.btnViewImage.visibility = View.GONE
        holder.imgWord.visibility = View.GONE

        if (!word.imageUri.isNullOrEmpty()) {
            holder.btnViewImage.visibility = View.VISIBLE
        }

        holder.btnViewImage.setOnClickListener {
            try {
                holder.imgWord.setImageURI(Uri.parse(word.imageUri))
                holder.imgWord.visibility = View.VISIBLE
                holder.btnViewImage.visibility = View.GONE
                Toast.makeText(holder.itemView.context, "Image loaded", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(holder.itemView.context, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
    }
        override fun getItemCount(): Int = words.size



}
