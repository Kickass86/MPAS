package turbotec.mpas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        Toast.makeText(context, "Alarm fired now!", Toast.LENGTH_SHORT).show();
        Log.v("AAA", "Alarm fired now!");
        //Check Database
    }

}
