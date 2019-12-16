package com.gebeya.apps.framework.data.column;

public final class Column {

    public String name;
    public String type;
    public boolean primary;
    public boolean nullable;

    private Column(String name) {
        this.name = name;
        this.type = ColumnTypes.TEXT;
        this.primary = false;
        this.nullable = false;
    }

    public static Column create(String name) {
        return new Column(name);
    }

    public static Column create(String name, String type) {
        Column column = new Column(name);
        column.type = type;
        return column;
    }

    public static Column create(String name, String type, boolean nullable) {
        Column column = new Column(name);
        column.type = type;
        column.nullable = nullable;
        return column;
    }

    public static Column createPrimary(String name) {
        Column column = new Column(name);
        column.type = ColumnTypes.TEXT;
        column.primary = true;
        return column;
    }

    public static Column createPrimary(String name, String type) {
        Column column = new Column(name);
        column.type = type;
        column.primary = true;
        return column;
    }

    public StringBuilder toSql() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(" ");
        builder.append(type).append(" ");
        if (primary) {
            builder.append("PRIMARY KEY");
        } else {
            builder.append(nullable ? "NULL" : "NOT NULL");
        }
        return builder;
    }

    @Override
    public String toString() {
        return name;
    }
}
