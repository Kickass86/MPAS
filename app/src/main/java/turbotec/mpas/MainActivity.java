package turbotec.mpas;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

//import android.graphics.Color;

//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;


public class MainActivity extends AppCompatActivity {

    private static DatabaseHandler db;
    //    private static int numrun = 0;
    private static List<MessageObject> MESSAGES;
    //    public static SQLiteDatabase database;
//    private int Message_Number = 0;
    //    private SharedPreferenceHandler sp;
    private final SharedPreferenceHandler share;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            boolean Titles = false;
//            String[] Userdata = new String[]{share.GetUsername(), share.GetPassword(), share.GetDeviceID()};
//
//        Application ap = this. getApplication();
//
//            NetworkAsyncTask task = new NetworkAsyncTask(context, isAppForeground(context));
//            try {
//                task.execute(Userdata).get();
            if (share.GetStatus().equals(getString(R.string.OK))) {
//                GetMessagesfromDB();
//                ShowMessages();
                GetMessages oo = new GetMessages();
                try {
                    oo.execute("").get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
//            } catch (ExecutionException | InterruptedException ei) {
//                ei.printStackTrace();
//            }
            Log.i("is this ", "BroadcastReceiver");

        }
    };
    private PendingIntent pendingIntent;
    //    BroadcastReceiver NotifyReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            Bundle extras = intent.getExtras();
//
//
//            NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(context)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentTitle(extras.getString("Title"))
//                            .setContentText("Hello ");
//
//            NotificationManager mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            Log.i("Notify","is running");
//// mId allows you to update the notification later on.
//            mNotificationManager.notify(extras.getInt("ID"), mBuilder.build());
//
//        }
//    };
    private EditText UsernameView;
    private EditText PasswordView;


    public MainActivity() {

        MESSAGES = new ArrayList<>();
        db = DatabaseHandler.getInstance(this);
        share = SharedPreferenceHandler.getInstance(this);


    }
    //    private String Name;

    @SuppressWarnings("deprecation")
    private static String getUniquePsuedoID() {

        String m_szDevIDShort;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            m_szDevIDShort = "46cd" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.SUPPORTED_ABIS[0].length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        } else {
            m_szDevIDShort = "46cd" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        }


        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        if (share.GetStatus().equals(getString(R.string.OK))) {
//            GetMessagesfromDB();
//            ShowMessages();
//        }
//    }

//    private boolean isAppForeground(Context mContext) {
//
//        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
//        if (!tasks.isEmpty()) {
//            ComponentName topActivity = tasks.get(0).topActivity;
//            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
//                return false;
//            }
//        }
//
//        return true;
//    }


//    private static void GetMessagesfromDB() {
//
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                try {
//
//
////        database =  db.getReadableDatabase(); execSQL("INSERT INTO Messages VALUES(1000,'IDUSER','TITLE23','BODY','2017-02-21',0); ")
//        MESSAGES = new ArrayList<>();
//        SQLiteDatabase database = db.getWritableDatabase();
//        Cursor cursor = database.rawQuery("SELECT * " +
//                "FROM Messages ORDER BY Critical DESC, MessageID DESC;", null);
//
//        // looping through all rows and adding to list
//        try {
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    do {
//                        MessageObject MObj = new MessageObject();
//                        MObj.setMessageID(cursor.getInt(0));
//                        MObj.setMessageTitle(cursor.getString(1));
//                        MObj.setMessageBody(cursor.getString(2));
//                        MObj.setInsertDate(cursor.getString(3));
//                        MObj.setCritical("1".equals(cursor.getString(4)));
//                        MObj.setSeen("1".equals(cursor.getString(5)));
//
//                        MESSAGES.add(MObj);
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//            }
//
////                    } catch (Exception e) {
////                        e.getStackTrace();
////                    }
//        } catch (Exception e) {
//            e.printStackTrace();
//                }
////            }
////        }).start();
//
//
//    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
//        unregisterReceiver(NotifyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        nMgr.cancelAll();
        if (share.GetStatus().equals(getString(R.string.OK))) {
//            GetMessagesfromDB();
//            ShowMessages();
            GetMessages oo = new GetMessages();
            try {
                oo.execute("").get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waiting_layout);
        registerReceiver(broadcastReceiver, new IntentFilter("Alarm fire"));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

//        registerReceiver(NotifyReceiver, new IntentFilter("Notification fire"));
//        if (!(share.GetDeviceID().equals(getString(R.string.defaultValue))) && (!share.GetUsername().equals(getString(R.string.defaultValue)))
//                && (!share.GetPassword().equals(getString(R.string.defaultValue))) && (share.GetActivation().equals(getString(R.string.Active)))) {
        String state = share.GetStatus();

        if (state.equals(getString(R.string.OK))) {
            setContentView(R.layout.activity_main_logged_in);
//            String result = "OK";
            String[] UserDetails = {share.GetUsername(), share.GetPassword(), share.GetDeviceID()};
            NetworkAsyncTask task = new NetworkAsyncTask(this);
            try {
                task.execute(UserDetails).get();
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
            if (share.GetStatus().equals(getString(R.string.OK))) {
                //set Repeating Alarm
                setContentView(R.layout.messages_layout);

                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.setAction("Alarm");
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                long interval = INTERVAL_FIFTEEN_MINUTES;
                int interval = 60000;

//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
                Log.i("Alarm", "Set");
                if (share.GetActivation().equals(getString(R.string.Active))) {
//                    GetMessagesfromDB();
//                    ShowMessages();
                    GetMessages oo = new GetMessages();
                    try {
                        oo.execute("").get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

//            if (!(share.GetDeviceID().equals(getString(R.string.defaultValue))) && (!share.GetUsername().equals(getString(R.string.defaultValue)))
//                    && (!share.GetPassword().equals(getString(R.string.defaultValue))) && (share.GetActivation().equals(getString(R.string.NotActive)))) {
        else if (state.equals(getString(R.string.Wait))) {
            setContentView(R.layout.wait_for_activation_layout);
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            alarmIntent.setAction("Alarm");
            pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                long interval = INTERVAL_FIFTEEN_MINUTES;
            int interval = 60000;

//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
            Log.i("Alarm", "Set");
            if (share.GetActivation().equals(getString(R.string.Active))) {
//                GetMessagesfromDB();
//                ShowMessages();
                GetMessages oo = new GetMessages();
                try {
                    oo.execute("").get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else {
            setContentView(R.layout.login_layout);


            new Thread(new Runnable() {
                @Override
                public void run() {

                    UsernameView = (EditText) findViewById(R.id.editText2);
                    PasswordView = (EditText) findViewById(R.id.editText);
                    Button registerButton = (Button) findViewById(R.id.button);
                    PasswordView.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                                String Username = UsernameView.getText().toString();
                                String Password = PasswordView.getText().toString();
                                String DeviceID = getUniquePsuedoID();
                                attemptLogin(Username, Password, DeviceID);
                            }
                            return false;
                        }
                    });


                    registerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String Username = UsernameView.getText().toString();
                            String Password = PasswordView.getText().toString();
                            String DeviceID = getUniquePsuedoID();
                            share.SaveLoginDetails(Username, Password);
                            share.SaveDeviceID(DeviceID);

                            attemptLogin(Username, Password, DeviceID);
//                Log.i("Successful Login ", "Welcome " + Name);
                        }
                    });
                }
            }).start();
        }





    }

    private void attemptLogin(String username, String password, String DeviceID) {

//        String result = getString(R.string.Invalid);

        UsernameView.setError(null);
        PasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            PasswordView.setError(getString(R.string.error_invalid_password));
            focusView = PasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            UsernameView.setError(getString(R.string.error_field_required));
            focusView = UsernameView;
            cancel = true;
        }

        String s = "";
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // save data in local shared preferences
            String[] Userdata = {username, password, DeviceID};
            NetworkAsyncTask task = new NetworkAsyncTask(this);
            try {
                s = task.execute(Userdata).get();
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
            String state = share.GetStatus();
            if (state.equals(getString(R.string.OK))) {
                //set Repeating Alarm
//                setContentView(R.layout.activity_main_logged_in);

                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.setAction("Alarm");
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                long interval = INTERVAL_FIFTEEN_MINUTES;
                int interval = 60000;

//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval, interval, pendingIntent);
                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
                Log.i("Alarm", "Set");
                if (share.GetActivation().equals(getString(R.string.Active))) {
//                    GetMessagesfromDB();
//                    ShowMessages();
                    GetMessages oo = new GetMessages();
                    try {
                        oo.execute("").get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } else if (state.equals(getString(R.string.Wait))) {

                setContentView(R.layout.wait_for_activation_layout);
                share.SaveDeviceID(DeviceID);
                share.SaveLoginDetails(username, password);
//                share.SaveActivation(getString(R.string.NotActive));


                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.setAction("Alarm");
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                long interval = INTERVAL_FIFTEEN_MINUTES;
                int interval = 60000;

//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval, interval, pendingIntent);
                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
                Log.i("Alarm", "Set");

//                ShowMessages();


            } else {
                //Error On LogIn
                Log.w("ERROR", "Wrong Information");
                Toast.makeText(this.getApplicationContext(),
                        "Something is wrong, check your connection and username/password :" + s, Toast.LENGTH_LONG).show();
                final Intent intent = getIntent();
//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
                        try {
                            Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                            MainActivity.this.finish();
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    }
//                };
//                thread.start();
            }


        }


    }

    private void ShowMessages() {

        setContentView(R.layout.messages_layout);
        ListView lv = (ListView) findViewById(R.id.list1);


        List<String> Mlist = new ArrayList<>(); //Messages List
        List<String> Tlist = new ArrayList<>(); //Title List
//        List<String> Dlist = new ArrayList<>(); //Date List
        List<Boolean> SList = new ArrayList<>(); //is Seen
        List<Integer> IList = new ArrayList<>(); //Message ID
        List<Boolean> CList = new ArrayList<>(); //Critical
        List<Boolean> SSList = new ArrayList<>(); //SendSeen

//        for (int i = Message_Number; i < MESSAGES.size(); i++) {
        for (int i = 0; i < MESSAGES.size(); i++) {
            Mlist.add(MESSAGES.get(i).getMessageBody());
            Tlist.add(MESSAGES.get(i).getMessageTitle());
//            Dlist.add(MESSAGES.get(i).getInsertDate());
            SList.add(MESSAGES.get(i).isSeen());
            IList.add(MESSAGES.get(i).getMessageID());
            CList.add(MESSAGES.get(i).getCritical());
            SSList.add(MESSAGES.get(i).isSendSeen());
        }
//        Message_Number = MESSAGES.size();
//        if (MESSAGES.isEmpty()) {
            TextView emptyText = (TextView) findViewById(android.R.id.empty);
            lv.setEmptyView(emptyText);
//        }


//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                MainActivity.this,
//                android.R.layout.simple_list_item_1,
//                Tlist);

        lv.setAdapter(new CustomAdapter(this, Tlist, Mlist, SList, IList, CList, SSList));

//        lv.setAdapter(arrayAdapter);

    }

    private class GetMessages extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {


            MESSAGES = new ArrayList<>();
            SQLiteDatabase database = db.getWritableDatabase();
            Cursor cursor = database.rawQuery("SELECT * " +
                    "FROM Messages ORDER BY Critical DESC, MessageID DESC;", null);

            // looping through all rows and adding to list
            try {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            MessageObject MObj = new MessageObject();
                            MObj.setMessageID(cursor.getInt(0));
                            MObj.setMessageTitle(cursor.getString(1));
                            MObj.setMessageBody(cursor.getString(2));
                            MObj.setInsertDate(cursor.getString(3));
                            MObj.setCritical("1".equals(cursor.getString(4)));
                            MObj.setSeen("1".equals(cursor.getString(5)));
                            MObj.setSendDelivered("1".equals(cursor.getString(6)));
                            MObj.setSendSeen("1".equals(cursor.getString(7)));

                            MESSAGES.add(MObj);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }

//                    } catch (Exception e) {
//                        e.getStackTrace();
//                    }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ShowMessages();
        }
    }

    public class CustomAdapter extends BaseAdapter {
        final Context context;
        List<String> Titles;
        List<String> Bodies;
        List<Boolean> isSeen;
        List<Integer> IList;
        List<Boolean> CList;
        List<Boolean> SSList;
        private LayoutInflater inflater = null;

        public CustomAdapter(MainActivity mainActivity, List<String> MessagesTitle, List<String> MessagesBody, List<Boolean> isSeen, List<Integer> IList, List<Boolean> CList, List<Boolean> SSList) {


            context = mainActivity;
            Titles = MessagesTitle;
            Bodies = MessagesBody;
            this.isSeen = isSeen;
            this.IList = IList;
            this.CList = CList;
            this.SSList = SSList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Titles.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();
            final View rowView;
            rowView = inflater.inflate(R.layout.list_row_layout, null);
            holder.tv1 = (TextView) rowView.findViewById(R.id.title);
            holder.tv2 = (TextView) rowView.findViewById(R.id.body);
            holder.iv = (ImageView) rowView.findViewById(R.id.state);
            holder.i2 = (ImageView) rowView.findViewById(R.id.Critical);
            holder.tv1.setText(Titles.get(position));
            holder.tv2.setText(Bodies.get(position));
            if (isSeen.get(position))
                holder.iv.setImageResource(R.mipmap.seen);
            else
                holder.iv.setImageResource(R.mipmap.delivered);

            if (CList.get(position)) {
                holder.i2.setImageResource(R.mipmap.critical);
            }

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    Toast.makeText(context, "You Clicked " + Titles.get(position), Toast.LENGTH_LONG).show();
                    rowView.setBackgroundColor(context.getResources().getColor(R.color.SelectColor1));
                    Intent showActivity = new Intent(MainActivity.this, Message_Detail_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.Title), Titles.get(position));
                    bundle.putString(getString(R.string.Body), Bodies.get(position));
                    bundle.putBoolean(getString(R.string.Critical), CList.get(position));
                    bundle.putBoolean(getString(R.string.SendSeen), SSList.get(position));
                    bundle.putInt(getString(R.string.ID), IList.get(position));
                    bundle.putBoolean(getString(R.string.Seen), isSeen.get(position));
                    showActivity.putExtras(bundle);
                    startActivity(showActivity);



                }
            });
            return rowView;
        }

        private class Holder {
            TextView tv1;
            TextView tv2;
            ImageView iv;
            ImageView i2;

        }

    }


}
