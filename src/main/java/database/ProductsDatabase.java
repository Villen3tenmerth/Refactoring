package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductsDatabase {
    private static final String SCHEMA = "jdbc:sqlite";
    private static final String TABLE_NAME = "PRODUCTS";
    private static final SQLAttribute[] ATTRIBUTES = new SQLAttribute[]{
            new SQLAttribute("NAME", SQLAttribute.SQLAttributeType.TEXT, false),
            new SQLAttribute("PRICE", SQLAttribute.SQLAttributeType.INT, false)};

    private final String file;

    public ProductsDatabase(String file) {
        this.file = file;
    }

    private String getUrl() {
        return SCHEMA + ":" + file;
    }

    private void executeSQL(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection(getUrl())) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public void createTable() throws SQLException {
        executeSQL(SQLQueryBuilder.buildCreateTableQuery(TABLE_NAME, ATTRIBUTES));
    }

    public void insertItem(Product product) throws SQLException {
        SQLAttribute name = new SQLAttribute(ATTRIBUTES[0], product.getName());
        SQLAttribute price = new SQLAttribute(ATTRIBUTES[1], Long.toString(product.getPrice()));
        executeSQL(SQLQueryBuilder.buildInsertQuery(TABLE_NAME, name, price));
    }
}
