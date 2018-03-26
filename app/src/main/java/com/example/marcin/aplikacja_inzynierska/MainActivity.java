package com.example.marcin.aplikacja_inzynierska;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button dodajrezerwacje = (Button) findViewById(R.id.buttondodajrezerwacje);
        dodajrezerwacje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DodajRezerwacjeActivity.class);
                startActivity(i);

            }
        });

        Button wyszukajrezerwacje = (Button) findViewById(R.id.buttonwyszukajrezerwacje);
        wyszukajrezerwacje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WyszukiwarkaActivity.class);
                startActivity(i);
            }
        });


    }
}
