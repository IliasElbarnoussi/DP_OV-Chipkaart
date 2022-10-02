package p5.database.adres;

import p5.database.factory.DAOFactory;
import p5.domein.Adres;
import p5.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    DAOFactory df;
    Connection connection;

    public AdresDAOPsql(DAOFactory df) {
        this.df = df;
        this.connection = df.getConn();
    }

    @Override
    public boolean save(Adres adres) {
        try {
//            String sequence = "CREATE SEQUENCE adres_id_pk START WITH 5 INCREMENT BY 1";
            String query = "INSERT INTO adres " + "(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES " +
                    "(?, ?, ?, ?, ?, ?);";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setInt(1, adres.getAdres_id());
            prst.setString(2, adres.getPostcode());
            prst.setString(3, adres.getHuisnummer());
            prst.setString(4, adres.getStraat());
            prst.setString(5, adres.getWoonplaats());
            prst.setInt(6, adres.getReiziger().getId());

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
            String query = "UPDATE adres " + "SET postcode = ?, straat = ?, huisnummer = ?, woonplaats = ? WHERE adres_id = ?";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setString(1, adres.getPostcode());
            prst.setString(2, adres.getStraat());
            prst.setString(3, adres.getHuisnummer());
            prst.setString(4, adres.getWoonplaats());
            prst.setInt(5, adres.getAdres_id());
            int rowsUpdated = prst.executeUpdate();

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
            prst.setInt(1, adres.getAdres_id());

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
//            if (!rs.next()) return null;

            int adres_id = 0;
            String postcode = null;
            String huisnummer = null;
            String straat = null;
            String woonplaats = null;


            while (rs.next()) {
                adres_id = rs.getInt("adres_id");
                postcode = rs.getString("postcode");
                huisnummer = rs.getString("huisnummer");
                straat = rs.getString("straat");
                woonplaats = rs.getString("woonplaats");
            }

            return new Adres(reiziger, adres_id, postcode, huisnummer, straat, woonplaats);

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
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");

                alleAdressen.add(new Adres(null, adres_id, postcode, huisnummer, straat, woonplaats));
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
