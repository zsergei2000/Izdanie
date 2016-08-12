package com.example.aftor.izdanie;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
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

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    private static final String TAG = "myLogs";
    private LayoutInflater ltInflater;
    private LinearLayout EL_ITEMS;
    private FrameLayout EL_SYNC, El_USER;
    private TextView EL_USER_POST, EL_USER_FIO;
    private ArrayList<Order> arrOrders;
    private DataBase sqdb;
    private User user;
    Adapter scAdapter;
    private ListView EL_LIST;
    Sync sync;
    private String baseName = "izdanie", serverUrl = "http://176.111.63.76:43440";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqdb = new DataBase();
        sqdb.OpenDB(this);
        user = new User();
        sync = new Sync(sqdb, callback);

        ltInflater = getLayoutInflater();

        EL_ITEMS = (LinearLayout) findViewById(R.id.items);
        EL_SYNC = (FrameLayout) findViewById(R.id.sync);
        El_USER = (FrameLayout) findViewById(R.id.b_user);
        EL_USER_POST = (TextView) findViewById(R.id.user);
        EL_LIST = (ListView) findViewById(R.id.lvData);
        EL_USER_FIO = (TextView) findViewById(R.id.tv_user_fio);

        El_USER.setOnClickListener(userClick);
        EL_SYNC.setOnClickListener(syncClick);

        User.writeUsers(this, sqdb);
        //tSave.start();
        //setUser("1");
        setId();
    }

    View.OnClickListener userClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            View item = ltInflater.inflate(R.layout.calc1, null, false);
            final EditText input = (EditText) item.findViewById(R.id.calc_text);
            alert.setView(item);
            alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    String value = input.getText().toString();
                    if (value.equals("123321")) {
                        Intent intent = new Intent(MainActivity.this, Technical.class);
                        startActivity(intent);
                    } else {
                        setUser(value);
                    }
                }
            });
            alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {}
            });
            alert.show();
        }
    };

    public void calc_click(View v) {

        View view = (View) v.getParent().getParent();

        EditText input = (EditText) view.findViewById(R.id.calc_text);
        String inValue = input.getText().toString();

        String textButton = ((TextView) v).getText().toString();

        input.setText(inValue + textButton);
    }

    private void setUser(String password) {

        user.setUser(sqdb, password);
        if (user.set) {

            EL_USER_POST.setText(user.post);
            EL_USER_FIO.setText(user.name);
            setAdapter();
            AsyncTask.Status status = sync.getStatus();
            if (status==AsyncTask.Status.RUNNING) {
                Log.d(TAG, "status = RUNNING");
            } else if (status==AsyncTask.Status.PENDING) {
                Log.d(TAG, "status = PENDING");
            } else if (status==AsyncTask.Status.FINISHED) {
                Log.d(TAG, "status = FINISHED");
            }
            if (status!=AsyncTask.Status.RUNNING) {
                Log.d(TAG, "status NEW = RUNNING");
                sync.execute();
            }
            //tSave.start();
        }
    }

    Thread tSave = new Thread(new Runnable() {
        public void run() {
            //while (true) {
                //try {
                   // TimeUnit.SECONDS.sleep(10);
                    //click_upload();
                    //Sync sync = new Sync(sqdb, callback);
                    //sync.execute();
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
            //}
        }
    });

    private void setAdapter() {

        String      name    = DataBaseTables.getTableName(DataBaseTables.ORDERS);
        String[][]  fields  = DataBaseTables.getFields(DataBaseTables.ORDERS);

        String[] from = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            from[i] = fields[i][0];
        }
        int[] to = new int[] { R.id._id, R.id.number, R.id.name, R.id.client, R.id.status };

        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);

        /*animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 50.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(10);
        set.addAnimation(animation); just comment if you don't want :) */
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        EL_LIST.setLayoutAnimation(controller);

        scAdapter = new Adapter(this, R.layout.item_menu, null, from, to, 0, user);

        EL_LIST.setAdapter(scAdapter);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, sqdb);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    interface mDownloadCallback {
        void onResult();
    }

    public void onStatusClick(View view) {

        View parent = (View) view.getParent();

        final String _id = ((TextView) parent.findViewById(R.id._id)).getText().toString();
        String kod = ((TextView) parent.findViewById(R.id.kod)).getText().toString();

        Log.d(TAG, "_id = " + _id);
        Log.d(TAG, "kod = " + kod);

        //animate(view);

        View viewOld = null;
        if (parent.findViewById(R.id.status1).isSelected()) {
            viewOld = parent.findViewById(R.id.status1);
        } else if (parent.findViewById(R.id.status2).isSelected()) {
            viewOld = parent.findViewById(R.id.status2);
        } else if (parent.findViewById(R.id.status3).isSelected()) {
            viewOld = parent.findViewById(R.id.status3);
        } else if (parent.findViewById(R.id.status4).isSelected()) {
            viewOld = parent.findViewById(R.id.status4);
        }

        if (view.getId()==viewOld.getId()) {
            return;
        }

        animateNewStatus(view);
        if (viewOld!=null) {
            animateOldStatus(viewOld);
        }

       // view.setSelected(true);
        final String status;

        switch (view.getId()) {
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
        Log.d(TAG, "status = " + status);

        Thread tSave = new Thread(new Runnable() {
            public void run() {
                sqdb.updateData(_id, status);
                //getSupportLoaderManager().getLoader(0).forceLoad();
            }
        });
        tSave.start();
    }

    private void animateNewStatus(final View view) {

        int width = view.getMeasuredWidth();

        Flip3dAnimation anim = new Flip3dAnimation(0, 90, width/2.0f, 0);
        anim.setDuration(300);
        //anim3.setStartOffset(300);
        //anim.setInterpolator(new BounceInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                animateNewStatus1(view);
            }
        });
        view.startAnimation(anim);
    }

    private void animateNewStatus1(final View view) {

        view.setSelected(true);

        int width = view.getMeasuredWidth();
        //Flip3dAnimation anim = new Flip3dAnimation(-90, 0, width/2.0f, height/2.0f);
        Flip3dAnimation anim = new Flip3dAnimation(-90, 0, width/2.0f, 0);
        //Flip3dAnimation anim = new Flip3dAnimation(0, 100, 0, 0);
        anim.setDuration(300);
        //anim3.setStartOffset(300);
        //anim.setInterpolator(new BounceInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                if (view.getId()==R.id.status4) {
                    view.setSelected(false);
                }
                getSupportLoaderManager().getLoader(0).forceLoad();
            }
        });
        view.startAnimation(anim);
    }

    private void animateOldStatus(final View view) {

        int width = view.getMeasuredWidth();

        Flip3dAnimation anim = new Flip3dAnimation(0, 90, width/2.0f, 0);
        anim.setDuration(300);
        //anim3.setStartOffset(300);
        //anim.setInterpolator(new BounceInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                animateOldStatus1(view);
            }
        });
        view.startAnimation(anim);
    }

    private void animateOldStatus1(View view) {

        view.setSelected(false);

        int width = view.getMeasuredWidth();

        //Flip3dAnimation anim = new Flip3dAnimation(-90, 0, width/2.0f, height/2.0f);
        Flip3dAnimation anim = new Flip3dAnimation(-90, 0, width/2.0f, 0);
        //Flip3dAnimation anim = new Flip3dAnimation(0, 100, 0, 0);
        anim.setDuration(300);
        //anim3.setStartOffset(300);
        //anim.setInterpolator(new BounceInterpolator());
        view.startAnimation(anim);
    }

    private void animate(View view) {

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotationY", 0.0f, 360f);
        animation.setDuration(2400);
        animation.setupEndValues();
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {}
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        animation.start();
    }

    mDownloadCallback callback = new mDownloadCallback() {
        public void onResult() {
            //addItems();
            getSupportLoaderManager().getLoader(0).forceLoad();
        }
    };

    View.OnClickListener syncClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Sync sync = new Sync(sqdb, callback);
            sync.execute();
        }
    };

    private void setId() {

        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        ((TextView) findViewById(R.id.device_id)).setText("ИД: "+deviceId);
    }

    static class MyCursorLoader extends CursorLoader {

        DataBase sqdb;

        public MyCursorLoader(Context context, DataBase sqdb) {
            super(context);
            this.sqdb = sqdb;
        }
        @Override
        public Cursor loadInBackground() {

            Cursor cursor = Order.getOrdersCursor(sqdb);
            /*try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            return cursor;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sync.cancel(true);
        sqdb.closeDB();
    }

}
