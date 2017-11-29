package com.example.marcin.aplikacja_inzynierska;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DodajRezerwacjeActivity extends AppCompatActivity {

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
    }
}
