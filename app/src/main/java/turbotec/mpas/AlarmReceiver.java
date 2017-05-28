package turbotec.mpas;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.util.Log;

//import java.lang.ref.WeakReference;

//AlarmReceiver

public class AlarmReceiver extends BroadcastReceiver {

    //    private static DatabaseHandler db;
//    private Boolean b = false;
    //    private WeakReference<MainActivity> MyActivity;
    //    private MainActivity MyActivity;
    private static final String BOOT_COMPLETED =
            "android.intent.action.BOOT_COMPLETED";
    private static final String QUICKBOOT_POWERON =
            "android.intent.action.QUICKBOOT_POWERON";

//    private SharedPreferenceHandler share;

    private Context mContext;
//    private MessageObject MObj;


    public AlarmReceiver() {


//        this.MyActivity = new WeakReference<MainActivity>(activity);
    }

    private Boolean CheckNetworkAvailability() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }



    @Override
    public void onReceive(final Context context, Intent intent) {


        mContext = context;
//        share = SharedPreferenceHandler.getInstance(context);

        String action = intent.getAction();
        if (action.equals(BOOT_COMPLETED) ||
                action.equals(QUICKBOOT_POWERON)) {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.setAction("Alarm");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int interval = 60000;
//            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
        }


        if (action.equals("Alarm") && (CheckNetworkAvailability())) {
//        if (intent.getAction().equals("Alarm")) {


//            mContext.sendBroadcast(new Intent("Alarm fire"));

            Log.v("AAA", "Alarm fired now!");

            //                b = (Boolean) task.execute(Userdata).get();
            NetworkAsyncTask task1 = new NetworkAsyncTask(mContext);
//                task1.execute(Userdata).get();
            task1.execute();


        }

    }
}

