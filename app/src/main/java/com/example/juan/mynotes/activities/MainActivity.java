package com.example.juan.mynotes.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.fragments.ManageBoardFragment;
import com.example.juan.mynotes.fragments.NotesFragment;
import com.example.juan.mynotes.models.Board;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RealmChangeListener<RealmResults<Board>> {

    private NotesFragment mainFragment;
    private Realm realm;
    private RealmResults<Board> boards;
    private Menu menu;
    private ManageBoardFragment manageBoardFragment;
    private int id = Menu.FIRST;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();

        realm = Realm.getDefaultInstance();

        createNewBoard("Main Board");

        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);

        setMenuBoards(boards);

        // Set main fragment
        if(savedInstanceState != null) return;
        mainFragment = new NotesFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragments_container, mainFragment)
                .commit();

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
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.edit_boards:
                manageBoardFragment = new ManageBoardFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragments_container, manageBoardFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setMenuBoards(RealmResults<Board> boards){
        for(Board board : boards){
            menu.add(R.id.menuBoards, id++, Menu.FIRST, board.getTitle()).setIcon(R.drawable.ic_book_black_24dp);
        }
    }

    @Override
    public void onChange(RealmResults<Board> boards) {
        setMenuBoards(boards);
    }

    private void createNewBoard(String title) {
        realm.beginTransaction();
        realm.copyToRealm(new Board(title));
        realm.commitTransaction();
    }
}
