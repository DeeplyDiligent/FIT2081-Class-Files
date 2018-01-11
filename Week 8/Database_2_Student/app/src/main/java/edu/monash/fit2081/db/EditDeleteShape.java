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
import android.widget.ListView;

import edu.monash.fit2081.db.provider.SchemeShapes;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDeleteShape extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    LayoutInflater myInflater = null;
    ListView shapesListView;        // renamed from lvList
    ItemCursorAdapter shapesListViewAdapter;  // renamed from listViewAdapter

    public EditDeleteShape() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myInflater = inflater;
        View v = inflater.inflate(R.layout.fragment_edit_delete_shape, container, false);

        shapesListView = (ListView) v.findViewById(R.id.list_edit_delete_shapes);
        shapesListView.addHeaderView(myInflater.inflate(R.layout.edit_delete_shape_list_header, null));

        shapesListViewAdapter = new ItemCursorAdapter(getContext(), null, getActivity().getSupportFragmentManager()); // null is an empty cursor
        shapesListView.setAdapter(shapesListViewAdapter);

        return v;
    }

    @Override //basically this is a call to the ContentProvider's query method
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                SchemeShapes.Shape.CONTENT_URI,
                //VersionContract.Version.buildUri(2),
                SchemeShapes.Shape.PROJECTION,
                null,
                null,
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //and here's the cursor returned by the ContentProvider's query method
        shapesListViewAdapter.swapCursor(cursor); // swap to refresh the current cursor.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        super.onResume();
    }
}


//package edu.monash.fit2081.db;
//
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//
//import edu.monash.fit2081.db.provider.SchemeShapes;
//import edu.monash.fit2081.db.provider.ShapeValues;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class EditDeleteShape extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
//
//
//    //  ShapesDbHelper dbHelper;
//    LayoutInflater myInflater = null;
//
//
//    public EditDeleteShape() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //   dbHelper = new ShapesDbHelper(getContext());
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        myInflater = inflater;
//        View v = inflater.inflate(R.layout.fragment_edit_delete_shape, container, false);
//
//        ListView listView = (ListView) v.findViewById(R.id.list_edit_delete_shapes);
//        listView.addHeaderView(myInflater.inflate(R.layout.edit_delete_shape_list_header, null));
//
//
//        getLoaderManager().initLoader(0, null, this); //create loader if doesn't exist, starts the loader
//        return v;
//    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//
//        CursorLoader cursorLoader = new CursorLoader(getActivity(),
//                                         SchemeShapes.Shape.CONTENT_URI,
//                                         //VersionContract.Version.buildUri(2),
//                                         SchemeShapes.Shape.PROJECTION,
//                                         null,
//                                         null,
//                                         null
//        );
//
//        return cursorLoader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) { ////the cursor is the one returned by the ContentProvider's query method
//        ShapeValues[] shapes = new ShapeValues[cursor.getCount()];
//        int i = 0;
//        if (cursor.moveToFirst()) {
//            do {
//
//                shapes[i] = new ShapeValues(
//                        cursor.getString(cursor.getColumnIndex(SchemeShapes.Shape.SHAPE_TYPE)),
//                        cursor.getInt(cursor.getColumnIndex(SchemeShapes.Shape.SHAPE_X)),
//                        cursor.getInt(cursor.getColumnIndex(SchemeShapes.Shape.SHAPE_Y)),
//                        cursor.getInt(cursor.getColumnIndex(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS)),
//                        cursor.getInt(cursor.getColumnIndex(SchemeShapes.Shape.SHAPE_RADIUS)),
//                        cursor.getInt(cursor.getColumnIndex(SchemeShapes.Shape.SHAPE_WIDTH)),
//                        cursor.getInt(cursor.getColumnIndex(SchemeShapes.Shape.SHAPE_HEIGHT)),
//                        cursor.getString(cursor.getColumnIndex(SchemeShapes.Shape.SHAPE_COLOR))
//                );
//                i++;
//                // do what ever you want here
//            } while (cursor.moveToNext());
//
//
//        }
//
//        ListView lvItems = (ListView) getActivity().findViewById(R.id.list_edit_delete_shapes);
//        ItemCursorAdapter itemAdaptor = new ItemCursorAdapter(getContext(), cursor, getActivity().getSupportFragmentManager());
//        lvItems.setAdapter(itemAdaptor);
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        super.onResume();
//    }
//
//}
