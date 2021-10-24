package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String SCHEMA = "jdbc:sqlite";
    private final String file;

    public Database(String file) {
        this.file = file;
    }

    public String getUrl() {
        return SCHEMA + ":" + file;
    }

    public void createProductsTable() throws SQLException {
        try (Connection c = DriverManager.getConnection(getUrl())) {
            String sql = SQLQuerryBuilder.getCreateTableQuerry("PRODUCT",
                    new SQLAttribute("NAME", "TEXT", false),
                    new SQLAttribute("PRICE", "INT", false));
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }
}
