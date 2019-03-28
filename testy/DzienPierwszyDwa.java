package com.example.jacek.gympartner.testy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacek.gympartner.R;
import com.example.jacek.gympartner.SQLite.GymContract;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jacek on 06.01.2017.
 */

public class DzienPierwszyDwa extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PET_LOADER = 0;
    private CountDownTimer restTimeMls;
    private TextView countDown, repText;
    private LinearLayout l1,l2,l3,l4,l5;
    private EditText e1,e2,e3,e4,e5;
    private Uri currentUriPet;
    private MediaPlayer mPlayer;
    private Button b,b1,b2,b3;
    private long zielony = 0,zielony1 = 0,zielony2 = 0;
    private long czasOgolnyMin = 0;
    private int series;

    private TextView timerTextView;
    private long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            czasOgolnyMin = minutes;


            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dzien_treningu_dwa);
        Intent intent = getIntent();
        currentUriPet = intent.getData();
        getSupportLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        l1 = (LinearLayout) findViewById(R.id.l1DT2);
        l2 = (LinearLayout) findViewById(R.id.l2DT2);
        l3 = (LinearLayout) findViewById(R.id.l3DT2);
        l4 = (LinearLayout) findViewById(R.id.l4DT2);
        l5 = (LinearLayout) findViewById(R.id.l5DT2);

        e1 = (EditText) findViewById(R.id.e1DT2);
        e2 = (EditText) findViewById(R.id.e2DT2);
        e3 = (EditText) findViewById(R.id.e3DT2);
        e4 = (EditText) findViewById(R.id.e4DT2);
        e5 = (EditText) findViewById(R.id.e5DT2);

        repText = (TextView) findViewById(R.id.repetitionsDT2);

        b = (Button) findViewById(R.id.mierzenieCzasuDT2);


        countDown = (TextView) findViewById(R.id.countDownDT2);

        mPlayer = MediaPlayer.create(this, R.raw.androidpik); // in 2nd param u have to pass your desire ringtone

        timerTextView = (TextView) findViewById(R.id.TimerDT2);

        b = (Button) findViewById(R.id.mierzenieCzasuDT2);
        b1 = (Button) findViewById(R.id.startTimeprzerwa2DT2);
        b.setText("start");

    }

    public void open(View view) {
        Intent intent = new Intent(getApplicationContext(), TestActivityDT2.class);
        intent.setData(currentUriPet);
        startActivity(intent);
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                GymContract.GymEntry._ID,
                GymContract.GymEntry.COLUMN_NAME,
                GymContract.GymEntry.COLUMN_SCORE,
                GymContract.GymEntry.COLUMN_SERIES,
                GymContract.GymEntry.COLUMN_REP
        };
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentUriPet,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            //int nameColumnIndex = cursor.getColumnIndex(GymContract.GymEntry.COLUMN_NAME);
            int scoreColumnIndex = cursor.getColumnIndex(GymContract.GymEntry.COLUMN_SCORE);
            int seriesColumnIndex = cursor.getColumnIndex(GymContract.GymEntry.COLUMN_SERIES);
            int repColumnIndex = cursor.getColumnIndex(GymContract.GymEntry.COLUMN_REP);
            // Extract out the value from the Cursor for the given column index
            //String name = cursor.getString(nameColumnIndex);
            int score = cursor.getInt(scoreColumnIndex);
            series = cursor.getInt(seriesColumnIndex);
            String rep = cursor.getString(repColumnIndex);

            if(series == 3) {
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.VISIBLE);
                l3.setVisibility(View.VISIBLE);
                //if (rep == "8-6 powtorzen"){
                    e1.setHint(String.valueOf(Math.round(score * 0.95)));
                    e2.setHint(String.valueOf(Math.round(score * 1.00)));
                    e3.setHint(String.valueOf(Math.round(score * 1.05)));
                /*}
                if (rep == "10-6 powtorzen"){
                    e1.setHint(String.valueOf(Math.round(score * 0.80)));
                    e2.setHint(String.valueOf(Math.round(score * 0.)));
                    e3.setHint(String.valueOf(Math.round(score * 0.67)));
                }
                if (rep == "10-8 powtorzen"){
                    e1.setHint(String.valueOf(Math.round(score * 0.80)));
                    e2.setHint(String.valueOf(Math.round(score * 0.)));
                    e3.setHint(String.valueOf(Math.round(score * 0.67)));
                }
                if (rep == "15 powtorzen"){
                    e1.setHint(String.valueOf(Math.round(score * 0.80)));
                    e2.setHint(String.valueOf(Math.round(score * 0.)));
                    e3.setHint(String.valueOf(Math.round(score * 0.67)));
                }
                */

            } else if(series == 4) {
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.VISIBLE);
                l3.setVisibility(View.VISIBLE);
                l4.setVisibility(View.VISIBLE);
                e1.setHint(String.valueOf(Math.round(score * 0.9)));
                e2.setHint(String.valueOf(Math.round(score * 0.95)));
                e3.setHint(String.valueOf(Math.round(score * 1.00)));
                e4.setHint(String.valueOf(Math.round(score * 1.05)));
            } else if(series == 5) {
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.VISIBLE);
                l3.setVisibility(View.VISIBLE);
                l4.setVisibility(View.VISIBLE);
                l5.setVisibility(View.VISIBLE);
                e1.setHint(String.valueOf(Math.round(score * 0.85)));
                e2.setHint(String.valueOf(Math.round(score * 0.90)));
                e3.setHint(String.valueOf(Math.round(score * 0.95)));
                e4.setHint(String.valueOf(Math.round(score * 1.00)));
                e5.setHint(String.valueOf(Math.round(score * 1.05)));
            }
            // Update the views on the screen with the values from the database


            repText.setText(rep);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        e1.setHint(Integer.toString(0));
        e2.setHint(Integer.toString(0));
        e3.setHint(Integer.toString(0));
        e4.setHint(Integer.toString(0));
        e5.setHint(Integer.toString(0));
   ;
    }

    public void countDown(View view) {
        b1.setEnabled(false);
        b1.postDelayed(new Runnable() {
            @Override
            public void run() {
                b1.setEnabled(true);
            }
        }, 5000);

        zielony++;
        if(series == 3) {
            if (zielony % 3 == 1) {
                e1.setTextColor(Color.GREEN);
            } else if (zielony % 3 == 2) {
                e2.setTextColor(Color.GREEN);
            } else {
                e3.setTextColor(Color.GREEN);
            }
        } else if(series == 4) {
            if (zielony % 4 == 1) {
                e1.setTextColor(Color.GREEN);
            } else if (zielony % 4 == 2) {
                e2.setTextColor(Color.GREEN);
            } else if (zielony % 4 == 3){
                e3.setTextColor(Color.GREEN);
            } else {
                e4.setTextColor(Color.GREEN);
            }
        }
        else if(series == 5) {
            if (zielony % 5 == 1) {
                e1.setTextColor(Color.GREEN);
            } else if (zielony % 5 == 2) {
                e2.setTextColor(Color.GREEN);
            } else if (zielony % 5 == 3){
                e3.setTextColor(Color.GREEN);
            } else if(zielony % 5 == 4){
                e4.setTextColor(Color.GREEN);
            } else {
                e5.setTextColor(Color.GREEN);
            }

        }
        restTimeMls = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long timeLeft = millisUntilFinished / 1000;
                countDown.setText("" + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                if (timeLeft <= 11 && timeLeft > 2) {
                    mPlayer.start();
                } else if (timeLeft == 0) {
                    mPlayer.stop();
                }

            }

            @Override
            public void onFinish() {
                countDown.setText("0:00");
                restTimeMls = null;
            }
        }.start();


    }




    public void TimerTrening(View view) {
        b.setEnabled(false);
        b.postDelayed(new Runnable() {
            @Override
            public void run() {
                b.setEnabled(true);
            }
        }, 5000);

        if (b.getText().equals("stop")) {
            timerHandler.removeCallbacks(timerRunnable);
            String czasWmin = Long.toString(czasOgolnyMin);
            Toast.makeText(this, "Trening trwał: " + czasWmin + " minuty", Toast.LENGTH_SHORT).show();
            finish();


        } else {
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);


            b.setText("stop");

        }


    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button) findViewById(R.id.mierzenieCzasuDT2);
        b.setText("start");
    }

    public void ZapiszWyniki(View view) {
        int wynik = 0;
        if(series == 3) {
             wynik = Integer.parseInt(e3.getText().toString());
        } else if(series == 4) {
             wynik = Integer.parseInt(e3.getText().toString());
        } else if(series == 5) {
             wynik = Integer.parseInt(e3.getText().toString());
        }

        ContentValues values = new ContentValues();

        values.put(GymContract.GymEntry.COLUMN_SCORE,wynik);

        int rowsAffected = getContentResolver().update(currentUriPet,values,null,null);

        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(getBaseContext(),"NIE UDALO SIE",Toast.LENGTH_LONG).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(getBaseContext(),"UDALO SIE",Toast.LENGTH_LONG).show();
        }
    }
}
