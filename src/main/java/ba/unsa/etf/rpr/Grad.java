package ba.unsa.etf.rpr;

import java.io.Serializable;

public class Grad implements Serializable {
    private String naziv;
    private int brojStanovnika;
    private Drzava drzava;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Grad () {
        naziv = "";
        brojStanovnika = 0;
        drzava = null;
        id = 0;
    }

    public Grad (int id, String naziv, int brojStanovnika, Drzava drzava) {
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava = drzava;
    }

    @Override
    public String toString() {
        String s = "";
        if (getDrzava() == null) s = s + getNaziv() + "()" + " - " + getBrojStanovnika() + "\n";
        else s = getNaziv() + " (" + getDrzava().getNaziv() + ")" + " - " + getBrojStanovnika() + "\n";
        return s;
    }



}
