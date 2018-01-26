/*
This class simply gives us constant global (note the public visibility) names for
the db table and the db columns that can be consistently used all over the project
There should be a nested class here for each table in the database (just one in the current case)
This class has the added benefit of documenting the db schema
 */
package edu.monash.fit2081.db.provider;

import android.net.Uri;

public class SchemeShapes {
    // Name of the Content Provider, use package name by convention so that it's unique on device
    public static final String CONTENT_AUTHORITY = "edu.monash.fit2081.db.provider";

    // A path that points to the version table
    public static final String PATH_VERSION = "shapes";

    // Construct the Base Content Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class Shape {
        // Content Uri = Content Authority + Path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VERSION).build();

        // Use MIME type prefix android.cursor.dir/ for returning multiple items
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/edu.monash.fit2081.db.provider";
        // Use MIME type prefix android.cursor.item/ for returning a single item
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/edu.monash.fit2081.db.provider";


        //Table name
        public static final String TABLE_NAME = "shapes";

        //Table Column names
        public static final String ID = "_id"; //CursorAdapters will not work if this column with this name is not present
        public static final String SHAPE_NAME = "shape_name";
        public static final String SHAPE_TYPE = "shape_type";
        public static final String SHAPE_X = "shape_x";
        public static final String SHAPE_Y = "shape_y";
        public static final String SHAPE_WIDTH = "shape_width";
        public static final String SHAPE_HEIGHT = "shape_height";
        public static final String SHAPE_RADIUS = "shape_radius";
        public static final String SHAPE_BORDER_THICKNESS = "shape_border_thickness";
        public static final String SHAPE_COLOR = "shape_color";

        // To prevent someone from accidentally instantiating the contract class, make the constructor private.
        private Shape(){}

        public static final String[] PROJECTION = new String[]{
                Shape.ID,
                Shape.SHAPE_NAME,
                Shape.SHAPE_TYPE,
                Shape.SHAPE_X,
                Shape.SHAPE_Y,
                Shape.SHAPE_WIDTH,
                Shape.SHAPE_HEIGHT,
                Shape.SHAPE_RADIUS,
                Shape.SHAPE_RADIUS,
                Shape.SHAPE_BORDER_THICKNESS,
                Shape.SHAPE_COLOR
        };
    }
}
