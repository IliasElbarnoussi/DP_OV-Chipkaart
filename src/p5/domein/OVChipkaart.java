package p5.domein;

import p5.database.factory.DAOFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    public int kaart_nummer;
    public Date geldig_tot;
    public int klasse;
    public int saldo;

    public Reiziger reiziger;
    public List<Product> alleProducten = new ArrayList<>();

    DAOFactory df = DAOFactory.newInstance();

    public OVChipkaart(int kaart_nummer, Date geldig_tot, int klasse, int saldo, Reiziger reiziger) {
        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public void addProduct(Product product) {
        alleProducten.add(product);
        product.voegOVChipkaartToe(this);

        df.getPdao().update(product);
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public void setKaart_nummer(int kaart_nummer) {
        this.kaart_nummer = kaart_nummer;
        df.getOvdao().update(this);
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
        df.getOvdao().update(this);
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
        df.getOvdao().update(this);
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
        df.getOvdao().update(this);
    }

    public List<Product> getAlleProducten() {
        return alleProducten;
    }

    public void setAlleProducten(List<Product> alleProducten) {
        this.alleProducten = alleProducten;
    }

    @Override
    public String toString() {
        return " Kaartnummer | " + getKaart_nummer() + " | Klasse " + getKlasse() + " | Saldo €" + getSaldo() + " | Geldig tot: " + getGeldig_tot();
    }
}
