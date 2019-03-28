package com.example.jacek.gympartner.testy;

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

public class DzienPierwszy extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PET_LOADER = 0;
    private CountDownTimer restTimeMls;
    private TextView pp1, pp2, pp3, s1, s2, s3, p1, p2, p3, pn, sr, pt, nd, countDown, countDown1, countDown2;
    private Uri currentUriPet;
    private MediaPlayer mPlayer;
    private Button b,b1,b2,b3;
    private long zielony = 0,zielony1 = 0,zielony2 = 0;
    private long czasOgolnyMin = 0;

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
        this.setContentView(R.layout.dzien_treningu);
        Intent intent = getIntent();
        currentUriPet = intent.getData();
        getSupportLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        p1 = (TextView) findViewById(R.id.p1);
        p2 = (TextView) findViewById(R.id.p2);
        p3 = (TextView) findViewById(R.id.p3);
        s1 = (TextView) findViewById(R.id.s1);
        s2 = (TextView) findViewById(R.id.s2);
        s3 = (TextView) findViewById(R.id.s3);
        pp1 = (TextView) findViewById(R.id.pp1);
        pp2 = (TextView) findViewById(R.id.pp2);
        pp3 = (TextView) findViewById(R.id.pp3);
        pn = (TextView) findViewById(R.id.pn);
        sr = (TextView) findViewById(R.id.sr);
        pt = (TextView) findViewById(R.id.pt);
        nd = (TextView) findViewById(R.id.nd);
        countDown = (TextView) findViewById(R.id.countDown);

        mPlayer = MediaPlayer.create(this, R.raw.androidpik); // in 2nd param u have to pass your desire ringtone

        timerTextView = (TextView) findViewById(R.id.Timer);

        b = (Button) findViewById(R.id.mierzenieCzasu);
        b1 = (Button) findViewById(R.id.startTimeprzerwa);
        b2 = (Button) findViewById(R.id.startTimeprzerwa1);
        b3 = (Button) findViewById(R.id.startTimeprzerwa2);
        b.setText("start");

    }

    public void open(View view) {
        Intent intent = new Intent(getApplicationContext(), TestActivity.class);
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
            int nameColumnIndex = cursor.getColumnIndex(GymContract.GymEntry.COLUMN_NAME);
            int scoreColumnIndex = cursor.getColumnIndex(GymContract.GymEntry.COLUMN_SCORE);
            int seriesColumnIndex = cursor.getColumnIndex(GymContract.GymEntry.COLUMN_SERIES);
            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int score = cursor.getInt(scoreColumnIndex);
            int weight = cursor.getInt(seriesColumnIndex);
            // Update the views on the screen with the values from the database
            p1.setText(String.valueOf(Math.round(score * 0.55)));
            p2.setText(String.valueOf(Math.round(score * 0.79)));
            p3.setText(String.valueOf(Math.round(score * 0.67)));
            s1.setText(String.valueOf(Math.round(score * 0.67)));
            s2.setText(String.valueOf(Math.round(score * 0.91)));
            s3.setText(String.valueOf(Math.round(score * 0.79)));
            pp1.setText(String.valueOf(Math.round(score * 0.79)));
            pp2.setText(String.valueOf(Math.round(score * 1.03)));
            pp3.setText(String.valueOf(Math.round(score * 0.91)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        p1.setText(Integer.toString(0));
        p2.setText(Integer.toString(0));
        p3.setText(Integer.toString(0));
        s1.setText(Integer.toString(0));
        s2.setText(Integer.toString(0));
        s3.setText(Integer.toString(0));
        pp1.setText(Integer.toString(0));
        pp2.setText(Integer.toString(0));
        pp3.setText(Integer.toString(0));
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
        if (zielony % 3 == 1) {
            p1.setTextColor(Color.GREEN);
        } else if (zielony % 3 == 2) {
            p2.setTextColor(Color.GREEN);
        } else {
            p3.setTextColor(Color.GREEN);
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

    public void countDown1(View view) {
        b2.setEnabled(false);
        b2.postDelayed(new Runnable() {
            @Override
            public void run() {
                b2.setEnabled(true);
            }
        }, 5000);


        zielony1++;
        if (zielony1 % 3 == 1) {
            s1.setTextColor(Color.GREEN);
        } else if (zielony1 % 3 == 2) {
            s2.setTextColor(Color.GREEN);
        } else {
            s3.setTextColor(Color.GREEN);;
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


    public void countDown2(View view) {
        b3.setEnabled(false);
        b3.postDelayed(new Runnable() {
            @Override
            public void run() {
                b3.setEnabled(true);
            }
        }, 5000);

        zielony2++;
        if (zielony2 % 3 == 1) {
            pp1.setTextColor(Color.GREEN);
        } else if (zielony2 % 3 == 2) {
            pp2.setTextColor(Color.GREEN);
        } else {
            pp3.setTextColor(Color.GREEN);
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
        Button b = (Button) findViewById(R.id.mierzenieCzasu);
        b.setText("start");
    }
}
