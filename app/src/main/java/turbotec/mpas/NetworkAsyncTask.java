package turbotec.mpas;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;


class NetworkAsyncTask extends AsyncTask<Object, Object, Boolean> {

    private final Context MyContext;
    private DatabaseHandler db;
    private SharedPreferenceHandler share;
    private String Title;
    private String Content;
    private int ID;
    private Boolean FLag = false;
//        private MessageObject MObj;

    public NetworkAsyncTask(Context context) {
//        WeakReference<MainActivity> myActivity = new WeakReference<>(activity);
        MyContext = context;

    }

    public boolean isAppForeground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }

        return true;
    }

    protected void onPreExecute() {
        //display progress dialog.
        db = DatabaseHandler.getInstance(MyContext);

//            MObj = new MessageObject();
//        List<MessageObject> MESSAGES = new ArrayList<>();
        share = SharedPreferenceHandler.getInstance(MyContext);
//             db = datab.getInstance(MainActivity.this);
//             SQLiteOpenHelper db = datab.getInstance(MainActivity.this);

    }

    protected Boolean doInBackground(Object... userDetails) {


//        if(Foreground)
//        {
//            Log.e("!!!!","it is in foreground");
//        }
//        else
//        {
//            Log.e("!!!!","it is not in foreground");
//        }
        Object username = userDetails[0];
        Object password = userDetails[1];
        Object DeviceID = userDetails[2];
        android.support.v4.app.NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager =
                (NotificationManager) MyContext.getSystemService(Context.NOTIFICATION_SERVICE);

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

            Statement stmt1 = conn.createStatement();

            ResultSet reset1 = stmt1.executeQuery("Use MIGT_Automation\n" +
                    " SELECT * FROM TbL_Users WHERE " + "Username = '" +
                    username + "' AND  password = '" + password + "' AND DeviceID = '" + DeviceID + "';");
//                ResultSet reset = stmt.executeQuery("SELECT * FROM TbL_Users WHERE " + "Username = '" + username +"';");

            Statement stmt4 = conn.createStatement();

            Boolean b = reset1.next();
            if (b) {
                Statement stmt2 = conn.createStatement();
                ResultSet reset2 = stmt2.executeQuery("Use MIGT_Automation\n" +
                        "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.UserID = TbL_Users.id \n" +
                        "Where TbL_Users.id = '" + reset1.getString("id") + "' AND TbL_Users.DeviceID = '" + DeviceID + "' AND TbL_Users.DeviceIDAccepted = 1;");



                if (reset2.next()) {
                    share.SaveUserID(reset1.getString("id"));
                    share.SaveActivation(MyContext.getString(R.string.Active));

                    Statement stmt3 = conn.createStatement();
                    ResultSet reset3 = stmt3.executeQuery("Use MIGT_Automation\n" +
                            "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.UserID = TbL_Users.id \n" +
                            "Where TbL_Users.id = '" + reset2.getString("id") + "' AND Messages.Delivered = 0 AND TbL_Users.DeviceID = '" + DeviceID + "';");

                    Log.w("Connection1", "open");
                    if (reset3.next()) {
//                        int UpdateDeviceID =

                        stmt4.executeUpdate("Use MIGT_Automation\n" +
                                "   update Messages\n" +
                                "   SET Delivered = 1\n" +
                                "   WHERE " + " MessageID = '" + reset3.getString("MessageID") + "' AND Delivered = 0;");
                        //
//                            MainActivity activity = this.MyActivity.get();
                        MessageObject MObj = new MessageObject(reset3.getInt("MessageID"), reset3.getString("UserID"),
                                reset3.getString("MessageTitle"), reset3.getString("MessageBody"), reset3.getDate("InsertDate").toString(), reset3.getInt("Delivered"), reset3.getBoolean("Critical") ? 1 : 0);
                        Log.w("Connection2", "open");
                        db.addMessage(MObj);
                        Log.i("User valid", "New message added");

                        Title = reset3.getString("MessageTitle");
                        ID = reset3.getInt("MessageID");
                        Content = reset3.getString("MessageBody");
                        b = reset3.getBoolean("Critical");

                        if (b) {
                            Intent i = new Intent(MyContext, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyContext.startActivity(i);
                            mNotificationManager.cancelAll();
                            return true;
                        } else {
                            if (!isAppForeground(MyContext)) {

                                Log.i("Notify", "is running");
                                Intent nid = new Intent(MyContext, MainActivity.class);
                                PendingIntent ci = PendingIntent.getActivity(MyContext, 0, nid, 0);

                                mBuilder =
                                        new android.support.v4.app.NotificationCompat.Builder(MyContext)
                                                .setSmallIcon(R.mipmap.ic_launcher)
//                                                .setSmallIcon(MyContext.getResources().getDrawable(R.mipmap.ic_launcher))
                                                .setContentTitle(Title)
                                                .setContentIntent(ci)
                                                .setAutoCancel(true)
                                                .setDefaults(Notification.DEFAULT_ALL)
                                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                .setContentText(Content);

//                                Intent notificationIntent = new Intent(MyContext, MainActivity.class);

//                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);


                                mNotificationManager.notify(ID, mBuilder.build());
                            }
                        }

                        while (reset3.next()) {
//                            UpdateDeviceID =
                            stmt4.executeUpdate("Use MIGT_Automation\n" +
                                    "   update Messages\n" +
                                    "   SET Delivered = 1\n" +
                                    "   WHERE " + " MessageID = '" + reset3.getString("MessageID") + "' AND Delivered = 0;");

                            MObj = new MessageObject(reset3.getInt("MessageID"), reset3.getString("UserID"),
                                    reset3.getString("MessageTitle"), reset3.getString("MessageBody"), reset3.getDate("InsertDate").toString(), reset3.getInt("Delivered"), reset3.getBoolean("Critical") ? 1 : 0);
                            db.addMessage(MObj);

                            Title = reset3.getString("MessageTitle");
                            ID = reset3.getInt("MessageID");
                            Content = reset3.getString("MessageBody");
                            b = reset3.getBoolean("Critical");
                            if (b) {
                                Intent i = new Intent(MyContext, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyContext.startActivity(i);
                                mNotificationManager.cancelAll();
                                return true;
                            } else {

                                if (!isAppForeground(MyContext)) {

                                    Log.i("Notify", "is running");
                                    Intent nid = new Intent(MyContext, MainActivity.class);
                                    PendingIntent ci = PendingIntent.getActivity(MyContext, 0, nid, 0);

                                    mBuilder =
                                            new android.support.v4.app.NotificationCompat.Builder(MyContext)
                                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                                    .setSmallIcon(ContextCompat.getDrawable(MyContext, R.mipmap.ic_launcher))
                                                    .setContentTitle(Title)
                                                    .setContentIntent(ci)
                                                    .setAutoCancel(true)
                                                    .setDefaults(Notification.DEFAULT_ALL)
                                                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                    .setContentText(Content);

                                    mNotificationManager =
                                            (NotificationManager) MyContext.getSystemService(Context.NOTIFICATION_SERVICE);

                                    mNotificationManager.notify(ID, mBuilder.build());
                                }
                            }


                        }
                        FLag = true;
                    } else {
                        //No new Message
                        Log.i("User valid", "No new message");
                        FLag = true;
                    }
                } else {
                    if (reset1.getBoolean("DeviceIDAccepted")) {
                        share.SaveActivation(MyContext.getString(R.string.Active));
                    } else {
                        share.SaveActivation(MyContext.getString(R.string.NotActive));
                    }
                    FLag = true;
                }

            } else {
                Statement stmt5 = conn.createStatement();
                ResultSet reset5 = stmt5.executeQuery("Use MIGT_Automation\n" +
                        " SELECT * FROM TbL_Users WHERE " + "Username = '" +
                        username + "' AND  password = '" + password + "';");
                reset5.next();

                if (reset5.getString("DeviceID") == null) {
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

//            return false;

//                return reset.getString(0);
//                return (reset.getString("password").equals(password)) ? true: false;
//                return reset.getString("password");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("Return false1", "User invalid");
//            return false;
        } catch (Exception e) {
            e.getStackTrace();
            Log.i("Return false2", "User invalid");
//            return false;
        }

//        onPostExecute();
        return FLag;
    }


//    protected void onPostExecute() {
//        // dismiss progress dialog and update ui
//        Log.d("SQLite", "Begin to Show");
////        ShowMessages(GetMessagesfromDB());
//
//    }


//    public void GetMessagesfromDB() {
//
//
////        database =  db.getReadableDatabase(); execSQL("INSERT INTO Messages VALUES(1000,'IDUSER','TITLE23','BODY','2017-02-21',0); ")
//        SQLiteDatabase database = db.getWritableDatabase();
//        Cursor cursor = database.rawQuery("SELECT * " +
//                "FROM Messages ORDER BY MessageID DESC;", null);
//
//        // looping through all rows and adding to list
//        try {
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    do {
//                        MObj = new MessageObject();
//                        MObj.setMessageID(cursor.getInt(0));
//                        MObj.setUserID(cursor.getString(1));
//                        MObj.setMessageTitle(cursor.getString(2));
//                        MObj.setMessageBody(cursor.getString(3));
//                        MObj.setInsertDate(cursor.getString(4));
//                        MObj.setDelivered(Integer.valueOf(cursor.getString(5)));
//
//                        MESSAGES.add(MObj);
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//            }
//
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//
//
//    }

//    public void ShowMessages() {
//
//        MainActivity activity = MyActivity.get();
//        activity.setContentView(R.layout.messages_layout);
//        lv = (ListView) activity.findViewById(R.id.list1);
//
//
//        List<String> Mlist = new ArrayList<>(); //Messages List
//        List<String> Tlist = new ArrayList<>(); //Title List
//        List<String> Dlist = new ArrayList<>(); //Date List
//
//        if (Messages != null) {
//            Mlist.add(Messages.get(0).getMessageBody());
//            Tlist.add(Messages.get(0).getMessageTitle());
//            Dlist.add(Messages.get(0).getInsertDate());
//        }
//        // This is the array adapter, it takes the context of the activity as a
//        // first parameter, the type of list view as a second parameter and your
//        // array as a third parameter.
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                MyContext,
//                android.R.layout.simple_list_item_1,
//                Tlist);
//
//        lv.setAdapter(arrayAdapter);
//
//    }
}