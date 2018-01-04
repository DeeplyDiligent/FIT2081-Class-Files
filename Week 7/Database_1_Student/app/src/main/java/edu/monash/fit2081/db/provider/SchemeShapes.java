/*
This class simply gives us constant global (note the public visibility) names for
the db table and the db columns that can be consistently used all over the project
There should be a nested class here for each table in the database (just one in the current case)
This class has the added benefit of documenting the db schema
 */
package edu.monash.fit2081.db.provider;

public class SchemeShapes {

    public static final class Shape {
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
    }
}
