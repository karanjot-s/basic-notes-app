package com.karanjotsingh.basicnotes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karanjotsingh.basicnotes.R
import com.karanjotsingh.basicnotes.data.Note
import com.karanjotsingh.basicnotes.databinding.NotesListItemBinding

class NotesListAdapter(private val notesList: LiveData<List<Note>>, private val itemOnClickListener: (Long) -> Unit): ListAdapter<Note, NotesListAdapter.ViewHolder>(
    DiffCallback
) {
    lateinit var binding: NotesListItemBinding

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                getItem(adapterPosition)?.id?.let { it1 -> itemOnClickListener.invoke(it1) }
            }
        }

        fun bind(note: Note) {
            binding.notesListItemTitle.text = note.title
            binding.notesListItemNote.text = note.note
            if (note.pinned) binding.notesListPinIcon.setImageResource(R.drawable.ic_baseline_push_pin_24_light)
            else binding.notesListPinIcon.setImageResource(R.drawable.ic_outline_push_pin_24_light)

        }
    }

    object DiffCallback: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = NotesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun getItem(position: Int): Note? {
        return notesList.value?.get(position)
    }

}
