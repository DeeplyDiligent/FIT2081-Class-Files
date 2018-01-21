package edu.monash.fit2081.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ShapesProvider extends ContentProvider {

    private static final int SHAPES = 100;
    private static final int SHAPES_ID = 200;
    private ShapesDbHelper mDbHelper;
    private static final UriMatcher sUriMatcher = createUriMatcher();

    //
    private static UriMatcher createUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SchemeShapes.CONTENT_AUTHORITY;

        //sUriMatcher will return code 100 if uri like authority/shapes
        uriMatcher.addURI(authority, SchemeShapes.Shape.PATH_VERSION, SHAPES);

        //sUriMatcher will return code 200 if uri like e.g. authority/shapes/7 (where 7 is id of row in shapes table)
        uriMatcher.addURI(authority, SchemeShapes.Shape.PATH_VERSION + "/#", SHAPES_ID);

        return uriMatcher;
    }


    //The following six methods are required to be implemented by any ContentProvider sub class
    //All but onCreate can be called by a client application that is attempting to access your content provider

    /*
    special note on the query method.
    Doco: Retrieve data from your provider. Use the arguments to select the table to query,
    the rows and columns to return, and the sort order of the result. Return the data as a Cursor object.
    In this app ViewShapes and EditDeleteShape both use a CursorLoader to execute this query on a background thread
    (the 2nd parameter of the CursorLoader constructor has been set in both cases to a content URI identifying the shapes table of the ShapesProvider)
    So the CursorLoader returns the cursor returned by the query method below
    It just a normal call to the query method below but performed on a background thread by the CursorLoader
     */


    @Override
    public boolean onCreate() {
        mDbHelper = new ShapesDbHelper(getContext());

        return true; //should return true if Content Provider successfully created
    }


    //*not used in this app
    @Override
    public String getType(Uri uri) {

        switch ((sUriMatcher.match(uri))) {
            case SHAPES:
                return SchemeShapes.Shape.CONTENT_TYPE;
            case SHAPES_ID:
                return SchemeShapes.Shape.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }


    //
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Use SQLiteQueryBuilder for querying db
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set the table name
        queryBuilder.setTables(SchemeShapes.Shape.TABLE_NAME);

        // Record id
        String id;

        // Match Uri pattern
        int uriType = sUriMatcher.match(uri);

        switch (uriType) {
            case SHAPES: //no trailing row id
                //selection = null;
                //selectionArgs = null;
                break;
            case SHAPES_ID: //trailing row id
                selection = SchemeShapes.Shape.ID + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        long rowId;

        switch (uriType) {
            case SHAPES: //no trailing row id
                rowId = db.insertOrThrow(SchemeShapes.Shape.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(SchemeShapes.Shape.CONTENT_URI, rowId);
            default: //a trailing row id is inappropriate for an insert as the db auto increments the primary key
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        int deletionCount = 0;

        switch (uriType) {
            case SHAPES: //no trailing row id so selection may indicate more than 1 row needs to be deleted if they can be found
                deletionCount = db.delete(SchemeShapes.Shape.TABLE_NAME, selection, selectionArgs);
                break;
            case SHAPES_ID: //trailing row id, so just one row to be deleted if it can be found
                String id = uri.getLastPathSegment();
                deletionCount = db.delete(
                        SchemeShapes.Shape.TABLE_NAME,
                        SchemeShapes.Shape.ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), // append selection to query if selection is not empty
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletionCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        int updateCount = 0;
        switch (uriType) {
            case SHAPES: //no trailing row id so selection may indicate more than 1 row needs to be updated if they can be found
                updateCount = db.update(SchemeShapes.Shape.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SHAPES_ID: //trailing row id, so just one row to be updated if it can be found
                String id = uri.getLastPathSegment();
                updateCount = db.update(SchemeShapes.Shape.TABLE_NAME,
                                        values,
                                        SchemeShapes.Shape.ID + " =" + id +
                                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), // append selection to query if selection is not empty
                                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
