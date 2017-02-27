package turbotec.mpas;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

//AlarmReceiver

public class AlarmReceiver extends BroadcastReceiver {

    public static DatabaseHandler db;
    //    private SharedPreferenceHandler shareP;
    private SharedPreferenceHandler share;
    private WeakReference<MainActivity> MyActivity;
    //    private MainActivity MyActivity;
    private String Title;
    private String Content;
    private int ID;
    private Context mContext;
    private MessageObject MObj;


    public AlarmReceiver() {

//        this.MyActivity = new WeakReference<MainActivity>(activity);
    }


    @Override
    public void onReceive(final Context context, Intent intent) {
        mContext = context;
        mContext.sendBroadcast(new Intent("Alarm fire"));
//        Toast.makeText(context, "Alarm fired now!", Toast.LENGTH_SHORT).show();
        Log.v("AAA", "Alarm fired now!");

        share = SharedPreferenceHandler.getInstance(context);
        db = DatabaseHandler.getInstance(context);

        final String[] Userdata = new String[]{share.GetUserID(), share.GetDeviceID()};

        AsyncTask task = new AsyncTask<Object, Object, Boolean>() {


            @Override
            protected Boolean doInBackground(Object... params) {

                Boolean z = false;
                Object UserID = params[0];
                Object DeviceID = params[1];

                Connection conn;
                String driver = "net.sourceforge.jtds.jdbc.Driver";


                String name = "sa";
                String pass = "left4de@d";


                String connString = "jdbc:jtds:sqlserver://192.168.1.131:1433/MIGT_Automation;user=" + name + ";password=" + pass + ";";


                try {
                    Class.forName(driver).newInstance();

                    conn = DriverManager.getConnection(connString);
                    Log.w("wake up check", "check to invoke activity");
                    Statement stmt1 = conn.createStatement();
                    Statement stmt2 = conn.createStatement();
                    ResultSet reset1 = stmt1.executeQuery("Use MIGT_Automation\n" +
                            "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.[User ID] = TbL_Users.id \n" +
                            " WHERE Messages.[User ID] = '" +
                            UserID + "' AND Messages.Delivered = 0" +
                            " AND TbL_Users.DeviceID = '" + DeviceID + "';");

//                    b = reset1.next();

                    if (reset1.next()) {
                        MObj = new MessageObject(reset1.getInt("Message ID"), reset1.getString("User ID"),
                                reset1.getString("Message Title"), reset1.getString("Message Body"), reset1.getDate("Insert Date").toString(), reset1.getInt("Delivered"));
                        db.addMessage(MObj);

                        stmt2.executeUpdate("Use MIGT_Automation\n" +
                                "   update Messages\n" +
                                "   SET Delivered = 1\n" +
                                "   WHERE " + "[Message ID] = '" + reset1.getString("Message ID") + "' AND Delivered = 0;");
                        Log.i("User valid", "New message added");
//                        Intent ii = new Intent("Notification fire");
//                        ii.putExtra("Title",reset1.getString("Message Title"));
//                        ii.putExtra("ID",reset1.getInt("Message ID"));
//                        ii.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
                        Title = reset1.getString("Message Title");
                        ID = reset1.getInt("Message ID");
                        Content = reset1.getString("Message Body");


                        android.support.v4.app.NotificationCompat.Builder mBuilder =
                                new android.support.v4.app.NotificationCompat.Builder(context)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle(Title)
                                        .setContentText(Content);

                        NotificationManager mNotificationManager =
                                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        Log.i("Notify", "is running");
                        mNotificationManager.notify(ID, mBuilder.build());


//                        mContext.sendBroadcast(ii);
                        while (reset1.next()) {
//                            UpdateDeviceID =
                            stmt2.executeUpdate("Use MIGT_Automation\n" +
                                    "   update Messages\n" +
                                    "   SET Delivered = 1\n" +
                                    "   WHERE " + "[Message ID] = '" + reset1.getString("Message ID") + "' AND Delivered = 0;");

                            MObj = new MessageObject(reset1.getInt("Message ID"), reset1.getString("User ID"),
                                    reset1.getString("Message Title"), reset1.getString("Message Body"), reset1.getDate("Insert Date").toString(), reset1.getInt("Delivered"));
                            db.addMessage(MObj);
//                            Intent ii2 = new Intent("Notification fire");
//                            ii2.putExtra("Title",reset1.getString("Message Title"));
//                            ii2.putExtra("ID",reset1.getInt("Message ID"));
//                            ii2.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
//                            mContext.sendBroadcast(ii2);

                            Title = reset1.getString("Message Title");
                            ID = reset1.getInt("Message ID");
                            Content = reset1.getString("Message Body");


                            mBuilder =
                                    new android.support.v4.app.NotificationCompat.Builder(context)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle(Title)
                                            .setContentText(Content);

                            mNotificationManager =
                                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                            Log.i("Notify", "is running");
                            mNotificationManager.notify(ID, mBuilder.build());


                        }

                        z = true;

                    }


                } catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return z;
            }
        };

        Boolean b = false;
        try {
            b = (Boolean) task.execute(Userdata).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

//        shareP = myActivity.sp;

//        this.sp = new WeakReference<SharedPreferenceHandler>(myActivity.sp);
//        SharedPreferenceHandler sharedp = this.sp.get();
        //Check Database


//        boolean result = false;
//
//        Userdata = new String[]{shareP.GetUsername(), shareP.GetPassword(), shareP.GetDeviceID()};
//
////        Application ap = this. getApplication();
//
//        NetworkAsyncTask task = new NetworkAsyncTask(context, (MyActivity.get()));
//        try {
//            result = task.execute(Userdata).get();
//        } catch (ExecutionException | InterruptedException ei) {
//            ei.printStackTrace();
//        }
    }


}

