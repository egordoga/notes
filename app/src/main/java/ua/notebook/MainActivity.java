package ua.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ImageButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ua.notebook.dialog.AddCategoryDialogFragment;
import ua.notebook.dialog.ExistsCategoryDialogFragment;
import ua.notebook.entity.Category;
import ua.notebook.entity.NoteWithCategoryName;
import ua.notebook.util.SwipeToDeleteCallback;
import ua.notebook.viewModel.NoteViewModel;

public class MainActivity extends AppCompatActivity implements AddCategoryDialogFragment.AddCategoryDialogListener {

    @BindView(R.id.rv_notes)
    RecyclerView notes;
    @BindView(R.id.ib_add)
    ImageButton btnAdd;

    private NotesAdapter adapter;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notes.setLayoutManager(layoutManager);
        NotesAdapter.ItemClickListener listener = (view, position) -> goAddActivity(adapter.getItem(position), true);
        adapter = new NotesAdapter(this, listener, noteViewModel);
        notes.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> goAddActivity(null, false));

        noteViewModel.getNotesWCN().observe(this, list ->
                adapter.setNotes(list));


        RecyclerView recyclerView = findViewById(R.id.rv_notes);
        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SubMenu subMenu = menu.addSubMenu("Find by category");
        List<Category> categories = noteViewModel.findAllCategory();
        subMenu.add(Menu.NONE, 500, Menu.NONE, "All");
        for (Category category : categories) {
            subMenu.add(Menu.NONE, (int) category.cid, Menu.NONE, category.name);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itm_category_add:
                addNewCategoryDialog();
                break;
            case 500:
                noteViewModel.findNotesWcn();
                break;
            default:
                noteViewModel.findNotesWcnByCategoryId(item.getItemId());
        }
        return true;
    }

    private void addNewCategoryDialog() {
        AddCategoryDialogFragment dialog = new AddCategoryDialogFragment();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void goAddActivity(NoteWithCategoryName noteWCN, boolean isUpdate) {
        Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
        if (isUpdate) {
            addIntent.putExtra("noteWCN", noteWCN);
        }
        startActivity(addIntent);
    }

    @Override
    public void applyCategoryName(String categoryName) {
        int cid = noteViewModel.addCategory(categoryName);
        if (cid == -1) {
            new ExistsCategoryDialogFragment().show(getSupportFragmentManager(), "exists");
        }
    }
}
