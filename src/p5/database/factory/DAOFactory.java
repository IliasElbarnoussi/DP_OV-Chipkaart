package p5.database.factory;

import p5.database.adres.AdresDAO;
import p5.database.adres.AdresDAOPsql;
import p5.database.ovchipkaart.OVChipkaartDAO;
import p5.database.ovchipkaart.OVChipkaartDAOPsql;
import p5.database.product.ProductDAO;
import p5.database.product.ProductDAOPsql;
import p5.database.reiziger.ReizigerDAO;
import p5.database.reiziger.ReizigerDAOPsql;

import java.sql.Connection;

public class DAOFactory {
    protected static Connection conn = ConnectionFactory.startConnection();

    protected OVChipkaartDAO ovdao = new OVChipkaartDAOPsql(this);
    protected AdresDAO adao = new AdresDAOPsql(this);
    protected ReizigerDAO rdao = new ReizigerDAOPsql(this);
    protected ProductDAO pdao = new ProductDAOPsql(this);

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

    public ProductDAO getPdao() {
        return pdao;
    }

    public Connection getConn() {
        return conn;
    }
}
