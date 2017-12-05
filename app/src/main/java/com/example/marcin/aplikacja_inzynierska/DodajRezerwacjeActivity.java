package com.example.marcin.aplikacja_inzynierska;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DodajRezerwacjeActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_rezerwacje);

        final Calendar mojKalendarz = Calendar.getInstance();
        final TextView date = (TextView) findViewById(R.id.data);

        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mojKalendarz.set(Calendar.YEAR, year);
                mojKalendarz.set(Calendar.MONTH, monthOfYear);
                mojKalendarz.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MMM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                date.setText(sdf.format(mojKalendarz.getTime()));

            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DodajRezerwacjeActivity.this, datePickerListener, mojKalendarz.get(Calendar.YEAR),
                        mojKalendarz.get(Calendar.MONTH),
                        mojKalendarz.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText pokazgodzine = (EditText) findViewById(R.id.czas);
        pokazgodzine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DodajRezerwacjeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        pokazgodzine.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Wybierz godzinę");
                mTimePicker.show();

            }
        });
        final EditText editFirma = (EditText) findViewById(R.id.editFirma);
        final CheckBox firma = (CheckBox) findViewById(R.id.Firma);
        firma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    editFirma.setVisibility(View.VISIBLE);
                } else {
                    editFirma.setVisibility(View.INVISIBLE);
                }
            }
        });

        final EditText editImie = (EditText) findViewById(R.id.editImie);
        final EditText editNazwisko = (EditText) findViewById(R.id.editNazwisko);
        final EditText editNrTel = (EditText) findViewById(R.id.editNumerTel);
        final EditText data1 = (EditText) findViewById(R.id.data);
        final EditText czas1 = (EditText) findViewById(R.id.czas);
        final EditText firma1 = (EditText) findViewById(R.id.editFirma);


        final Button dodaj = (Button) findViewById(R.id.buttonkrok1);

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
                    mDatabase = FirebaseDatabase.getInstance().getReference().push();


                    String imie = editImie.getText().toString().trim();
                    String nazwisko = editNazwisko.getText().toString().trim();
                    String telefon = editNrTel.getText().toString().trim();
                    String data = data1.getText().toString().trim();
                    String czas = czas1.getText().toString().trim();
                    String firma = firma1.getText().toString().trim();

                    Rezerwacja rezerwacja = new Rezerwacja();
                    rezerwacja.setImie(imie);
                    rezerwacja.setNazwisko(nazwisko);
                    rezerwacja.setNrTelefonu(telefon);
                    rezerwacja.setData1(data);
                    rezerwacja.setCzas1(czas);
                    rezerwacja.setFirma(firma);
                    mDatabase.child("rez").setValue(rezerwacja);
                    Toast.makeText(DodajRezerwacjeActivity.this, "Dodano", Toast.LENGTH_SHORT).show();
                    editImie.setText(null);
                    editNazwisko.setText(null);
                    editNrTel.setText(null);
                    data1.setText(null);
                    czas1.setText(null);
                    firma1.setText(null);
                }
            }
        });

    }


}
