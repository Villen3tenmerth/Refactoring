package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

    private interface SQLFunction<S, T> {
        T apply(S s) throws SQLException;
    }

    private <R> R executeSQLQuery(String sql, SQLFunction<ResultSet, R> function) throws SQLException {
        AtomicReference<R> res = new AtomicReference<>();
        executeWithConnection(c -> {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            res.set(function.apply(rs));

            rs.close();
            stmt.close();
        });
        return res.get();
    }

    private List<Product> selectAll(ResultSet rs) throws SQLException {
        List<Product> items = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString(NAME.getName());
            int price = rs.getInt(PRICE.getName());
            items.add(new Product(name, price));
        }
        return items;
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
        return executeSQLQuery(SQLQueryBuilder.buildSelectAllQuery(TABLE_NAME), this::selectAll);
    }

    public Product getMaxPriceItem() throws SQLException {
        List<Product> items = executeSQLQuery(
                SQLQueryBuilder.buildSelectAllOrderedQuery(TABLE_NAME, "PRICE", true, 1),
                this::selectAll);
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }
    }

    public Product getMinPriceItem() throws SQLException {
        List<Product> items = executeSQLQuery(
                SQLQueryBuilder.buildSelectAllOrderedQuery(TABLE_NAME, "PRICE", false, 1),
                this::selectAll);
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }
    }

    public long getSumPrice() throws SQLException {
        return executeSQLQuery(SQLQueryBuilder.buildSelectSumQuery(TABLE_NAME, "PRICE"),
                rs -> rs.getLong(1));
    }

    public int getCount() throws SQLException {
        return executeSQLQuery(SQLQueryBuilder.buildSelectCountQuery(TABLE_NAME),
                rs -> rs.getInt(1));
    }
}
