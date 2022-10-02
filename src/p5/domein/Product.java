package p5.domein;

import p5.database.factory.DAOFactory;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;

    private List<OVChipkaart> alleOvchipkaarten = new ArrayList<>();
    static DAOFactory df = DAOFactory.newInstance(); // bij errors maak dit niet meer static

    public Product(int product_nummer, String naam, String beschrijving, double prijs) {
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }



    public static Product createNewProduct(int product_nummer, String naam, String beschrijving, double prijs) {
        Product product = new Product(product_nummer, naam, beschrijving, prijs);
        df.getPdao().save(product);
        return product;
    }

    public void deleteProduct() {
        for (OVChipkaart ov : getAlleOvchipkaarten()) {
            ov.getAlleProducten().remove(this);
        }
        df.getPdao().delete(this);
    }

    public void voegOVChipkaartToe(OVChipkaart ovChipkaart) {
        alleOvchipkaarten.add(ovChipkaart);
        df.getPdao().update(this);
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
        df.getPdao().update(this);
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
        df.getPdao().update(this);
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(int prijs) {
        this.prijs = prijs;
        df.getPdao().update(this);
    }

    public List<OVChipkaart> getAlleOvchipkaarten() {
        return alleOvchipkaarten;
    }

    public void setAlleOvchipkaarten(List<OVChipkaart> alleOvchipkaarten) {
        this.alleOvchipkaarten = alleOvchipkaarten;
        df.getPdao().update(this);
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                ", alleOvchipkaarten=" + alleOvchipkaarten +
                "} \n";
    }
}
