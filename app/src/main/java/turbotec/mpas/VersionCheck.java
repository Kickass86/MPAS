package turbotec.mpas;

import android.app.IntentService;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public static final String REQUEST_DOWNLOAD = "myDownloadRequest";
    private final String ip = "192.168.1.13";
    private final int port = 80;
    URL url = null;
    private SharedPreferenceHandler share;
    private String requestString;
    private String downloadString;


    public VersionCheck() {
        super("Version Check");


        share = SharedPreferenceHandler.getInstance(this);

    }

    private boolean isLocalReachable() {

        boolean exists = false;

        try {
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 50000;   // 50000 milliseconds
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
        if (isLocalReachable()) {
            requestString = "http://192.168.1.13/Andr/CheckVersion.ashx?Value=";
            downloadString = "http://192.168.1.13/Andr/Download.ashx?Value=";

        } else {
            requestString = "https://mpas.migtco.com:3000/Andr/CheckVersion.ashx?Value=";
            downloadString = "https://mpas.migtco.com:3000/Andr/Download.ashx?Value=";

        }

//        String requestString = intent.getStringExtra(REQUEST_STRING);
        Log.v("Intent Service", "Check Request");
        int responseMessage = 0;
        String value = "Val1=" + share.GetDeviceID() + ",Val2=" + share.GetToken();
        requestString = requestString + new String(Base64.encode(value.getBytes(), Base64.DEFAULT));
        downloadString = downloadString + new String(Base64.encode(value.getBytes(), Base64.DEFAULT));
        requestString = requestString.replaceAll("\n", "");
        downloadString = downloadString.replaceAll("\n", "");

        try {


            url = new URL(requestString);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
//            c.setDoOutput(true);
            c.connect();

//
//            URL url = new URL(requestString);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.connect();
            int code = c.getResponseCode();
            String s = "";
            if (code == 200) {

//                String s = urlConnection.getResponseMessage();
                final InputStream is = c.getInputStream();

                if (is != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        s += line;
                }
                is.close();
            }
            c.disconnect();
            if (!(s.contains("Invalid") | s.contains("Error") | s.contains("Unable") | (s.contains("unexpected")))) {
                responseMessage = Integer.valueOf(s);
            } else {
                responseMessage = 0;
            }


        } catch (Exception e) {
            Log.w("HTTP:", e);
            responseMessage = 0;
        }


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(SplashActivity.VersionCheckReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_MESSAGE, responseMessage);
        broadcastIntent.putExtra(REQUEST_DOWNLOAD, downloadString);
        sendBroadcast(broadcastIntent);

    }

}