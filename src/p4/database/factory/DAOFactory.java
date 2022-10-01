package p4.database.factory;

import p4.database.adres.AdresDAO;
import p4.database.adres.AdresDAOPsql;
import p4.database.ovchipkaart.OVChipkaartDAO;
import p4.database.ovchipkaart.OVChipkaartDAOPsql;
import p4.database.reiziger.ReizigerDAO;
import p4.database.reiziger.ReizigerDAOPsql;

import java.sql.Connection;

public class DAOFactory {
    protected static Connection conn = ConnectionFactory.startConnection();

    protected OVChipkaartDAO ovdao = new OVChipkaartDAOPsql(this);
    protected AdresDAO adao = new AdresDAOPsql(this);
    protected ReizigerDAO rdao = new ReizigerDAOPsql(this);

    public static DAOFactory newInstance() {
        return new DAOFactory();
    }

    public OVChipkaartDAO getOvdao() {

        return ovdao;
    }

    public AdresDAO getAdao() {
        return adao;
    }

    public ReizigerDAO getRdao() {
        return rdao;
    }

    public Connection getConn() {
        return conn;
    }
}
