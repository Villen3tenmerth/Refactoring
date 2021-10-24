package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductsDatabase {
    private static final String SCHEMA = "jdbc:sqlite";
    private static final String TABLE_NAME = "PRODUCTS";
    private static final SQLAttribute[] ATTRIBUTES = new SQLAttribute[]{
            new SQLAttribute("NAME", "TEXT", false),
            new SQLAttribute("PRICE", "INT", false)};

    private final String file;

    public ProductsDatabase(String file) {
        this.file = file;
    }

    public String getUrl() {
        return SCHEMA + ":" + file;
    }

    public void createTable() throws SQLException {
        try (Connection c = DriverManager.getConnection(getUrl())) {
            String sql = SQLQueryBuilder.buildCreateTableQuery(TABLE_NAME, ATTRIBUTES);
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }
}
