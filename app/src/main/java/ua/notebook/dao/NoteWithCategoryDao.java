package ua.notebook.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import ua.notebook.entity.Category;
import ua.notebook.entity.Note;
import ua.notebook.entity.NoteWithCategoryName;

@Dao
public abstract class NoteWithCategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long addCategory(Category category);

    @Insert
    abstract void addNote(Note note);

    @Transaction
    public void addNoteWithCategory(Note note, Category category) {
        long id = addCategory(category);
        if (id != -1) {
            note.categoryId = id;
        }
        addNote(note);
    }

    @Update
    abstract void updateNote(Note note);

    @Transaction
    public void updateNoteWithCategory(Note note, Category category) {
        if (category != null) {
            note.categoryId = addCategory(category);
        }
        updateNote(note);
    }

    @Query("select name, title, body, date, nid, categoryId from category, note where note.categoryId == category.cid")
    abstract List<NoteWithCategoryName> findAllWithCategory();

    @Query("select name, title, body, date from  note inner join category on cid == categoryId where categoryId == :cid")
    abstract List<NoteWithCategoryName> findAllWithCategoryById(long cid);
}
