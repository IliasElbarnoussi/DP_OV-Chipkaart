package p5.domein;

import p5.database.factory.DAOFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

public class Reiziger {
    private int reiziger_id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;

    private Adres adres;
    private ArrayList<OVChipkaart> alleOVChipkaarten = new ArrayList<>();

    DAOFactory df = DAOFactory.newInstance();

    Random rand = new Random();

    public Reiziger(int reiziger_id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
//        this.reiziger_id = rand.nextInt(100);
        this.reiziger_id = reiziger_id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;

    }

    public OVChipkaart createNewOvchipkaart(int kaartnummer, Date datum, int klasse, int saldo) {
        OVChipkaart ov = new OVChipkaart(kaartnummer, datum, klasse, saldo, this);
        alleOVChipkaarten.add(ov);
        df.getOvdao().save(ov);
        return ov;
    }

    public void deleteOvChipkaart(int index) {
        df.getOvdao().delete(alleOVChipkaarten.get(index));
        this.alleOVChipkaarten.remove(index);
    }

    public Adres createNewAdres(String postcode, String huisnummer, String straat, String woonplaats) {
        Adres adres = new Adres(this, this.getId(), postcode, huisnummer, straat, woonplaats);
        this.adres = adres;
        df.getAdao().save(adres);
        return adres;
    }

    public void deleteAdres() {
        if (adres != null) {
            df.getAdao().delete(adres);
            this.adres = null;
        }
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
        df.getRdao().update(this);
    }

    public int getId() {
        return reiziger_id;
    }

    public void setId(int reiziger_id) {
        this.reiziger_id = reiziger_id;
        df.getRdao().update(this);
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
        df.getRdao().update(this);
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
        df.getRdao().update(this);
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
        df.getRdao().update(this);
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
        df.getRdao().update(this);
    }

    public ArrayList<OVChipkaart> getAlleOVChipkaarten() {
        return alleOVChipkaarten;
    }

    public void setAlleOVChipkaarten(ArrayList<OVChipkaart> alleOVChipkaarten) {
        this.alleOVChipkaarten = alleOVChipkaarten;
    }

    @Override
    public String toString() {
        return "Reiziger{" +
                "id=" + reiziger_id +
                ", voorletters='" + voorletters + '\'' +
                ", tussenvoegsel='" + tussenvoegsel + '\'' +
                ", achternaam='" + achternaam + '\'' +
                ", geboortedatum=" + geboortedatum +
                ", adres=" + adres +
                ", alleOVChipkaarten=" + alleOVChipkaarten +
                '}';
    }
}
