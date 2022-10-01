package p4;

import p4.database.adres.AdresDAO;
import p4.database.adres.AdresDAOPsql;
import p4.database.factory.ConnectionFactory;
import p4.database.factory.DAOFactory;
import p4.database.ovchipkaart.OVChipkaartDAO;
import p4.database.ovchipkaart.OVChipkaartDAOPsql;
import p4.database.reiziger.ReizigerDAO;
import p4.database.reiziger.ReizigerDAOPsql;
import p4.domein.Adres;
import p4.domein.OVChipkaart;
import p4.domein.Reiziger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    static DAOFactory df = DAOFactory.newInstance();

    public static void main(String[] args) throws SQLException {
        Main main = new Main();

        main.testOVChipkaartDAO();
        main.testReizigerDAO();
        main.testAdresDAO();
    }

    public void testOVChipkaartDAO() {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende OVChipkaarten:");
        ArrayList<OVChipkaart> Chipkaarten = df.getOvdao().findAll();
        for (OVChipkaart ovc : Chipkaarten) {
            System.out.println(ovc);
        }
        System.out.println("Er staan " + Chipkaarten.size() + " OVChipkaarten in de database\n");


        System.out.println("[Test] Er staan " + Chipkaarten.size() + " OVChipkaarten in de database voordat OVChipkaartDAO.save() wordt aangeroepen");
        Reiziger r1 = new Reiziger(9, "l", "", "Dijkstra", Date.valueOf("1999-06-22"));
        df.getRdao().save(r1);
        r1.createNewOvchipkaart(77712, Date.valueOf("2023-09-22"), 1, 50);
        Chipkaarten = df.getOvdao().findAll();
        System.out.println("Er staan " + Chipkaarten.size() + " OVChipkaarten in de database, nadat OVChipkaartDAO.save() is aangeroepen!\n");

        System.out.println("[Test] OVChipkaartDAO.update() past het saldo aan van de OVChipkaart");
        OVChipkaart eerste_ovchipkaart_van_reiziger = r1.getAlleOVChipkaarten().get(0);
        System.out.println("Huidige saldo van kaartnummer 77712 is: €" + eerste_ovchipkaart_van_reiziger.getSaldo());
        eerste_ovchipkaart_van_reiziger.setSaldo(94);

        Chipkaarten = df.getOvdao().findAll();
        for (OVChipkaart ovchipkaart : Chipkaarten) {
            if (77712 == ovchipkaart.getKaart_nummer()) System.out.println("OVChipkaart uit database met kaartnummer 7712 heeft een saldo van €" + ovchipkaart.getSaldo() + "\n");
        }

        System.out.println("[Test] OVChipkaartDAO.findByReiziger() geeft de volgende OVChipkaart(en): ");
//        Reiziger reiziger = new Reiziger( 10,"G", "van der", "Linden", Date.valueOf("1988-01-27"));
        ArrayList<OVChipkaart> gevondenOVChipkaarten = df.getOvdao().findByReiziger(r1);

        for (OVChipkaart ovchip : gevondenOVChipkaarten) {
            System.out.println(ovchip);
        }

        System.out.println(gevondenOVChipkaarten.size() + " aantal gevonden OVChipkaart(en) van reiziger " + r1.getAchternaam() + "\n");

        System.out.println("[Test] Er staan " + Chipkaarten.size() + " OVChipkaarten in de database, voordat OVChipkaartDAO.delete() wordt aangeroepen");
        r1.deleteOvChipkaart(0);
        Chipkaarten = df.getOvdao().findAll();
        System.out.println("Er staan " + Chipkaarten.size() + " OVChipkaarten in de database, nadat OVChipkaartDAO.delete() is aangeroepen!\n");

        df.getRdao().delete(r1);
    }


    public void testReizigerDAO() throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");
        List<Reiziger> reizigers = df.getRdao().findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");

        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() \n");
        Reiziger sietske = new Reiziger( 6,"S", "", "Boers", Date.valueOf("1981-03-14"));
        df.getRdao().save(sietske);
        reizigers = df.getRdao().findAll(); // aantal reizigers in database nadat een nieuwe Reiziger is aangemaakt
        System.out.println(reizigers.size() + " reizigers\n");

        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        System.out.print("[Test] Voor het opzoeken van een reiziger aan de hand van ID");
        Reiziger gevondenReizigerById = df.getRdao().findById(sietske.getId());
        System.out.println(gevondenReizigerById);

        System.out.print("[Test] Voor het verwijderen van een reiziger, er zijn nu " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        df.getRdao().delete(sietske);
        reizigers = df.getRdao().findAll();
        System.out.println(reizigers.size() + " reizigers\n");


        String geboortedatum = "2002-07-25";
        Reiziger Ilias = new Reiziger(7,"i", "", "Elbarnoussi", Date.valueOf(geboortedatum));
        df.getRdao().save(Ilias);
        List<Reiziger> gevondenReizigerBijGeboortedatum = df.getRdao().findByGbdatum(geboortedatum);
        System.out.print("[Test] Aantal gevonden reizigers met de geboortedatum " + geboortedatum + " is " + gevondenReizigerBijGeboortedatum.size());
        for (Reiziger reiziger : gevondenReizigerBijGeboortedatum) {
            System.out.println(reiziger);
        }
        System.out.println();

        System.out.println("[Test] Reiziger voor de aanpassing (Geboortdatum & Voorletters)\n" + Ilias);
        Ilias.setGeboortedatum(Date.valueOf("2002-06-20"));
        Ilias.setVoorletters("I.");
        Reiziger reizigerMetAanpassingen = df.getRdao().findById(Ilias.getId());
        System.out.print("De reiziger na update \n " + reizigerMetAanpassingen);

        df.getRdao().delete(Ilias);
    }

    public void testAdresDAO() {
        System.out.println("\n---------- Test AdresDAO -------------");

        List<Adres> adressen = df.getAdao().findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende Adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        Reiziger r1 = new Reiziger(8, "G", "van", "Rijn", Date.valueOf("2002-09-17"));
        df.getRdao().save(r1);
        r1.createNewAdres("3445AR", "22", "Vermeulenstraat", "Utrecht");

        System.out.println("[Test] aantal adressen " + adressen.size() + " voor AdresDAO.save()");
        adressen = df.getAdao().findAll();
        System.out.println(adressen.size() + " aantal adressen, na AdresDAO.save()\n");

        System.out.println("[Test] Adres gezocht bij Reiziger met adres_id = " + r1.getAdres().getAdres_id());
        Adres gevondenAdres = df.getAdao().findByReiziger(r1);
        System.out.println(gevondenAdres);
        System.out.println("\n");

        System.out.println("[Test] Adres dat gewijzigd zal worden voor AdresDAO.update()");
        System.out.println(r1.getAdres());

        r1.getAdres().setPostcode("3451DZ");
        r1.getAdres().setStraat("Schoolstraat");
        System.out.println("[Test] Het adres na AdresDAO.update() \n" + df.getAdao().findByReiziger(r1));

        adressen = df.getAdao().findAll();
        System.out.println("[Test] aantal adressen " + adressen.size() + " voor AdresDAO.delete()");
        r1.deleteAdres();
        adressen = df.getAdao().findAll();
        System.out.println(adressen.size() + " aantal adressen, na AdresDAO.delete()\n");

        df.getRdao().delete(r1);

    }

}
