/*
This class does all the detailed database work for the other classes in the app
including
    creating the database if it does not exist
    opening and closing the database when it exists
    updating the database schema if there is a new schema version
    adding, updating and deleting records
    returning a cursor that can access all the existing shape records in the database's only table (shapes)
*/

package edu.monash.fit2081.db.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.monash.fit2081.db.R;

public class ShapesDbHelper extends SQLiteOpenHelper {
    // Database name and version
    private final static String DB_NAME = "ShapesDB.db";
    private final static int DB_VERSION = 1;

    private final static String SHAPES_TABLE_NAME = SchemeShapes.Shape.TABLE_NAME;

    // SQL statement to create the database's only table
    private final static String SHAPES_TABLE_CREATE =
            "CREATE TABLE " +
                    SchemeShapes.Shape.TABLE_NAME + " (" +
                    SchemeShapes.Shape.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    SchemeShapes.Shape.SHAPE_NAME + " TEXT, " +
                    SchemeShapes.Shape.SHAPE_TYPE + " TEXT," +
                    SchemeShapes.Shape.SHAPE_X + " INTEGER," +
                    SchemeShapes.Shape.SHAPE_Y + " INTEGER," +
                    SchemeShapes.Shape.SHAPE_WIDTH + " INTEGER," +
                    SchemeShapes.Shape.SHAPE_HEIGHT + " INTEGER," +
                    SchemeShapes.Shape.SHAPE_RADIUS + " INTEGER," +
                    SchemeShapes.Shape.SHAPE_BORDER_THICKNESS + " INTEGER," +
                    SchemeShapes.Shape.SHAPE_COLOR + " TEXT);";


    public ShapesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION); //null = default cursor
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SHAPES_TABLE_CREATE);
    }

    //couldn't afford to be this drastic in the real world
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SHAPES_TABLE_NAME);
        onCreate(db);
    }

    //could return a boolean because insert returns the row ID of the newly inserted row, or
    //-1 if an error occurred, we don't bother though
    public void addShape(ShapeValues shape) {
        //insert method requires record data to be packed into a ContentValues data structure
        ContentValues contentValues = new ContentValues();

        contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, shape.getShapeType());
        contentValues.put(SchemeShapes.Shape.SHAPE_X, shape.getX());
        contentValues.put(SchemeShapes.Shape.SHAPE_Y, shape.getY());
        contentValues.put(SchemeShapes.Shape.SHAPE_RADIUS, shape.getRadius());
        contentValues.put(SchemeShapes.Shape.SHAPE_WIDTH, shape.getWidth());
        contentValues.put(SchemeShapes.Shape.SHAPE_HEIGHT, shape.getHeight());
        contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, shape.getBorder());
        contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, shape.getColor());

        //even though we have exclusive access to this database it's a good to get into a habit
        //of opening and closing a database as quickly as possible so as not to lock out other users
        SQLiteDatabase db = this.getWritableDatabase();


        long newID = db.insert(SchemeShapes.Shape.TABLE_NAME, null, contentValues);
        shape.setId((int) newID);
        db.close();
    }

    public boolean deleteShape(int shapeID) {

        boolean result = false; //did the delete succeed or not

        //your code here
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor shapes = db.rawQuery("select * from " + SchemeShapes.Shape.TABLE_NAME + "where" + SchemeShapes.Shape.ID + "=" + shapeID, null);

        int rowsEffected = db.delete(SchemeShapes.Shape.TABLE_NAME, SchemeShapes.Shape.ID + "=" + shapeID, null);
        if(rowsEffected > 0){
            result = true;
        }

        return result;
    }

    public Cursor getAllShapes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + SchemeShapes.Shape.TABLE_NAME;
        Cursor shapes = db.rawQuery(query, null);

        return shapes;
    }

    //update method requires record data to be packed into a ContentValues data structure
    public boolean updateShape(ShapeValues shape) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, shape.getShapeType());
        contentValues.put(SchemeShapes.Shape.SHAPE_X, shape.getX());
        contentValues.put(SchemeShapes.Shape.SHAPE_Y, shape.getY());
        contentValues.put(SchemeShapes.Shape.SHAPE_RADIUS, shape.getRadius());
        contentValues.put(SchemeShapes.Shape.SHAPE_WIDTH, shape.getWidth());
        contentValues.put(SchemeShapes.Shape.SHAPE_HEIGHT, shape.getHeight());
        contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, shape.getBorder());
        contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, shape.getColor());


        boolean result = false; //did the edit succeed or not=

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(SchemeShapes.Shape.TABLE_NAME, contentValues, SchemeShapes.Shape.ID + "=" + shape.getId(),null);
        result = true;

        return result;
    }

    private ContentValues getContentValues(ShapeValues shape){
        ContentValues contentValues = new ContentValues();

        contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, shape.getShapeType());
        contentValues.put(SchemeShapes.Shape.SHAPE_X, shape.getX());
        contentValues.put(SchemeShapes.Shape.SHAPE_Y, shape.getY());
        contentValues.put(SchemeShapes.Shape.SHAPE_RADIUS, shape.getRadius());
        contentValues.put(SchemeShapes.Shape.SHAPE_WIDTH, shape.getWidth());
        contentValues.put(SchemeShapes.Shape.SHAPE_HEIGHT, shape.getHeight());
        contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, shape.getBorder());
        contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, shape.getColor());
        return contentValues;
    }
}


