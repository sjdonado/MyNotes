package com.example.juan.mynotes.models;

import io.realm.Realm;

/**
 * Created by juan on 9/12/17.
 */

public class Crud {

    //** CRUD Actions **/
    public static void createNewBoard(Realm realm, String boardName) {
        realm.beginTransaction();
        Board board = new Board(boardName);
        realm.copyToRealm(board);
        realm.commitTransaction();
    }

    public static void editBoard(Realm realm, String newName, Board board) {
        realm.beginTransaction();
        board.setTitle(newName);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    public static void deleteBoard(Realm realm, Board board) {
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }

    public static void deleteAll(Realm realm) {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

}
