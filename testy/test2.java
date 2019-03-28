package com.example.jacek.gympartner.testy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacek.gympartner.R;


public class test2 extends Fragment {



    TextView textView;
    Button b1,b2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dnitreningowe, container, false);



        textView = (TextView) rootView.findViewById(R.id.poziom);

        b1 = (Button) rootView.findViewById(R.id.b1_dni);
        b2 = (Button) rootView.findViewById(R.id.b2_dni);

        //textView.setText(R.string.zestaw);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), L3pnczw.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), L3wtpt.class);
                startActivity(i);
            }
        });





        return rootView;
    }


}
