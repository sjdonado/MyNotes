package com.example.juan.mynotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.fragments.BoardsFragment;
import com.example.juan.mynotes.fragments.NotesFragment;
import com.example.juan.mynotes.models.Board;
import com.example.juan.mynotes.models.Crud;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RealmChangeListener<RealmResults<Board>> {

    private Realm realm;
    private RealmResults<Board> boards;
    private Menu menu;
    private BoardsFragment boardsFragment;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();

        realm = Realm.getDefaultInstance();

        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);

        if(boards.isEmpty()) Crud.createNewBoard(realm, "Main board");

        // Instance fragments
        boardsFragment = new BoardsFragment();

        Bundle bundle = new Bundle();
        NotesFragment notesFragment = new NotesFragment();
        bundle.putInt("id", boards.get(0).getId());
//                    bundle.putString("board", new Gson().toJson(realm.copyFromRealm(boards.get(id))));
        notesFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragments_container, notesFragment)
                .commit();


//        if (savedInstanceState != null) return;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        clearBackstack();
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.edit_boards:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragments_container, boardsFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.backup:
                startActivity(new Intent(this, BackupActivity.class));
                break;
            case R.id.settings_user:
                break;
            default:
                if(boards.get(id) != null){
                    Bundle bundle = new Bundle();
                    NotesFragment notesFragment = new NotesFragment();
                    bundle.putInt("id", boards.get(id).getId());
//                    bundle.putString("board", new Gson().toJson(realm.copyFromRealm(boards.get(id))));
                    notesFragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragments_container, notesFragment)
                            .commit();
                    break;
                }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setMenu(RealmResults<Board> boards){
        menu.removeGroup(R.id.menuBoards);
        for(int i = 0; i < boards.size(); i++){
            menu.add(R.id.menuBoards, i, 0, boards.get(i).getTitle()).setIcon(R.drawable.ic_book_black_24dp);
        }
        menu.setGroupCheckable(R.id.menuBoards, true, false);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setMenu(boards);
        this.menu.findItem(0).setChecked(true);
        return true;
    }

    @Override
    public void onChange(RealmResults<Board> boards) {
        setMenu(boards);
    }

    public void clearBackstack() {
        int backStackEntryCount = getSupportFragmentManager()
                .getBackStackEntryCount();

        for (int i = 0; i < backStackEntryCount; i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

}
