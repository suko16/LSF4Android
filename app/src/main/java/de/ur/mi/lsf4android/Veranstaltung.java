package de.ur.mi.lsf4android;

/**
 * Created by Susanne on 13.04.2017.
 */

public class Veranstaltung {
    // Variablen anlegen
    private String titel;
    private String beginn;
    private String ende;

    public Veranstaltung(String beginn, String ende, String titel) {
        this.titel = titel;
        this.beginn = beginn;
        this.ende = ende;
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

    public String getTitel() {

        return titel;
    }

    public String getBeginn() {
        return beginn;
    }

    public String getEnde() {
        return ende;
    }



    // Getter und Setter Methoden
    // Constructor
}
