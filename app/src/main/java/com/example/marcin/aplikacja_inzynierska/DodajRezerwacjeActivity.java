package com.example.marcin.aplikacja_inzynierska;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DodajRezerwacjeActivity extends AppCompatActivity {

    EditText data1, czas1, editFirma, editImie, editNazwisko, editNrTel, firma1;
    Button dodaj;
    CheckBox firma;
    private DatabaseReference mDatabase;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_rezerwacje);

        data1 = (EditText) findViewById(R.id.data);
        czas1 = (EditText) findViewById(R.id.czas);

        editImie = (EditText) findViewById(R.id.editImie);
        editNazwisko = (EditText) findViewById(R.id.editNazwisko);
        editNrTel = (EditText) findViewById(R.id.editNumerTel);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);


        // Problem w tym że nie mogę odczytać pozycji spinnnera i wsadzić go do rezerwacji
        // Oraz zastanawiam się jak zrobić np co tygodniową rezerwacje
        //test
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hala, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


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

                                data1.setText(dayOfMonth + " "
                                        + (setMonthName(monthOfYear)) + " (" + setMonthNumber(monthOfYear) + ") " + year);

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
                        czas1.setText(selectedHour + ":" + setMinute(selectedMinute));
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
                    mDatabase.child("rezerwacja").orderByChild("czas1").equalTo(String.valueOf(czas1));

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                // w tym ifie wykonuje tylko to co znajduje się po else lub w przypadku braku negacji datasnapshota wykonuje się pierwsza część
                                Rezerwacja rezerwacja = new Rezerwacja(editImie.getText().toString(), editNazwisko.getText().toString(), editNrTel.getText().toString(), data1.getText().toString(), czas1.getText().toString());
                                mDatabase.child("rezerwacja").push().setValue(rezerwacja);
                                Toast.makeText(DodajRezerwacjeActivity.this, "Dodano", Toast.LENGTH_SHORT).show();
                                editImie.setText(null);
                                editNazwisko.setText(null);
                                editNrTel.setText(null);
                                data1.setText(null);
                                czas1.setText(null);
                                firma1.setText(null);

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



