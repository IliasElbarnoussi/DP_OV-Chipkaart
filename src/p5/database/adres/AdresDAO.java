package p5.database.adres;

import p5.domein.Adres;
import p5.domein.Reiziger;

import java.util.List;

public interface AdresDAO {
    public boolean save(Adres adres);
    public boolean update(Adres adres);
    public boolean delete(Adres adres);
    public Adres findByReiziger(Reiziger reiziger);
    public List<Adres> findAll();

}
