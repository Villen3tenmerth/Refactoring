package database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLQueryBuilderTest {
    @Test
    void buildCreateTableQueryTest() {
        String sql = SQLQueryBuilder.buildCreateTableQuery("PRODUCT",
                new SQLAttribute("NAME", "TEXT", false),
                new SQLAttribute("PRICE", "INT", false));

        String correctQuery = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "PRICE INT NOT NULL)";
        assertEquals(correctQuery, sql);
    }
}