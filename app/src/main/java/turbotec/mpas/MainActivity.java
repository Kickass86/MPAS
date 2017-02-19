package turbotec.mpas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private EditText UsernameView;
    private EditText PasswordView;
    private Button RegisterButton;
    private String UserName;
    private String PassWord;
    private String MyID;
    private String Name;


    @SuppressWarnings("deprecation")
    public static String getUniquePsuedoID() {

        String m_szDevIDShort;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            m_szDevIDShort = "46cd" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.SUPPORTED_ABIS[0].length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        }
        else
        {
            m_szDevIDShort = "46cd" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
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

        UserName = GetUsername();
        PassWord = GetPassword();
        MyID = GetID();


        UsernameView = (EditText) findViewById(R.id.editText2);
        PasswordView = (EditText) findViewById(R.id.editText);
        RegisterButton = (Button) findViewById(R.id.button);



        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Username = UsernameView.getText().toString();
                String Password = PasswordView.getText().toString();
                String DeviceID = getUniquePsuedoID();

                Name = attemptLogin(Username, Password, DeviceID);
//                Log.i("Successful Login ", "Welcome " + Name);
            }
        });

        if ((!UserName.equals(getString(R.string.defaultValue))) && ((!PassWord.equals(getString(R.string.defaultValue)))) && (!MyID.equals(getString(R.string.defaultValue)))) {
            setContentView(R.layout.waiting_layout);
            boolean result = false;
            String[] Userdetails = {UserName, PassWord, MyID};
            NetworkTask task = new NetworkTask();
            try {
                result = task.execute(Userdetails).get();
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
            if (result) {
                setContentView(R.layout.activity_main_logged_in);
            } else {
                //Error On LogIn
                Log.w("ERROR", "Wrong Information");
                Toast.makeText(this.getApplicationContext(),
                        "Wrong Info", Toast.LENGTH_LONG).show();
                final Intent intent = getIntent();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                            MainActivity.this.finish();
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }

        }


    }


    public String attemptLogin(String username, String password, String DeviceID) {

        boolean result = false;

        UsernameView.setError(null);
        PasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            PasswordView.setError(getString(R.string.error_invalid_password));
            focusView = PasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            UsernameView.setError(getString(R.string.error_field_required));
            focusView = UsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // save data in local shared preferences
            String[] Userdetails = {username, password, DeviceID};
            NetworkTask task = new NetworkTask();
            try {
                result = task.execute(Userdetails).get();
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
            if (result) {

                SaveID(DeviceID);
                SaveLoginDetails(username, password);
                setContentView(R.layout.activity_main_logged_in);
            } else {
                //Error On LogIn
                Log.w("ERROR", "Wrong Information");
                Toast.makeText(this.getApplicationContext(),
                        "Wrong Info", Toast.LENGTH_LONG).show();
                final Intent intent = getIntent();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                            MainActivity.this.finish();
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }


        }
        return null;



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

    private class NetworkTask extends AsyncTask<Object, Object, Boolean> {


        protected void onPreExecute() {
            //display progress dialog.

        }

        protected Boolean doInBackground(Object... userdetails) {

            Object username = userdetails[0];
            Object password = userdetails[1];
            Object DeviceID = userdetails[2];

            Connection conn;
            try {
                String driver = "net.sourceforge.jtds.jdbc.Driver";


                String name = "sa";
                String pass = "left4de@d";


                String connString = "jdbc:jtds:sqlserver://192.168.1.131:1433/MIGT_Automation;user=" + name + ";password=" + pass + ";";


                try {
                    Class.forName(driver).newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                conn = DriverManager.getConnection(connString);
                Log.w("Connection", "open");
                Statement stmt = conn.createStatement();
                ResultSet reset = stmt.executeQuery("Use MIGT_Automation\n" +
                        " SELECT * FROM TbL_Users WHERE " + "Username = '" +
                        username + "' AND " + " password = '" + password + "';");
//                ResultSet reset = stmt.executeQuery("SELECT * FROM TbL_Users WHERE " + "Username = '" + username +"';");

                Boolean b = reset.next();
                if (b) {
                    reset = stmt.executeQuery("Use MIGT_Automation\n" +
                            " SELECT * FROM TbL_Users WHERE " + "Username = '" +
                            username + "' AND " + " password = '" + password + "' AND DeviceID = '" + DeviceID + "';");

                    if (reset.next()) {
                        int UpdateDeviceID = stmt.executeUpdate("Use MIGT_Automation\n" +
                                "   update TbL_Users\n" +
                                "   SET DeviceID = '" + getUniquePsuedoID() + "'\n" +
                                "   WHERE " + "Username = '" +
                                username + "' AND " + " password = '" + password + "';");
                        if (UpdateDeviceID != 0) {
                            Log.i("SQL Database", "DeviceID Updated Successfully");
                            return true;
                        }
                    } else {
                        return false;
                    }

                } else {
                    return false;
                }
                return false;

//                return reset.getString(0);
//                return (reset.getString("password").equals(password)) ? true: false;
//                return reset.getString("password");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.getStackTrace();
                return false;
            }
        }


        protected void onPostExecute(Boolean IsAuthorized) {
            // dismiss progress dialog and update ui

        }
    }



}
