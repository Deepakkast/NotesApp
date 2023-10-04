package app.ind.notesapp.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.ind.notesapp.databinding.FragmentNoteBinding
import app.ind.notesapp.models.NoteRequest
import app.ind.notesapp.models.NoteResponse
import app.ind.notesapp.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()

    private var note :NoteResponse? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentNoteBinding.inflate(inflater,container,false)
        return binding.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setIntialData()
        bindHandlers()
        bindObservers()
    }

    private fun setIntialData() {
        val jsonNote = arguments?.getString("note")
        if (jsonNote!=null){
            note = Gson().fromJson(jsonNote,NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        }else{
            binding.addEditText.text = "ADD NOTE"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }
        })
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNote(it!!._id) }
        }
        binding.apply {
            btnSubmit.setOnClickListener {
                val title = txtTitle.text.toString()
                val description = txtDescription.text.toString()
                val noteRequest = NoteRequest(title, description)
                if (note == null) {
                    noteViewModel.createNote(noteRequest)
                } else {
                    noteViewModel.updateNote(noteRequest,note!!._id)
                }
            }
        }
    }


}