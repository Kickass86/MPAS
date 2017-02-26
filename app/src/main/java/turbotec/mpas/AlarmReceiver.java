package turbotec.mpas;


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

    //    private SharedPreferenceHandler shareP;
    private SharedPreferenceHandler share;
    private WeakReference<MainActivity> MyActivity;
    //    private MainActivity MyActivity;
    private String[] Userdata;


    public AlarmReceiver() {

//        this.MyActivity = new WeakReference<MainActivity>(activity);
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        context.sendBroadcast(new Intent("Alarm fire"));
//        Toast.makeText(context, "Alarm fired now!", Toast.LENGTH_SHORT).show();
        Log.v("AAA", "Alarm fired now!");

        share = SharedPreferenceHandler.getInstance(context);

        final String[] Userdata = new String[]{share.GetUserID(), share.GetDeviceID()};

        AsyncTask task = new AsyncTask<Object, Object, Boolean>() {


            @Override
            protected Boolean doInBackground(Object... params) {

                Boolean b = false;
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
                    ResultSet reset1 = stmt1.executeQuery("Use MIGT_Automation\n" +
                            "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.[User ID] = TbL_Users.id \n" +
                            " WHERE Messages.[User ID] = '" +
                            UserID + "' AND Messages.Delivered = 0" +
                            " AND TbL_Users.DeviceID = '" + DeviceID + "';");

                    b = reset1.next();


                } catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return b;
            }
        };

        Boolean b = false;
        try {
            b = (Boolean) task.execute(Userdata).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (b) {
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
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

