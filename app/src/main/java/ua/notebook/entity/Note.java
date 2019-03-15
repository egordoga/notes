package ua.notebook.entity;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Category.class, parentColumns = "cid", childColumns = "categoryId",
        onDelete = ForeignKey.CASCADE))
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long nid;

    public String title;
    public String body;
    public String date;

    public long categoryId;

    public Note(String title, String body, String date) {
        this.title = title;
        this.body = body;
        this.date = date;
    }
}
