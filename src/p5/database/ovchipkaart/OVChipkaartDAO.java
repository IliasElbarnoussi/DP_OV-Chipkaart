package p5.database.ovchipkaart;

import p5.domein.OVChipkaart;
import p5.domein.Reiziger;

import java.util.ArrayList;

public interface OVChipkaartDAO {
    public boolean save(OVChipkaart ovchipkaart);
    public ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger);
    public boolean update(OVChipkaart ovchipkaart);
    public boolean delete(OVChipkaart ovchipkaart);

    public ArrayList<OVChipkaart> findAll();
}
