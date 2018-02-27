package com.example.marcin.aplikacja_inzynierska;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WyszukiwarkaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView mlista;

    ArrayList<Rezerwacja> lista = new ArrayList<>();
    ArrayList<String> listav = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyszukiwarka);

        mlista = (ListView) findViewById(R.id.listarez);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {

        ArrayAdapter adapter;

        for (DataSnapshot sa : dataSnapshot.child("rezerwacja").getChildren()) {

            Rezerwacja rezerwacja = new Rezerwacja(
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("imie").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("nazwisko").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("nrTelefonu").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("czas1").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("data1").getValue().toString()

            );
            lista.add(rezerwacja);
            listav.add(rezerwacja.data1);
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listav);
        mlista.setAdapter(adapter);
    }
}
/*
    private DatabaseReference mDatabase;
    private ListView mlista;
    ArrayList<Rezerwacja> lista = new ArrayList<>();
    ArrayList<String> listav = new ArrayList<>();
 mlista = (ListView) findViewById(R.id.listView);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void showData(DataSnapshot dataSnapshot){
        ArrayAdapter adapter;

        for (DataSnapshot sa:dataSnapshot.child("rez").getChildren()){

            Rezerwacja rezerwacja = new Rezerwacja(dataSnapshot.child("rez").child(sa.getKey()).child("imie").getValue().toString(),
                    dataSnapshot.child("rez").child(sa.getKey()).child("nazwisko").getValue().toString(),
                    //  dataSnapshot.child("rez").child(sa.getKey()).child("firma").getValue().toString(),
                    dataSnapshot.child("rez").child(sa.getKey()).child("nrTelefonu").getValue().toString(),
                    dataSnapshot.child("rez").child(sa.getKey()).child("data").getValue().toString(),
                    dataSnapshot.child("rez").child(sa.getKey()).child("czas").getValue().toString());

            lista.add(rezerwacja);
            listav.add(rezerwacja.imie);
        }
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listav);
        mlista.setAdapter(adapter);
        */