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
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;


public class MainActivity extends AppCompatActivity {

    public static DatabaseHandler db;
    private static List<MessageObject> MESSAGES;
    //    public static SQLiteDatabase database;
    private int Message_Number = 0;
    //    private SharedPreferenceHandler sp;
    private SharedPreferenceHandler share;
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
                GetMessagesfromDB();
                ShowMessages();
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

    @SuppressWarnings("deprecation")
    private static String getUniquePsuedoID() {

        String m_szDevIDShort;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            m_szDevIDShort = "46cd" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.SUPPORTED_ABIS[0].length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        }
        else
        {
            m_szDevIDShort = "46cd" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        }


        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return  new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
    //    private String Name;

    private static void GetMessagesfromDB() {


//        database =  db.getReadableDatabase(); execSQL("INSERT INTO Messages VALUES(1000,'IDUSER','TITLE23','BODY','2017-02-21',0); ")
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * " +
                "FROM Messages ORDER BY MessageID DESC;", null);

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
                        MObj.setCritical(Boolean.valueOf(cursor.getString(4)));
                        MObj.setSeen("1".equals(cursor.getString(5)));

                        MESSAGES.add(MObj);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.getStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (share.GetStatus().equals(getString(R.string.OK))) {
            GetMessagesfromDB();
            ShowMessages();
        }
    }

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
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
//        unregisterReceiver(NotifyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MESSAGES = new ArrayList<>();
        db = DatabaseHandler.getInstance(this);
        share = SharedPreferenceHandler.getInstance(this);
        registerReceiver(broadcastReceiver, new IntentFilter("Alarm fire"));

//        registerReceiver(NotifyReceiver, new IntentFilter("Notification fire"));
//        if (!(share.GetDeviceID().equals(getString(R.string.defaultValue))) && (!share.GetUsername().equals(getString(R.string.defaultValue)))
//                && (!share.GetPassword().equals(getString(R.string.defaultValue))) && (share.GetActivation().equals(getString(R.string.Active)))) {
        if (share.GetStatus().equals(getString(R.string.OK))) {
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
                    GetMessagesfromDB();
                    ShowMessages();
                }
            }
        }

//            if (!(share.GetDeviceID().equals(getString(R.string.defaultValue))) && (!share.GetUsername().equals(getString(R.string.defaultValue)))
//                    && (!share.GetPassword().equals(getString(R.string.defaultValue))) && (share.GetActivation().equals(getString(R.string.NotActive)))) {
        else if (share.GetStatus().equals(getString(R.string.Wait))) {
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
                    GetMessagesfromDB();
                    ShowMessages();
                }
            } else {
//        database = t.getReadableDatabase();

//        AlarmReceiver AR = new AlarmReceiver();
//        registerReceiver(AR,);


//            String userName = share.GetUsername();
//            String passWord = share.GetPassword();
//            String myID = share.GetDeviceID();


                UsernameView = (EditText) findViewById(R.id.editText2);
                PasswordView = (EditText) findViewById(R.id.editText);
                Button registerButton = (Button) findViewById(R.id.button);


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

//            if ((!userName.equals(getString(R.string.defaultValue))) && ((!passWord.equals(getString(R.string.defaultValue)))) && (!myID.equals(getString(R.string.defaultValue)))) {
//                setContentView(R.layout.waiting_layout);
//                boolean Titles = false;
//                String[] UserDetails = {userName, passWord, myID};
//                NetworkAsyncTask task = new NetworkAsyncTask(this, this);
//                try {
//                    Titles = task.execute(UserDetails).get();
//                } catch (ExecutionException | InterruptedException ei) {
//                    ei.printStackTrace();
//                }
//                if (Titles) {
//                    //set Repeating Alarm
//                    setContentView(R.layout.activity_main_logged_in);
//
//                    Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//                    alarmIntent.setAction("Alarm");
//                    pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////                long interval = INTERVAL_FIFTEEN_MINUTES;
//                    int interval = 60000;
//
////                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//                    manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
//                    Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//                    Log.i("Alarm", "Set");
//                    GetMessagesfromDB();
//                    ShowMessages();
//
////                ShowMessages(GetMessagesfromDB());
//
//                } else {
//                    //Error On LogIn
//                    Log.w("ERROR", "Wrong Information");
//                    Toast.makeText(this.getApplicationContext(),
//                            "Wrong Info", Toast.LENGTH_LONG).show();
//                    final Intent intent = getIntent();
//                    Thread thread = new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
//                                MainActivity.this.finish();
//                                startActivity(intent);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    thread.start();
//                }
//
//            }


    }

    private void attemptLogin(String username, String password, String DeviceID) {

        String result = getString(R.string.Invalid);

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // save data in local shared preferences
            String[] Userdata = {username, password, DeviceID};
            NetworkAsyncTask task = new NetworkAsyncTask(this);
            try {
                task.execute(Userdata).get();
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
            if (share.GetStatus().equals(getString(R.string.OK))) {
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
                    GetMessagesfromDB();
                    ShowMessages();
                }
            } else if (share.GetStatus().equals(getString(R.string.Wait))) {

                share.SaveDeviceID(DeviceID);
                share.SaveLoginDetails(username, password);
//                share.SaveActivation(getString(R.string.NotActive));
                setContentView(R.layout.wait_for_activation_layout);

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
                        "Something is wrong, check your connection and username/password", Toast.LENGTH_LONG).show();
                final Intent intent = getIntent();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                            MainActivity.this.finish();
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }


        }



    }

    private void ShowMessages() {

        setContentView(R.layout.messages_layout);
        ListView lv = (ListView) findViewById(R.id.list1);


        List<String> Mlist = new ArrayList<>(); //Messages List
        List<String> Tlist = new ArrayList<>(); //Title List
        List<String> Dlist = new ArrayList<>(); //Date List
        List<Boolean> SList = new ArrayList<>(); //is Seen
        List<Integer> IList = new ArrayList<>();

        for (int i = Message_Number; i < MESSAGES.size(); i++) {
            Mlist.add(MESSAGES.get(i).getMessageBody());
            Tlist.add(MESSAGES.get(i).getMessageTitle());
            Dlist.add(MESSAGES.get(i).getInsertDate());
            SList.add(MESSAGES.get(i).isSeen());
            IList.add(MESSAGES.get(i).getMessageID());
        }
        Message_Number = MESSAGES.size();
        if (MESSAGES.isEmpty()) {
            TextView emptyText = (TextView) findViewById(android.R.id.empty);
            lv.setEmptyView(emptyText);
        }


//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                MainActivity.this,
//                android.R.layout.simple_list_item_1,
//                Tlist);

        lv.setAdapter(new CustomAdapter(this, Tlist, Mlist, SList, IList));

//        lv.setAdapter(arrayAdapter);

    }


    public class CustomAdapter extends BaseAdapter {
        List<String> Titles;
        List<String> Bodies;
        List<Boolean> isSeen;
        List<Integer> IList;
        Context context;

        private LayoutInflater inflater = null;

        public CustomAdapter(MainActivity mainActivity, List<String> MessagesTitle, List<String> MessagesBody, List<Boolean> isSeen, List<Integer> IList) {

            // TODO Auto-generated constructor stub
            context = mainActivity;
            Titles = MessagesTitle;
            Bodies = MessagesBody;
            this.isSeen = isSeen;
            this.IList = IList;
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
            holder.tv1.setText(Titles.get(position));
            holder.tv2.setText(Bodies.get(position));
            if (isSeen.get(position))
                holder.iv.setImageResource(R.mipmap.seen);
            else
                holder.iv.setImageResource(R.mipmap.delivered);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    Toast.makeText(context, "You Clicked " + Titles.get(position), Toast.LENGTH_LONG).show();
                    setContentView(R.layout.messages_preview_layout);
                    TextView t1 = (TextView) findViewById(R.id.titledetail);
                    TextView t2 = (TextView) findViewById(R.id.bodydetail);
                    ImageView i1 = (ImageView) findViewById(R.id.statedetail);
                    t1.setText(Titles.get(position));
                    t2.setText(Bodies.get(position));
                    i1.setImageResource(R.mipmap.seen);
                    ContentValues values = new ContentValues();
                    values.put("Seen", true);
                    SQLiteDatabase database = db.getWritableDatabase();
                    database.update("Messages", values, "MessageID  = ?", new String[]{String.valueOf(IList.get(position))});
                    database.close();


                    int z = 3;
                    String[] data = new String[]{z + "", "1", String.valueOf(IList.get(position))};

                    SendStatusAsyncTask taskstate = new SendStatusAsyncTask(context);

                    try {
                        Object b = taskstate.execute(data).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                }
            });
            return rowView;
        }

        private class Holder {
            TextView tv1;
            TextView tv2;
            ImageView iv;

        }

    }






}
