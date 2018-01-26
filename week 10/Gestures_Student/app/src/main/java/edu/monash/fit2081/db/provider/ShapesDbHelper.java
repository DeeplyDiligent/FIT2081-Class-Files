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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

}


