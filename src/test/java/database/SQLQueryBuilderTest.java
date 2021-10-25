package database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLQueryBuilderTest {
    private final String TABLE_NAME = "PRODUCT";
    private final SQLAttribute NAME =
            new SQLAttribute("NAME", SQLAttribute.SQLAttributeType.TEXT, false);
    private final SQLAttribute PRICE =
            new SQLAttribute("PRICE", SQLAttribute.SQLAttributeType.INT, false);

    @Test
    void buildCreateTableQueryTest() {
        String sql = SQLQueryBuilder.buildCreateTableQuery(TABLE_NAME, NAME, PRICE);

        String correctQuery = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "PRICE INT NOT NULL)";
        assertEquals(correctQuery, sql);
    }

    @Test
    void buildInsertQueryTest() {
        final String nameValue = "iphone";
        final String priceValue = "300";

        String sql = SQLQueryBuilder.buildInsertQuery(TABLE_NAME,
                new SQLAttribute(NAME, nameValue),
                new SQLAttribute(PRICE, priceValue));
        String correctQuery = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + nameValue + "\", " + priceValue + ")";
        assertEquals(correctQuery, sql);
    }

    @Test
    void buildSelectAllQueryTest() {
        String sql = SQLQueryBuilder.buildSelectAllQuery(TABLE_NAME);
        String correctQuery = "SELECT * FROM PRODUCT";
        assertEquals(correctQuery, sql);
    }

    @Test
    void buildSelectAllOrderedQueryTest() {
        String sql = SQLQueryBuilder.buildSelectAllOrderedQuery(TABLE_NAME, "PRICE", true, 1);
        String correctQuery = "SELECT * FROM PRODUCT ORDER BY (PRICE) DESC LIMIT 1";
        assertEquals(correctQuery, sql);
    }

    @Test
    void buildSelectSumQueryTest() {
        String sql = SQLQueryBuilder.buildSelectSumQuery(TABLE_NAME, "PRICE");
        String correctQuery = "SELECT SUM(PRICE) FROM PRODUCT";
        assertEquals(correctQuery, sql);
    }

    @Test
    void buildSelectCountQueryTest() {
        String sql = SQLQueryBuilder.buildSelectCountQuery(TABLE_NAME);
        String correctQuery = "SELECT COUNT(*) FROM PRODUCT";
        assertEquals(correctQuery, sql);
    }
}