package com.example.jacek.gympartner.SQLite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Jacek on 05.02.2017.
 */

public class GymProvider extends ContentProvider {

    private static final String TAG_LOG = GymProvider.class.getSimpleName();
    private static final int EXERCISES = 100;
    private static final int EXERCISE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(GymContract.CONTENT_AUTHORITY,GymContract.PATH_GYM, EXERCISES);
        sUriMatcher.addURI(GymContract.CONTENT_AUTHORITY,GymContract.PATH_GYM + "/#", EXERCISE_ID);
    }
    private GymDbHelper gymDbHelper;
    /** Tag for the log messages */
    public static final String LOG_TAG = GymProvider.class.getSimpleName();
    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        gymDbHelper = new GymDbHelper(getContext());
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }
    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = gymDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch(match) {
            case EXERCISES:
                cursor = database.query(GymContract.GymEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case EXERCISE_ID:
                selection = GymContract.GymEntry._ID + "=?";
                selectionArgs = new String [] { String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(GymContract.GymEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("query - Something wrong: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXERCISES:
                insertExercise(uri, contentValues);
                break;
            default:
                throw new IllegalArgumentException("insert - Something wrong: " + uri);
        }
        return null;
    }
    private Uri insertExercise(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(GymContract.GymEntry.COLUMN_NAME);
        if(name == null) {
            throw new IllegalArgumentException("Gimme name of exercise");
        }
        String kind = contentValues.getAsString(GymContract.GymEntry.COLUMN_KIND);
        if(kind == null) {
            throw new IllegalArgumentException("Which muscle works in this exercise");
        }
        Integer score = contentValues.getAsInteger(GymContract.GymEntry.COLUMN_SCORE);
        if(score < 0 ) {
            throw new IllegalArgumentException("Score have to be more than zero");
        }
        Integer serie = contentValues.getAsInteger(GymContract.GymEntry.COLUMN_SERIES);
        if(serie < 1 || serie == null ) {
            throw new IllegalArgumentException("More than 1 and cant be null");
        }
        SQLiteDatabase database = gymDbHelper.getWritableDatabase();
        long id = database.insert(GymContract.GymEntry.TABLE_NAME,null,contentValues);
        if(id == -1) {
            Log.e(TAG_LOG,"Something goes wrong with inserting...");
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }
    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXERCISES:
                return updateExercise(uri, contentValues, selection, selectionArgs);
            case EXERCISE_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = GymContract.GymEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateExercise(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateExercise(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(GymContract.GymEntry.COLUMN_NAME)) {
            String name = values.getAsString(GymContract.GymEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Gimme name of exercise");
            }
        }
        if(values.containsKey(GymContract.GymEntry.COLUMN_KIND)) {
            String kind = values.getAsString(GymContract.GymEntry.COLUMN_KIND);
            if (kind == null) {
                throw new IllegalArgumentException("Gimme kind of exercise");
            }
        }
        if(values.containsKey(GymContract.GymEntry.COLUMN_SCORE)) {
            Integer score = values.getAsInteger(GymContract.GymEntry.COLUMN_SCORE);
            if (score == null || score < 0) {
                throw new IllegalArgumentException("Score is null or is less than 0");
            }
        }
        if(values.containsKey(GymContract.GymEntry.COLUMN_SERIES)) {
            Integer serie = values.getAsInteger(GymContract.GymEntry.COLUMN_SERIES);
            if (serie == null || serie < 1) {
                throw new IllegalArgumentException("Series is null or is less than 0");
            }
        }
        if(values.containsKey(GymContract.GymEntry.COLUMN_REP)) {
            String reps = values.getAsString(GymContract.GymEntry.COLUMN_REP);
            if (reps == null) {
                throw new IllegalArgumentException("Gimme how many repetitions of exercise");
            }
        }
        if(values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = gymDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(GymContract.GymEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }
    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = gymDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeletes;
        switch (match) {
            case EXERCISES:
                rowsDeletes = database.delete(GymContract.GymEntry.TABLE_NAME,selection,selectionArgs);
                if (rowsDeletes != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeletes;
            case EXERCISE_ID:
                selection = GymContract.GymEntry.COLUMN_ID + "=?";
                selectionArgs = new String [] { String.valueOf(ContentUris.parseId(uri))};
                rowsDeletes = database.delete(GymContract.GymEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeletes != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return  rowsDeletes;
            default:
                throw new IllegalArgumentException("BAD URI");
        }
    }
    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        SQLiteDatabase database = gymDbHelper.getReadableDatabase();
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = database.query(GymContract.GymEntry.TABLE_NAME, new String[] {GymContract.GymEntry.COLUMN_NAME},
                    null, null, null, null, null);
        }
        else {
            mCursor = database.query(true, GymContract.GymEntry.TABLE_NAME, new String[] {GymContract.GymEntry.COLUMN_NAME},
                    GymContract.GymEntry.COLUMN_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXERCISES:
                return GymContract.GymEntry.CONTENT_LIST_TYPE;
            case EXERCISE_ID:
                return GymContract.GymEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}