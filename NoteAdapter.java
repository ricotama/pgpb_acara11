package com.example.mynotesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public NoteAdapter(List<Note> notes, OnItemClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note, listener);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView descriptionView;
        private TextView dateView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.tv_title);
            descriptionView = itemView.findViewById(R.id.tv_description);
            dateView = itemView.findViewById(R.id.tv_date);
        }

        public void bind(final Note note, final OnItemClickListener listener) {
            titleView.setText("Title: " + note.getTitle());
            descriptionView.setText("Desc: " + note.getDescription());
            dateView.setText("Date: " + note.getDate());

            itemView.setOnClickListener(v -> listener.onItemClick(note));
        }
    }
}
