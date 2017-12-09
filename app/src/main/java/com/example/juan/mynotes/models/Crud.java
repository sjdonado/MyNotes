package com.example.juan.mynotes.models;

import io.realm.Realm;

/**
 * Created by juan on 9/12/17.
 */

public class Crud {

    public static void createNewBoard(Realm realm, String title) {
        realm.beginTransaction();
        realm.copyToRealm(new Board(title));
        realm.commitTransaction();
    }

    public static void deleteBoard(Realm realm, Board board) {
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }
}
