/* launch activity */

package edu.monash.fit2081.db;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.thebluealliance.spectrum.SpectrumDialog;

import edu.monash.fit2081.db.provider.SchemeShapes;

public class MainActivity extends AppCompatActivity {

    private String selectedShapeDrawing = "Circle";
    // used to access the app bar menu icons
    private Menu myMenu = null;

    private NewShape newShape;
    private EditDeleteShape editDeleteShape;
    private ViewShapes viewShapes;

    private ContentResolver resolver;

    private int selectedColor = -1; //-1 = no selected colour
    private String[] arColorsNames;
    private int[] arColorsValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int width, height;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // to be used by delete all shapes
        resolver = getApplicationContext().getContentResolver();


        arColorsNames = getResources().getStringArray(R.array.colorNames);
        arColorsValues = getResources().getIntArray(R.array.colorValues);

        if (selectedColor == -1)
            selectedColor = ContextCompat.getColor(this, R.color.md_blue_500);


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

        //Save the default drawing shape
        getSharedPreferences("settings", MODE_PRIVATE).edit().putString("selectedShapeDrawing", selectedShapeDrawing).apply();

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

    //OPTIONS MENU STUFF AND RELATED METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.myMenu = menu;
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
        } else if (id == R.id.edit_delete_shape) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom, editDeleteShape, "editDeleteFragment").addToBackStack("editDeleteFragment").commit();
            return true;
        } else if (id == R.id.draw_circle) {
            selectedShapeDrawing = "Circle";
            myMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_circle_white, null));
            myMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_square_black, null));
            myMenu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_line_black, null));
        } else if (id == R.id.draw_rectangle) {
            selectedShapeDrawing = "Rectangle";
            myMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_circle_black, null));
            myMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_square_white, null));
            myMenu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_line_black, null));
        } else if (id == R.id.draw_line) {
            selectedShapeDrawing = "Line";
            myMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_circle_black, null));
            myMenu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_square_black, null));
            myMenu.getItem(2).setIcon(getResources().getDrawable(R.drawable.ic_line_white, null));
        } else if (id == R.id.delete_all) {
            deleteAllShapes();
        } else if (id == R.id.show_Color_selector) {
            showSelectColor();
        } else {
            return super.onOptionsItemSelected(item);
        }
        getSharedPreferences("settings", MODE_PRIVATE).edit().putString("selectedShapeDrawing", selectedShapeDrawing).apply();

        return true;
    }


    private void deleteAllShapes() {
        resolver.delete(SchemeShapes.Shape.CONTENT_URI, null, null);
    }

    public void showSelectColor() {
        new SpectrumDialog.Builder(this)
                .setColors(R.array.demo_colors)
                .setSelectedColor(selectedColor)
                .setDismissOnColorSelected(true)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                            selectedColor = color;
                            //save the selected color to be used by ViewShapes (also set by NewShape)
                            getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putInt("selectColor", selectedColor).apply();

                            //by the color value, get the name of the color, which is the name of the color picker icon
                            String colorName = getColorName(selectedColor);
                            //change the icon based on the color
                            myMenu.getItem(3).setIcon(getResources().getIdentifier(colorName, "drawable", getPackageName()));
                        }
                    }
                }).build().show(this.getSupportFragmentManager(), "");
    }

    private String getColorName(int color) {
        String colorName = null;
        int index = 0;
        while (arColorsValues[index] != color)
            index++;

        colorName = arColorsNames[index];
        return colorName;
    }

}



/*
* Swipe gesture activities vary based on context. The speed at which a gesture is performed is the primary distinction between Drag, Swipe, and Fling.

Drag: Fine gesture, slower, more controlled, typically has an on-screen target
Swipe: Gross gesture, faster, typically has no on-screen target
Fling: Gross gesture, with no on-screen target
Gesture velocity impacts whether the action is immediately reversible.

A swipe becomes a fling based on ending velocity and whether the affected element has crossed a threshold (or point past which an action can be undone).
A drag maintains contact with an element, so reversing the direction of the gesture will drag the element back across the threshold.
A fling moves at a faster speed and removes contact with the element while it crosses the threshold, preventing the action from being undone.
*/


