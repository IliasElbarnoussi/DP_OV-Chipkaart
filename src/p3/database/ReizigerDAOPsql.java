package p3.database;

import p3.domein.Adres;
import p3.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection connection;
    private AdresDAO adoa;

    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {

            String query = "INSERT INTO reiziger " + "(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES " +
                    "(?, ?, ?, ?, ?);";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setInt(1, reiziger.getId());
            prst.setString(2, reiziger.getVoorletters());
            prst.setString(3, reiziger.getTussenvoegsel());
            prst.setString(4, reiziger.getAchternaam());
            prst.setDate(5, reiziger.getGeboortedatum());

            System.out.println(prst);
            prst.executeUpdate();
            prst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("[SQLException] reiziger kan niet opgeslagen worden in de database: " + e);
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try {
            String query = "UPDATE reiziger " + "SET tussenvoegsel = ? WHERE reiziger_id = ?";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setString(1, reiziger.getTussenvoegsel());
            prst.setInt(2, reiziger.getId());
            int rowsUpdated = prst.executeUpdate();

            System.out.println(rowsUpdated);
            prst.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[SQLException] reiziger kan niet worden aangepast in database: " + e);
        }


        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            String query = "DELETE FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setInt(1, reiziger.getId());

            int rowsDeleted = prst.executeUpdate();

            System.out.println(rowsDeleted + " aantal reizigers verwijderd!");

            prst.close();
            return true;

        } catch(SQLException e) {
            System.err.println("[SQLException] reiziger kan niet verwijderd worden uit de database: " + e);
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        try {
            String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            System.out.println("ID IS => " + id);

            PreparedStatement prst = connection.prepareStatement(query);
            prst.setInt(1, id);
            ResultSet rs = prst.executeQuery();

            int reiziger_id = 0;
            String voorletters = null;
            String tussenvoegsel = null;
            String achternaam = null;
            Date geboortedatum = null;

            while (rs.next()) {
                reiziger_id = rs.getInt("reiziger_id");
                voorletters = rs.getString("voorletters");
                tussenvoegsel = rs.getString("tussenvoegsel");
                achternaam = rs.getString("achternaam");
                geboortedatum = rs.getDate("geboortedatum");
            }

            prst.close();
            return new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);

        } catch (SQLException e) {
            System.err.println("[SQLException] reiziger kan niet verwijderd worden uit de database: " + e);
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        try {
            String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
            PreparedStatement prst = connection.prepareStatement(query);
            prst.setDate(1, Date.valueOf(datum));
            ResultSet rs = prst.executeQuery();

            ArrayList<Reiziger> alleReizigers = new ArrayList<>();

            while (rs.next()) {
                int reiziger_id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");
                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");

                alleReizigers.add(new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum));
            }

            prst.close();
            return alleReizigers;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[SQLException] reiziger kan niet opgehaald worden (d.m.v geboortedatum) uit de database: " + e);
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() {
        try {
            String query = "SELECT * FROM reiziger inner join adres on reiziger.reiziger_id = adres.reiziger_id;";
            PreparedStatement prst = connection.prepareStatement(query);
            ResultSet rs = prst.executeQuery();


            ArrayList<Reiziger> alleReizigers = new ArrayList<>();

            while (rs.next()) {
                int reiziger_id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");

                if (tussenvoegsel == null) tussenvoegsel = "";

                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");

                int adres_id = rs.getInt("adres_id");
                String postcode  = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");

                Reiziger reiziger = new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);
                reiziger.setAdres(new Adres(adres_id, reiziger_id, postcode, huisnummer,straat, woonplaats));

                alleReizigers.add(reiziger);
            }

            prst.close();
            return alleReizigers;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[SQLException] alle reizigers kan niet opgehaald worden uit de database: " + e);
            return null;
        }
    }
}
