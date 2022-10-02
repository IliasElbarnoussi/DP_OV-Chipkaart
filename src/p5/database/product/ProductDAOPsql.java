package p5.database.product;

import p5.database.factory.DAOFactory;
import p5.domein.OVChipkaart;
import p5.domein.Product;
import p5.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO{
    private Connection connection;
    DAOFactory df;

    public ProductDAOPsql(DAOFactory df) {
        this.df = df;
    }

    @Override
    public boolean save(Product product) {
        try {
            String query = "INSERT INTO product " + "(product_nummer, naam, beschrijving, prijs) VALUES " +
                    "(?, ?, ?, ?);";
            PreparedStatement prst = df.getConn().prepareStatement(query);
            prst.setInt(1, product.getProduct_nummer());
            prst.setString(2, product.getNaam());
            prst.setString(3, product.getBeschrijving());
            prst.setDouble(4, product.getPrijs());
            prst.executeUpdate();

            for (OVChipkaart ovChipkaart : product.getAlleOvchipkaarten()) {
                query = "INSERT INTO ov_chipkaart_product " + "(product_nummer, kaart_nummer) VALUES " +
                        "(?, ?);";
                prst = df.getConn().prepareStatement(query);
                prst.setInt(1, product.getProduct_nummer());
                prst.setInt(2, ovChipkaart.getKaart_nummer());
                prst.executeUpdate();
            }

            prst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("[SQLException] product kan niet opgeslagen worden in de database: " + e);
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        try {
            String query = "UPDATE product " + "SET product_nummer = ?, naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
            PreparedStatement prst = df.getConn().prepareStatement(query);
            prst.setInt(1, product.getProduct_nummer());
            prst.setString(2, product.getNaam());
            prst.setString(3, product.getBeschrijving());
            prst.setDouble(4, product.getPrijs());
            prst.setInt(5, product.getProduct_nummer());

            List<OVChipkaart> alleOvchipkaarten = product.getAlleOvchipkaarten();
            List<OVChipkaart> alleOvchipkaartenDatabase = new ArrayList<>();
            String query2 = "SELECT ov_chipkaart_product.product_nummer, ov_chipkaart.kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart_product JOIN ov_chipkaart ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer AND ov_chipkaart_product.product_nummer = ?;";
            prst = df.getConn().prepareStatement(query2);
            prst.setInt(1, product.getProduct_nummer());
            ResultSet rs = prst.executeQuery();



            while (rs.next()) {
                int kaart_nummer = rs.getInt("kaart_nummer");
                Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                int saldo = rs.getInt("saldo");

                int reiziger_id = rs.getInt("reiziger_id");
                Reiziger reizger = df.getRdao().findById(reiziger_id);
                alleOvchipkaartenDatabase.add(new OVChipkaart(kaart_nummer, geldig_tot, klasse, saldo, reizger));
            }

            prst.executeUpdate();
            prst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("[SQLException] product kon niet aangepast worden: " + e);
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try {
            String query = "DELETE FROM product WHERE product_nummer = ?";
            PreparedStatement prst = df.getConn().prepareStatement(query);
            prst.setInt(1, product.getProduct_nummer());
            prst.executeUpdate();

            query = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
            prst = df.getConn().prepareStatement(query);
            prst.setInt(1, product.getProduct_nummer());
            prst.executeUpdate();

            prst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("[SQLException] product kon niet verwijderd worden: " + e);
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        try {
            String query = "SELECT product.product_nummer, product.naam, product.beschrijving, product.prijs, ov_chipkaart_product.kaart_nummer FROM ov_chipkaart_product JOIN product ON product.product_nummer = ov_chipkaart_product.product_nummer AND ov_chipkaart_product.kaart_nummer = ?;";
            PreparedStatement prst = df.getConn().prepareStatement(query);
            prst.setInt(1, ovChipkaart.getKaart_nummer());

            ResultSet rs = prst.executeQuery(query);
            List<Product> producten = new ArrayList<>();
            while (rs.next()) {
                int product_nummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                int prijs = rs.getInt("prijs");

                producten.add(new Product(product_nummer, naam, beschrijving, prijs));
            }

            prst.close();
            return producten;
        } catch (SQLException e ) {
            System.err.println("[SQLException] product kon niet gevonden worden aan de hand van OVChipkaart: " + e);
            return null;
        }
    }

    @Override
    public List<Product> findAll() {
        try {
            String query = "SELECT * FROM product";
            PreparedStatement prst = df.getConn().prepareStatement(query);

            ResultSet rs = prst.executeQuery();
            List<Product> producten = new ArrayList<>();

            while (rs.next()) {
                int product_nummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");

                producten.add(new Product(product_nummer, naam, beschrijving, prijs));
            }

            for (Product product : producten) {
                String query2 = "SELECT ov_chipkaart_product.product_nummer, ov_chipkaart.kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart_product JOIN ov_chipkaart ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer AND ov_chipkaart_product.product_nummer = ?;";
                prst = df.getConn().prepareStatement(query2);
                prst.setInt(1, product.getProduct_nummer());
                rs = prst.executeQuery();

                while (rs.next()) {
                    int kaart_nummer = rs.getInt("kaart_nummer");
                    Date geldig_tot = rs.getDate("geldig_tot");
                    int klasse = rs.getInt("klasse");
                    int saldo = rs.getInt("saldo");

                    int reiziger_id = rs.getInt("reiziger_id");
                    Reiziger reizger = df.getRdao().findById(reiziger_id);
                    product.getAlleOvchipkaarten().add(new OVChipkaart(kaart_nummer, geldig_tot, klasse, saldo, reizger));
                }
            }

            prst.close();
            return producten;
        } catch (SQLException e) {
            System.err.println("[SQLException] alle producten kon niet gevonden worden in de database: " + e);
            return null;
        }
    }
}
