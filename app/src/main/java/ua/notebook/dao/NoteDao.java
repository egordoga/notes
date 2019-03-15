package ua.notebook.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import ua.notebook.entity.Note;

@Dao
public interface NoteDao {

    @Delete
    void delete(Note note);

    @Query("select * from note")
    List<Note> findAll();
}
