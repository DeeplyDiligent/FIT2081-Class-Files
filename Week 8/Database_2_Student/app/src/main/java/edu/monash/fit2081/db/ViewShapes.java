/*

 */
package edu.monash.fit2081.db;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import edu.monash.fit2081.db.provider.ShapeValues;

import static edu.monash.fit2081.db.provider.SchemeShapes.Shape;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewShapes extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static CustomView customView = null;
//    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        customView = new CustomView(getContext());

        getLoaderManager().initLoader(0, null, this); //create loader if doesn't exist, starts the loader

        return (customView);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) { //basically this is a call to the ContentProvider's query method


        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                                                     Shape.CONTENT_URI,
                                                     //VersionContract.Version.buildUri(2),
                                                     Shape.PROJECTION,
                                                     null,
                                                     null,
                                                     null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) { //and here's the cursor returned by the ContentProvider's query method
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //do your stuff for your fragment here
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    //    simpleCursorAdapter.swapCursor(null);
    }


}


