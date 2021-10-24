package database;

public class SQLQuerryBuilder {
    public static String getCreateTableQuerry(String tableName, SQLAttribute... attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
        sb.append("(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
        for (int i = 0; i < attributes.length; i++) {
            SQLAttribute attribute = attributes[i];
            sb.append(" ").append(attribute.getName());
            sb.append(" ").append(attribute.getType());
            sb.append(" ").append(attribute.isNullable() ? "NULL" : "NOT NULL");
            if (i == attributes.length - 1) {
                sb.append(")");
            } else {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
