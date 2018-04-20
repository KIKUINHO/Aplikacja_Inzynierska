package com.example.marcin.aplikacja_inzynierska;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class WyszukiwarkaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    public ListView mlista;
    Button wybierzdzien;
    public boolean warunek1 = true;
    String placeId = "";
    String data;

    public ArrayList<Rezerwacja> lista = new ArrayList<>();
    public ArrayList<String> listav = new ArrayList<>();
    ArrayAdapter adapter;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyszukiwarka);


        wybierzdzien = (Button) findViewById(R.id.szukajrez);
        mlista = (ListView) findViewById(R.id.listarez);
        // obsługa przycisku wyboru daty do wyświetlenia rezerwacji z danego dnia
        wybierzdzien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(WyszukiwarkaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                wybierzdzien.setText(year + "-" + setMonthNumber(monthOfYear) + "-" + setNumber(dayOfMonth));
                                data = (year + "-" + setMonthNumber(monthOfYear) + "-" + setNumber(dayOfMonth));


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        warunek1 = danyDzien(data);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (warunek1) {
                    showData(dataSnapshot);
                    delete(dataSnapshot);
                    adapter.clear();
                    showData(dataSnapshot);
                } else {
                    Toast.makeText(WyszukiwarkaActivity.this, "Wybierz dzień!!!", Toast.LENGTH_LONG).show();

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void showData(DataSnapshot dataSnapshot) {


        for (DataSnapshot sa : dataSnapshot.child("rezerwacja").getChildren()) {


            placeId = dataSnapshot.child("rezerwacja").child(sa.getKey()).getKey();
            Rezerwacja rezerwacja = new Rezerwacja(placeId,
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("imie").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("nazwisko").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("nrTelefonu").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("czas1").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("data1").getValue().toString()

            );

            lista.add(rezerwacja);
            String dane = rezerwacja.data1 + " " + rezerwacja.nazwisko;
            listav.add(dane);
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listav);
        mlista.setAdapter(adapter);
    }

    // usuwanie z bazy danych
    private void delete(final DataSnapshot dataSnapshot) {

        mlista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, final long l) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WyszukiwarkaActivity.this);
                alertDialogBuilder.setMessage("Czy napewno chcesz usunąć element?");
                alertDialogBuilder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("rezerwacja").child(placeId).removeValue();

                        Toast.makeText(WyszukiwarkaActivity.this, "Usunięto element", Toast.LENGTH_LONG).show();

                    }
                });
                alertDialogBuilder.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(WyszukiwarkaActivity.this, "Nie usunięto elementu", Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
    }

    private String setMonthNumber(int monthOfYear) {
        if (monthOfYear < 9)
            return "0" + String.valueOf(monthOfYear + 1);
        else
            return String.valueOf(monthOfYear + 1);
    }

    private String setNumber(int num) {
        if (num < 10)
            return "0" + String.valueOf(num);
        else
            return String.valueOf(num);
    }

    private boolean danyDzien(String data) {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sa : dataSnapshot.child("rezerwacja").getChildren()) {


                    Rezerwacja rezerwacja = new Rezerwacja(dataSnapshot.child("rezerwacja").child(sa.getKey()).child("id").getValue().toString(),
                            dataSnapshot.child("rezerwacja").child(sa.getKey()).child("imie").getValue().toString(),
                            dataSnapshot.child("rezerwacja").child(sa.getKey()).child("nazwisko").getValue().toString(),
                            dataSnapshot.child("rezerwacja").child(sa.getKey()).child("nrTelefonu").getValue().toString(),
                            dataSnapshot.child("rezerwacja").child(sa.getKey()).child("czas1").getValue().toString(),
                            dataSnapshot.child("rezerwacja").child(sa.getKey()).child("data1").getValue().toString()

                    );


                    lista.add(rezerwacja);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).czas1.equals(data))
                return true;
        }
        return false;
    }


}