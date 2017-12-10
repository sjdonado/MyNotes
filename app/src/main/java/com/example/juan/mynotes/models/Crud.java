package com.example.juan.mynotes.models;

import io.realm.Realm;

/**
 * Created by juan on 9/12/17.
 */

public class Crud {

    //** CRUD Actions BOARD **/
    public static boolean createNewBoard(Realm realm, String boardName) {
        if(!verifyBoard(realm, boardName)){
            realm.beginTransaction();
            Board board = new Board(boardName);
            realm.copyToRealm(board);
            realm.commitTransaction();
            return true;
        }else{
            return false;
        }
    }

    public static boolean editBoard(Realm realm, String newName, Board board) {
        if(!verifyBoard(realm, newName)){
            realm.beginTransaction();
            board.setTitle(newName);
            realm.copyToRealmOrUpdate(board);
            realm.commitTransaction();
            return true;
        }else{
            return false;
        }
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

    private static boolean verifyBoard(Realm realm, String title){
        if(realm.where(Board.class).equalTo("title", title).findFirst() != null){
            return true;
        }else{
            return false;
        }
    }

    //** CRUD Actions NOTE **/

    public static void createNote(Realm realm, Board board, String title, String content){
        realm.beginTransaction();
        board.getNotes().add(new Note(title, content));
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    public static void deleteNote(Realm realm, Board board, Note note){
        realm.beginTransaction();
        board.getNotes().remove(note);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    public static void editNote(Realm realm, Board board, Note note, int noteId){
        realm.beginTransaction();
        board.getNotes().set(noteId, note);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }
}
