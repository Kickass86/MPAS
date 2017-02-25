package turbotec.mpas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    public static DatabaseHandler db;
    public static SQLiteDatabase database;
    public static List<MessageObject> MESSAGES;
    public SharedPreferenceHandler sp;
    public SharedPreferenceHandler share;
    private PendingIntent pendingIntent;
    private ListView lv;
    private EditText UsernameView;
    private EditText PasswordView;
    private Button RegisterButton;
    private String UserName;
    private String PassWord;
    private String MyID;
    private String Name;


    @SuppressWarnings("deprecation")
    public static String getUniquePsuedoID() {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        share = SharedPreferenceHandler.getInstance(this);
        SQLiteOpenHelper t = DatabaseHandler.getInstance(this);

        database = t.getReadableDatabase();

//        AlarmReceiver AR = new AlarmReceiver();
//        registerReceiver(AR,);


        UserName = share.GetUsername();
        PassWord = share.GetPassword();
        MyID = share.GetDeviceID();


        UsernameView = (EditText) findViewById(R.id.editText2);
        PasswordView = (EditText) findViewById(R.id.editText);
        RegisterButton = (Button) findViewById(R.id.button);



        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Username = UsernameView.getText().toString();
                String Password = PasswordView.getText().toString();
                String DeviceID = getUniquePsuedoID();

                Name = attemptLogin(Username, Password, DeviceID);
//                Log.i("Successful Login ", "Welcome " + Name);
            }
        });

        if ((!UserName.equals(getString(R.string.defaultValue))) && ((!PassWord.equals(getString(R.string.defaultValue)))) && (!MyID.equals(getString(R.string.defaultValue)))) {
            setContentView(R.layout.waiting_layout);
            boolean result = false;
            String[] Userdetails = {UserName, PassWord, MyID};
            NetworkAsyncTask task = new NetworkAsyncTask(this);
            try {
                result = task.execute(Userdetails).get();
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
            if (result) {
                //set Repeating Alarm
                setContentView(R.layout.activity_main_logged_in);

                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                long interval = INTERVAL_FIFTEEN_MINUTES;
                int interval = 60000;

//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
                Log.i("Alarm", "Set");

//                ShowMessages(GetMessagesfromDB());

            } else {
                //Error On LogIn
                Log.w("ERROR", "Wrong Information");
                Toast.makeText(this.getApplicationContext(),
                        "Wrong Info", Toast.LENGTH_LONG).show();
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


    public String attemptLogin(String username, String password, String DeviceID) {

        boolean result = false;

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
                result = task.execute(Userdata).get();
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
            if (result) {

                share.SaveDeviceID(DeviceID);
                share.SaveLoginDetails(username, password);
                setContentView(R.layout.activity_main_logged_in);

                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                long interval = INTERVAL_FIFTEEN_MINUTES;
                int interval = 60000;

//                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
                Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
                Log.i("Alarm", "Set");

//                ShowMessages(GetMessagesfromDB());


            } else {
                //Error On LogIn
                Log.w("ERROR", "Wrong Information");
                Toast.makeText(this.getApplicationContext(),
                        "Wrong Info", Toast.LENGTH_LONG).show();
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
        return null;



    }


    public Boolean CheckInternetAvailability() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    //Network AsyncTask


    public class NetworkAsyncTask extends AsyncTask<Object, Object, Boolean> {

        public DatabaseHandler db;
        public List<MessageObject> MESSAGES;
        public SQLiteDatabase database;
        private WeakReference<MainActivity> MyActivity;
        private ListView lv;
        private Boolean FLag = false;
        private MessageObject MObj;

        public NetworkAsyncTask(Context context) {
//        this.MyActivity = new WeakReference<MainActivity>(activity);

        }

        protected void onPreExecute() {
            //display progress dialog.


        }

        protected Boolean doInBackground(Object... userdetails) {

            Object username = userdetails[0];
            Object password = userdetails[1];
            Object DeviceID = userdetails[2];

            Connection conn;
            try {
                String driver = "net.sourceforge.jtds.jdbc.Driver";


                String name = "sa";
                String pass = "left4de@d";


                String connString = "jdbc:jtds:sqlserver://192.168.1.131:1433/MIGT_Automation;user=" + name + ";password=" + pass + ";";


                try {
                    Class.forName(driver).newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                conn = DriverManager.getConnection(connString);
                Log.w("Connection", "open");
                Statement stmt1 = conn.createStatement();
                ResultSet reset1 = stmt1.executeQuery("Use MIGT_Automation\n" +
                        " SELECT * FROM TbL_Users WHERE " + "Username = '" +
                        username + "' AND  password = '" + password + "';");
//                ResultSet reset = stmt.executeQuery("SELECT * FROM TbL_Users WHERE " + "Username = '" + username +"';");

                Statement stmt4 = conn.createStatement();

                Boolean b = reset1.next();
                if (b) {
                    Statement stmt2 = conn.createStatement();
                    ResultSet reset2 = stmt2.executeQuery("Use MIGT_Automation\n" +
                            "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.[User ID] = TbL_Users.id \n" +
                            "Where TbL_Users.id = '" + reset1.getString("id") + "' AND TbL_Users.DeviceID = '" + DeviceID + "';");

                    if (reset2.next()) {

                        Statement stmt3 = conn.createStatement();
                        ResultSet reset3 = stmt3.executeQuery("Use MIGT_Automation\n" +
                                "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.[User ID] = TbL_Users.id \n" +
                                "Where TbL_Users.id = '" + reset1.getString("id") + "' AND Messages.Delivered = 0 AND TbL_Users.DeviceID = '" + DeviceID + "';");
                        if (reset3.next()) {
                            int UpdateDeviceID = stmt4.executeUpdate("Use MIGT_Automation\n" +
                                    "   update Messages\n" +
                                    "   SET Delivered = 1\n" +
                                    "   WHERE " + "[Message ID] = '" + reset2.getString("Message ID") + "' AND Delivered = 0;");
                            //
                            MainActivity activity = this.MyActivity.get();
                            MObj = new MessageObject(reset2.getString("Message ID"), reset2.getString("User ID"),
                                    reset2.getString("Message Title"), reset2.getString("Message Body"), reset2.getDate("Insert Date").toString(), reset2.getInt("Delivered"));
                            db.addMessage(MObj);
                            Log.i("User valid", "New message added");
                            while (reset2.next()) {
                                UpdateDeviceID = stmt4.executeUpdate("Use MIGT_Automation\n" +
                                        "   update Messages\n" +
                                        "   SET Delivered = 1\n" +
                                        "   WHERE " + "[Message ID] = '" + reset2.getString("Message ID") + "' AND Delivered = 0;");

                                MObj = new MessageObject(reset2.getString("Message ID"), reset2.getString("User ID"),
                                        reset2.getString("Message Title"), reset2.getString("Message Body"), reset2.getDate("Insert Date").toString(), reset2.getInt("Delivered"));
                                db.addMessage(MObj);
                            }
                            FLag = true;
                        } else {
                            //No new Message
                            Log.i("User valid", "No new message");
                            FLag = true;
                        }
                    } else {
                        if (reset1.getString("DeviceID") == null) {
                            //First Time Launch
                            int UpdateDeviceID = stmt4.executeUpdate("Use MIGT_Automation\n" +
                                    "   update TbL_Users\n" +
                                    "   SET DeviceID = '" + DeviceID + "'\n" +
                                    "   WHERE " + "Username = '" +
                                    username + "' AND " + " password = '" + password + "';");
                            if (UpdateDeviceID != 0) {
                                Log.i("SQL Database", "DeviceID Updated Successfully");
                                FLag = true;
                            }
                        }
                    }

                } else {
                    Log.i("Return false", "User invalid");
//                return false;

                }

//            return false;

//                return reset.getString(0);
//                return (reset.getString("password").equals(password)) ? true: false;
//                return reset.getString("password");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.i("Return false", "User invalid");
//            return false;
            } catch (Exception e) {
                e.getStackTrace();
                Log.i("Return false", "User invalid");
//            return false;
            }

            return FLag;
        }


        protected void onPostExecute() {
            // dismiss progress dialog and update ui
            Log.d("SQLite", "Begin to Show");
            ShowMessages(GetMessagesfromDB());

        }


        public List<MessageObject> GetMessagesfromDB() {


//        database =  db.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * " +
                    "FROM Messages ;", null);

            // looping through all rows and adding to list
            try {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            MessageObject MObj = new MessageObject();
                            MObj.setMessageID(cursor.getString(1));
                            MObj.setUserID(cursor.getString(2));
                            MObj.setMessageTitle(cursor.getString(3));
                            MObj.setMessageBody(cursor.getString(4));
                            MObj.setInsertDate(cursor.getString(5));
                            MObj.setDelivered(cursor.getInt(6));

                            MESSAGES.add(MObj);
                        } while (cursor.moveToNext());
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
            return MESSAGES;

        }

        public void ShowMessages(List<MessageObject> Messages) {

            setContentView(R.layout.messages_layout);
            lv = (ListView) findViewById(R.id.list1);


            List<String> Mlist = new ArrayList<String>(); //Messages List
            List<String> Tlist = new ArrayList<String>(); //Title List
            List<String> Dlist = new ArrayList<String>(); //Date List

            if (Messages != null) {
                Mlist.add(Messages.get(0).getMessageBody());
                Tlist.add(Messages.get(0).getMessageTitle());
                Dlist.add(Messages.get(0).getInsertDate());
            }
            // This is the array adapter, it takes the context of the activity as a
            // first parameter, the type of list view as a second parameter and your
            // array as a third parameter.
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    Tlist);

            lv.setAdapter(arrayAdapter);

        }





    }


}
