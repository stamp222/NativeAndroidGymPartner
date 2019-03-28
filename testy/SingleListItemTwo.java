package com.example.jacek.gympartner.testy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacek.gympartner.R;
import com.example.jacek.gympartner.SQLite.GymContract;

public class SingleListItemTwo extends Activity{
    private static final int EXISTING_PET_LOADER = 0;

    private Uri currentUriPet;

    //private OpenHelper1 dbHelper;
    Button button,testButton,treningButton;

    String url,name;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_two);

        Intent intent = getIntent();
        currentUriPet = intent.getData();

        TextView txtProduct = (TextView) findViewById(R.id.product_label);
        button = (Button) findViewById(R.id.instrukcja) ;
        testButton = (Button) findViewById(R.id.test) ;
        treningButton = (Button) findViewById(R.id.trening) ;


        String [] projection ={GymContract.GymEntry.COLUMN_NAME,GymContract.GymEntry.COLUMN_URL};

        Cursor c = getContentResolver().query(currentUriPet, projection,null,null,null);

        try {

            int iurl = c.getColumnIndex(GymContract.GymEntry.COLUMN_URL);
            int iname = c.getColumnIndex(GymContract.GymEntry.COLUMN_NAME);

            while (c.moveToNext()) {
                url = c.getString(iurl);
                name = c.getString(iname);
            }
        }
        finally {
            c.close();
        }


        txtProduct.setText(name);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext().getApplicationContext(), TestActivityDT2.class);
                i.setData(currentUriPet);
                startActivity(i);
            }
        });
        treningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext().getApplicationContext(), DzienPierwszyDwa.class);
                i.setData(currentUriPet);
                startActivity(i);
            }
        });

    }

}


