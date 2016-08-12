package com.example.aftor.izdanie;

import android.database.Cursor;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Order {

    public static final int STATUS_1 = 0, STATUS_2 = 1, STATUS_3 = 2, STATUS_4 = 3;
    private static final String TAG = "myLogs";
    public String _id, number, name, client;
    public int status;
    public View status1, status2, status3, status4;

    public static ArrayList<Order> getOrders(DataBase sqdb) {

        ArrayList<Order> array = new ArrayList<>();
        Order order;

        String query = "select _id, number, name, client, status "
                + "from orders as orders";

        Cursor c = sqdb.getSQLData(query, null);

        Log.d(TAG, "orders - " + c.getCount());

        if (c!=null) {
            if (c.moveToFirst()) {
                do {

                    order = new Order();

                    order._id	    = c.getString(0);
                    order.number	= c.getString(1);
                    order.name      = c.getString(2);
                    order.client	= c.getString(3);
                    order.status	= c.getInt(4);

                    array.add(order);

                } while (c.moveToNext());
            }
        }
        c.close();

        return  array;
    }

    public static Cursor getOrdersCursor(DataBase sqdb) {

        String query = "select _id, number, name, client, status "
                + "from orders as orders "
                + "where status <> 4";

        Cursor c = sqdb.getSQLData(query, null);

        Log.d(TAG, "orders - " + c.getCount());
        return  c;
    }

    public static JSONArray getOrdersChange(DataBase sqdb) {

        //Calendar ccc = Calendar.getInstance();
        //String дата = formatter.format(ccc.getTime());

        String query = "select number, name, client, status "
                + "from orders as orders "
                + "where orders.status = 4";

        Cursor c = sqdb.getSQLData(query, null);

        Log.d(TAG, "upload orders - " + c.getCount());

        JSONArray jsonArr = new JSONArray();

        if (c!=null) {
            if (c.moveToFirst()) {
                do {
                    JSONObject pnObj = new JSONObject();
                    try {

                        pnObj.put("number",		c.getString(0));
                        pnObj.put("name",		c.getString(1));
                        pnObj.put("client",	    c.getString(2));
                        pnObj.put("status",	    c.getString(3));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArr.put(pnObj);

                } while (c.moveToNext());
            }
        }
        c.close();

        return jsonArr;
    }

}
