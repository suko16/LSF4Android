package de.ur.mi.lsf4android;

/**
 * Created by Susanne on 13.04.2017.
 */

public class Veranstaltung {
    // Variablen anlegen
    public String titel;
    public String beginn;
    public String ende;
    public String number;


    public Veranstaltung(String beginn, String ende, String number, String titel) {
        this.titel = titel;
        this.beginn = beginn;
        this.ende = ende;
        this.number = number;

    }
    public Veranstaltung(){
        Veranstaltung v = new Veranstaltung("", "", "", "");
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setBeginn(String beginn) {
        this.beginn = beginn;
    }

    public void setEnde(String ende) {
        this.ende = ende;
    }

    public void setNumber(String number) {this.number = number;}

    public String getTitel() {return titel;   }

    public String getBeginn() {
        return beginn;
    }

    public String getEnde() {
        return ende;
    }

    public String getNumber() {return number;}



}
