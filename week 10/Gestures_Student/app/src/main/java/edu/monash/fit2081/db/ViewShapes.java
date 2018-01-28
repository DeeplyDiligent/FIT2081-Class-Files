/*

 */
package edu.monash.fit2081.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import edu.monash.fit2081.db.provider.SchemeShapes;
import edu.monash.fit2081.db.provider.ShapeValues;

import static edu.monash.fit2081.db.provider.SchemeShapes.Shape;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewShapes extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String DEBUG_TAG = "Gestures";

    private int mLastTouchX;
    private int mLastTouchY;
    private ContentResolver resolver;

    public static CustomView customView = null;

    //gesture stuff
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleDetector;

    //interference flags
    private boolean isLongAndDrag = false;
    private boolean isOnScroll = false;
    private boolean isOnScale = false;
    private boolean topLeftTouchDone = false;

    //other
    String selectedShapeDrawing = "Circle"; //default

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);

        resolver = getActivity().getContentResolver(); //***
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        customView = new CustomView(getContext());
        mDetector = new GestureDetector(getContext(), new MyGestureListener());
        mScaleDetector = new ScaleGestureDetector(getContext(), new myScaleListener());

        //***
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                //Circle is the default shape if can't find the key
                selectedShapeDrawing = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).getString("selectedShapeDrawing", "Circle");
                int x = (int) ev.getX(); int y = (int) ev.getY();
                int dX, dY;

                //"Note that MotionEventCompat is not a replacement for the MotionEvent class. Rather,
                //it provides static utility methods to which you pass your MotionEvent object in order
                //to receive the desired action associated with that event."
                int action = MotionEventCompat.getActionMasked(ev);
                switch (action){
                    case MotionEvent.ACTION_MOVE:
                        //this event is used to move last drawn shape after long press
                        //OnScroll is not fired after a long press
                        if (isLongAndDrag) { //set in
                            updateLastShape(x, y, -1, -1, -1, false); //-1 value unchanged, false means no pinch scaling required
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //Log.d(DEBUG_TAG, "up ");
                        //onSingleTapUp cannot be used here to reset flags as it only occurs
                        //after an isolated single tap not after a long drag or scroll

                        //reset two flags, if anything was in progress (e.g. long drag, scroll) an up will kill it
                        isLongAndDrag = false;
                        isOnScroll = false;
                        break;
                }

                //pass all MotionEvent to Gesture Detectors for them to decide if some combination of MotionEvents is a Gesture or not
                mDetector.onTouchEvent(ev);
                mScaleDetector.onTouchEvent(ev);

                return true; //event handled no need to pass it on for further handling
            }
        });
        //***

        return (customView);
    }


    private void storeShape(String shape, int x, int y, int deltaX, int deltaY) {
        int selectedColor = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("selectColor", 0);

        ContentValues contentValues = new ContentValues();
        contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, shape);
        contentValues.put(SchemeShapes.Shape.SHAPE_X, x);
        contentValues.put(SchemeShapes.Shape.SHAPE_Y, y);
        contentValues.put(SchemeShapes.Shape.SHAPE_RADIUS, Math.max(deltaX, deltaY));
        contentValues.put(SchemeShapes.Shape.SHAPE_WIDTH, deltaX);
        contentValues.put(SchemeShapes.Shape.SHAPE_HEIGHT, deltaY);
        contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, 10);
        contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, selectedColor);

        resolver.insert(SchemeShapes.Shape.CONTENT_URI, contentValues);
    }
    //***

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

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
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        ShapeValues[] shapes = new ShapeValues[cursor.getCount()];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {

                shapes[i] = new ShapeValues(cursor.getString(
                    cursor.getColumnIndex(Shape.SHAPE_TYPE)),
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

    //GESTURE STUFF
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            int height = customView.getHeight();
            int width = customView.getWidth();
            boolean topleft = (y <= height/16)&&(x <= width/16) ;
            boolean bottomright = (x >= 15 * width/16) && (y >= 15 * height/16);

            if (topleft ){
                topLeftTouchDone = true;
            } else if (topLeftTouchDone && bottomright) {
                resolver.delete(SchemeShapes.Shape.CONTENT_URI, null, null);
            } else {
                topLeftTouchDone = false;
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(!isOnScale && e1.getPointerCount() == 1 && e2.getPointerCount() == 1) { // if there is no pinching ongoing right now
                //Log.d(DEBUG_TAG, "onScroll " + selectedShapeDrawing);
                int x1, y1, x2, y2;
                int dX, dY;

                x1 = (int) e1.getX(); y1 = (int) e1.getY();
                x2 = (int) e2.getX(); y2 = (int) e2.getY();

                dX = x2 - x1; dY = y2 - y1;


                if (!selectedShapeDrawing.equals("Line")) { //if the selected shape is not a freehand line
                    //following work for circles but not for other shapes, need fixing
                    dX = Math.abs(dX);
                    dY = Math.abs(dY);

                    if (!isOnScroll) { // if this is the first call, draw the selected shape
                        storeShape(selectedShapeDrawing, x1, y1, dX, dY);
                        isOnScroll = true;
                    } else { // if this is not first, and you are still onScroll (isOnScroll is still true), update the selected shape
                        updateLastShape(x1, y1, dX, dY, Math.max(dX, dY), false); //-1 no value change required, false no pinch scaling required
                    }
                }
                else { //if the selected shape is hand drawn line, draw the standard circle that represents a point
                    if (!isLongAndDrag) {
                        dX = 5;
                        dY = 5;
                        storeShape("Circle", x2, y2, dX, dY);
                    }
                }

            }
            return true;
        }

        // Not required any more, ACTION_UP replaces this callBack
//       @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            Log.d(DEBUG_TAG, "OnSingleTop");
//
//            return super.onSingleTapUp(e);
//        }

        //this callback, draws the standard shape for the selected shapes
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            storeShape(selectedShapeDrawing, (int) e.getX(), (int) e.getY(), 50, 50);
            //return super.onDoubleTap(e);
            return true;
        }

        // set a Long press flag to true to be used later by ACTION_MOVE
        @Override
        public void onLongPress(MotionEvent e) {
            //Log.d(DEBUG_TAG, "onLongPress");
            super.onLongPress(e);
            isLongAndDrag = true;
        }
    }

    private class myScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float scale = 1.0f;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            //Log.d(DEBUG_TAG, "onScaleBegin ");
            isOnScale = true; // start pinching

            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //Log.d(DEBUG_TAG, "onScale " + detector.getScaleFactor());
            scale = detector.getScaleFactor();
            int scaleFactor = (int) (scale * 100);
            updateLastShape(-1, -1, scaleFactor, scaleFactor, scaleFactor, true);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            //Log.d(DEBUG_TAG, "onScaleEnd " + scale);


            //convert form double to int
            //eg 1.5--> 150
            int scaleFactor = (int) (scale * 100);
            scale = 1.0f;         // reset the scale
            isOnScale = false; // done with pinching, lets OnScroll get some work

            /**
             * updateLastShape(x,y,width,height,radius)
             * -1 means no need to update and use the original value
             * last parameter is the scale flag which is used to perform the pinching event
             * scaleFactor will be applied on width and height or radius
             */
            updateLastShape(-1, -1, scaleFactor, scaleFactor, scaleFactor, true);
            super.onScaleEnd(detector);
        }
    }

    private void updateLastShape(int x, int y, int width, int height, int radius, boolean scaleFlag) {
        Cursor cursor = getLastShape();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, cursor.getString(cursor.getColumnIndex(Shape.SHAPE_TYPE)));

            if (x != -1)
                contentValues.put(Shape.SHAPE_X, x);
            else
                contentValues.put(Shape.SHAPE_X, cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_X)));

            if (y != -1)
                contentValues.put(Shape.SHAPE_Y, y);
            else
                contentValues.put(Shape.SHAPE_Y, cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_Y)));

            if (scaleFlag)
                contentValues.put(Shape.SHAPE_RADIUS, (radius / 100.0) * cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_RADIUS)));
            else if (radius != -1)
                contentValues.put(Shape.SHAPE_RADIUS, radius);
            else

                contentValues.put(Shape.SHAPE_RADIUS, cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_RADIUS)));


            if (width != -1)
                contentValues.put(Shape.SHAPE_WIDTH, width);
            else if (scaleFlag)
                contentValues.put(Shape.SHAPE_WIDTH, (width / 100.0) * cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_WIDTH)));
            else
                contentValues.put(Shape.SHAPE_WIDTH, cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_WIDTH)));

            if (height != -1)
                contentValues.put(Shape.SHAPE_HEIGHT, height);
            else if (scaleFlag)
                contentValues.put(Shape.SHAPE_HEIGHT, (height / 100.0) * cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_WIDTH)));
            else
                contentValues.put(Shape.SHAPE_HEIGHT, cursor.getInt(cursor.getColumnIndex(Shape.SHAPE_HEIGHT)));

            contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, cursor.getString(cursor.getColumnIndex(Shape.SHAPE_BORDER_THICKNESS)));
            contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, cursor.getString(cursor.getColumnIndex(Shape.SHAPE_COLOR)));
            resolver.update(SchemeShapes.Shape.CONTENT_URI, contentValues, "_id=" + cursor.getInt(cursor.getColumnIndex(Shape.ID)), null);
        }
    }

    public Cursor getLastShape() {
        Cursor cursor = resolver.query(Shape.CONTENT_URI, Shape.PROJECTION, null, null, Shape.ID + " DESC LIMIT 1");
        return cursor;
    }
}


