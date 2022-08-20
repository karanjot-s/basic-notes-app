package com.karanjotsingh.basicnotes.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karanjotsingh.basicnotes.R
import com.karanjotsingh.basicnotes.data.Note
import com.karanjotsingh.basicnotes.databinding.FragmentNoteDetailsBinding

class NoteDetailsFragment : Fragment() {

    private lateinit var viewModel: NoteDetailsViewModel
    private var _binding: FragmentNoteDetailsBinding? = null
    private var menuHost: MenuHost? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NoteDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)

        val binding = _binding!!

        // set fab
        val fab = (activity as MainActivity).fab
        fab?.let {
            it.setImageResource(R.drawable.ic_round_save_24)
            it.setOnClickListener {
                saveNote()
                requireActivity().onBackPressed()
            }
        }

        // set menu
        menuHost = requireActivity()

        // get id from previous fragment
        val id = NoteDetailsFragmentArgs.fromBundle(requireArguments()).noteId
        viewModel.setNoteId(id)

        // observer onchange for note
        viewModel.note.observe(viewLifecycleOwner) {
            it?.let { setData(it, fab) }
        }

        setMenu(menuHost!!)

        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        menuHost = null
    }

    private fun setData(note: Note, fab: FloatingActionButton?) {
        /*
        only call between onCreateView and onDestroyView
         */
        val binding = _binding!!

        binding.notesDetailTitle.editText?.setText(note.title)
        binding.notesDetailNote.editText?.setText(note.note)
        viewModel.pinned = note.pinned
        activity?.invalidateOptionsMenu()

    }

    private fun saveNote() {
        _binding?.let {
            val title = it.notesDetailTitle.editText?.text.toString()
            val noteText = it.notesDetailNote.editText?.text.toString()
            val id = viewModel.noteId.value!!
            val pinned = viewModel.pinned
            val note = Note(title, noteText, pinned, id)
            viewModel.saveNote(note)
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Note").setMessage("Are you sure you want to delete this note")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                viewModel.deleteNote()
                requireActivity().onBackPressed()
            }).setNegativeButton("No", DialogInterface.OnClickListener { _, _ -> }).show()
    }

    private fun setMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.note_detail_menu, menu)

                if (viewModel.pinned) menu.findItem(R.id.pinNote)
                    .setIcon(R.drawable.ic_baseline_push_pin_24)
                else menu.findItem(R.id.pinNote).setIcon(R.drawable.ic_outline_push_pin_24)


            }



            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.deleteNote -> {
                        deleteNote()
                        false
                    }
                    R.id.pinNote -> {
                        pinNote()
                        false
                    }
                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun pinNote() {
        viewModel.togglePinned()
        activity?.invalidateOptionsMenu()
    }
}
