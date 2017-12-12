package com.example.marcin.aplikacja_inzynierska;


public class Rezerwacja {

    public String imie;
    public String nazwisko;

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String firma;

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getNrTelefonu() {
        return nrTelefonu;
    }

    public void setNrTelefonu(String nrTelefonu) {
        this.nrTelefonu = nrTelefonu;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getCzas1() {
        return czas1;
    }

    public void setCzas1(String czas1) {
        this.czas1 = czas1;
    }

    public String nrTelefonu;
    public String data1;
    public String czas1;

    public String toString() {
        return this.imie + " " + nazwisko + " " + data1 + " " + czas1 + " " + firma;


    }


}
