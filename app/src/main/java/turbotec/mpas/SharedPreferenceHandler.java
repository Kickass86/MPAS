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



    public void SaveDeviceID(String newDeviceID) {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.DeviceID), newDeviceID);
        editor.apply();

    }


//    public void SavePublickey(String pubkey) {
//
//        SharedPreferences sharedPref = menu.getSharedPreferences(menu.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(menu.getString(R.string.PublicKey), pubkey);
//        editor.apply();
//
//    }

//    public void SavePrivatekey(String privkey) {
//
//        SharedPreferences sharedPref = menu.getSharedPreferences(menu.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(menu.getString(R.string.PrivateKey), privkey);
//        editor.apply();
//
//    }


    public void SaveToken(String Token) {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.Token), Token);
        editor.apply();

    }


    public void SaveFileVersion(String version) {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.FileVersion), version);
        editor.apply();

    }




    public void SaveActivation(String Activated) {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.Activation), Activated);
        editor.apply();

    }


//    public void SaveUserID(String userID) {
//
//        SharedPreferences sharedPref = menu.getSharedPreferences(menu.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(menu.getString(R.string.UID), userID);
//        editor.apply();
//
//    }


    public void SaveLoginDetails(String email, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.Username), email);
        editor.putString(context.getString(R.string.Password), password);
        editor.apply();
    }


    public void SaveStatus(String newDeviceID) {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.Status), newDeviceID);
        editor.apply();

    }

    public String GetStatus() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.Status), context.getString(R.string.defaultValue));

    }


    public String GetToken() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.Token), context.getString(R.string.defaultValue));

    }


    public String GetFileVersion() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.FileVersion), context.getString(R.string.defaultValue));

    }


//    public String GetUserID() {
//
//        SharedPreferences sharedPref = menu.getSharedPreferences(menu.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
//        return sharedPref.getString(menu.getString(R.string.UID), menu.getString(R.string.defaultValue));
//
//    }

    public String GetActivation() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.Activation), context.getString(R.string.NotActive));

    }



    public String GetDeviceID() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.DeviceID), context.getString(R.string.defaultValue));

    }


//    public String GetPublicKey() {
//
//        SharedPreferences sharedPref = menu.getSharedPreferences(menu.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
//        return sharedPref.getString(menu.getString(R.string.PublicKey), menu.getString(R.string.defaultValue));
//
//    }

//    public String GetPrivateKey() {
//
//        SharedPreferences sharedPref = menu.getSharedPreferences(menu.getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
//        return sharedPref.getString(menu.getString(R.string.PrivateKey), menu.getString(R.string.defaultValue));
//
//    }




    public String GetUsername() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.Username), context.getString(R.string.defaultValue));

    }

    public String GetPassword() {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.Password), context.getString(R.string.defaultValue));

    }

}
