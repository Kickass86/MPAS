package turbotec.mpas;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import android.widget.Toast;

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
    List<String> Mlist = new ArrayList<>(); //Messages List
    List<String> Tlist = new ArrayList<>(); //Title List
    //        List<String> Dlist = new ArrayList<>(); //Date List
    List<Boolean> SList = new ArrayList<>(); //is Seen
    List<Integer> IList = new ArrayList<>(); //Message ID
    List<Boolean> CList = new ArrayList<>(); //Critical
    List<Boolean> SSList = new ArrayList<>(); //SendSeen
    private Menu mMenu;
    private boolean isSelected = false;
    private CustomAdapter adapt;
    private int interval = 60000;
    private String Username;
    private String Password;
    private String DeviceID;
    private boolean first = false;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            if (action.equals("Alarm fire")) {
                /// /            if (share.GetStatus().equals(getString(R.string.OK)))
//            {
//                setContentView(R.layout.messages_layout);
//                share.SaveLoginDetails(Username, Password);
//                share.SaveDeviceID(DeviceID);
//            }
                boolean b = intent.getBooleanExtra("New", false);
                if (b) {
                    UpdateUI(share.GetStatus());
//                GetMessages oo = new GetMessages(); // TODO
//                oo.execute("");
                }

                if ((first) & (share.GetStatus().equals("OK"))) {
                    GetMessagesfromDB();
                    ShowMessages();
                    first = false;
                }


//            if (share.GetStatus().equals(getString(R.string.OK))) {
////                GetMessagesfromDB();
////                ShowMessages();
//                GetMessages oo = new GetMessages();
//                oo.execute("");
//            }

                Log.i("is this ", "BroadcastReceiver");
            }

        }
    };
    //    private static String DeviceID;
//    private static String username;
//    private static String password;
    private PendingIntent pendingIntent;
    //    BroadcastReceiver NotifyReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context menu, Intent intent) {
//
//            Bundle extras = intent.getExtras();
//
//
//            NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(menu)
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
        first = false;


    }

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
    //    private String Name;

    private static void GetMessagesfromDB() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {


//        database =  db.getReadableDatabase(); execSQL("INSERT INTO Messages VALUES(1000,'IDUSER','TITLE23','BODY','2017-02-21',0); ")
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
//            }
//        }).start();


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

    @Override
    protected void onDestroy() {

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
//        unregisterReceiver(NotifyReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
//        menu.findItem(R.id.menu_read).setVisible(false);
//        menu.findItem(R.id.menu_delete).setVisible(false);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (share.GetStatus().equals(getString(R.string.OK)) & first) {
////            GetMessagesfromDB();
////            ShowMessages();
//            GetMessages oo = new GetMessages();
//            oo.execute("");
//        }
//        if (first) {
//            UpdateUI(share.GetStatus());
//        }
        if (mMenu != null) {
            mMenu.clear();
        }
        isSelected = false;
        if (share.GetStatus().equals("OK")) {
            GetMessagesfromDB();
            ShowMessages();
        }
//        adapt.notifyDataSetChanged();
//        GetMessages oo = new GetMessages();
//        oo.execute("");

//        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        nMgr.cancelAll();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.waiting_layout);
        registerReceiver(broadcastReceiver, new IntentFilter("Alarm fire"));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

//        String[] UserDetails = {share.GetUsername(), share.GetPassword(), share.GetDeviceID()};
//        NetworkAsyncTask task = new NetworkAsyncTask(this);
//        task.execute(UserDetails);
        first = false;
        isSelected = false;

        String state = share.GetStatus();

        State st = State.valueOf(state);

        switch (st) {
            case OK: {

                setContentView(R.layout.messages_layout);

                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.setAction("Alarm");
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                long interval = INTERVAL_FIFTEEN_MINUTES;
//                    int interval = 60000;

//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
//                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
                Log.i("Alarm", "Set");
//                    if (share.GetActivation().equals(getString(R.string.Active))) {
////                    GetMessagesfromDB();
////                    ShowMessages();
                GetMessagesfromDB();
                ShowMessages();
//                GetMessages oo = new GetMessages();
//                oo.execute("");
//                UpdateUI();
//                    }

                break;
            }
            case Wait: {
                setContentView(R.layout.wait_for_activation_layout);
                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.setAction("Alarm");
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                long interval = INTERVAL_FIFTEEN_MINUTES;
//                int interval = 60000;

//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
//            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
                Log.i("Alarm", "Set");
//                if (share.GetActivation().equals(getString(R.string.Active))) {
////                GetMessagesfromDB();
////                ShowMessages();
//                    GetMessages oo = new GetMessages();
//                    oo.execute("");
//                }
                break;
            }
            case Invalid:
            case Not_Saved:
            default: {
                setContentView(R.layout.login_layout);


//            new Thread(new Runnable() {
//                @Override
//                public void run() {

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
                            share.SaveLoginDetails(Username, Password);
                            share.SaveDeviceID(DeviceID);

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


        }


//        if (state.equals(getString(R.string.OK))) {
//            setContentView(R.layout.activity_main_logged_in);
////            String result = "OK";
//            String[] UserDetails = {share.GetUsername(), share.GetPassword(), share.GetDeviceID()};
//            NetworkAsyncTask task = new NetworkAsyncTask(this);
//            task.execute(UserDetails);
//            if (share.GetStatus().equals(getString(R.string.OK))) {
//                //set Repeating Alarm
//                setContentView(R.layout.messages_layout);
//
//                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//                alarmIntent.setAction("Alarm");
//                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////                long interval = INTERVAL_FIFTEEN_MINUTES;
//                int interval = 60000;
//
////                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
////                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//                Log.i("Alarm", "Set");
//                if (share.GetActivation().equals(getString(R.string.Active))) {
////                    GetMessagesfromDB();
////                    ShowMessages();
//                    GetMessages oo = new GetMessages();
//                    oo.execute("");
//                }
//            }
//        }
//
////            if (!(share.GetDeviceID().equals(getString(R.string.defaultValue))) && (!share.GetUsername().equals(getString(R.string.defaultValue)))
////                    && (!share.GetPassword().equals(getString(R.string.defaultValue))) && (share.GetActivation().equals(getString(R.string.NotActive)))) {
//        else if (state.equals(getString(R.string.Wait))) {
//            setContentView(R.layout.wait_for_activation_layout);
//            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//            alarmIntent.setAction("Alarm");
//            pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////                long interval = INTERVAL_FIFTEEN_MINUTES;
//            int interval = 60000;
//
////                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
////            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//            Log.i("Alarm", "Set");
//            if (share.GetActivation().equals(getString(R.string.Active))) {
////                GetMessagesfromDB();
////                ShowMessages();
//                GetMessages oo = new GetMessages();
//                oo.execute("");
//            }
//        } else {
//            setContentView(R.layout.login_layout);
//
//
////            new Thread(new Runnable() {
////                @Override
////                public void run() {
//
//                    UsernameView = (EditText) findViewById(R.id.editText2);
//                    PasswordView = (EditText) findViewById(R.id.editText);
//                    Button registerButton = (Button) findViewById(R.id.button);
//                    PasswordView.setOnKeyListener(new View.OnKeyListener() {
//
//                        @Override
//                        public boolean onKey(View v, int keyCode, KeyEvent event) {
//                            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                                String Username = UsernameView.getText().toString();
//                                String Password = PasswordView.getText().toString();
//                                String DeviceID = getUniquePsuedoID();
//                                share.SaveLoginDetails(Username, Password);
//                                share.SaveDeviceID(DeviceID);
//
//                                attemptLogin(Username, Password, DeviceID);
//                            }
//                            return false;
//                        }
//                    });
//
//
//                    registerButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            String Username = UsernameView.getText().toString();
//                            String Password = PasswordView.getText().toString();
//                            String DeviceID = getUniquePsuedoID();
//                            share.SaveLoginDetails(Username, Password);
//                            share.SaveDeviceID(DeviceID);
//
//                            attemptLogin(Username, Password, DeviceID);
////                Log.i("Successful Login ", "Welcome " + Name);
//                        }
//                    });
////                }
////            }).start();
//        }


    }

    @Override
    protected void onStop() {
        super.onStop();
//        first = true;
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

//        String s = "";
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // save data in local shared preferences
            Username = username;
            Password = password;
            this.DeviceID = DeviceID;
            String[] Userdata = {username, password, DeviceID};
            first = true;
            NetworkAsyncTask task = new NetworkAsyncTask(this);
            task.execute(Userdata);

//            UpdateUI(username, password, DeviceID, null);
        }


    }

    private void UpdateUI(String state1) {
        if (state1 == null) {
            state1 = share.GetStatus();
        }
//        if(state1.equals(getString(R.string.defaultValue)))
//        {
//            state1 = "Not_Saved";
//        }
//        String state = share.GetStatus();

        State state2 = State.valueOf(state1);

        switch (state2) {
            case OK: {
                setContentView(R.layout.messages_layout);
//                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//                alarmIntent.setAction("Alarm");
//                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////                long interval = INTERVAL_FIFTEEN_MINUTES;
////                int interval = 60000;
//
////                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
////                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//                Log.i("Alarm", "Set");
////                if (share.GetActivation().equals(getString(R.string.Active))) {
                GetMessagesfromDB();
                ShowMessages();
//                GetMessages oo = new GetMessages();
//                oo.execute("");
//                }
                break;
            }

            case Wait: {

                setContentView(R.layout.wait_for_activation_layout);

//                share.SaveActivation(getString(R.string.NotActive));


//                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//                alarmIntent.setAction("Alarm");
//                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////                long interval = INTERVAL_FIFTEEN_MINUTES;
////                int interval = 60000;
//
////                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
////                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//                Log.i("Alarm", "Set");
                break;
            }
            case Invalid:
            case Not_Saved:
            default: {
                Log.w("ERROR", "Wrong Information");
//                Toast.makeText(this.getApplicationContext(),
//                        "Something is wrong, check your connection and username/password :", Toast.LENGTH_LONG).show();
                final Intent intent = getIntent();
//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                try {
//                    Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                finish();
                startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            }
        }

//        if (state1.equals(getString(R.string.OK))) {
//            //set Repeating Alarm
////                setContentView(R.layout.activity_main_logged_in);
//
//            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//            alarmIntent.setAction("Alarm");
//            pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////                long interval = INTERVAL_FIFTEEN_MINUTES;
//            int interval = 60000;
//
////                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval, interval, pendingIntent);
//            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//            Log.i("Alarm", "Set");
//            if (share.GetActivation().equals(getString(R.string.Active))) {
////                    GetMessagesfromDB();
////                    ShowMessages();
//                GetMessages oo = new GetMessages();
//                oo.execute("");
//            }
//        } else if (state1.equals(getString(R.string.Wait))) {
//
//            setContentView(R.layout.wait_for_activation_layout);
//            share.SaveDeviceID(DeviceID);
//            share.SaveLoginDetails(username, password);
////                share.SaveActivation(getString(R.string.NotActive));
//
//
//            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//            alarmIntent.setAction("Alarm");
//            pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////                long interval = INTERVAL_FIFTEEN_MINUTES;
//            int interval = 60000;
//
////                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval, interval, pendingIntent);
//            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//            Log.i("Alarm", "Set");
//
////                ShowMessages();
//
//
//        } else {
//            //Error On LogIn
//            Log.w("ERROR", "Wrong Information");
//            Toast.makeText(this.getApplicationContext(),
//                    "Something is wrong, check your connection and username/password :", Toast.LENGTH_LONG).show();
//            final Intent intent = getIntent();
////                Thread thread = new Thread() {
////                    @Override
////                    public void run() {
//            try {
//                Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
//                MainActivity.this.finish();
//                startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////                    }
////                };
////                thread.start();
//        }


    }

    private void MarkRead() {


        ListView lv = (ListView) findViewById(R.id.list1);
        CheckBox cb;

        SQLiteDatabase database = db.getWritableDatabase();
        for (int i = 0; i < lv.getCount(); i++) {
            cb = (CheckBox) lv.getChildAt(i).findViewById(R.id.checkbox1);

            if (cb.isChecked()) {
                Integer ID;
                ID = MESSAGES.get(i).getMessageID();
                ContentValues values = new ContentValues();
                values.put("Seen", true);
                database.update("Messages", values, "MessageID  = ?", new String[]{String.valueOf(ID)});
            }
        }
        database.close();
        isSelected = false;
        mMenu.clear();
        UpdateUI(share.GetStatus());

    }


    private void Markdelete() {

        ListView lv = (ListView) findViewById(R.id.list1);
        CheckBox cb;

        SQLiteDatabase database = db.getWritableDatabase();
        for (int i = 0; i < lv.getCount(); i++) {
            cb = (CheckBox) lv.getChildAt(i).findViewById(R.id.checkbox1);

            if (cb.isChecked()) {
                Integer ID;
                ID = MESSAGES.get(i).getMessageID();
                database.delete("Messages", "MessageID  = ?", new String[]{String.valueOf(ID)});
            }
        }
        database.close();
        isSelected = false;
        mMenu.clear();
        UpdateUI(share.GetStatus());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case Menu.FIRST:
                Markdelete();
                break;
            case Menu.FIRST + 1:
                MarkRead();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowMessages() {

        setContentView(R.layout.messages_layout);
        ListView lv = (ListView) findViewById(R.id.list1);


        Mlist = new ArrayList<>(); //Messages List
        Tlist = new ArrayList<>(); //Title List
//      Dlist = new ArrayList<>(); //Date List
        SList = new ArrayList<>(); //is Seen
        IList = new ArrayList<>(); //Message ID
        CList = new ArrayList<>(); //Critical
        SSList = new ArrayList<>(); //SendSeen

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

        adapt = new CustomAdapter(this, Tlist, Mlist, SList, IList, CList, SSList);
//        lv.setAdapter(new CustomAdapter(this, Tlist, Mlist, SList, IList, CList, SSList));

        lv.setAdapter(adapt);
//        lv.setAdapter(arrayAdapter);


//        registerForContextMenu(lv);
//
//        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                // TODO Auto-generated method stub
//
//                MenuInflater inflater = getMenuInflater();
//                inflater.inflate(R.menu.menu, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position,
//                                                  long id, boolean checked) {
//                // TODO Auto-generated method stub
//            }
//        });







    }

    private void deleteSelectedItems() {
    }

    private enum State {
        OK, Wait, Invalid, Not_Saved
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
                            MObj.setMessageTitle(cursor.getString(1).trim());
                            MObj.setMessageBody(cursor.getString(2).trim());
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
//            ShowMessages();
            adapt.notifyDataSetChanged();
        }
    }

    public class CustomAdapter extends BaseAdapter {
        final Context context;
        int num_selected;
        CheckBox c;
        List<String> Titles;
        List<String> Bodies;
        List<Boolean> isSeen;
        List<Integer> IList;
        List<Boolean> CList;
        List<Boolean> SSList;
        private LayoutInflater inflater = null;

        public CustomAdapter(MainActivity mainActivity, List<String> MessagesTitle, List<String> MessagesBody, List<Boolean> isSeen, List<Integer> IList, List<Boolean> CList, List<Boolean> SSList) {


            num_selected = 0;
            context = mainActivity;
            Titles = MessagesTitle;
            Bodies = MessagesBody;
            this.isSeen = isSeen;
            this.IList = IList;
            this.CList = CList;
            this.SSList = SSList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            c = (CheckBox) findViewById(R.id.checkbox1);
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

        private void countCheck(boolean isChecked) {

            num_selected += isChecked ? 1 : -1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder = new Holder();
            final View rowView;
            rowView = inflater.inflate(R.layout.list_row_layout, parent, false);

            holder.tv1 = (TextView) rowView.findViewById(R.id.title);
            holder.tv2 = (TextView) rowView.findViewById(R.id.body);
            holder.cb = (CheckBox) rowView.findViewById(R.id.checkbox1);
            holder.iv = (ImageView) rowView.findViewById(R.id.state);
            holder.i2 = (ImageView) rowView.findViewById(R.id.Critical);
            holder.tv1.setText(Titles.get(position));
            holder.tv2.setText(Bodies.get(position));
            if (isSeen.get(position))
                holder.iv.setImageResource(R.mipmap.ic_done_all_black_24dp);
            else
                holder.iv.setImageResource(R.mipmap.ic_done_black_24dp);

            if (CList.get(position)) {
                holder.i2.setImageResource(R.mipmap.ic_priority_high_black_24dp);
            }


            if (num_selected > 0) {
                holder.cb.setVisibility(View.VISIBLE);
                mMenu.findItem(Menu.FIRST).setVisible(true);
                mMenu.findItem(Menu.FIRST + 1).setVisible(true);
            } else if (isSelected) {
                holder.cb.setVisibility(View.GONE);
                mMenu.findItem(Menu.FIRST).setVisible(false);
                mMenu.findItem(Menu.FIRST + 1).setVisible(false);
            }


            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    Toast.makeText(menu, "You Clicked " + Titles.get(position), Toast.LENGTH_LONG).show();
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
//                    showActivity.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                    finish();
                    startActivity(showActivity);



                }
            });

            rowView.setOnLongClickListener(new View.OnLongClickListener() {
//                LinearLayout l = (LinearLayout) findViewById(R.id.linearLayout1);
CheckBox c = (CheckBox) findViewById(R.id.checkbox1);
//                CheckBox[] cb = new CheckBox[Titles.size()];
//                Holder holder = new Holder();

                //                CheckBox[] cbs = new CheckBox[5];
                @Override
                public boolean onLongClick(View v) {

                    if ((num_selected == 1) & (holder.cb.isChecked())) {
//                        for (int i = 0; i < Titles.size(); i++) {
//                            l.removeViewInLayout(cb[i]);
//                        }
//                        l.removeView(c);
                        holder.cb.setChecked(false);
//                        c.setVisibility(View.GONE);
                        mMenu.findItem(Menu.FIRST).setVisible(false);
                        mMenu.findItem(Menu.FIRST + 1).setVisible(false);
                        num_selected--;
//                        isSelected = false;
//                        notifyDataSetChanged();
                    } else if (holder.cb.isChecked()) {
                        holder.cb.setChecked(false);
//                        holder.cb.setVisibility(View.GONE);
                        num_selected--;
                    } else {
                        if (c != null) {
                            c.setVisibility(View.VISIBLE);
                        }
                        CheckBox.OnCheckedChangeListener checkListener = new CheckBox.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                countCheck(isChecked);
                                Log.i("MAIN", num_selected + "");
                            }
                        };
                        c = (CheckBox) findViewById(R.id.checkbox1);
                        if (c != null) {
                            c.setOnCheckedChangeListener(checkListener);
                        }

//                        holder.cb.invalidate();
//                        holder.cb.requestLayout();
//                            notifyDataSetChanged();
                        holder.cb.setVisibility(View.VISIBLE);
                        holder.cb.setChecked(true);

//                        Menu mMenu = (Menu) findViewById(R.id.group);
                        if (!isSelected) {
                            mMenu.clear();
                            MenuItem item1 = mMenu.add(Menu.NONE, Menu.FIRST, 10, R.string.menu_delete);
                            item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                            item1.setIcon(R.mipmap.ic_delete_black_24dp);

                            MenuItem item2 = mMenu.add(Menu.NONE, Menu.FIRST + 1, 10, R.string.menu_read);
                            item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                            item2.setIcon(R.mipmap.ic_done_all_black_24dp);

                            isSelected = true;
                        } else {
                            mMenu.findItem(Menu.FIRST).setVisible(true);
                            mMenu.findItem(Menu.FIRST + 1).setVisible(true);
                        }
                        num_selected++;


//                            l.invalidate();
//                            l.requestLayout();




                    }

                    return true;
                }
            });

            return rowView;
        }

//        @Override
//        public void notifyDataSetChanged() {
//            super.notifyDataSetChanged();
//        }

        private class Holder {
            TextView tv1;
            TextView tv2;
            ImageView iv;
            ImageView i2;
            CheckBox cb;

        }

    }


}
