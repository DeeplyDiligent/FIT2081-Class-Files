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
    private static final UriMatcher sUriMatcher = createUriMatcher();

    private static UriMatcher createUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SchemeShapes.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, SchemeShapes.PATH_VERSION, SHAPES);
        uriMatcher.addURI(authority, SchemeShapes.PATH_VERSION + "/#", SHAPES_ID);

        return uriMatcher;
    }

    private ShapesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ShapesDbHelper(getContext());
        // Content Provider created
        return true;
    }


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
            case SHAPES:
                break;
            case SHAPES_ID:
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
            case SHAPES:
                rowId = db.insertOrThrow(SchemeShapes.Shape.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null); //this will trigger an eventual redraw
                return ContentUris.withAppendedId(SchemeShapes.Shape.CONTENT_URI, rowId);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        int deletionCount = 0;

        switch (uriType) {
            case SHAPES:
                deletionCount = db.delete(SchemeShapes.Shape.TABLE_NAME, selection, selectionArgs);
                break;
            case SHAPES_ID:
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
            case SHAPES:
                updateCount = db.update(SchemeShapes.Shape.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SHAPES_ID:
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
