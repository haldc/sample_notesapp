package com.example.notesapp_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_TEXT = "EXTRA_TEXT";

    private EditText noteTitle, noteText;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        noteText = findViewById(R.id.idEdtNoteText);
        noteTitle = findViewById(R.id.idEdtNoteTitle);
        saveBtn = findViewById(R.id.idBtnSaveNote);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)) {
            noteText.setText(intent.getStringExtra(EXTRA_TEXT));
            noteTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = noteTitle.getText().toString();
                String text = noteText.getText().toString();

                if(title.isEmpty() || text.isEmpty()) {
                    Toast.makeText(NewNoteActivity.this, "Please enter something", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveNote(title, text);
            }
        });
    }

    private void saveNote(String title, String text) {
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_TEXT, text);
        String id = getIntent().getStringExtra(EXTRA_ID);
        if (id != null && id.length() > 0) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}