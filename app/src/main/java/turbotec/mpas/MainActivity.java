package turbotec.mpas;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {


    @SuppressWarnings("deprecation")
    public static String getUniquePsuedoID() {

        String m_szDevIDShort;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.SUPPORTED_ABIS[0].length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        }
        else
        {

            m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        }


        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return  new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public String getDeviceID() {

//        Context context = getActivity();
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);


        if (sharedPref.contains(getString(R.string.UID))) {
            return sharedPref.getString(getString(R.string.UID), "NO ID");
        }
        else{
            return null;
        }
    }

    public Boolean CheckInternetAvailability() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    public void SaveID(String newID) {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.ID), newID);
        editor.apply();

    }


    public String GetID() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.PREFERENCE_FILE), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.ID), getString(R.string.defaultValue));


    }

    public void SaveLoginDetails(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.Username), email);
        editor.putString(getString(R.string.Password), password);
        editor.apply();
    }


    public String GetUsername() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.Username), getString(R.string.defaultValue));

    }


    public String GetPassword() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.Username_Password_File), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.Password), getString(R.string.defaultValue));

    }



}
