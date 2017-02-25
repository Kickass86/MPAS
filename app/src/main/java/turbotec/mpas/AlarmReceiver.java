package turbotec.mpas;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.ref.WeakReference;

//AlarmReceiver

public class AlarmReceiver extends BroadcastReceiver {

    private SharedPreferenceHandler shareP;
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

        shareP = SharedPreferenceHandler.getInstance(context);
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

