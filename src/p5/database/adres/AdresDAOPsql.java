package p5.database.adres;

import p5.database.reiziger.ReizigerDAO;
import p5.domein.Adres;
import p5.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    ReizigerDAO rdao;
    Connection connection;

    public AdresDAOPsql(Connection connection, ReizigerDAO rdaopsql) {
        this.connection = connection;
        this.rdao = rdaopsql;

    }

    @Override
    public boolean save(Adres adres) {
        try {

            String query = "INSERT INTO adres " + "(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES " +
                    "(?, ?, ?, ?, ?, ?);";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setInt(1, adres.getId());
            prst.setString(2, adres.getPostcode());
            prst.setString(3, adres.getHuisnummer());
            prst.setString(4, adres.getStraat());
            prst.setString(5, adres.getWoonplaats());
            prst.setInt(6, adres.getReiziger_id());

            prst.executeUpdate();
            prst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("[SQLException] adres kan niet opgeslagen worden in de database: " + e);
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            String query = "UPDATE adres " + "SET postcode = ? WHERE adres_id = ?";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setString(1, adres.getPostcode());
            prst.setInt(2, adres.getId());
            int rowsUpdated = prst.executeUpdate();

            System.out.println(rowsUpdated);
            prst.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[SQLException] adres kan niet worden aangepast in database: " + e);
        }
        return false;
    }

    @Override
    public boolean delete(Adres adres) {
        try {
            String query = "DELETE FROM adres WHERE adres_id = ?";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setInt(1, adres.getId());

            int rowsDeleted = prst.executeUpdate();

            System.out.println(rowsDeleted + " aantal adressen verwijderd!");

            prst.close();
            return true;

        } catch(SQLException e) {
            System.err.println("[SQLException] reiziger kan niet verwijderd worden uit de database: " + e);
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try {
            String query = "SELECT * FROM adres WHERE reiziger_id = ?";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setInt(1, reiziger.getId());

            ResultSet rs = prst.executeQuery();

            int adres_id = 0;
            String postcode = null;
            String huisnummer = null;
            String straat = null;
            String woonplaats = null;
            int reiziger_id = 0;

            while (rs.next()) {
                adres_id = rs.getInt("adres_id");
                reiziger_id = rs.getInt("reiziger_id");
                postcode = rs.getString("postcode");
                huisnummer = rs.getString("huisnummer");
                straat = rs.getString("straat");
                woonplaats = rs.getString("woonplaats");
            }

            return new Adres(adres_id, reiziger_id, postcode, huisnummer, straat, woonplaats);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[SQLException] adres niet gevonden aan de hand van reiziger id " + e);
            return null;
        }

    }

    @Override
    public List<Adres> findAll() {
        try {
            String query = "SELECT * FROM adres";
            PreparedStatement prst = connection.prepareStatement(query);
            ResultSet rs = prst.executeQuery();


            ArrayList<Adres> alleAdressen = new ArrayList<>();

            while (rs.next()) {
                int adres_id = rs.getInt("adres_id");
                int reiziger_id = rs.getInt("reiziger_id");
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");

                alleAdressen.add(new Adres(adres_id, reiziger_id, postcode, huisnummer, straat, woonplaats));
            }

            prst.close();
            return alleAdressen;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[SQLException] alle reizigers kan niet opgehaald worden uit de database: " + e);
            return null;
        }
    }
}
