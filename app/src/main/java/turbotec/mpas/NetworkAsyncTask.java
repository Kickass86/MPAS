package turbotec.mpas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class NetworkAsyncTask extends AsyncTask<Object, Object, Boolean> {

    public DatabaseHandler db;
    public List<MessageObject> MESSAGES;
    public MessageObject MObj;
    Context mContetx;
    //        public SQLiteDatabase database;
    private WeakReference<MainActivity> MyActivity;
    private ListView lv;
    private Boolean FLag = false;
//        private MessageObject MObj;

    public NetworkAsyncTask(Context context, MainActivity activity) {
        this.MyActivity = new WeakReference<MainActivity>(activity);
        mContetx = context;
    }

    protected void onPreExecute() {
        //display progress dialog.
        db = MainActivity.db;
//            MObj = new MessageObject();
        MESSAGES = new ArrayList<MessageObject>();
//             db = datab.getInstance(MainActivity.this);
//             SQLiteOpenHelper db = datab.getInstance(MainActivity.this);

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
//                            MainActivity activity = this.MyActivity.get();
                        MObj = new MessageObject(reset3.getInt("Message ID"), reset3.getString("User ID"),
                                reset3.getString("Message Title"), reset3.getString("Message Body"), reset3.getDate("Insert Date").toString(), reset3.getInt("Delivered"));
                        db.addMessage(MObj);
                        Log.i("User valid", "New message added");
                        while (reset3.next()) {
                            UpdateDeviceID = stmt4.executeUpdate("Use MIGT_Automation\n" +
                                    "   update Messages\n" +
                                    "   SET Delivered = 1\n" +
                                    "   WHERE " + "[Message ID] = '" + reset3.getString("Message ID") + "' AND Delivered = 0;");

                            MObj = new MessageObject(reset3.getInt("Message ID"), reset3.getString("User ID"),
                                    reset3.getString("Message Title"), reset3.getString("Message Body"), reset3.getDate("Insert Date").toString(), reset3.getInt("Delivered"));
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

//        onPostExecute();
        return FLag;
    }


    protected void onPostExecute() {
        // dismiss progress dialog and update ui
        Log.d("SQLite", "Begin to Show");
//        ShowMessages(GetMessagesfromDB());

    }


    public List<MessageObject> GetMessagesfromDB() {


//        database =  db.getReadableDatabase(); execSQL("INSERT INTO Messages VALUES(1000,'IDUSER','TITLE23','BODY','2017-02-21',0); ")
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * " +
                "FROM Messages ;", null);

        // looping through all rows and adding to list
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        MObj = new MessageObject();
                        MObj.setMessageID(cursor.getInt(0));
                        MObj.setUserID(cursor.getString(1));
                        MObj.setMessageTitle(cursor.getString(2));
                        MObj.setMessageBody(cursor.getString(3));
                        MObj.setInsertDate(cursor.getString(4));
                        MObj.setDelivered(Integer.valueOf(cursor.getString(5)));

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

        MainActivity activity = MyActivity.get();
        activity.setContentView(R.layout.messages_layout);
        lv = (ListView) activity.findViewById(R.id.list1);


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
                mContetx,
                android.R.layout.simple_list_item_1,
                Tlist);

        lv.setAdapter(arrayAdapter);

    }
}