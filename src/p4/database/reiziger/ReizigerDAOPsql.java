package p4.database.reiziger;

import p4.database.factory.DAOFactory;
import p4.domein.Adres;
import p4.domein.OVChipkaart;
import p4.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection connection;
    DAOFactory df;

    public ReizigerDAOPsql(DAOFactory df) {
        this.df = df;
        this.connection = df.getConn();
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {
            String query = "INSERT INTO reiziger " + "(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES " +
                    "(?, ?, ?, ?, ?);";
            PreparedStatement prst = df.getConn().prepareStatement(query);
            prst.setInt(1, reiziger.getId());
            prst.setString(2, reiziger.getVoorletters());
            prst.setString(3, reiziger.getTussenvoegsel());
            prst.setString(4, reiziger.getAchternaam());
            prst.setDate(5, reiziger.getGeboortedatum());

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
            String query = "UPDATE reiziger " + "SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
            PreparedStatement prst = df.getConn().prepareStatement(query);
            prst.setString(1, reiziger.getVoorletters());
            prst.setString(2, reiziger.getTussenvoegsel());
            prst.setString(3, reiziger.getAchternaam());
            prst.setDate(4, reiziger.getGeboortedatum());
            prst.setInt(5, reiziger.getId());
            int rowsUpdated = prst.executeUpdate();

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
            PreparedStatement prst = df.getConn().prepareStatement(query);
            prst.setInt(1, reiziger.getId());

            int rowsDeleted = prst.executeUpdate();

//            System.out.println(rowsDeleted + " aantal reizigers verwijderd!");

            prst.close();
            return true;

        } catch(SQLException e) {
            System.err.println("[SQLException] reiziger kan niet verwijderd worden uit de database: " + e);
            return false;
        }
    }

    @Override
    public Reiziger findById(int reiziger_id) {
        try {
            String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";

            PreparedStatement prst = df.getConn().prepareStatement(query);
            prst.setInt(1, reiziger_id);
            ResultSet rs = prst.executeQuery();


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
            return new Reiziger( reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);

        } catch (SQLException e) {
            System.err.println("[SQLException] reiziger kan niet verwijderd worden uit de database: " + e);
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        try {
            String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
            PreparedStatement prst = df.getConn().prepareStatement(query);
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
//            inner join adres on reiziger.reiziger_id = adres.reiziger_id;
            String query = "SELECT * FROM reiziger";
            PreparedStatement prst = df.getConn().prepareStatement(query);
            ResultSet rs = prst.executeQuery();
            ArrayList<Reiziger> alleReizigers = new ArrayList<>();

            while (rs.next()) {
                int reiziger_id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = rs.getString("tussenvoegsel");

                if (tussenvoegsel == null) tussenvoegsel = "";

                String achternaam = rs.getString("achternaam");
                Date geboortedatum = rs.getDate("geboortedatum");
                Reiziger reiziger = new Reiziger( reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);

                Adres adres = df.getAdao().findByReiziger(reiziger);
                reiziger.setAdres(adres);

                ArrayList<OVChipkaart> alleOvchipkaarten = df.getOvdao().findByReiziger(reiziger);
                reiziger.setAlleOVChipkaarten(alleOvchipkaarten);
//                int adres_id = rs.getInt("adres_id");
//                String postcode  = rs.getString("postcode");
//                String huisnummer = rs.getString("huisnummer");
//                String straat = rs.getString("straat");
//                String woonplaats = rs.getString("woonplaats");


//                reiziger.setAdres(new Adres(reiziger, adres_id, postcode, huisnummer,straat, woonplaats));
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
