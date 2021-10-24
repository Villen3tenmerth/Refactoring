package database;

public class SQLAttribute {
    public enum SQLAttributeType {
        TEXT(true), INT(false);

        private final boolean isText;

        SQLAttributeType(boolean isText) {
            this.isText = isText;
        }

        public boolean isText() {
            return isText;
        }
    }

    private final String name;
    private final SQLAttributeType type;
    private final boolean isNullable;
    private final String value;

    public SQLAttribute(String name, SQLAttributeType type, boolean isNullable) {
        this.name = name;
        this.type = type;
        this.isNullable = isNullable;
        value = null;
    }

    public SQLAttribute(SQLAttribute attribute, String value) {
        name = attribute.name;
        type = attribute.type;
        isNullable = attribute.isNullable;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public SQLAttributeType getType() {
        return type;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public String getValue() {
        return value;
    }
}
