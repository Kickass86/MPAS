package turbotec.mpas;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class VersionCheck extends IntentService {

    public static final String RESPONSE_MESSAGE = "myResponseMessage";
    private final String ip = "192.168.1.13";
    private final int port = 80;
    URL url = null;
    private String requestString;


    public VersionCheck() {
        super("Version Check");
        if (isLocalReachable()) {
            requestString = "http://192.168.1.13/Andr/Download.ashx";

        } else {
            requestString = "https://mpas.migtco.com:3000/Andr/Download.ashx";

        }

    }

    private boolean isLocalReachable() {

        boolean exists = false;

        try {
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 200;   // 200 milliseconds
            sock.connect(sockaddr, timeoutMs);
            exists = true;

            sock.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        String requestString = intent.getStringExtra(REQUEST_STRING);
        Log.v("Intent Service", "Check Request");
        String responseMessage = "";

        try {

            url = new URL(requestString);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "app.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();//till here, it works fine - .apk is download to my sdcard in download file

            Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(PATH + "app.apk"))
                    .setType("application/android.com.app");
            startActivity(promptInstall);//installation is not working


        } catch (IOException e) {
            Log.w("HTTP3:", e);
            responseMessage = e.getMessage();
        } catch (Exception e) {
            Log.w("HTTP4:", e);
            responseMessage = e.getMessage();
        }


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(SplashActivity.VersionCheckReciever.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_MESSAGE, responseMessage);
        sendBroadcast(broadcastIntent);

    }

}