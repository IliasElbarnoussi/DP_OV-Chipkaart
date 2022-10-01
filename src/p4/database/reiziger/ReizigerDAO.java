package p4.database.reiziger;

import p4.domein.Reiziger;

import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger);
    public boolean update(Reiziger reiziger);
    public boolean delete(Reiziger reiziger);
    public Reiziger findById(int reiziger_id);
    public List<Reiziger> findByGbdatum(String datum);
    public List<Reiziger> findAll();
}
