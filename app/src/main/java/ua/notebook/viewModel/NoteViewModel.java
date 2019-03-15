package ua.notebook.viewModel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ua.notebook.dao.NoteRepository;
import ua.notebook.entity.Category;
import ua.notebook.entity.Note;
import ua.notebook.entity.NoteWithCategoryName;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private MutableLiveData<List<NoteWithCategoryName>> notesWCN = new MutableLiveData<>();

    public NoteViewModel(@NonNull Application application) {
        super(application);

        noteRepository = new NoteRepository(application);
        notesWCN.setValue(noteRepository.getNotesWCN());
    }

    public LiveData<List<NoteWithCategoryName>> getNotesWCN() {
        return notesWCN;
    }

    public void insert(NoteWithCategoryName noteWCN) {
        noteRepository.insert(noteWCN);
    }

    public void update(NoteWithCategoryName noteWCN) {
        noteRepository.update(noteWCN);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public List<Category> findAllCategory() {
        return noteRepository.findAllCategory();
    }

    public void findNotesWcn() {
        notesWCN.setValue(noteRepository.findAllWithCategory());
    }

    public void findNotesWcnByCategoryId(long cid) {
        noteRepository.findAllWithCategoryById(cid);
        notesWCN.setValue(noteRepository.getNotesWCN());
    }

    public int addCategory(String name) {
        return noteRepository.addCategory(name);
    }
}
