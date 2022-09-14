package p4.database.ovchipkaart;

import p4.database.reiziger.ReizigerDAO;
import p4.domein.OVChipkaart;
import p4.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    Connection conn;
    ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection connection, ReizigerDAO rdao) {
        this.conn = connection;
        this.rdao = rdao;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    @Override
    public boolean save(OVChipkaart ovchipkaart) {
        try {
            String query = "INSERT INTO ov_chipkaart VALUES(?, ?, ?, ?, ?)";
            PreparedStatement prst = conn.prepareStatement(query);
            prst.setInt(1, ovchipkaart.getKaart_nummer());
            prst.setDate(2, ovchipkaart.geldig_tot);
            prst.setInt(3, ovchipkaart.getKlasse());
            prst.setInt(4, ovchipkaart.getSaldo());
            prst.setInt(5, ovchipkaart.getReiziger_id());

            int results = prst.executeUpdate();
            if (results < 1) return false;

            prst.close();
            return true;

        } catch (SQLException e) {
            System.err.println("[SQLException] OVChipkaarten kon niet opgeslagen worden in de database: " + e);
            return false;
        }
    }

    @Override
    public ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            String query = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
            PreparedStatement prst = conn.prepareStatement(query);
            prst.setInt(1, reiziger.getId());

            ResultSet rs = prst.executeQuery();

            ArrayList<OVChipkaart> alleOVChipkaarten = new ArrayList<>();
            while (rs.next()) {
                int kaart_nummer = rs.getInt("kaart_nummer");
                Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                int saldo = rs.getInt("saldo");
                int reiziger_id = rs.getInt("reiziger_id");

                alleOVChipkaarten.add(new OVChipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id));
            }

            prst.close();
            return alleOVChipkaarten;


        } catch (SQLException e) {
            System.err.println("[SQLException] OVChipkaarten van reiziger kon niet gevonden worden uit de database: " + e);
            return null;
        }

    }

    @Override
    public boolean update(OVChipkaart ovchipkaart) {
        try {
            String query = "UPDATE ov_chipkaart SET saldo = ? WHERE kaart_nummer = ?";
            PreparedStatement prst = conn.prepareStatement(query);

            prst.setInt(1, ovchipkaart.getSaldo());
            prst.setInt(2, ovchipkaart.getKaart_nummer());

            int result = prst.executeUpdate();

            // if statement if result < 0 return false;

            prst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("[SQLException] OVChipkaarten van reiziger kon niet gevonden worden uit de database: " + e);
            return false;
        }

    }

    @Override
    public boolean delete(OVChipkaart ovchipkaart) {
        try {
            String query = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
            PreparedStatement prst = conn.prepareStatement(query);

            prst.setInt(1, ovchipkaart.getKaart_nummer());

            int result = prst.executeUpdate();
            // if statement if result < 0 return false;

            prst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("[SQLException] OVChipkaarten van reiziger kon niet gevonden worden uit de database: " + e);
            return false;
        }
    }

    @Override
    public ArrayList<OVChipkaart> findAll() {
        try {
            String query = "SELECT * FROM ov_chipkaart";
            PreparedStatement prst = conn.prepareStatement(query);
            ResultSet rs = prst.executeQuery();

            ArrayList<OVChipkaart> alleOVChipkaarten = new ArrayList<>();
            while (rs.next()) {
                int kaart_nummer = rs.getInt("kaart_nummer");
                Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                int saldo = rs.getInt("saldo");
                int reiziger_id = rs.getInt("reiziger_id");

                alleOVChipkaarten.add(new OVChipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id));
            }

            prst.close();
            return alleOVChipkaarten;
        } catch (SQLException e) {
            System.err.println("[SQLException] Kan niet alle OVChipkaarten ophalen uit database: " + e);
            return null;
        }
    }
}
