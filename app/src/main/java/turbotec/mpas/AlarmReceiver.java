package turbotec.mpas;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.concurrent.ExecutionException;

//import java.lang.ref.WeakReference;

//AlarmReceiver

public class AlarmReceiver extends BroadcastReceiver {

    //    private static DatabaseHandler db;
//    private Boolean b = false;
    //    private WeakReference<MainActivity> MyActivity;
    //    private MainActivity MyActivity;
    private SharedPreferenceHandler share;

    private Context mContext;
//    private MessageObject MObj;


    public AlarmReceiver() {


//        this.MyActivity = new WeakReference<MainActivity>(activity);
    }

    public Boolean CheckNetworkAvailability() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }



    @Override
    public void onReceive(final Context context, Intent intent) {


        mContext = context;
        share = SharedPreferenceHandler.getInstance(context);


        if (intent.getAction().equals("Alarm") && (CheckNetworkAvailability())) {
//        if (intent.getAction().equals("Alarm")) {

//            mContext.sendBroadcast(new Intent("Alarm fire"));
//        Toast.makeText(context, "Alarm fired now!", Toast.LENGTH_SHORT).show();
            Log.v("AAA", "Alarm fired now!");

//            final SharedPreferenceHandler share = SharedPreferenceHandler.getInstance(context);
//            db = DatabaseHandler.getInstance(context);

//            final String[] Userdata = new String[]{share.GetUsername(), share.GetPassword(), share.GetDeviceID(), share.GetToken()};


//            final AsyncTask task = new AsyncTask<Object, Boolean, Boolean>() {
//
//
//                @Override
//                protected Boolean doInBackground(Object... params) {
//
//                    Boolean z = false;
//                    Object UserID = params[0];
//                    Object DeviceID = params[1];
//                    Object Activation = params[2];
//
//                    Connection conn;
//                    String driver = "net.sourceforge.jtds.jdbc.Driver";
//
//
//                    String name = "sa";
//                    String pass = "left4de@d";
//
//
//                    String connString = "jdbc:jtds:sqlserver://192.168.1.131:1433/MIGT_Automation;user=" + name + ";password=" + pass + ";";
//
//
//                    try {
//                        android.support.v4.app.NotificationCompat.Builder mBuilder;
//                        NotificationManager mNotificationManager =
//                                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                        Class.forName(driver).newInstance();
//
//                        conn = DriverManager.getConnection(connString);
//                        Log.w("wake up check", "check to invoke activity");
//                        Statement stmt1 = conn.createStatement();
//                        Statement stmt2 = conn.createStatement();
//                        ResultSet reset1 = stmt1.executeQuery("Use MIGT_Automation\n" +
//                                "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.UserID = TbL_Users.id \n" +
//                                " WHERE Messages.UserID = '" +
//                                UserID + "' AND Messages.Delivered = 0" +
//                                " AND TbL_Users.DeviceID = '" + DeviceID + "' AND TbL_Users.DeviceIDAccepted = 1;");
//
////                    b = reset1.next();
//
//                        if (reset1.next()) {
//
//                            if(share.GetActivation().equals(mContext.getString(R.string.NotActive)))
//                            {
//                                share.SaveActivation(mContext.getString(R.string.Active));
//                            }
//                            MObj = new MessageObject(reset1.getInt("MessageID"), reset1.getString("UserID"),
//                                    reset1.getString("MessageTitle"), reset1.getString("MessageBody"), reset1.getDate("InsertDate").toString(), reset1.getInt("Delivered"), reset1.getBoolean("Critical"));
//                            db.addMessage(MObj);
//
//                            stmt2.executeUpdate("Use MIGT_Automation\n" +
//                                    "   update Messages\n" +
//                                    "   SET Delivered = 1\n" +
//                                    "   WHERE " + " MessageID = '" + reset1.getString("MessageID") + "' AND Delivered = 0;");
//                            Log.i("User valid", "New message added");
////                        Intent ii = new Intent("Notification fire");
////                        ii.putExtra("Title",reset1.getString("MessageTitle"));
////                        ii.putExtra("ID",reset1.getInt("MessageID"));
////                        ii.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
//                            Title = reset1.getString("MessageTitle");
//                            ID = reset1.getInt("MessageID");
//                            Content = reset1.getString("MessageBody");
//                            b = reset1.getBoolean("Critical");
//                            if (b) {
//                                Intent i = new Intent(context, MainActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(i);
//                                mNotificationManager.cancelAll();
//                                return true;
//                            } else {
//                                if (!InForeground) {
//
//                                    Intent nid = new Intent(mContext, MainActivity.class);
//                                    PendingIntent ci = PendingIntent.getActivity(mContext, 0, nid, 0);
//
//                                    mBuilder =
//                                            new android.support.v4.app.NotificationCompat.Builder(context)
//                                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                                    .setContentTitle(Title)
//                                                    .setContentIntent(ci)
//                                                    .setAutoCancel(true)
//                                                    .setDefaults(Notification.DEFAULT_ALL)
//                                                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                                                    .setContentText(Content);
//
//                                    Intent notificationIntent = new Intent(mContext, MainActivity.class);
//
////                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
////                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//
//                                    Log.i("Notify", "is running");
//                                    mNotificationManager.notify(ID, mBuilder.build());
//                                }
//                            }
//
////                        mContext.sendBroadcast(ii);
//                            while (reset1.next()) {
////                            UpdateDeviceID =
//                                stmt2.executeUpdate("Use MIGT_Automation\n" +
//                                        "   update Messages\n" +
//                                        "   SET Delivered = 1\n" +
//                                        "   WHERE " + " MessageID = '" + reset1.getString("MessageID") + "' AND Delivered = 0;");
//
//                                MObj = new MessageObject(reset1.getInt("MessageID"), reset1.getString("UserID"),
//                                        reset1.getString("MessageTitle"), reset1.getString("MessageBody"), reset1.getDate("InsertDate").toString(), reset1.getInt("Delivered"), reset1.getBoolean("Critical"));
//                                db.addMessage(MObj);
////                            Intent ii2 = new Intent("Notification fire");
////                            ii2.putExtra("Title",reset1.getString("MessageTitle"));
////                            ii2.putExtra("ID",reset1.getInt("MessageID"));
////                            ii2.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
////                            mContext.sendBroadcast(ii2);
//
//                                Title = reset1.getString("MessageTitle");
//                                ID = reset1.getInt("MessageID");
//                                Content = reset1.getString("MessageBody");
//                                b = reset1.getBoolean("Critical");
//                                if (b) {
//                                    Intent i = new Intent(context, MainActivity.class);
//                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(i);
//                                    mNotificationManager.cancelAll();
//                                    return true;
//                                } else {
//
//                                    if (!InForeground) {
//
//                                        Intent nid = new Intent(mContext, MainActivity.class);
//                                        PendingIntent ci = PendingIntent.getActivity(mContext, 0, nid, 0);
//
//                                        mBuilder =
//                                                new android.support.v4.app.NotificationCompat.Builder(context)
//                                                        .setSmallIcon(R.mipmap.ic_launcher)
//                                                        .setContentTitle(Title)
//                                                        .setContentIntent(ci)
//                                                        .setAutoCancel(true)
//                                                        .setDefaults(Notification.DEFAULT_ALL)
//                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                                                        .setContentText(Content);
//
//                                        mNotificationManager =
//                                                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                                        Log.i("Notify", "is running");
//                                        mNotificationManager.notify(ID, mBuilder.build());
//                                    }
//                                }
//
//                            }
//
//                            z = true;
//
//                        }
//
//
//                    } catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    return z;
//                }
//            };


            try {
//                b = (Boolean) task.execute(Userdata).get();
                NetworkAsyncTask task1 = new NetworkAsyncTask(mContext);
//                task1.execute(Userdata).get();
                task1.execute().get();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


//            mContext.sendBroadcast(new Intent("Alarm fire"));


//        shareP = myActivity.sp;

//        this.sp = new WeakReference<SharedPreferenceHandler>(myActivity.sp);
//        SharedPreferenceHandler sharedp = this.sp.get();
            //Check Database


//        boolean Titles = false;
//
//        Userdata = new String[]{shareP.GetUsername(), shareP.GetPassword(), shareP.GetDeviceID()};
//
////        Application ap = this. getApplication();
//
//        NetworkAsyncTask task = new NetworkAsyncTask(context, (MyActivity.get()));
//        try {
//            Titles = task.execute(Userdata).get();
//        } catch (ExecutionException | InterruptedException ei) {
//            ei.printStackTrace();
//        }
        }

    }
}

