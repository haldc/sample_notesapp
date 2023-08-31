package com.example.notesapp_sample.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Note {

    public static List<Note> notes = new ArrayList<>();

    public Note(UUID id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    private UUID id;
    private String title;
    private String text;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
