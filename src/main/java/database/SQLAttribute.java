package database;

public class SQLAttribute {
    private final String name;
    private final String type;
    private final boolean isNullable;

    public SQLAttribute(String name, String type, boolean isNullable) {
        this.name = name;
        this.type = type;
        this.isNullable = isNullable;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isNullable() {
        return isNullable;
    }
}
