package turbotec.mpas;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.ExecutionException;



public class AlarmReceiver extends BroadcastReceiver {

    private SharedPreferenceHandler shareP;
    private SharedPreferenceHandler share;
    //    private WeakReference<SharedPreferenceHandler>  sp;
//    private MainActivity myActivity;
    private String[] Userdata;


    public AlarmReceiver() {


    }


    @Override
    public void onReceive(Context context, Intent intent) {

//        Toast.makeText(context, "Alarm fired now!", Toast.LENGTH_SHORT).show();
        Log.v("AAA", "Alarm fired now!");

        shareP = SharedPreferenceHandler.getInstance(context);
//        shareP = myActivity.sp;

//        this.sp = new WeakReference<SharedPreferenceHandler>(myActivity.sp);
//        SharedPreferenceHandler sharedp = this.sp.get();
        //Check Database

        boolean result = false;

        Userdata = new String[]{shareP.GetUsername(), shareP.GetPassword(), shareP.GetDeviceID()};

//        Application ap = this. getApplication();

        NetworkAsyncTask task = new NetworkAsyncTask(context);
        try {
            result = task.execute(Userdata).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
    }


}
