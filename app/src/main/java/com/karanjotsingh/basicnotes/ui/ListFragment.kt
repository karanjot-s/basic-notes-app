package com.karanjotsingh.basicnotes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.karanjotsingh.basicnotes.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var viewModel: NotesListViewModel
    private var _binding: FragmentListBinding? = null  // between onCreateView and onDestroyView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NotesListViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        val binding = _binding!!

        binding.notesList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = NotesListAdapter(viewModel.notesList) {
                findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToNoteDetailsFragment(
                        it
                    )
                )
            }
        }

        viewModel.notesList.observe(viewLifecycleOwner) {
            // refreshing the list when notes list changes
            (binding.notesList.adapter as NotesListAdapter).submitList(it)
        }

        val fab = (activity as MainActivity).fab ?: error("Fab called outside of main activity")

        fab.setImageResource(android.R.drawable.ic_input_add)
        fab.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToNoteDetailsFragment(
                    0
                )
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
