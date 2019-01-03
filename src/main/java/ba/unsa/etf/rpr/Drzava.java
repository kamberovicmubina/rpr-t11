package ba.unsa.etf.rpr;

import java.io.Serializable;

public class Drzava implements Serializable {
    private String naziv;
    private Grad glavniGrad;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drzava () {
        naziv = "";
        glavniGrad = null;
        id = 0;
    }

    public Drzava (int id, String naziv, Grad glavniGrad) {
        this.id = id;
        this.naziv = naziv;
        this.glavniGrad = glavniGrad;
    }
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Grad getGlavniGrad() {
        return glavniGrad;
    }

    public void setGlavniGrad(Grad glavniGrad) {
        this.glavniGrad = glavniGrad;
    }
}
