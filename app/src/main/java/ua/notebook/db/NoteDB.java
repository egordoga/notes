package ua.notebook.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ua.notebook.dao.CategoryDao;
import ua.notebook.dao.NoteDao;
import ua.notebook.dao.NoteWithCategoryDao;
import ua.notebook.entity.Category;
import ua.notebook.entity.Note;

@Database(entities = {Note.class, Category.class}, version = 1)
public abstract class NoteDB extends RoomDatabase {
    private static NoteDB instance;
    private static final String DB_NAME = "note.db";

    public abstract NoteWithCategoryDao noteWithCategoryDao();

    public abstract NoteDao noteDao();

    public abstract CategoryDao categoryDao();

    public static NoteDB getDatabase(final Context context) {
        if (instance == null) {
            synchronized (NoteDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), NoteDB.class, DB_NAME)
                            .build();
                }
            }
        }
        return instance;
    }
}
