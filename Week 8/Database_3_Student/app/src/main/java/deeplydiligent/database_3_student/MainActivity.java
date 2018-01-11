package deeplydiligent.database_3_student;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView displayText;
    ArrayList<ContentValues> shapes = new ArrayList<ContentValues>();
    int maxRadius = 0;
    int numberOfCircles = 0;
    int maxAreaRect = 0;
    int numberOfRect = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        displayText = (TextView) findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Clearing Display", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                displayText.setText("");
            }
        });

        Button circles = (Button) findViewById(R.id.button_circles);
        circles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetrieveCircles();
                displayText.setText("There are "+numberOfCircles+" circles. \nMax radius is "+maxRadius+".");
            }
        });
        Button rect = (Button) findViewById(R.id.button_rect);
        rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetrieveRect();
                displayText.setText("There are "+ numberOfRect+" rectangles. \nMax area is "+maxAreaRect+".");
            }
        });

    }

    private void onRetrieveCircles(){
        maxRadius = 0;
        numberOfCircles =0;
        // Use SQLiteQueryBuilder for querying db
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set the table name
        queryBuilder.setTables(SchemeShapes.Shape.TABLE_NAME);

        Cursor c = getContentResolver().query(SchemeShapes.Shape.CONTENT_URI, null, null, null,null);

        if (c.moveToFirst()) {
            do{
//                ContentValues contentValues = new ContentValues();
//
//                contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_TYPE)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_X, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_X)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_Y, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_Y)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_RADIUS, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_RADIUS)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_WIDTH, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_WIDTH)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_HEIGHT, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_HEIGHT)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_COLOR)));
//
//                shapes.add(contentValues);
                if(c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_TYPE)).equalsIgnoreCase("circle")){
                    int radiusOfCircle = Integer.parseInt(c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_RADIUS)));
                    if (maxRadius<radiusOfCircle){
                        maxRadius = radiusOfCircle;
                    }
                    numberOfCircles++;
                }
            } while (c.moveToNext());
        }
    }
    private void onRetrieveRect(){
        numberOfRect= 0;
        maxAreaRect = 0;
        Cursor c = getContentResolver().query(SchemeShapes.Shape.CONTENT_URI, null, null, null,null);

        if (c.moveToFirst()) {
            do{
//                ContentValues contentValues = new ContentValues();
//
//                contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_TYPE)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_X, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_X)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_Y, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_Y)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_RADIUS, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_RADIUS)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_WIDTH, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_WIDTH)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_HEIGHT, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_HEIGHT)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS)));
//                contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_COLOR)));
//
//                shapes.add(contentValues);
                if(c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_TYPE)).equalsIgnoreCase("rectangle")){
                    int areaofRect = Integer.parseInt(c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_WIDTH)))
                            * Integer.parseInt(c.getString(c.getColumnIndex(SchemeShapes.Shape.SHAPE_HEIGHT)));
                    if (maxAreaRect<areaofRect){
                        maxAreaRect = areaofRect;
                    }
                    numberOfRect++;
                }
            } while (c.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
