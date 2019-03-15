package ua.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ua.notebook.entity.Category;
import ua.notebook.entity.Note;
import ua.notebook.entity.NoteWithCategoryName;
import ua.notebook.viewModel.NoteViewModel;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_body)
    EditText etBody;
    @BindView(R.id.actv_category)
    AutoCompleteTextView actvCategory;
    @BindView(R.id.btn_save)
    Button btnSave;

    private List<String> names = new ArrayList<>();
    private NoteViewModel mNoteViewModel;
    private NoteWithCategoryName noteWcnUpdate;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ButterKnife.bind(this);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        List<Category> categories = mNoteViewModel.findAllCategory();
        for (Category category : categories) {
            names.add(category.name);
        }

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this,
                android.R.layout.select_dialog_item, names);
        actvCategory.setThreshold(1);
        actvCategory.setAdapter(categoriesAdapter);

        Intent intentFromMain = getIntent();
        if (intentFromMain.hasExtra("noteWCN")) {
            isUpdate = true;
            noteWcnUpdate = (NoteWithCategoryName) intentFromMain.getSerializableExtra("noteWCN");
            actvCategory.setText(noteWcnUpdate.categoryName);
            etTitle.setText(noteWcnUpdate.note.title);
            etBody.setText(noteWcnUpdate.note.body);
        }

        btnSave.setOnClickListener(v -> saveNote(isUpdate));
    }

    public void saveNote(boolean isUpdate) {
        String categoryName = actvCategory.getText().toString();
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();

        if (isUpdate) {
            noteWcnUpdate.categoryName = categoryName;
            noteWcnUpdate.note.title = title;
            noteWcnUpdate.note.body = body;
            mNoteViewModel.update(noteWcnUpdate);
            this.isUpdate = false;
        } else {
            Note note = new Note(title, body, getDate());
            NoteWithCategoryName noteWCN = new NoteWithCategoryName(note, categoryName);
            mNoteViewModel.insert(noteWCN);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
        return sdf.format(new Date());
    }
}
