package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductsDatabase {
    private static final String SCHEMA = "jdbc:sqlite";
    private static final String TABLE_NAME = "PRODUCTS";
    private static final SQLAttribute NAME =
            new SQLAttribute("NAME", SQLAttribute.SQLAttributeType.TEXT, false);
    private static final SQLAttribute PRICE =
            new SQLAttribute("PRICE", SQLAttribute.SQLAttributeType.INT, false);

    private final String file;

    public ProductsDatabase(String file) {
        this.file = file;
    }

    private String getUrl() {
        return SCHEMA + ":" + file;
    }

    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    private void executeWithConnection(SQLConsumer<Connection> consumer) throws SQLException {
        try (Connection c = DriverManager.getConnection(getUrl())) {
            consumer.accept(c);
        }
    }

    private void executeSQLUpdate(String sql) throws SQLException {
        executeWithConnection(c -> {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        });
    }

    public void createTable() throws SQLException {
        executeSQLUpdate(SQLQueryBuilder.buildCreateTableQuery(TABLE_NAME, NAME, PRICE));
    }

    public void insertItem(Product product) throws SQLException {
        SQLAttribute name = new SQLAttribute(NAME, product.getName());
        SQLAttribute price = new SQLAttribute(PRICE, Long.toString(product.getPrice()));
        executeSQLUpdate(SQLQueryBuilder.buildInsertQuery(TABLE_NAME, name, price));
    }

    public List<Product> getAllItems() throws SQLException {
        List<Product> items = new ArrayList<>();
        executeWithConnection(c -> {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(SQLQueryBuilder.buildSelectAllQuery(TABLE_NAME));

            while (rs.next()) {
                String name = rs.getString(NAME.getName());
                int price = rs.getInt(PRICE.getName());
                items.add(new Product(name, price));
            }

            rs.close();
            stmt.close();
        });
        return items;
    }
}
