package p5.domein;

public class Adres {
    private int id;
    private int reiziger_id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;

    public Adres(int id, int reiziger_id, String postcode, String huisnummer, String straat, String woonplaats) {
        this.id = id;
        this.reiziger_id = reiziger_id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    @Override
    public String toString() {
        return "id " + id + " | reiziger_id=" + reiziger_id + " | postcode " + postcode + " | huisnummer " + huisnummer + " | straat " + straat + " | woonplaats " + woonplaats + "\n";
    }
}
