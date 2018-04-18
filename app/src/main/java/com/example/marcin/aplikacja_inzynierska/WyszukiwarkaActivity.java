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
    char zmienna;

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

                                wybierzdzien.setText(dayOfMonth + " "
                                        + (setMonthName(monthOfYear)) + " (" + setMonthNumber(monthOfYear) + ") " + year);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        // nie działa filtrowanie danych
        mDatabase = FirebaseDatabase.getInstance().getReference();
/*
        Query query = mDatabase.child("rezerwacja").orderByChild("imie").equalTo("marcin");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    showData(dataSnapshot);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
/*
       mDatabase.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               showData(dataSnapshot);
           }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {

               delete(dataSnapshot);
           }

           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
*/
        // działające wyświetlanie rezerwacji z usuwaniem wszystkiego z bazy danych oraz brak odświeżania listview
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
                delete(dataSnapshot);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void showData(DataSnapshot dataSnapshot) {


        for (DataSnapshot sa : dataSnapshot.child("rezerwacja").getChildren()) {

            String placeId = dataSnapshot.child("rezerwacja").child(sa.getKey()).getKey();
            Rezerwacja rezerwacja = new Rezerwacja(placeId,
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("imie").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("nazwisko").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("nrTelefonu").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("czas1").getValue().toString(),
                    dataSnapshot.child("rezerwacja").child(sa.getKey()).child("data1").getValue().toString()

            );
            lista.add(rezerwacja);
            String dane = rezerwacja.czas1 + rezerwacja.data1;
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

                // TWORZY  za każdym razem nowy klucz
                //String placeId = mDatabase.child("rezerwacja").push().getKey();
                // Zaraca jako klucz rezerwacja
                // String placeId = mDatabase.child("rezerwacja").getKey();
                Rezerwacja remRez = dataSnapshot.getValue(Rezerwacja.class);
                mDatabase.child("rezerwacja").child("id").removeValue();

                Toast.makeText(WyszukiwarkaActivity.this, "usunieto", Toast.LENGTH_SHORT).show();
                // mlista.getSelectedItemPosition(i).toString();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WyszukiwarkaActivity.this);
                alertDialogBuilder.setMessage("Czy napewno chcesz usunąć element?");
                alertDialogBuilder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        // z remRez zwraca mi wartość null
                        // mlista.getSelectedItemPosition(i).toString();


                        //Toast.makeText(WyszukiwarkaActivity.this, "Usunięto element" + String.valueOf(i), Toast.LENGTH_LONG).show();

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

    private String setMonthName(int monthOfYear) {

        String res = null;
        switch (monthOfYear) {
            case Calendar.JANUARY:
                res = "stycznia";
                break;
            case Calendar.FEBRUARY:
                res = "lutego";
                break;
            case Calendar.MARCH:
                res = "marca";
                break;
            case Calendar.APRIL:
                res = "kwietnia";
                break;
            case Calendar.MAY:
                res = "maja";
                break;
            case Calendar.JUNE:
                res = "czerwca";
                break;
            case Calendar.JULY:
                res = "lipca";
                break;
            case Calendar.AUGUST:
                res = "sierpnia";
                break;
            case Calendar.SEPTEMBER:
                res = "września";
                break;
            case Calendar.OCTOBER:
                res = "października";
                break;
            case Calendar.NOVEMBER:
                res = "listopada";
                break;
            case Calendar.DECEMBER:
                res = "grudnia";
                break;
        }
        return res;
    }


}