package turbotec.mpas;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHandler {

    private static SharedPreferenceHandler instance;
    private Context context = null;


//    public SharedPreferenceHandler() {
//    }

    private SharedPreferenceHandler(Context act) {
        context = act;
    }

    public static SharedPreferenceHandler getInstance(Context mContext) {
        if (instance == null) {
            instance = new SharedPreferenceHandler(mContext);
        }
        return instance;
    }

//    public void SaveID(String newID) {
//
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(context.getString(R.string.ID), newID);
//        editor.apply();
//
//    }

    public void SaveDeviceID(String newDeviceID) {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.DeviceID), newDeviceID);
        editor.apply();

    }


    public void SaveUserID(String userID) {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.UID), userID);
        editor.apply();

    }


    public String GetUserID() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.UID), context.getString(R.string.defaultValue));

    }



    public String GetDeviceID() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.DeviceID), context.getString(R.string.defaultValue));

    }


    public void SaveLoginDetails(String email, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.Username), email);
        editor.putString(context.getString(R.string.Password), password);
        editor.apply();
    }

    public String GetUsername() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.Username), context.getString(R.string.defaultValue));

    }

    public String GetPassword() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.Password), context.getString(R.string.defaultValue));

    }

}
