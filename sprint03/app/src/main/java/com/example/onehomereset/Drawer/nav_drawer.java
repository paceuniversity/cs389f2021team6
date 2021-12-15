package com.example.onehomereset.Drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onehomereset.HomePageFragment;
import com.example.onehomereset.MainActivity;
import com.example.onehomereset.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class nav_drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Menu menu;
    MenuItem item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomePageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_homePage);
        }

    }


    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    //@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_homePage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomePageFragment()).commit();
                break;
            case R.id.nav_addItem:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddItemFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_helpUsimprove:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HelpUsImproveFragment()).commit();
                break;
            case R.id.nav_share:
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "This is a free share App \n" + "https://drive.google.com/file/d/1OQz6lNXLoLTG1coyijDEz3RYgWVZHy_I/view";
                String shareSub = "Get what you want for free! " ;
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
                Toast.makeText(this,"share",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent Mainactivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Mainactivity);
                Toast.makeText(this,"Log Out",Toast.LENGTH_LONG).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}