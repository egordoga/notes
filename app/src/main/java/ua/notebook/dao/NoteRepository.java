package ua.notebook.dao;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.notebook.db.NoteDB;
import ua.notebook.entity.Category;
import ua.notebook.entity.Note;
import ua.notebook.entity.NoteWithCategoryName;

public class NoteRepository {

    private NoteDao noteDao;
    private CategoryDao categoryDao;
    private NoteWithCategoryDao mNoteWithCategoryDao;
    private List<NoteWithCategoryName> notesWCN;

    public NoteRepository(Application application) {
        NoteDB db = NoteDB.getDatabase(application);
        noteDao = db.noteDao();
        categoryDao = db.categoryDao();
        mNoteWithCategoryDao = db.noteWithCategoryDao();
        notesWCN = findAllWithCategory();
    }

    public List<NoteWithCategoryName> getNotesWCN() {
        return notesWCN;
    }

    public void insert(NoteWithCategoryName noteWCN) {
        new InsertWcnAsyncTask(mNoteWithCategoryDao, categoryDao).execute(noteWCN);
    }

    public void update(NoteWithCategoryName noteWCN) {
        new UpdateWcnAsyncTask(mNoteWithCategoryDao, categoryDao).execute(noteWCN);
    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public List<Category> findAllCategory() {
        try {
            return new FindAllCategoryAsyncTask(categoryDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NoteWithCategoryName> findAllWithCategory() {
        try {
            return new FindAllNotesWcnAsyncTask(mNoteWithCategoryDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void findAllWithCategoryById(long cid) {
        try {
            notesWCN = new FindNotesWcnByCategoryIdAsyncTask(mNoteWithCategoryDao).execute(cid).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int addCategory(String name) {
        int id = 0;
        try {
            id = new AddCategoryAsyncTask(categoryDao).execute(name).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    private static class InsertWcnAsyncTask extends AsyncTask<NoteWithCategoryName, Void, Void> {

        private NoteWithCategoryDao asyncNoteWcDao;
        private CategoryDao asyncCategoryDao;

        InsertWcnAsyncTask(NoteWithCategoryDao asyncNoteWcDao, CategoryDao asyncCategoryDao) {
            this.asyncNoteWcDao = asyncNoteWcDao;
            this.asyncCategoryDao = asyncCategoryDao;
        }

        @Override
        protected Void doInBackground(NoteWithCategoryName... notes) {
            Category category;
            Note note = notes[0].note;
            Category categoryFromDB = asyncCategoryDao.findByName(notes[0].categoryName);
            if (categoryFromDB != null) {
                category = categoryFromDB;
                note.categoryId = categoryFromDB.cid;
            } else {
                category = new Category(notes[0].categoryName);
            }
            asyncNoteWcDao.addNoteWithCategory(note, category);
            return null;
        }
    }

    private static class UpdateWcnAsyncTask extends AsyncTask<NoteWithCategoryName, Void, Void> {

        private NoteWithCategoryDao asyncNoteWcDao;
        private CategoryDao asyncCategoryDao;

        UpdateWcnAsyncTask(NoteWithCategoryDao asyncNoteWcDao, CategoryDao asyncCategoryDao) {
            this.asyncNoteWcDao = asyncNoteWcDao;
            this.asyncCategoryDao = asyncCategoryDao;
        }

        @Override
        protected Void doInBackground(NoteWithCategoryName... notes) {
            Note note = notes[0].note;
            Category categoryFromDB = asyncCategoryDao.findByName(notes[0].categoryName);
            if (categoryFromDB != null) {
                note.categoryId = categoryFromDB.cid;
                asyncNoteWcDao.updateNoteWithCategory(note, null);
            } else {
                Category category = new Category(notes[0].categoryName);
                asyncNoteWcDao.updateNoteWithCategory(note, category);
            }

            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao asyncNoteDao;

        DeleteNoteAsyncTask(NoteDao asyncNoteDao) {
            this.asyncNoteDao = asyncNoteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            asyncNoteDao.delete(notes[0]);
            return null;
        }
    }

    private static class FindAllCategoryAsyncTask extends AsyncTask<Void, Void, List<Category>> {
        private CategoryDao asyncCategoryDao;

        FindAllCategoryAsyncTask(CategoryDao categoryDao) {
            asyncCategoryDao = categoryDao;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            return asyncCategoryDao.findAll();
        }
    }

    private static class FindNotesWcnByCategoryIdAsyncTask extends AsyncTask<Long, Void,
            List<NoteWithCategoryName>> {
        private NoteWithCategoryDao asyncNoteWcnDao;

        FindNotesWcnByCategoryIdAsyncTask(NoteWithCategoryDao asyncNoteWcnDao) {
            this.asyncNoteWcnDao = asyncNoteWcnDao;
        }

        @Override
        protected List<NoteWithCategoryName> doInBackground(Long... ids) {
            return asyncNoteWcnDao.findAllWithCategoryById(ids[0]);
        }
    }

    private static class FindAllNotesWcnAsyncTask extends AsyncTask<Void, Void, List<NoteWithCategoryName>> {
        private NoteWithCategoryDao asyncNoteWcnDao;

        FindAllNotesWcnAsyncTask(NoteWithCategoryDao asyncNoteWcnDao) {
            this.asyncNoteWcnDao = asyncNoteWcnDao;
        }

        @Override
        protected List<NoteWithCategoryName> doInBackground(Void... voids) {
            return asyncNoteWcnDao.findAllWithCategory();
        }
    }

    private static class AddCategoryAsyncTask extends AsyncTask<String, Void, Integer> {
        private CategoryDao asyncCategoryDao;

        AddCategoryAsyncTask(CategoryDao asyncCategoryDao) {
            this.asyncCategoryDao = asyncCategoryDao;
        }


        @Override
        protected Integer doInBackground(String... strings) {
            Category fromDB = asyncCategoryDao.findByName(strings[0]);
            if (fromDB == null) {
                long cid = asyncCategoryDao.add(new Category(strings[0]));
                return (int) cid;
            }
            return -1;
        }
    }

}
