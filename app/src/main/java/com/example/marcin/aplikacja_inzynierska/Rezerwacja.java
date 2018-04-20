package com.example.marcin.aplikacja_inzynierska;


import java.util.HashMap;
import java.util.Map;

public class Rezerwacja {
    public String id;
    public String imie;
    public String nazwisko;
    public String nrTelefonu;
    public String data1;
    public String czas1;


    public Rezerwacja() {
    }

    public String getData1() {
        return data1;
    }

    public Rezerwacja(String id, String imie, String nazwisko, String nrTelefonu, String data1, String czas1) {

        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrTelefonu = nrTelefonu;
        this.czas1 = czas1;
        this.data1 = data1;


    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("imie", imie);
        result.put("nazwisko", nazwisko);
        result.put("nrTelefonu", nrTelefonu);
        result.put("czas", czas1);
        result.put("data", data1);

        return result;
    }


}
