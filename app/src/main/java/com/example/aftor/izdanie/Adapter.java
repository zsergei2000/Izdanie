package com.example.aftor.izdanie;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Adapter extends SimpleCursorAdapter {

    private Context context;
    private User user;

    public Adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, User user) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.user = user;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        final String status = ((TextView) view.findViewById(R.id.status)).getText().toString();

        Log.d("myLogs", "status = " + status);
        //Log.d("myLogs", "bindView");

        ((FrameLayout) view.findViewById(R.id.status2)).setSelected(false);
        ((FrameLayout) view.findViewById(R.id.status3)).setSelected(false);
        ((FrameLayout) view.findViewById(R.id.status4)).setSelected(false);
        ((FrameLayout) view.findViewById(R.id.status1)).setSelected(false);

        if (status.equals("2")) {
            ((FrameLayout) view.findViewById(R.id.status2)).setSelected(true);
        } else if (status.equals("3")) {
            ((FrameLayout) view.findViewById(R.id.status3)).setSelected(true);
        } else if (status.equals("4")) {
            ((FrameLayout) view.findViewById(R.id.status4)).setSelected(true);
        } else {
            ((FrameLayout) view.findViewById(R.id.status1)).setSelected(true);
        }

        if (user.kod.equals("1")) {
            ((TextView) view.findViewById(R.id.text1)).setText(context.getString(R.string.item_status_1));
            ((TextView) view.findViewById(R.id.text2)).setText(context.getString(R.string.item_status_2));
            ((TextView) view.findViewById(R.id.text3)).setText(context.getString(R.string.item_status_3));
            ((TextView) view.findViewById(R.id.text4)).setText(context.getString(R.string.item_status_4));
        } else if (user.kod.equals("2")) {
            ((TextView) view.findViewById(R.id.text1)).setText(context.getString(R.string.item_status_1));
            ((TextView) view.findViewById(R.id.text2)).setText(context.getString(R.string.item_status_2));
            ((TextView) view.findViewById(R.id.text3)).setText(context.getString(R.string.item_status_5));
            ((TextView) view.findViewById(R.id.text4)).setText(context.getString(R.string.item_status_6));
        } else if (user.kod.equals("3")) {
            ((TextView) view.findViewById(R.id.text1)).setText(context.getString(R.string.item_status_7));
            ((TextView) view.findViewById(R.id.text2)).setText(context.getString(R.string.item_status_8));
            ((TextView) view.findViewById(R.id.text3)).setText("");
            ((TextView) view.findViewById(R.id.text4)).setText("");
        }

    }

}
