package p5.database;

import p5.database.adres.AdresDAO;
import p5.database.ovchipkaart.OVChipkaartDAO;
import p5.database.ovchipkaart.OVChipkaartDAOPsql;
import p5.database.product.ProductDAO;
import p5.database.product.ProductDAOPsql;
import p5.database.reiziger.ReizigerDAO;
import p5.database.reiziger.ReizigerDAOPsql;
import p5.domein.Adres;
import p5.domein.OVChipkaart;
import p5.domein.Product;
import p5.domein.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        getConnection();
        ReizigerDAOPsql rdaopsql = new ReizigerDAOPsql(connection);
//        AdresDAOPsql adoapsql = new AdresDAOPsql(connection, rdaopsql);
        OVChipkaartDAOPsql ovdaosql = new OVChipkaartDAOPsql(connection, rdaopsql);
        ProductDAOPsql pdaosql = new ProductDAOPsql(connection);

        testProductDAO(pdaosql);
//        testOVChipkaartDAO(ovdaosql);
        closeConnection();
//        testReizigerDAO(rdaopsql);
//        testAdresDAO(adoapsql);

    }

    private static void getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=123";
        connection = DriverManager.getConnection(url);
    }

    private static void closeConnection() throws SQLException {
        connection.close();
    }

    private static void testProductDAO(ProductDAO pdao) {
        System.out.println("\n---------- Test ProductDAO -------------");

        System.out.println("[Test] ProductDAO.findAll() geeft de volgende Producten:");
        List<Product> producten = pdao.findAll();
        for (Product product : producten) {
            System.out.println(product);
        }
        System.out.println("Er staan " + producten.size() + " producten in de database\n");
    }

    private static void testOVChipkaartDAO(OVChipkaartDAO ovdao) {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende OVChipkaarten:");
        ArrayList<OVChipkaart> Chipkaarten = ovdao.findAll();
        for (OVChipkaart ovc : Chipkaarten) {
            System.out.println(ovc);
        }
        System.out.println("Er staan " + Chipkaarten.size() + " OVChipkaarten in de database\n");


        System.out.println("[Test] Er staan " + Chipkaarten.size() + " OVChipkaarten in de database voordat OVChipkaartDAO.save() wordt aangeroepen");
        OVChipkaart ovc = new OVChipkaart(77712, Date.valueOf("2023-09-22"), 1, 50, 1);
        ovdao.save(ovc);
        Chipkaarten = ovdao.findAll();
        System.out.println("Er staan " + Chipkaarten.size() + " OVChipkaarten in de database, nadat OVChipkaartDAO.save() is aangeroepen!\n");

        System.out.println("[Test] OVChipkaartDAO.update() past het saldo aan van de OVChipkaart");
        System.out.println("Huidige saldo van kaartnummer 77712 is: €" + ovc.getSaldo());
        ovc.setSaldo(100);
        ovdao.update(ovc);

        Chipkaarten = ovdao.findAll();
        for (OVChipkaart ovchipkaart : Chipkaarten) {
            if (77712 == ovchipkaart.getKaart_nummer()) System.out.println("OVChipkaart uit database met kaartnummer 7712 heeft een saldo van €" + ovchipkaart.getSaldo() + "\n");
        }

        System.out.println("[Test] OVChipkaartDAO.findByReiziger() geeft de volgende OVChipkaart(en): ");
        Reiziger reiziger = new Reiziger(5, "G", "", "Piccardo", Date.valueOf("2002-12-03"));
        ArrayList<OVChipkaart> gevondenOVChipkaarten = ovdao.findByReiziger(reiziger);

        for (OVChipkaart ovchip : gevondenOVChipkaarten) {
            System.out.println(ovchip);
        }
        System.out.println(gevondenOVChipkaarten.size() + " aantal gevonden OVChipkaart(en) bij reiziger_id " + reiziger.getId() + "\n");

        System.out.println("[Test] Er staan " + Chipkaarten.size() + " OVChipkaarten in de database, voordat OVChipkaartDAO.delete() wordt aangeroepen");
        ovdao.delete(ovc);
        Chipkaarten = ovdao.findAll();
        System.out.println("Er staan " + Chipkaarten.size() + " OVChipkaarten in de database, nadat OVChipkaartDAO.delete() is aangeroepen!\n");

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
        Adres a1 = new Adres(1, r1.getId(),"3445AR", "22", "Vermeulenstraat", "Utrecht");
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
