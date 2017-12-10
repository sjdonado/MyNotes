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

    public static Board createNote(Realm realm, Board board, String title, String content){
        realm.beginTransaction();
        board.getNotes().add(new Note(title, content));
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
        return board;
    }

    public static Board deleteNote(Realm realm, Board board, int position){
        realm.beginTransaction();
        board.getNotes().remove(position);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
        return board;
    }
}
