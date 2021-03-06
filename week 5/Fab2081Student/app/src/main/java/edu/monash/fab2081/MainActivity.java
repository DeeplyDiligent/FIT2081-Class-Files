package edu.monash.fab2081;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ListView myListView;
    private String dateAdded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myListView = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        myListView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addListItem()   ;
                Snackbar.make(view, "Item "+ dateAdded +" added to list", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listItems.remove(listItems.size() -1);
                        adapter.notifyDataSetChanged();
                        Snackbar.make(view, "Item "+ dateAdded +" deleted from list.", Snackbar.LENGTH_LONG).show();
                    }
                }).show();
            }
        });
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
        if (id == R.id.action_add_divider_to_list) {
            listItems.add("DIVIDER");
            adapter.notifyDataSetChanged();
        } else if (id == R.id.action_clear_list) {
            listItems.clear();
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addListItem() {
        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.ENGLISH);
        dateAdded = dateformat.format(new Date());
        listItems.add(dateAdded);
        adapter.notifyDataSetChanged();
    }
}
