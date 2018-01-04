/*

 */
package edu.monash.fit2081.db;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.monash.fit2081.db.provider.ShapeValues;
import edu.monash.fit2081.db.provider.ShapesDbHelper;

import static edu.monash.fit2081.db.provider.SchemeShapes.Shape;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewShapes extends Fragment {

    public static CustomView customView = null;
    ShapesDbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dbHelper = new ShapesDbHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        customView = new CustomView(getContext());
        //dbHelper = new ShapesDbHelper(getContext());


//        customView.post(new Runnable() {
//            @Override
//            public void run() {
//                customView.invalidate(); //tells Android to redraw ASAP
//            }
//        });

//        customView.invalidate(); probably not required

        return (customView);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //do your stuff for your fragment here
    }


    public void reDraw() {
        Cursor cursor = dbHelper.getAllShapes();
        ShapeValues[] shapes = new ShapeValues[cursor.getCount()];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {

                shapes[i] = new ShapeValues(
                        cursor.getString(cursor.getColumnIndex(Shape.SHAPE_TYPE)),
                        cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_X)),
                        cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_Y)),
                        cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_BORDER_THICKNESS)),
                        cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_RADIUS)),
                        cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_WIDTH)),
                        cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_HEIGHT)),
                        cursor.getString(cursor.getColumnIndex(Shape.SHAPE_COLOR))
                );
                i++;
                // do what ever you want here
            } while (cursor.moveToNext());
        }
        // cursor.close();
        customView.numberShapes = cursor.getCount();
        customView.shapes = shapes;
        customView.invalidate();
    }

    @Override
    public void onResume() {
        reDraw();
        super.onResume();
    }
}


