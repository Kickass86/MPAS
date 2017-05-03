package turbotec.mpas;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private final String LOG_TAG = "AppUpgrade";
    String appURI = "";
    private int versionCode;
    private VersionCheckReciever receiver;
    private long downloadReference;
    private DownloadManager downloadManager;
    //broadcast receiver to get notification about ongoing downloads
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadReference == referenceId) {

                Log.v(LOG_TAG, "Downloading of the new app version complete");
                //start the installation of the latest version
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(downloadReference),
                        "application/vnd.android.package-archive");
                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(installIntent);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //get the app version Name for display
        String version = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;
        //display the current version in a TextView
        TextView versionText = (TextView) findViewById(R.id.versionName);
        versionText.setText(version);


        ///////////////////////////////////////////////////////////////////////////////
        IntentFilter filter = new IntentFilter(VersionCheckReciever.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new VersionCheckReciever();
        registerReceiver(receiver, filter);

        //Broadcast receiver for the download manager
        filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);


    }

    public class VersionCheckReciever extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {

            String reponseMessage = intent.getStringExtra(VersionCheck.RESPONSE_MESSAGE);
            Log.v(LOG_TAG, reponseMessage);

            //parse the JSON response
            JSONObject responseObj;
            try {
                responseObj = new JSONObject(reponseMessage);
                boolean success = responseObj.getBoolean("success");
                //if the reponse was successful check further
                if (success) {
                    //get the latest version from the JSON string
                    int latestVersion = responseObj.getInt("latestVersion");
                    //get the lastest application URI from the JSON string
                    appURI = responseObj.getString("appURI");
                    //check if we need to upgrade?
                    if (latestVersion > versionCode) {
                        //oh yeah we do need an upgrade, let the user know send an alert message
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                        builder.setMessage("There is newer version of this application available, click OK to upgrade now?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    //if the user agrees to upgrade
                                    public void onClick(DialogInterface dialog, int id) {
                                        //start downloading the file using the download manager
                                        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                        Uri Download_Uri = Uri.parse(appURI);
                                        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                                        request.setAllowedOverRoaming(false);
                                        request.setTitle("My Andorid App Download");
                                        request.setDestinationInExternalFilesDir(SplashActivity.this, Environment.DIRECTORY_DOWNLOADS, "MyAndroidApp.apk");
                                        downloadReference = downloadManager.enqueue(request);
                                    }
                                })
                                .setNegativeButton("Remind Later", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        //show the alert message
                        builder.create().show();
                    }

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                            SplashActivity.this.startActivity(mainIntent);
                            SplashActivity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}


