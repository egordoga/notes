package ua.notebook.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import ua.notebook.R;

public class AddCategoryDialogFragment extends DialogFragment {
    private AddCategoryDialogListener listener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_category, null);
        EditText etName = view.findViewById(R.id.et_category_add);
        builder
                .setTitle("Add category")
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    String categoryName = etName.getText().toString();
                    listener.applyCategoryName(categoryName);
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddCategoryDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement AddCategoryDialogListener");
        }
    }

    public interface AddCategoryDialogListener {
        void applyCategoryName(String categoryName);
    }
}
