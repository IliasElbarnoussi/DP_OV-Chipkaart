package p3.database;

import p3.domein.Reiziger;
import p3.domein.Adres;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        getConnection();
        ReizigerDAOPsql rdaopsql = new ReizigerDAOPsql(connection);
        AdresDAOPsql adoapsql = new AdresDAOPsql(connection, rdaopsql);
        testAdresDAO(adoapsql);

//        testReizigerDAO(rdaopsql);
    }

    private static void getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=123";
        connection = DriverManager.getConnection(url);
    }

    private static void closeConnection() throws SQLException {
        connection.close();
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");

        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf("1981-03-14"));
        Adres adres = new Adres(77, 77,"3520XT", "7", "Straatnaam", "Woonplaats");
        sietske.setAdres(adres);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


        int id = sietske.getId();
        rdao.findById(id);
        System.out.print("[Test] Voor het verwijderen van een reiziger, er zijn nu " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


        String geboortedatum = "2002-12-03";
        List<Reiziger> gevondenReizigerBijGeboortedatum = rdao.findByGbdatum(geboortedatum);
        System.out.print("[Test] Aantal gevonden reizigers met de geboortedatum " + geboortedatum + " is " + gevondenReizigerBijGeboortedatum.size());

        System.out.println();
        Reiziger reizigerVoorAanpassing = rdao.findById(3);
        System.out.println(reizigerVoorAanpassing);
        Reiziger reizigerObject = new Reiziger(3, "H", "van", "Lubben", Date.valueOf("1998-08-11"));
        rdao.update(reizigerObject);
        Reiziger reizigerMetAanpassingen = rdao.findById(3);
        System.out.print("[Test] Reiziger voor de update " + reizigerVoorAanpassing + "\n De reiziger met reiziger_id na update \n " + reizigerMetAanpassingen);
    }

    private static void testAdresDAO(AdresDAO adao) {
        System.out.println("\n---------- Test AdresDAO -------------");

        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende Adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        Reiziger r1 = new Reiziger(1, "G", "van", "Rijn", Date.valueOf("2002-09-17"));
        Adres a1 = new Adres (1, r1.getId(),"3445AR", "22", "Vermeulenstraat", "Utrecht");
        r1.setAdres(a1);
        System.out.println("[Test] Adres gezocht bij Reiziger met adres_id = " + r1.getAdres().getId());
        Adres gevondenAdres = adao.findByReiziger(r1);
        System.out.println(gevondenAdres);
        System.out.println("\n");

        adressen = adao.findAll();
        System.out.println("[Test] aantal adressen " + adressen.size() + " voor AdresDAO.delete()");
        adao.delete(a1);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " aantal adressen, na AdresDAO.delete()\n");

        System.out.println("[Test] aantal adressen " + adressen.size() + " voor AdresDAO.save()");
        adao.save(a1);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " aantal adressen, na AdresDAO.save()\n");

        System.out.println("[Test] Adres met adres_id is 1 \n" + adao.findByReiziger(r1) + " voor AdresDAO.update()");
        a1.setPostcode("3445AR");
        adao.update(a1);
        System.out.println("[Test] Postcode aangepast voor adres_id = 1, na AdresDAO.update() \n" + adao.findByReiziger(r1));
    }

}
