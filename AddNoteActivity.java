package com.example.mynotesapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mynotesapp.databinding.ActivityAddNoteBinding;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNoteActivity extends AppCompatActivity {
    private ActivityAddNoteBinding binding;
    private ExecutorService executorService;
    private NoteDao noteDao;
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(this);
        noteDao = db.noteDao();

        // Periksa jika ada catatan yang dipilih untuk diperbarui
        selectedNote = (Note) getIntent().getSerializableExtra("note");
        if (selectedNote != null) {
            binding.edtTitle.setText(selectedNote.getTitle());
            binding.edtDescription.setText(selectedNote.getDescription());
            binding.edtDate.setText(selectedNote.getDate());
        }

        // Tambahkan fitur DatePicker pada kolom tanggal
        binding.edtDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Tampilkan DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format tanggal yang dipilih
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        binding.edtDate.setText(dateFormat.format(selectedDate.getTime()));
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Logika untuk menambahkan catatan baru
        binding.btnAdd.setOnClickListener(v -> {
            String title = binding.edtTitle.getText().toString();
            String description = binding.edtDescription.getText().toString();
            String date = binding.edtDate.getText().toString();

            Note note = new Note(title, description, date);
            executorService.execute(() -> {
                noteDao.insert(note);
                finish();
            });
        });

        // Logika untuk memperbarui catatan yang dipilih
        binding.btnUpdate.setOnClickListener(v -> {
            if (selectedNote != null) {
                selectedNote.setTitle(binding.edtTitle.getText().toString());
                selectedNote.setDescription(binding.edtDescription.getText().toString());
                selectedNote.setDate(binding.edtDate.getText().toString());

                executorService.execute(() -> {
                    noteDao.update(selectedNote);
                    finish();
                });
            }
        });
    }
}
