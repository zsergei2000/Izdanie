package com.example.aftor.izdanie;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class User {

    private static final String TAG = "myLogs";
    public String kod, name, post;
    public boolean set = false;

    public void setUser(DataBase sqdb, String password) {

        set = false;
        Log.d(TAG, "password = " + password);

        String query = "select kod, name, post, pass "
                + "from users as users "
                + "where pass = ?";

        //Cursor c = null;
        //c = sqdb.sqdb.rawQuery(query, null);

        Cursor c = sqdb.getSQLData(query, new String[]{ password });

        if (c!=null) {
            if (c.moveToFirst()) {
                do {

                    set = true;

                    kod			= c.getString(0);
                    name		= c.getString(1);
                    post		= c.getString(2);
                    String pass	= c.getString(3);

                    Log.d(TAG, "user kod = " + kod + ", name = " + name + ", post = " + post + ", pass = " + pass);

                } while (c.moveToNext());
            }
        }
        c.close();
    }

    public static void writeUsers(Context context, DataBase sqdb) {

        sqdb.clearTable(DataBaseTables.USERS);

        ArrayList<String[]> arrUsers = new ArrayList<String[]>();

        String[] data = new String[4];
        data[0] = "1";
        //data[1] = "Carmack";
        data[1] = context.getString(R.string.user1);
        data[2] = context.getString(R.string.post1);
        data[3] = "1";
        arrUsers.add(data);

        data = new String[4];
        data[0] = "2";
        //data[1] = "Torvalds";
        data[1] = context.getString(R.string.user2);
        data[2] = context.getString(R.string.post2);
        data[3] = "2";
        arrUsers.add(data);

        data = new String[4];
        data[0] = "3";
        //data[1] = "Gates";
        data[1] = context.getString(R.string.user3);
        data[2] = context.getString(R.string.post3);
        data[3] = "3";
        arrUsers.add(data);

        sqdb.addData(arrUsers, DataBaseTables.USERS);
    }

}
