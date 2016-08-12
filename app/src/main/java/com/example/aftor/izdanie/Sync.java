package com.example.aftor.izdanie;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Sync extends AsyncTask<String, Integer, String> {

    private static final String TAG = "myLogs";
    DataBase sqdb;
    ProgressDialog pd;
    String title, deviceId, serverUrl = "http://176.111.63.76:43440", baseName;
    private MainActivity.mDownloadCallback downloadCallback;
    private HttpURLConnection urlConnection;

    Sync(DataBase sqdb, MainActivity.mDownloadCallback downloadCallback) {

        this.sqdb = sqdb;
        this.downloadCallback = downloadCallback;

        baseName = "izdanie";
        deviceId = "00000000-72c1-a362-a49d-fe5d3af36214";
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //pd.setTitle(title);
        //pd.setMessage(tableName);
        //pd.incrementProgressBy(values[0]);
    }

    protected String doInBackground(String... params) {

        Log.d(TAG, "Sync start");

        String[] serverTables   = new String[1];
        serverTables[0]         = "TaskOut";

        String result = "0";

        title = "download";

        while (true) {

            try {
                //Log.d(TAG, "downloadJSON = " + serverUrl+"/"+baseName+"/hs/TaskOut/"+deviceId);
                result = downloadJSON(serverUrl + "/" + baseName + "/hs/TaskOut/" + deviceId);
                Log.d(TAG, "resultJson = " + result);

                writeData(result);
                click_upload();
                downloadCallback.onResult();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //pd.dismiss();
        //result = "2";
        //return result;
    }

    private String downloadJSON(String url) throws ClientProtocolException, IOException {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        InputStream inputStream = null;

        //pd.setMax(0);
        //pd.setProgress(0);
        //publishProgress(0);

        try {
            URL url1 = new URL(url);

            Log.d(TAG, "Connection to " + url);
            Log.d(TAG, "urlConnection = " + urlConnection);

            if (urlConnection == null) {
                Log.d(TAG, "New connection");
                urlConnection = (HttpURLConnection) url1.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            }

            Log.d(TAG, "After connection");

            final int response = urlConnection.getResponseCode();
            Log.d(TAG, "response = " + Integer.toString(response));

            inputStream = urlConnection.getInputStream();

            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            Log.d(TAG, "Read JSON");

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();

            Log.d(TAG, "Error http.");
        }
        urlConnection.disconnect();
        return resultJson;
    }

    public void click_upload() {

        JSONArray jsonArr = Order.getOrdersChange(sqdb);
        final JSONObject jsonOrder = new JSONObject();
        try {
            jsonOrder.put("item", jsonArr);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        //Thread t = new Thread() {
         //   public void run() {

         //       Looper.prepare();
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                try {
                    HttpPost post = new HttpPost(serverUrl+"/"+baseName+"/hs/TaskIn");
                    StringEntity se = new StringEntity(jsonOrder.toString(), HTTP.UTF_8);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);
                    if(response!=null){
                        //InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error order upload");
                }
         //       Looper.loop(); //Loop in the message queue
         //   }
        //};
        //t.start();
    }

    @Override
    protected void onPostExecute(String result) {
        //downloadCallback.onResult(result);
        //super.onPostExecute(result);

        //downloadCallback.onResult(result);
    }

    private String writeData(String result) {

        String role = "0";
        //ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
        //Map<String, String> m;

        JSONObject keys;
        String key;
        String value;
        String[] data;
        int index;
        int rows = 0;

        try {
            JSONObject dataJsonObj = new JSONObject(result);
            JSONArray arr = dataJsonObj.getJSONArray("items");
            ArrayList<String[]> arrData = new ArrayList<String[]>();

            //pd.setMax(trt.length());
            //pd.setProgress(0);

            for (int i = 0; i < arr.length(); i++, rows++) {

                JSONObject description = arr.getJSONObject(i);

                String nomber = description.getString("nomber");
                String name = description.getString("name");
                String zakazhcik = description.getString("zakazhcik");
                String status = description.getString("status");

                Log.d(TAG, "nomber = " + nomber);
                Log.d(TAG, "name = " + name);
                Log.d(TAG, "zakazhcik = " + zakazhcik);
                Log.d(TAG, "status = " + status);

                data = new String[4];

                data[0] = nomber;
                data[1] = name;
                data[2] = zakazhcik;
                data[3] = status;

                arrData.add(data);
            }

            /*for (int i = 0; i < arr.length(); i++, rows++) {

                keys = arr.getJSONObject(i);

                for(int j = 0; j<keys.names().length(); j++){
                    Log.v(TAG, "key = " + keys.names().getString(j) + " value = " + keys.get(keys.names().getString(j)));
                }

                data = new String[keys.length()];
                index = 0;

                Iterator<String> iter = keys.keys();
                while (iter.hasNext()) {

                    key = iter.next();
                    try {
                        value = keys.getString(key);
                        data[index++] = value;

                        Log.d(TAG, "key = " + key);
                        Log.d(TAG, "value = " + value);
                    } catch (JSONException e) {
                        Log.d(TAG, "ERROR JSON READING");
                    }
                }
                arrData.add(data);
            }*/

            //sqdb.clearTable(DataBaseTables.ORDERS);
            sqdb.clearTableVirtual();
            Log.d(TAG, "rows = " + rows);
            sqdb.addData1(arrData, DataBaseTables.ORDERS);

            /*String query = ""
                    +"INSERT INTO #table1 (id, guidd, TimeAdded, ExtraData) "
                    +"SELECT id, guidd, TimeAdded, ExtraData from #table2 "
                    +"EXCEPT "
                    +"SELECT id, guidd, TimeAdded, ExtraData from #table1";*/

            String query = "delete " +
                    "from vTable " +
                    "where number in " +
                    "(select number " +
                    "from orders)";

            sqdb.executeSQL(query);

            query = ""
                    +"INSERT INTO orders (number, name, client, status) "
                    +"SELECT number, name, client, status from vTable ";

            /*query = ""
                    +"INSERT INTO orders (number, name, client, status) "
                    +"SELECT number, name, client, status from vTable "
                    +"EXCEPT "
                    +"SELECT number, name, client, status from orders";*/

            //"select order";

           /* query = "insert into orders " +
                    "select * from vTable" +
                    "where number in " +
                    "(select number " +
                    "from vTable)";*/

            /*query = "insert into orders " +
                    "select * from vTable" +
                    "left join orders as orders " +
                    "on saloutH._id = saloutT.headId " +
                    "where number in " +
                    "(select number " +
                    "from vTable)";*/

            Log.d(TAG, query);
            sqdb.executeSQL(query);


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }

        return role;
    }

}
