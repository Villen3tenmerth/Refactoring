package database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductsDatabaseTest {
    private ProductsDatabase db;
    private final List<Product> PRODUCTS = List.of(
            new Product("iphone", 500),
            new Product("apple", 100),
            new Product("pen", 200),
            new Product("pineapple", 300)
    );

    @BeforeEach
    void beforeAll() throws IOException, SQLException {
        File file = File.createTempFile("test", "db", new File("."));
        file.deleteOnExit();
        db = new ProductsDatabase(file.getAbsolutePath());
        db.createTable();
    }
    
    private void fillTable() throws SQLException {
        for (Product p : PRODUCTS) {
            db.insertItem(p);
        }
    }

    @Test
    void emptyTest() throws SQLException {
        assertEquals(0, db.getCount());
        assertEquals(0, db.getSumPrice());
        assertNull(db.getMaxPriceItem());
        assertNull(db.getMinPriceItem());
        assertTrue(db.getAllItems().isEmpty());
    }

    @Test
    void simpleTest() throws SQLException {
        fillTable();
        assertEquals(PRODUCTS.size(), db.getCount());
        assertEquals(PRODUCTS.stream().map(Product::getPrice).reduce(0L, Long::sum), db.getSumPrice());
        assertEquals(PRODUCTS.stream().map(Product::getPrice).reduce(Long.MAX_VALUE, Long::min),
                db.getMinPriceItem().getPrice());
        assertEquals(PRODUCTS.stream().map(Product::getPrice).reduce(0L, Long::max),
                db.getMaxPriceItem().getPrice());

        List<Product> items = db.getAllItems();
        assertTrue(PRODUCTS.containsAll(items));
    }
}