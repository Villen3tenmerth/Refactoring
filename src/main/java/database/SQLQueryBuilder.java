package database;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SQLQueryBuilder {
    public static String buildCreateTableQuery(String tableName, SQLAttribute... attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
        sb.append("(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sb.append(Arrays.stream(attributes).map(attr ->
            attr.getName() + " " + attr.getType() + " " + (attr.isNullable() ? "NULL" : "NOT NULL")
        ).collect(Collectors.joining(", ")));
        sb.append(")");
        return sb.toString();
    }

    public static String buildInsertQuery(String tableName, SQLAttribute... attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName).append(" (");
        sb.append(Arrays.stream(attributes).map(SQLAttribute::getName).collect(Collectors.joining(", ")));
        sb.append(") VALUES (");
        sb.append(Arrays.stream(attributes).map(attr -> {
            if (attr.getType().isText()) {
                return "\"" + attr.getValue() + "\"";
            } else {
                return attr.getValue();
            }
        }).collect(Collectors.joining(", ")));
        sb.append(")");
        return sb.toString();
    }

    public static String buildSelectAllQuery(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    public static String buildSelectAllOrderedQuery(String tableName,
                                                    String attribute,
                                                    boolean isDesc,
                                                    int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(tableName);
        sb.append(" ORDER BY (").append(attribute).append(") ");
        if (isDesc) {
            sb.append("DESC ");
        }
        sb.append("LIMIT ").append(limit);
        return sb.toString();
    }

    public static String buildSelectSumQuery(String tableName, String attribute) {
        return "SELECT SUM(" + attribute + ") FROM " + tableName;
    }

    public static String buildSelectCountQuery(String tableName) {
        return "SELECT COUNT(*) FROM " + tableName;
    }
}
