package com.example.juan.mynotes.app;

import android.app.Application;

import com.example.juan.mynotes.models.Board;
import com.example.juan.mynotes.models.Note;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by juan on 8/12/17.
 */

public class MyApp extends Application {

    public static AtomicInteger idBoard = new AtomicInteger();
    public static AtomicInteger idNote = new AtomicInteger();

    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        // Set Realm default configuration
        Realm.init(getApplicationContext());
        Realm.setDefaultConfiguration(new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build());
        // Set realm instance
        realm = Realm.getDefaultInstance();
        idBoard = getIdByTable(realm, Board.class);
        idNote = getIdByTable(realm, Note.class);
        realm.close();
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
