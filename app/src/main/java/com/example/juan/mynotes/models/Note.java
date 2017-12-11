package com.example.juan.mynotes.models;

import com.example.juan.mynotes.app.MyApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by juan on 8/12/17.
 */

public class Note extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private String content;
    private Date createdAt;

    public Note(){}

    public Note(String title, String content) {
        this.id = MyApp.idNote.incrementAndGet();
        this.title = title;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAtString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(createdAt);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
