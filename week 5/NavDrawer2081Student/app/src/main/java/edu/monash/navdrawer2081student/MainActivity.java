package edu.monash.navdrawer2081student;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public boolean alreadyCenter = true;
    private float vpos = 0.5f;
    private float hpos = 0.5f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Centering text!", Snackbar.LENGTH_LONG)
                        .setAction("DO IT", (alreadyCenter)? null : new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(((ConstraintLayout) findViewById(R.id.nav_demo_text_container)));
                                constraintSet.setVerticalBias(R.id.nav_demo_text, 0.5f);
                                constraintSet.setHorizontalBias(R.id.nav_demo_text, 0.5f);
                                constraintSet.applyTo((ConstraintLayout) findViewById(R.id.nav_demo_text_container));
                            }
                        }).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        alreadyCenter = false;
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(((ConstraintLayout) findViewById(R.id.nav_demo_text_container)));
        if (id == R.id.up) {
            vpos = 0f;
//            if (vpos-0.2f<0f){
//                vpos = 1f;
//            }else{
//                vpos -= 0.2f;
//            }
            constraintSet.setVerticalBias(R.id.nav_demo_text, vpos);
            constraintSet.applyTo((ConstraintLayout) findViewById(R.id.nav_demo_text_container));
        } else if (id == R.id.down) {
//            if (vpos+0.2f>1f){
//                vpos = 0f;
//            }else{
//                vpos += 0.2f;
//            }
            vpos = 1f;
            constraintSet.setVerticalBias(R.id.nav_demo_text, vpos);
            constraintSet.applyTo((ConstraintLayout) findViewById(R.id.nav_demo_text_container));

        } else if (id == R.id.left) {
//            if (hpos-0.2f<0f){
//                hpos = 1f;
//            }else{
//                hpos -=0.2f;
//            }
            hpos = 0f;
            constraintSet.setHorizontalBias(R.id.nav_demo_text, hpos);
            constraintSet.applyTo((ConstraintLayout) findViewById(R.id.nav_demo_text_container));

        } else if (id == R.id.right) {
//            if (hpos+0.2f>1f){
//                hpos = 0f;
//            }else{
//                hpos += 0.2f;
//            }
            hpos = 1f;
            constraintSet.setHorizontalBias(R.id.nav_demo_text, hpos);
            constraintSet.applyTo((ConstraintLayout) findViewById(R.id.nav_demo_text_container));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
