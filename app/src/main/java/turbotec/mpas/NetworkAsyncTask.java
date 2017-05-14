package turbotec.mpas;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

//import org.apache.commons.codec.binary.Base64;

class NetworkAsyncTask extends AsyncTask<Object, Void, String> {

    private final Context MyContext;
    private final String ip = "192.168.1.13";
    private final int port = 80;
    private KeyPairGenerator kpg;
    private KeyPair kp;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private byte[] encryptedBytes, decryptedBytes;
    private Cipher cipher, cipher1;
    private String encrypted, decrypted;
    private DatabaseHandler db;
    private SharedPreferenceHandler share;
    private SQLiteDatabase database;
    private String[] MIDs = new String[10000];
    private String SOAP_ACTION_CHECK;
    private String SOAP_ACTION_DELIVERED;
    private String OPERATION_NAME_CHECK;
    private String OPERATION_NAME_DELIVERED;

    private boolean isCritical = false;
    //    private  final String SOAP_ACTION = "http://192.168.1.13/Delivered";
//    private  final String OPERATION_NAME = "Delivered";
//    private  final String WSDL_TARGET_NAMESPACE = "http://192.168.1.13/";
//    private  final String SOAP_ADDRESS = "http://192.168.1.13/Andr/WS.asmx";
    private String WSDL_TARGET_NAMESPACE;
    private String SOAP_ADDRESS;
    //        private MessageObject MObj;
    private String Title;
    private String Content;
    private boolean New = false;
    private String IDs = "";
    private boolean returnflag = false;
    private int ID;
    private String FLag = "Invalid";
    private Document D;
    private ArrayList<JSONObject> ans;
    public NetworkAsyncTask(Context context) {
//        WeakReference<MainActivity> myActivity = new WeakReference<>(activity);
        ans = new ArrayList<>();
        MyContext = context;
        db = DatabaseHandler.getInstance(MyContext);
        share = SharedPreferenceHandler.getInstance(MyContext);

        OPERATION_NAME_CHECK = "CheckUser";
        OPERATION_NAME_DELIVERED = "Delivered";
//        char[] encryptedchar = null; for(int i = 0; i < encryptedBytes.length; i++) {     encryptedchar[i] = (char) (encryptedBytes[i] & 0xFF);      }
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



    private boolean isAppForeground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }

        return true;
    }


    protected void onPreExecute() {
        //display progress dialog.


//            MObj = new MessageObject();
//        List<MessageObject> MESSAGES = new ArrayList<>();


//        String ur = "";
//        String plaintext = "value1=Esmati,value2=Aa@123,value3=992742-35dsew-32213gbd,value4=";
//        try {
//            ur = "http://mpas.migtco.com/Andr/WS/CheckUser?Value=" + Base64.encode(plaintext.getBytes("UTF-8"),Base64.DEFAULT);
////            RSAEncrypt(plaintext);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//             db = datab.getInstance(MainActivity.this);
//             SQLiteOpenHelper db = datab.getInstance(MainActivity.this);

    }




    protected void onPostExecute(String b) {
        // dismiss progress dialog and update ui
        Log.d("SQLite", "Begin to Show");
        if ((New) & (isAppForeground(MyContext))) {
            MyContext.sendBroadcast(new Intent("Alarm fire"));
        }
//        ShowMessages(GetMessagesfromDB());

    }


    private void CheckVersion() {


//        try
//        {
//            URL url = new URL("http://192.168.1.13/Download/FileDownload.ashx?Idfile=&TypeDoc=&");
//            String message = "Code";
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            connection.setDoOutput(true);
//
//            connection.setRequestMethod("GET");
//
//
//            OutputStreamWriter writer = new OutputStreamWriter(
//                    connection.getOutputStream());
//
//
//            writer.write("message=" + message);
//
//
//            writer.close();
//
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                // OK
//
//                Log.i("Response Message", connection.getContent().toString());
//                InputStream is = connection.getInputStream();

//                Drawable d = Drawable.createFromStream(is, "imagename");
//                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//                FileOutputStream out = openFileOutput("jjjjj", Context.MODE_PRIVATE);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
//            } else {
//                // Server returned HTTP error code.
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.v("Intent Service", "Check Request");
//        String responseMessage = "";
//        String value = "Value1=" + share.GetToken() + ",Value2=" + share.GetDeviceID();
//        requestString = requestString + new String(Base64.encode(value.getBytes(), Base64.DEFAULT));
//
//        try {
//
//            url = new URL(requestString);
//            HttpURLConnection c = (HttpURLConnection) url.openConnection();
//            c.setRequestMethod("GET");
//            c.setDoOutput(true);
//            c.connect();
//
////            String PATH = Environment.getExternalStorageDirectory() + "/download/";
//            String PATH = Environment.getDataDirectory() + "/download/";
//            File file = new File(PATH);
//            file.mkdirs();
//            File outputFile = new File(file, "app.apk");
//            FileOutputStream fos = new FileOutputStream(outputFile);
//
//            InputStream is = c.getInputStream();
//
//            byte[] buffer = new byte[1024];
//            int len1 = 0;
//            while ((len1 = is.read(buffer)) != -1) {
//                fos.write(buffer, 0, len1);
//            }
//            fos.close();
//            is.close();//till here, it works fine - .apk is download to my sdcard in download file
//
//            Intent promptInstall = new Intent(Intent.ACTION_VIEW)
//                    .setData(Uri.parse(PATH + "app.apk"))
//                    .setType("application/android.com.app");
//            startActivity(promptInstall);//installation is not working
//
//
//        } catch (IOException e) {
//            Log.w("HTTP3:", e);
//            responseMessage = e.getMessage();
//        } catch (Exception e) {
//            Log.w("HTTP4:", e);
//            responseMessage = e.getMessage();
//        }
//
//
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction(SplashActivity.VersionCheckReceiver.PROCESS_RESPONSE);
//        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
//        broadcastIntent.putExtra(RESPONSE_MESSAGE, responseMessage);
//        sendBroadcast(broadcastIntent);


    }

    @Override
    protected String doInBackground(Object... params) {





        if (isLocalReachable()) {
            SOAP_ACTION_CHECK = "http://192.168.1.13/CheckUser";
            SOAP_ACTION_DELIVERED = "http://192.168.1.13/Delivered";
            WSDL_TARGET_NAMESPACE = "http://192.168.1.13/";
            SOAP_ADDRESS = "http://192.168.1.13/Andr/WS.asmx";
        } else {
            SOAP_ACTION_CHECK = "https://mpas.migtco.com:3000/CheckUser";
            SOAP_ACTION_DELIVERED = "https://mpas.migtco.com:3000/Delivered";
            WSDL_TARGET_NAMESPACE = "https://mpas.migtco.com:3000/";
            SOAP_ADDRESS = "https://mpas.migtco.com:3000/Andr/WS.asmx";
        }

        CheckVersion();

        String username = share.GetUsername();
        String password = share.GetPassword();
        String DeviceID = share.GetDeviceID();

//        Object username = userDetails[0];
//        Object password = userDetails[1];
//        Object DeviceID = userDetails[2];
//        Object Token    = userDetails[3];
        String Token = share.GetToken();
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager =
                (NotificationManager) MyContext.getSystemService(Context.NOTIFICATION_SERVICE);

//        InputStream is = null;
//        String result = "";
//        JSONObject jsonObject = null;

        try {

            // requests!

//            String ur = "";
            String plaintext = "value1=" + username + ",value2=" + password
                    + ",value3=" + DeviceID
                    + ",value4=";
            if (!Token.equals(MyContext.getString(R.string.defaultValue))) {
                plaintext += Token;
            }

//            ur = "http://mpas.migtco.com/Andr/WS.asmx/CheckUser?Value=" + new String(Base64.encode(plaintext.getBytes(), Base64.DEFAULT));
//
//            try {
//                URL url = new URL(ur);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }

            ////

            plaintext = new String(Base64.encode(plaintext.getBytes(), Base64.DEFAULT));
//            SoapObject request = new SoapObject("http://mpas.migtco.com/", "CheckUser");
            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_CHECK);
            PropertyInfo pi = new PropertyInfo();
            pi.setName("Value");
            pi.setValue(plaintext);
            pi.setType(String.class);
            request.addProperty(pi);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

//            HttpTransportSE httpTransport = new HttpTransportSE("http://mpas.migtco.com/Andr/WS.asmx");
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
//            Object response = null;
            Object response;
//            httpTransport.call("http://mpas.migtco.com/CheckUser", envelope);
            httpTransport.call(SOAP_ACTION_CHECK, envelope);
            response = envelope.getResponse();


            SoapObject response1 = (SoapObject) response;
            SoapObject message = (SoapObject) response1.getProperty(0);
            SoapObject token = (SoapObject) response1.getProperty(1);
            SoapObject auth = (SoapObject) response1.getProperty(2);


            String Authresp = "";
            String Tokenresp = "";
//            SoapObject Messagesresp = new SoapObject("http://mpas.migtco.com/", "CheckUser");
            SoapObject Messagesresp = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_CHECK);

            if (auth.getPropertyCount() > 0)
                Authresp = auth.getProperty(0).toString();

            if (token.getPropertyCount() > 0)
                Tokenresp = token.getProperty(0).toString();

//            if (message.getPropertyCount() > 0)
//                Messagesresp = (SoapObject) message.getProperty(0);


            if (Authresp.contains("Invalid")) {
                share.SaveActivation(MyContext.getString(R.string.NotActive));
                FLag = "Invalid";
            }
            if (Authresp.contains("Wait")) {
                share.SaveActivation(MyContext.getString(R.string.NotActive));
                FLag = "Wait";
            }
            if (Authresp.contains("No Message")) {
                share.SaveActivation(MyContext.getString(R.string.Active));
                FLag = "OK";
            }
            if (!Tokenresp.isEmpty()) {
//                    share.SaveActivation(MyContext.getString(R.string.Active));
                share.SaveToken(Tokenresp);
                FLag = "OK";
            }


            int index = 0;

            while (index < message.getPropertyCount()) {
                share.SaveActivation(MyContext.getString(R.string.Active));
                FLag = "OK";

                MessageObject temp;
                SoapObject Message = (SoapObject) message.getProperty(index);


                temp = new MessageObject(
                        Integer.valueOf(Message.getProperty(0).toString()),
                        Message.getProperty(1).toString(),
                        Message.getProperty(2).toString(),
                        Message.getProperty(3).toString(),
                        Boolean.valueOf(Message.getProperty(4).toString()),
                        false, false, false
                );
                if (db.CheckExist(Integer.valueOf(Message.getProperty(0).toString()), MyContext)) {
                    index++;
                    continue;
                }
                New = true;
                IDs = IDs + Message.getProperty(0).toString() + ";";
                MIDs[index] = Message.getProperty(0).toString();


                isCritical = Boolean.valueOf(Message.getProperty(4).toString());


                if (isCritical) {
//

                    long[] pattern = {1000, 1000, 1000, 1000, 1000};
                    Vibrator vibrator = (Vibrator) MyContext.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(pattern, -1);
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(MyContext, notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (!isAppForeground(MyContext)) {

                        Log.i("Notify", "is running");
                        Intent nid = new Intent(MyContext, MainActivity.class);
                        PendingIntent ci = PendingIntent.getActivity(MyContext, 0, nid, 0);

                        mBuilder =
                                new android.support.v4.app.NotificationCompat.Builder(MyContext)
                                        .setSmallIcon(R.mipmap.ic_launcher)
//                                                .setSmallIcon(MyContext.getResources().getDrawable(R.mipmap.ic_launcher))
                                        .setContentTitle(temp.getMessageTitle())
                                        .setContentIntent(ci)
                                        .setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                        .setContentText(temp.getMessageBody());



                        mNotificationManager.notify(temp.getMessageID(), mBuilder.build());
                    }
                }


                db.addMessage(temp);
                index++;
            }
            database = db.getWritableDatabase();

            if (New) {
                String Status = "0";
                String plaintxt = "value1=" + IDs + ",value2=" + share.GetToken()
                        + ",value3=" + Status;


                plaintxt = new String(Base64.encode(plaintxt.getBytes(), Base64.DEFAULT));

                SoapObject requestDel = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_DELIVERED);
                PropertyInfo Pinf = new PropertyInfo();
                Pinf.setName("Value");
                Pinf.setValue(plaintxt);
                Pinf.setType(String.class);
                requestDel.addProperty(Pinf);


                SoapSerializationEnvelope envelopeDel = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelopeDel.dotNet = true;

                envelopeDel.setOutputSoapObject(requestDel);


                httpTransport.call(SOAP_ACTION_DELIVERED, envelopeDel);
                response = envelopeDel.getResponse();

//                int z = 3;
//                String[] data = MIDs;
//
//                SendStatusAsyncTask taskstate = new SendStatusAsyncTask(MyContext);
//
//                try {
//                    Object b = taskstate.execute(data).get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }


                if (response.toString().contains(MyContext.getString(R.string.Delivered))) {

                    ContentValues values = new ContentValues();
                    values.put("SendDelivered", true);
                    String[] MIDs = IDs.split(";");
                    for (int i = 0; i < MIDs.length; i++) {
                        database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                    }

                } else if (response.toString().contains(MyContext.getString(R.string.Seen))) {

                    ContentValues values = new ContentValues();
                    values.put("SendSeen", true);
                    values.put("SendDelivered", true);
                    String[] MIDs = IDs.split(";");
                    for (int i = 0; i < MIDs.length; i++) {
                        database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                    }
                }


            }

            int undone = db.unSend(MyContext);
            if (undone == 1) {
//                DatabaseHandler d = DatabaseHandler.getInstance(MyContext);
                int f = 0;
                IDs = "";
                Cursor cursor = database.rawQuery("SELECT * from Messages WHERE SendDelivered = 0;", null);
                try {
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                IDs = IDs + cursor.getInt(0) + ";";

                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    }

                } catch (Exception e) {
                    e.getStackTrace();
                }


                String Status = "0";
                String plaintxt = "value1=" + IDs + ",value2=" + share.GetToken()
                        + ",value3=" + Status;


                plaintxt = new String(Base64.encode(plaintxt.getBytes(), Base64.DEFAULT));

                SoapObject requestDel = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_DELIVERED);
                PropertyInfo Pinf = new PropertyInfo();
                Pinf.setName("Value");
                Pinf.setValue(plaintxt);
                Pinf.setType(String.class);
                requestDel.addProperty(Pinf);


                SoapSerializationEnvelope envelopeDel = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelopeDel.dotNet = true;

                envelopeDel.setOutputSoapObject(requestDel);


                httpTransport.call(SOAP_ACTION_DELIVERED, envelopeDel);
                response = envelopeDel.getResponse();


                if (response.toString().contains(MyContext.getString(R.string.Delivered))) {
                    ContentValues values = new ContentValues();
                    values.put("SendDelivered", true);
                    String[] MIDs = IDs.split(";");
                    for (int i = 0; i < MIDs.length; i++) {
                        database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                    }

                }
            } else if (undone == 2) {
//                DatabaseHandler d = DatabaseHandler.getInstance(MyContext);
                int f = 1;
                IDs = "";
                Cursor cursor = database.rawQuery("SELECT * from Messages WHERE SendSeen = 0  AND Seen = 1;", null);
                try {
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                IDs = IDs + cursor.getInt(0) + ";";

                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    }

                } catch (Exception e) {
                    e.getStackTrace();
                }


                String Status = "1";
                String plaintxt = "value1=" + IDs + ",value2=" + share.GetToken()
                        + ",value3=" + Status;


                plaintxt = new String(Base64.encode(plaintxt.getBytes(), Base64.DEFAULT));

                SoapObject requestDel = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_DELIVERED);
                PropertyInfo Pinf = new PropertyInfo();
                Pinf.setName("Value");
                Pinf.setValue(plaintxt);
                Pinf.setType(String.class);
                requestDel.addProperty(Pinf);


                SoapSerializationEnvelope envelopeDel = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelopeDel.dotNet = true;

                envelopeDel.setOutputSoapObject(requestDel);


                httpTransport.call(SOAP_ACTION_DELIVERED, envelopeDel);
                response = envelopeDel.getResponse();


                if (response.toString().contains(MyContext.getString(R.string.Seen))) {
                    ContentValues values = new ContentValues();
                    values.put("SendSeen", true);
                    values.put("SendDelivered", true);
                    String[] MIDs = IDs.split(";");
                    for (int i = 0; i < MIDs.length; i++) {
                        database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                    }

                }
            }

            database.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        if ((FLag.equals(MyContext.getString(R.string.OK))) | (FLag.equals(MyContext.getString(R.string.Wait))))
            share.SaveStatus(FLag);

        if ((isCritical) & (!isAppForeground(MyContext))) {
            Intent i = new Intent(MyContext, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyContext.startActivity(i);
            mNotificationManager.cancelAll();
//            if ((FLag.equals(MyContext.getString(R.string.OK))) | (FLag.equals(MyContext.getString(R.string.Wait))))
//                share.SaveStatus(FLag);
            return FLag;

        }

        return FLag;
    }


}
