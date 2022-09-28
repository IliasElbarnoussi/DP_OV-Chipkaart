package p5.database.product;


import p5.domein.OVChipkaart;
import p5.domein.Product;

import java.util.List;

public interface ProductDAO {
    public boolean save(Product product);
    public boolean update(Product product);
    public boolean delete(Product product);
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    public List<Product> findAll();
}
