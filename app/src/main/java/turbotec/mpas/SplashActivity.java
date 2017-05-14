package turbotec.mpas;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;


public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private final String LOG_TAG = "AppUpgrade";
    private ProgressBar bar;
    private String appURI = "";
    private File myFile;
    private int i = 0;
    private boolean isVCRregistered = false;
    private int versionCode;
    private Uri Download_Uri;
    private VersionCheckReceiver receiver;
    private long downloadReference;
    private DownloadManager downloadManager;
    //broadcast receiver to get notification about ongoing downloads
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //check if the broadcast message is for our Enqueued download

                DownloadManager dMgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                Cursor c = dMgr.query(new DownloadManager.Query().setFilterById(referenceId));

                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //Download completed, celebrate


                        if (downloadReference == referenceId) {

                            Log.v(LOG_TAG, "Downloading of the new app version complete");
                            //start the installation of the latest version
//                            File outputFile = new File(Environment.DIRECTORY_DOWNLOADS + "/MyAndroidApp.apk");
//                            if(outputFile.exists()) {
                            Intent installIntent = new Intent(Intent.ACTION_VIEW);
//                    installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(downloadReference),
//                            "application/vnd.android.package-archive");
                            installIntent.setDataAndType(Uri.fromFile(myFile),
                                    "application/vnd.android.package-archive");
//                                installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(downloadReference),
//                                        "application/vnd.android.package-archive");
//                    installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(downloadReference),
//                                        downloadManager.getMimeTypeForDownloadedFile(downloadReference));
                            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(installIntent);

                            unregisterReceiver(this);
                            finish();
                            return;
                        }


//                        }


                    } else {
                        int reason = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
                        Log.d("Failed!", "Download not correct, status [" + status + "] reason [" + reason + "]");

                    }
                }


            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        bar = (ProgressBar) findViewById(R.id.progressBar);

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
        IntentFilter filter1 = new IntentFilter(VersionCheckReceiver.PROCESS_RESPONSE);
        filter1.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new VersionCheckReceiver();
        registerReceiver(receiver, filter1);
        isVCRregistered = true;

        //Broadcast receiver for the download manager
        IntentFilter filter2 = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter2);

        Intent in = new Intent(this, VersionCheck.class);
        startService(in);

    }

    @Override
    protected void onStart() {
        super.onStart();
        bar.setProgress(0);
    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(downloadReceiver);
        if (isVCRregistered) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    public class VersionCheckReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(VersionCheckReceiver.PROCESS_RESPONSE)) {

//                String responseMessage = intent.getStringExtra(VersionCheck.RESPONSE_MESSAGE);
                int latestVersion = intent.getIntExtra(VersionCheck.RESPONSE_MESSAGE, 0);
                appURI = intent.getStringExtra(VersionCheck.REQUEST_DOWNLOAD);
                Log.v(LOG_TAG, latestVersion + "");


                try {
//

                    if ((latestVersion != 0) & (latestVersion != (versionCode))) {


                        //check if we need to upgrade?
                        myFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/MPAS-V" + latestVersion + ".apk");
                        if (myFile.exists()) // file downloaded before
                        {
                            Intent installIntent = new Intent(Intent.ACTION_VIEW);

                            installIntent.setDataAndType(Uri.fromFile(myFile),
                                    "application/vnd.android.package-archive");

                            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(installIntent);

                            if (isVCRregistered) {
                                unregisterReceiver(VersionCheckReceiver.this);
                                isVCRregistered = false;
                            }
                            finish();
                            return;
                        }

                        //oh yeah we do need an upgrade, let the user know send an alert message
//                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//                        builder.setMessage("There is newer version of this application available, click OK to upgrade now")
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    //if the user agrees to upgrade
//                                    public void onClick(DialogInterface dialog, int id) {
                        //start downloading the file using the download manager
                        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        Download_Uri = Uri.parse(appURI);
                        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

//                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//                            request.setAllowedOverRoaming(false);
//                            request.setTitle("MyAndroidApp.apk");
//                            request.setDestinationInExternalFilesDir(SplashActivity.this, Environment.DIRECTORY_DOWNLOADS, "MyAndroidApp.apk");
                        request.setDestinationUri(Uri.fromFile(myFile));
                        request.setMimeType("application/vnd.android.package-archive");
                        downloadReference = downloadManager.enqueue(request);

//                        BroadcastReceiver onComplete = new BroadcastReceiver() {
//                            public void onReceive(Context ctxt, Intent intent) {
//                                Intent install = new Intent(Intent.ACTION_VIEW);
//                                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                install.setDataAndType(Download_Uri,
//                                        downloadManager.getMimeTypeForDownloadedFile(downloadReference));
//                                startActivity(install);
//
//                                unregisterReceiver(this);
//                                finish();
//                            }
//                        };
////                            register receiver for when .apk download is compete
//                        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                boolean downloading = true;

                                while (downloading) {

                                    DownloadManager.Query q = new DownloadManager.Query();
                                    q.setFilterById(downloadReference);

                                    Cursor cursor = downloadManager.query(q);
                                    cursor.moveToFirst();
                                    int bytes_downloaded = cursor.getInt(cursor
                                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                        downloading = false;
                                    }

                                    final double dl_progress = (bytes_downloaded / bytes_total) * 100;

                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            bar.setProgress((int) dl_progress);

                                        }
                                    });

                                    cursor.close();
                                }

                            }
                        }).start();
//                                    }
//                                });
//                                .setNegativeButton("Remind Later", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        // User cancelled the dialog
//                                    }
//                                });
                        //show the alert message
//                        builder.create().show();
//                    }

                    } else {
                        CountDownTimer mCountDownTimer;
                        i = 0;

                        bar.setProgress(i);
                        mCountDownTimer = new CountDownTimer(4000, 500) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                                i++;
                                bar.setProgress(i * 12);

                            }

                            @Override
                            public void onFinish() {
                                //Do what you want
                                i++;
                                bar.setProgress(i);
                                bar.setProgress(100);

                                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                SplashActivity.this.startActivity(mainIntent);
                                if (isVCRregistered) {
                                    unregisterReceiver(VersionCheckReceiver.this);
                                    isVCRregistered = false;
                                }
                                unregisterReceiver(downloadReceiver);
                                SplashActivity.this.finish();
//
                            }
                        };
                        mCountDownTimer.start();
                    }


                } catch (Exception e) {

                    e.printStackTrace();


                    CountDownTimer mCountDownTimer;
                    i = 0;

                    bar.setProgress(i);
                    mCountDownTimer = new CountDownTimer(4000, 500) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                            i++;
                            bar.setProgress(i * 12);

                        }

                        @Override
                        public void onFinish() {
                            //Do what you want
                            i++;
                            bar.setProgress(i);
                            bar.setProgress(100);

                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                            SplashActivity.this.startActivity(mainIntent);
                            if (isVCRregistered) {
                                unregisterReceiver(VersionCheckReceiver.this);
                                isVCRregistered = false;
                            }
                            unregisterReceiver(downloadReceiver);
                            SplashActivity.this.finish();
//
                        }
                    };
                    mCountDownTimer.start();


                } finally {


//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                        SplashActivity.this.startActivity(mainIntent);
//                        SplashActivity.this.finish();
//                    }
//                }, SPLASH_DISPLAY_LENGTH);

                }
            }

        }

    }

}


