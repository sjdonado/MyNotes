package com.example.juan.mynotes.models;

import com.example.juan.mynotes.app.MyApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by juan on 8/12/17.
 */

public class Board extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private RealmList<Note> notes;
    private Date createdAt;

    public Board(){}

    public Board(String title) {
        this.id = MyApp.idBoard.incrementAndGet();
        this.title = title;
        this.notes = new RealmList<Note>();
        this.createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

    public String getNotesSize(){return notes.size() + "";}
    
    public String getCreatedAt() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(createdAt);
    }

}
