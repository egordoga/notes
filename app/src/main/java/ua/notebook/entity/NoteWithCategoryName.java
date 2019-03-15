package ua.notebook.entity;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class NoteWithCategoryName implements Serializable {

    @Embedded
    public Note note;

    @ColumnInfo(name = "name")
    public String categoryName;

    public NoteWithCategoryName(Note note, String categoryName) {
        this.note = note;
        this.categoryName = categoryName;
    }
}
