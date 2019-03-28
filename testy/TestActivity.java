package com.example.jacek.gympartner.testy;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacek.gympartner.R;
import com.example.jacek.gympartner.SQLite.GymContract;

/**
 * Created by Jacek on 06.01.2017.
 */



public class TestActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button button,button1;
    //OpenHelper1 mDb;


    private Uri currentUriPet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.test_layout);

        Intent intent = getIntent();
        currentUriPet = intent.getData();

        textView = (TextView) findViewById(R.id.polecenie);
        editText = (EditText) findViewById(R.id.wartosc);
        button = (Button) findViewById(R.id.treningactivity);
        button1 = (Button) findViewById(R.id.zapisz);
        //button2 = (Button) findViewById(R.id.button);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wynik = Integer.parseInt(editText.getText().toString());

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
              ;


            }
        });

        /*
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.open();
                Cursor t = mDb.fetchOneCwiczenie(product);
                int c = t.getInt(t.getColumnIndex("score"));
                String k = Integer.toString(c);
                textView1.setText(k);
                mDb.close();

            }
        });
        */

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext().getApplicationContext(), DzienPierwszy.class);
                i.setData(currentUriPet);
                startActivity(i);
                finish();
            }
        });



    }
}
