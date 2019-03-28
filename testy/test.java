package com.example.jacek.gympartner.testy;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jacek.gympartner.R;
import com.example.jacek.gympartner.SQLite.GymContract;
import com.example.jacek.gympartner.SQLite.GymCursorAdapter;


public class test extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PET_LOADER = 0;

    GymCursorAdapter mCursorAdapter;
    ListView gymListView;
    TextView textView;
    SimpleCursorAdapter dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trening_activity_main, container, false);

        gymListView = (ListView) rootView.findViewById(R.id.listView1);

        textView = (TextView) rootView.findViewById(R.id.poziom);

        textView.setText(R.string.zestaw);

        mCursorAdapter = new GymCursorAdapter(getContext(), null);

        gymListView.setAdapter(mCursorAdapter);
        //Generate ListView from SQLite Database

        gymListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), SingleListItem.class);

                Uri currentPetUri = ContentUris.withAppendedId(GymContract.GymEntry.CONTENT_URI, id);
                i.setData(currentPetUri);
                startActivity(i);
            }
        });
        getLoaderManager().initLoader(PET_LOADER, null, this);


    /*
        EditText myFilter = (EditText) rootView.findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchCountriesByName(constraint.toString());
            }
        });
        */



        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = GymContract.GymEntry._ID + " LIMIT 9";
        String [] projection = {
                GymContract.GymEntry._ID,
                GymContract.GymEntry.COLUMN_NAME,
                GymContract.GymEntry.COLUMN_KIND,
        };

        return new CursorLoader(
                getContext(),
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
