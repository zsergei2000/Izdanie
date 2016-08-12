package com.example.aftor.izdanie;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aftor on 09.08.2016.
 */
public class Tresh {

    /*private void addItems() {

        arrOrders = Order.getOrders(sqdb);
        EL_ITEMS.removeAllViews();
        int i = 0;
        for (Order order : arrOrders) {

            View item = ltInflater.inflate(R.layout.item_menu, EL_ITEMS, false);

            ((TextView) item.findViewById(R.id.kod)).setText(Integer.toString(i++));
            ((TextView) item.findViewById(R.id.number)).setText(order.number);
            ((TextView) item.findViewById(R.id.client)).setText(order.client);
            ((TextView) item.findViewById(R.id.name)).setText(order.name);

            FrameLayout status1 = (FrameLayout) item.findViewById(R.id.status1);
            FrameLayout status2 = (FrameLayout) item.findViewById(R.id.status2);
            FrameLayout status3 = (FrameLayout) item.findViewById(R.id.status3);
            FrameLayout status4 = (FrameLayout) item.findViewById(R.id.status4);

            status1.setOnClickListener(statusClick);
            status2.setOnClickListener(statusClick);
            status3.setOnClickListener(statusClick);
            status4.setOnClickListener(statusClick);

            order.status1 = status1;
            order.status2 = status2;
            order.status3 = status3;
            order.status4 = status4;

            order.status1.setSelected(true);

            EL_ITEMS.addView(item);
        }
    }*/

     /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    /*private void writeOrders() {

        arrOrders = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            Order order = new Order();
            order.number = Integer.toString(i);
            order.client = "client #" + Integer.toString(i);
            order.name = "order #" + Integer.toString(i);

            arrOrders.add(order);
        }
    }*/

    /*View.OnClickListener statusClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (user.kod.equals("3")&(view.getId()==R.id.status3|view.getId()==R.id.status4)) {
                return;
            }

            View parent = (View) view.getParent();

            final String _id = ((TextView) parent.findViewById(R.id._id)).getText().toString();
            String kod = ((TextView) parent.findViewById(R.id.kod)).getText().toString();
            //String status = ((TextView) parent.findViewById(R.id.status)).getText().toString();

            Order order = arrOrders.get(Integer.parseInt(kod));

            ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotationY", 0.0f, 360f);
            animation.setDuration(2400);
            animation.setRepeatCount(ObjectAnimator.INFINITE);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();

            order.status1.setSelected(false);
            order.status2.setSelected(false);
            order.status3.setSelected(false);
            order.status4.setSelected(false);

            view.setSelected(true);

            final String status;

            switch (view.getId()) {
                case R.id.status1:
                    status = "1";
                    break;
                case R.id.status2:
                    status = "2";
                    break;
                case R.id.status3:
                    status = "3";
                    break;
                case R.id.status4:
                    status = "4";
                    break;
                default:
                    status = "1";
            }

            Log.d(TAG, "_id = " + _id);
            Log.d(TAG, "status = " + status);

            Thread tSave = new Thread(new Runnable() {
                public void run() {
                    sqdb.updateData(_id, status);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            });
            tSave.start();
        }
    };*/

}
