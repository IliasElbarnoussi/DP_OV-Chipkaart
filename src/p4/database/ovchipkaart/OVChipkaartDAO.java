package p4.database.ovchipkaart;

import p4.domein.OVChipkaart;
import p4.domein.Reiziger;

import java.util.ArrayList;
import java.util.List;

public interface OVChipkaartDAO {
    public boolean save(OVChipkaart ovchipkaart);
    public ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger);
    public boolean update(OVChipkaart ovchipkaart);
    public boolean delete(OVChipkaart ovchipkaart);

    public ArrayList<OVChipkaart> findAll();
}
