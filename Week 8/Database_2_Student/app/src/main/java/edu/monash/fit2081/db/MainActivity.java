/* launch activity */

package edu.monash.fit2081.db;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import edu.monash.fit2081.db.provider.SchemeShapes;

public class MainActivity extends AppCompatActivity {
    private NewShape newShape;
    private EditDeleteShape editDeleteShape;
    private ViewShapes viewShapes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int width, height;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //let's get the size of the display so we can set the size of the top FrameLayout to be 40% of this height
        //the status, app bar and bottom FrameLayout will take up the rest of the screen
        //the bottom FrameLayout will take up the rest of its RelativeLayout parent which it shares with
        //the top frame layout using match_parent
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        //now let the top FrameLayout's RelativeLayout parent size it
        FrameLayout frame = (FrameLayout) findViewById(R.id.fragment_top);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, (int) (height * 0.4));
        frame.setLayoutParams(lp);

        //lets instantiate all the fragment instances now to be efficient
        //viewShape and newShape will be needed immediately for the top and bottom FrameLayouts respectively
        //the other two will be needed when the option menu items are selected (see onOptionsItemSelected below)
        viewShapes = new ViewShapes();
        newShape = new NewShape();

        editDeleteShape = new EditDeleteShape();

        // Add the fragments to their parent 'fragment_container' FrameLayout
        //
        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.fragment_top, viewShapes, "viewFragment")
                                   .addToBackStack("viewFragment")
                                   .commit();

        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.fragment_bottom, newShape, "addFragment")
                                   .commit();

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

        if (id == R.id.add_shape) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom, newShape, "addFragment").commit();
            return true;
        }
        else if (id == R.id.delete_shape) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom, editDeleteShape, "editDeleteFragment").addToBackStack("editDeleteFragment").commit();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

    }

//    //following is not recommended but it is a way of disarming the back button if you need to
//    @Override
//    public void onBackPressed() {
//        // Empty to cancel the back button
//    }
}
