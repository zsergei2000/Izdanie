package com.example.aftor.izdanie;

public class DataBaseTables {

    public static final int SETTINGS = 0, USERS = 1, ORDERS = 2, COUNT = 3;

    public static String[] getAllTables() {

        String[] tables = new String[COUNT];
        tables[0] = "settings";
        tables[1] = "users";
        tables[2] = "orders";

        return tables;
    }

    public static String getTable(int table) {

        String name = getTableName(table);

        String[][] fields = getFields(table);

        String tableStr = "create table " + name + " (";
        for (int j = 0; j < fields.length; j++) {
            tableStr = tableStr + fields[j][0] + " ";
            tableStr = tableStr + fields[j][1] + ", ";
        }
        tableStr = tableStr.substring(0, (tableStr.length() - 2));
        tableStr = tableStr + ");";

        return tableStr;
    }

    public static String getTableName(int table) {

        String[] tables = getAllTables();

        return tables[table];
    }

    public static String[][] getFields(int table) {

        String[][] fields = null;

        switch (table) {
            case SETTINGS:
                fields = getSettings();
                break;
            case USERS:
                fields = getUsers();
                break;
            case ORDERS:
                fields = getOrders();
                break;
        }
        return fields;
    }

    private static String[][] getSettings() {

        String[][] array = new String[2][2];

        array[0][0] = "_id integer primary key";
        array[0][1] = "autoincrement";
        array[1][0] = "user";
        array[1][1] = "text";

        return array;
    }

    private static String[][] getUsers() {

        String[][] array = new String[5][2];

        array[0][0] = "_id integer primary key";
        array[0][1] = "autoincrement";
        array[1][0] = "kod";
        array[1][1] = "text";
        array[2][0] = "name";
        array[2][1] = "text";
        array[3][0] = "post";
        array[3][1] = "text";
        array[4][0] = "pass";
        array[4][1] = "text";

        return array;
    }

    private static String[][] getOrders() {

        String[][] array = new String[5][2];

        array[0][0] = "_id";
        array[0][1] = "integer primary key autoincrement";
        array[1][0] = "number";
        array[1][1] = "text";
        array[2][0] = "name";
        array[2][1] = "text";
        array[3][0] = "client";
        array[3][1] = "text";
        array[4][0] = "status";
        array[4][1] = "text";

        return array;
    }

}
