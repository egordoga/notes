package ua.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ua.notebook.entity.Note;
import ua.notebook.entity.NoteWithCategoryName;
import ua.notebook.util.ItemTouchHelperAdapter;
import ua.notebook.viewModel.NoteViewModel;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>
        implements ItemTouchHelperAdapter {

    private List<NoteWithCategoryName> noteList;
    private LayoutInflater inflater;
    private ItemClickListener mListener;
    private NoteViewModel mViewModel;

    NotesAdapter(Context context, ItemClickListener listener, NoteViewModel viewModel) {
        inflater = LayoutInflater.from(context);
        mListener = listener;
        mViewModel = viewModel;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notes_item, parent, false);
        return new NoteViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.tvNote.setText(noteList.get(position).note.body);
        holder.tvTitle.setText(noteList.get(position).note.title);
        holder.tvCategory.setText(noteList.get(position).categoryName);
        holder.tvDate.setText(noteList.get(position).note.date);
    }

    @Override
    public int getItemCount() {
        if (noteList != null) {
            return noteList.size();
        } else {
            return 0;
        }
    }

    void setNotes(List<NoteWithCategoryName> notes) {
        noteList = notes;
        notifyDataSetChanged();
    }

    NoteWithCategoryName getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public void onItemDelete(int position) {
        Note note = noteList.get(position).note;
        mViewModel.delete(note);
        notifyItemRemoved(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNote;
        TextView tvTitle;
        TextView tvCategory;
        TextView tvDate;

        private ItemClickListener mListener;

        NoteViewHolder(@NonNull View itemView, ItemClickListener listener) {
            super(itemView);

            mListener = listener;
            itemView.setOnClickListener(this);
            tvNote = itemView.findViewById(R.id.tv_note_body);
            tvTitle = itemView.findViewById(R.id.tv_note_title);
            tvCategory = itemView.findViewById(R.id.tv_note_category);
            tvDate = itemView.findViewById(R.id.tv_note_date);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
}
