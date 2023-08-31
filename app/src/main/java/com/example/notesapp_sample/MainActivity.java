package com.example.notesapp_sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp_sample.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Optional;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView notesRV;
    private final NotesRVAdapter adapter = new NotesRVAdapter(Note.notes);

    ActivityResultLauncher<Intent> addNoteActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String title = data.getStringExtra(NewNoteActivity.EXTRA_TITLE);
                        String text = data.getStringExtra(NewNoteActivity.EXTRA_TEXT);
                        UUID id = UUID.randomUUID();
                        Note note = new Note(id, title, text);
                        Note.notes.add(note);
                        adapter.notifyItemInserted(Note.notes.indexOf(note));
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> editNoteActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        UUID id = UUID.fromString(data.getStringExtra(NewNoteActivity.EXTRA_ID));
                        String title = data.getStringExtra(NewNoteActivity.EXTRA_TITLE);
                        String text = data.getStringExtra(NewNoteActivity.EXTRA_TEXT);

                        Optional<Note> note = Note.notes.stream().filter((Note n) ->
                                n.getId().toString().equals(id.toString())).findFirst();

                        if(note.isPresent()) {
                            note.get().setText(text);
                            note.get().setTitle(title);
                            adapter.notifyItemChanged(Note.notes.indexOf(note.get()));
                            Toast.makeText(MainActivity.this, "Note updated", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesRV = findViewById(R.id.idRVNotes);
        FloatingActionButton fab = findViewById(R.id.idAdd);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                addNoteActivityLauncher.launch(intent);

            }
        });

        notesRV.setAdapter(adapter);

        notesRV.setLayoutManager(new LinearLayoutManager(this));
        notesRV.setHasFixedSize(true);

        //below method is use to add swipe to delete method for item of recycler view.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //on recycler view item swiped then we are deleting the item of our recycler view.
                Note.notes.remove(viewHolder.getAdapterPosition());
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(notesRV);

        //below line is use to set item click listner for our item of recycler view.
        adapter.setOnItemClickListener(new NotesRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note model) {
                //after clicking on item of recycler view
                //we are opening a new activity and passing a data to our activity.
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                intent.putExtra(NewNoteActivity.EXTRA_ID, model.getId().toString());
                intent.putExtra(NewNoteActivity.EXTRA_TEXT, model.getText());
                intent.putExtra(NewNoteActivity.EXTRA_TITLE, model.getTitle());
                //below line is to start a new activity and adding a edit course constant.
                editNoteActivityLauncher.launch(intent);

            }
        });


    }


}