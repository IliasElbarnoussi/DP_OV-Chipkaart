package p1;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=123";

        try {
            Connection conn = DriverManager.getConnection(url);

            Statement st = conn.createStatement();
            String query = "SELECT reiziger_id as id, voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger r";

            ResultSet rs = st.executeQuery(query);

            String voorletters;
            String tussenvoegsel;

            String achternaam;
            String geboortedatum;
            int id;

            while (rs.next()) {
                voorletters = rs.getString("voorletters");
                tussenvoegsel = rs.getString("tussenvoegsel");
                achternaam = rs.getString("achternaam");
                geboortedatum = rs.getString("geboortedatum");
                id = rs.getInt("id");

                if (tussenvoegsel == null) System.out.println("#" + id + " " + voorletters + ". " + achternaam + " " + geboortedatum);
                else System.out.println("#" + id + " " + voorletters + ". " + tussenvoegsel + " " + achternaam + " " + geboortedatum);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
