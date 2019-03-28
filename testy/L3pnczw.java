package com.example.jacek.gympartner.testy;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jacek.gympartner.R;
import com.example.jacek.gympartner.SQLite.GymContract;
import com.example.jacek.gympartner.SQLite.GymCursorAdapter;

/**
 * Created by Jacek on 26.02.2017.
 */

public class L3pnczw extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER = 0;

    GymCursorAdapter mCursorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trening_activity_main);


        ListView petListView = (ListView) findViewById(R.id.listView1);


        mCursorAdapter = new GymCursorAdapter(this,null);

        petListView.setAdapter(mCursorAdapter);



        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(L3pnczw.this, SingleListItem.class);

                Uri currentPetUri = ContentUris.withAppendedId(GymContract.GymEntry.CONTENT_URI, id);
                i.setData(currentPetUri);
                startActivity(i);
            }
        });
        getSupportLoaderManager().initLoader(PET_LOADER, null, this);

    }





    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = GymContract.GymEntry._ID + " LIMIT 19, 9";
        String [] projection = {
                GymContract.GymEntry._ID,
                GymContract.GymEntry.COLUMN_NAME,
                GymContract.GymEntry.COLUMN_KIND,
        };

        return new CursorLoader(
                this,
                GymContract.GymEntry.CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
