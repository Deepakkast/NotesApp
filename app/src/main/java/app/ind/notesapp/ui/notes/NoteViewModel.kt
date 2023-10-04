package app.ind.notesapp.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.ind.notesapp.models.NoteRequest
import app.ind.notesapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository):ViewModel() {


    val notesLiveData get() = noteRepository.notesLiveData
    val statusLiveData get() = noteRepository.stausLiveData


    fun getNotes(){
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }

    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }

    fun updateNote(noteRequest: NoteRequest,noteId:String){
        viewModelScope.launch {
            noteRepository.updateNote(noteRequest,noteId)
        }
    }

    fun deleteNote(noteId: String){
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

}