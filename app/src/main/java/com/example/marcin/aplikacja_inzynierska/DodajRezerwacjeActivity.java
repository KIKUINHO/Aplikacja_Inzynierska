package com.example.marcin.aplikacja_inzynierska;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class DodajRezerwacjeActivity extends AppCompatActivity {

    EditText data1, czas1, editImie, editNazwisko, editNrTel;
    Button dodaj;
    boolean warunek = true;
    private DatabaseReference mDatabase;
    DatePickerDialog datePickerDialog;
    public ArrayList<Rezerwacja> lista = new ArrayList<>();
    public ArrayList<String> listav = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_rezerwacje);

        data1 = (EditText) findViewById(R.id.data);
        czas1 = (EditText) findViewById(R.id.czas);
        editImie = (EditText) findViewById(R.id.editImie);
        editNazwisko = (EditText) findViewById(R.id.editNazwisko);
        editNrTel = (EditText) findViewById(R.id.editNumerTel);


        dodaj = (Button) findViewById(R.id.buttonkrok1);

        data1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(DodajRezerwacjeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                data1.setText(year + "-" + setMonthNumber(monthOfYear) + "-" + setNumber(dayOfMonth));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });

        czas1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DodajRezerwacjeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        czas1.setText(setNumber(selectedHour) + ":" + setMinute(selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Wybierz godzinę");
                mTimePicker.show();
            }
        });


        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editImie.getText().toString().equals("") | editNazwisko.getText().toString().equals("") | editNrTel.getText().toString().equals("") |
                        data1.getText().toString().equals("") | czas1.getText().toString().equals("")) {
                    Toast.makeText(DodajRezerwacjeActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                    if (editImie.getText().toString().equals("")) {
                        editImie.setBackgroundColor(Color.RED);
                        editImie.setTextColor(Color.WHITE);

                    }
                    if (editNazwisko.getText().toString().equals("")) {
                        editNazwisko.setBackgroundColor(Color.RED);
                        editNazwisko.setTextColor(Color.WHITE);
                    }
                    if (editNrTel.getText().toString().equals("")) {
                        editNrTel.setBackgroundColor(Color.RED);
                        editNrTel.setTextColor(Color.WHITE);
                    }
                    if (data1.getText().toString().equals("")) {
                        data1.setBackgroundColor(Color.RED);
                        data1.setTextColor(Color.WHITE);
                    }
                    if (czas1.getText().toString().equals("")) {
                        czas1.setBackgroundColor(Color.RED);
                        czas1.setTextColor(Color.WHITE);
                    }
                } else {
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    warunek = czyistnieje(data1.getText().toString() + czas1.getText().toString());

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (warunek) {
                                Rezerwacja rezerwacja = new Rezerwacja(data1.getText().toString() + czas1.getText().toString(), editImie.getText().toString(), editNazwisko.getText().toString(), editNrTel.getText().toString(), data1.getText().toString(), czas1.getText().toString());
                                mDatabase.child("rezerwacja").push().setValue(rezerwacja);
                                Toast.makeText(DodajRezerwacjeActivity.this, "Dodano", Toast.LENGTH_SHORT).show();
                                editImie.setText(null);
                                editNazwisko.setText(null);
                                editNrTel.setText(null);
                                data1.setText(null);
                                czas1.setText(null);
                            } else {
                                Toast.makeText(DodajRezerwacjeActivity.this, "Na daną godzinę istnieje już rezerwacja", Toast.LENGTH_SHORT).show();
                            }


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    mDatabase.addListenerForSingleValueEvent(eventListener);


                }
            }
        });
    }


    private boolean czyistnieje(String dane) {

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
            if (lista.get(i).id.equals(dane))
                return false;
        }
        return true;
    }

    private String setMonthNumber(int monthOfYear) {
        if (monthOfYear < 9)
            return "0" + String.valueOf(monthOfYear + 1);
        else
            return String.valueOf(monthOfYear + 1);
    }

    private String setMinute(int selectedMinute) {
        if (selectedMinute < 10)
            return "0" + String.valueOf(selectedMinute);
        else
            return String.valueOf(selectedMinute);
    }

    private String setNumber(int num) {
        if (num < 10)
            return "0" + String.valueOf(num);
        else
            return String.valueOf(num);
    }

}



