package com.example.jacek.gympartner.SQLite;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.jacek.gympartner.R;

/**
 * Created by Jacek on 05.02.2017.
 */

public class GymCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link GymCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public GymCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_view_item,parent,false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView breed = (TextView) view.findViewById(R.id.kind);
        TextView seria = (TextView) view.findViewById(R.id.seria);
        TextView max = (TextView) view.findViewById(R.id.max);



        String sName = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.GymEntry.COLUMN_NAME));
        String sKind = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.GymEntry.COLUMN_KIND));
        //int sSeria = cursor.getColumnIndexOrThrow(GymContract.GymEntry.COLUMN_SERIES);
        //int sMax = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.GymEntry.COLUMN_SCORE));
        //String ssSeria = Integer.toString(sSeria);
        //String ssMax = Integer.toString(sMax);

        name.setText(sName);
        breed.setText(sKind);
        //seria.setText(ssSeria);
        //max.setText(ssMax);
    }
}