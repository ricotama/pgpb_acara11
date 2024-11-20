package com.example.mynotesapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mynotesapp.AddNoteActivity;
import com.example.mynotesapp.NoteAdapter;
import com.example.mynotesapp.NoteDao;
import com.example.mynotesapp.NoteRoomDatabase;
import com.example.mynotesapp.databinding.ActivityMainBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NoteAdapter adapter;
    private NoteDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(this);
        noteDao = db.noteDao();

        adapter = new NoteAdapter(new ArrayList<>(), note -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra("note", note);
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
        });

        getAllNotes();
    }

    private void getAllNotes() {
        noteDao.getAllNotes().observe(this, notes -> {
            adapter.setNotes(notes);
        });
    }
}