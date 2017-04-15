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
import android.os.AsyncTask;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

//import org.apache.commons.codec.binary.Base64;

class NetworkAsyncTask extends AsyncTask<Object, Void, String> {

    private final Context MyContext;
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
    private String[] MIDs;
    //        private MessageObject MObj;
    private String Title;
    private String Content;
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
//        char[] encryptedchar = null; for(int i = 0; i < encryptedBytes.length; i++) {     encryptedchar[i] = (char) (encryptedBytes[i] & 0xFF);      }
    }

    private byte[] Encrypt(String raw) throws Exception {

        byte[] input = raw.getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance("RSA");

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
                "12345678", 36), new BigInteger("11", 36));
        RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger(
                "12345678", 36), new BigInteger("12345678", 36));

        RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
        RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] cipherText = cipher.doFinal(input);
        System.out.println("cipher: " + new String(cipherText));

        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] plainText = cipher.doFinal(cipherText);
        System.out.println("plain : " + new String(plainText));

        return cipherText;
    }

    private void GetfromXML(String input1) {


        byte[] expBytes = Base64.decode("t3Y5Nmj3+9H03nuWOOAHau1Ya+FxA+z+j/No3T5v9smAjPTmUyVTqAyIYnh9u59UD0pI0K3m+Y6+PjNP5eUvefFUmtH5blsahs+UAOUAg0br00HLg80AaeoCI2/SIgDBJh01o5LHjv1YeW2Xvzse9W6MAveH/A2Fj1gxOD7OPXs=", Base64.DEFAULT);
        byte[] modBytes = Base64.decode("AQAB", Base64.DEFAULT);


        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger exponent = new BigInteger(1, expBytes);
//        BigInteger d = new BigInteger(1, dBytes);

        KeyFactory factory1 = null;
        try {
            factory1 = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
//        input1 = "test";

        RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
        PublicKey pubKey = null;
        try {
            pubKey = factory1.generatePublic(pubSpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] encrypted = new byte[0];
        char[] encryptedcahr = "a".toCharArray();
        try {
            encrypted = cipher.doFinal(input1.getBytes("UTF-8"));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < encrypted.length; i++) {
            encryptedcahr[i] = (char) (encrypted[i] & 0xFF);
        }

        System.out.println("encrypted: " + new String(encryptedcahr));

//        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
//        PrivateKey privKey = factory.generatePrivate(privSpec);
//        cipher.init(Cipher.DECRYPT_MODE, privKey);
//        byte[] decrypted = cipher.doFinal(encrypted);
//        System.out.println("decrypted: " + new String(decrypted));


    }

    public String RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//        kpg = KeyPairGenerator.getInstance("RSA");
//        kpg.initialize(1024);
//        Provider o = kpg.getProvider();

//        kp = kpg.genKeyPair();
//        publicKey = kp.getPublic();
//        privateKey = kp.getPrivate();
//        tv0tzng4pW7erDo2ke/Ku9TGKRukAzx+lihZVblIOE6GWIoiwlILOANeVliZLi1s5qMsXEUA4GV2woC1zFdhJvfFja8Nacl4I3CJ4JYmGqcSZinWKgo3MJdoEqFl9NliF4wTYLow3GYoUh03WxoeArozV1S03drP898b9PdbjPY+ji4jpZHJWnbfg+qWSziF1Q/pSAxpzabeoamz1+ekqlhuxZavQUl+hIhx/quHqy3ybFWcX6yE5NKeY1fzX3L7
        PublicKey pub = null;
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger("172154210408329882942455911053636463298042289283826725451362917895982220394949902813077787991501090407453206037420165987298198005076255417237346559722645975462447959380297240472396383975798693784507547585717769809932608807375442464498125372246829301236299142412530876636758999078324092398387140571970725344609"), new BigInteger("65537"));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        try {
            pub = factory.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }


//        try {
//            KeyStore KS = KeyStore.getInstance("AndroidKeyStore");
////            KS.load(null);
//
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pub);

        encryptedBytes = cipher.doFinal(plain.getBytes());
        String EncryptStr = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        Log.i("EEncrypted?????", EncryptStr);
        return EncryptStr;
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
        share = SharedPreferenceHandler.getInstance(MyContext);

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

//    protected String SSSS(Object... userDetails) {
//
//
//        Object username = userDetails[0];
//        Object password = userDetails[1];
//        Object DeviceID = userDetails[2];
//
//        InputStream is;
//        String ur = "";
//        String result = "";
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//        JSONObject jsonObject;
//
//        String plaintext = "value1=" + username + ",value2=" + password
//                + ",value3=" + DeviceID
//                + ",value4=";
////        if(Foreground)
////        {
////            Log.e("!!!!","it is in foreground");
////        }
////        else
////        {
////            Log.e("!!!!","it is not in foreground");
////        }
//
//        android.support.v4.app.NotificationCompat.Builder mBuilder;
//        NotificationManager mNotificationManager =
//                (NotificationManager) MyContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        String Token = MyContext.getString(R.string.defaultValue);
//        try {
//
//            // requests!
//
//
//            if (!Token.equals(MyContext.getString(R.string.defaultValue))) {
//                plaintext += Token;
//            }
////            byte[] En = Base64.encode(plaintext.getBytes("UTF-8"),Base64.DEFAULT);
////            String En2 = Base64.encodeToString((plaintext.getBytes("UTF-8")),Base64.DEFAULT);
////
////            char[] Encahr = new char[En.length];
//////            Encahr[0] = (char) En[0] & 0xFF;
////            for(int i = 0; i < En.length;i++)
////            {
////                Encahr[i] = (char) ( En[i] & 0xFF);
////            }
//
//
//            ur = "http://mpas.migtco.com/Andr/WS.asmx/CheckUser?Value=" + RSAEncrypt(plaintext);
//
//            URL url = new URL(ur);
//
//            HttpURLConnection httpconnection = (HttpURLConnection) url.openConnection(); // for port 80
//
//            try {
//                is = new BufferedInputStream(httpconnection.getInputStream());
////                    readStream(in);
//            } finally {
//                httpconnection.disconnect();
//            }
//
//            try {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(
//                        is, "utf-8"), 8);
//
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//                is.close();
//                result = sb.toString();
//            } catch (Exception e) {
//                return null;
//            }
//
//            // Convert string to object
//            try {
//                jsonObject = new JSONObject(result);
//            } catch (JSONException e) {
//                return null;
//            }
//
//
////                is = entity.getContent();
//        } catch (Exception e) {
//            e.printStackTrace();
//            // return null;
//        }
//
//
//        Connection conn;
//        try {
//            String driver = "net.sourceforge.jtds.jdbc.Driver";
//
//
//            String name = "sa";
//            String pass = "left4de@d";
//
//
//            String connString = "jdbc:jtds:sqlserver://192.168.1.131:1433/MIGT_Automation;user=" + name + ";password=" + pass + ";";
//
//
//            try {
//                Class.forName(driver).newInstance();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            conn = DriverManager.getConnection(connString);
//
//            Statement stmt1 = conn.createStatement();
//
//            ResultSet reset1 = stmt1.executeQuery("Use MIGT_Automation\n" +
//                    " SELECT * FROM TbL_Users WHERE " + "Username = '" +
//                    username + "' AND  password = '" + password + "' AND DeviceID = '" + DeviceID + "';");
////                ResultSet reset = stmt.executeQuery("SELECT * FROM TbL_Users WHERE " + "Username = '" + username +"';");
//
//            Statement stmt4 = conn.createStatement();
//
//            Boolean b = reset1.next();
//            if (b) {
//                Statement stmt2 = conn.createStatement();
//                ResultSet reset2 = stmt2.executeQuery("Use MIGT_Automation\n" +
//                        "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.UserID = TbL_Users.id \n" +
//                        "Where TbL_Users.id = '" + reset1.getString("id") + "' AND TbL_Users.DeviceID = '" + DeviceID + "' AND TbL_Users.DeviceIDAccepted = 1;");
//
//
//                if (reset2.next()) {
//                    share.SaveUserID(reset1.getString("id"));
//                    share.SaveActivation(MyContext.getString(R.string.Active));
//
//                    Statement stmt3 = conn.createStatement();
//                    ResultSet reset3 = stmt3.executeQuery("Use MIGT_Automation\n" +
//                            "SELECT * FROM Messages INNER JOIN TbL_Users  ON Messages.UserID = TbL_Users.id \n" +
//                            "Where TbL_Users.id = '" + reset2.getString("id") + "' AND Messages.Delivered = 0 AND TbL_Users.DeviceID = '" + DeviceID + "';");
//
//                    Log.w("Connection1", "open");
//                    if (reset3.next()) {
////                        int UpdateDeviceID =
//
//                        stmt4.executeUpdate("Use MIGT_Automation\n" +
//                                "   update Messages\n" +
//                                "   SET Delivered = 1\n" +
//                                "   WHERE " + " MessageID = '" + reset3.getString("MessageID") + "' AND Delivered = 0;");
//                        //
////                            MainActivity activity = this.MyActivity.get();
//                        MessageObject MObj = new MessageObject(reset3.getInt("MessageID"), reset3.getString("UserID"),
//                                reset3.getString("MessageTitle"), reset3.getString("MessageBody"), reset3.getDate("InsertDate").toString(), reset3.getBoolean("Critical") ? 1 : 0);
//                        Log.w("Connection2", "open");
//                        db.addMessage(MObj);
//                        Log.i("User valid", "New message added");
//
//                        Title = reset3.getString("MessageTitle");
//                        ID = reset3.getInt("MessageID");
//                        Content = reset3.getString("MessageBody");
//                        b = reset3.getBoolean("Critical");
//
//                        if (b) {
//                            Intent i = new Intent(MyContext, MainActivity.class);
//                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            MyContext.startActivity(i);
//                            mNotificationManager.cancelAll();
//                            return "OK";
//                        } else {
//                            if (!isAppForeground(MyContext)) {
//
//                                Log.i("Notify", "is running");
//                                Intent nid = new Intent(MyContext, MainActivity.class);
//                                PendingIntent ci = PendingIntent.getActivity(MyContext, 0, nid, 0);
//
//                                mBuilder =
//                                        new android.support.v4.app.NotificationCompat.Builder(MyContext)
//                                                .setSmallIcon(R.mipmap.ic_launcher)
////                                                .setSmallIcon(MyContext.getResources().getDrawable(R.mipmap.ic_launcher))
//                                                .setContentTitle(Title)
//                                                .setContentIntent(ci)
//                                                .setAutoCancel(true)
//                                                .setDefaults(Notification.DEFAULT_ALL)
//                                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                                                .setContentText(Content);
//
////                                Intent notificationIntent = new Intent(MyContext, MainActivity.class);
//
////                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
////                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//
//                                mNotificationManager.notify(ID, mBuilder.build());
//                            }
//                        }
//
//                        while (reset3.next()) {
////                            UpdateDeviceID =
//                            stmt4.executeUpdate("Use MIGT_Automation\n" +
//                                    "   update Messages\n" +
//                                    "   SET Delivered = 1\n" +
//                                    "   WHERE " + " MessageID = '" + reset3.getString("MessageID") + "' AND Delivered = 0;");
//
//                            MObj = new MessageObject(reset3.getInt("MessageID"), reset3.getString("UserID"),
//                                    reset3.getString("MessageTitle"), reset3.getString("MessageBody"), reset3.getDate("InsertDate").toString(), reset3.getBoolean("Critical") ? 1 : 0);
//                            db.addMessage(MObj);
//
//                            Title = reset3.getString("MessageTitle");
//                            ID = reset3.getInt("MessageID");
//                            Content = reset3.getString("MessageBody");
//                            b = reset3.getBoolean("Critical");
//                            if (b) {
//                                Intent i = new Intent(MyContext, MainActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                MyContext.startActivity(i);
//                                mNotificationManager.cancelAll();
//                                return "OK";
//                            } else {
//
//                                if (!isAppForeground(MyContext)) {
//
//                                    Log.i("Notify", "is running");
//                                    Intent nid = new Intent(MyContext, MainActivity.class);
//                                    PendingIntent ci = PendingIntent.getActivity(MyContext, 0, nid, 0);
//
//                                    mBuilder =
//                                            new android.support.v4.app.NotificationCompat.Builder(MyContext)
//                                                    .setSmallIcon(R.mipmap.ic_launcher)
////                                                    .setSmallIcon(ContextCompat.getDrawable(MyContext, R.mipmap.ic_launcher))
//                                                    .setContentTitle(Title)
//                                                    .setContentIntent(ci)
//                                                    .setAutoCancel(true)
//                                                    .setDefaults(Notification.DEFAULT_ALL)
//                                                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                                                    .setContentText(Content);
//
//                                    mNotificationManager =
//                                            (NotificationManager) MyContext.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                                    mNotificationManager.notify(ID, mBuilder.build());
//                                }
//                            }
//
//
//                        }
//                        FLag = "OK";
//                    } else {
//                        //No new Message
//                        Log.i("User valid", "No new message");
//                        FLag = "OK";
//                    }
//                } else {
//                    if (reset1.getBoolean("DeviceIDAccepted")) {
//                        share.SaveActivation(MyContext.getString(R.string.Active));
//                    } else {
//                        share.SaveActivation(MyContext.getString(R.string.NotActive));
//                    }
//                    FLag = "OK";
//                }
//
//            } else {
//                Statement stmt5 = conn.createStatement();
//                ResultSet reset5 = stmt5.executeQuery("Use MIGT_Automation\n" +
//                        " SELECT * FROM TbL_Users WHERE " + "Username = '" +
//                        username + "' AND  password = '" + password + "';");
//                reset5.next();
//
//                if (reset5.getString("DeviceID") == null) {
//                    //First Time Launch
//                    int UpdateDeviceID = stmt4.executeUpdate("Use MIGT_Automation\n" +
//                            "   update TbL_Users\n" +
//                            "   SET DeviceID = '" + DeviceID + "'\n" +
//                            "   WHERE " + "Username = '" +
//                            username + "' AND " + " password = '" + password + "';");
//                    if (UpdateDeviceID != 0) {
//                        Log.i("SQL Database", "DeviceID Updated Successfully");
//                        FLag = "OK";
//                    }
//                }
//
//            }
//
////            return false;
//
////                return reset.getString(0);
////                return (reset.getString("password").equals(password)) ? true: false;
////                return reset.getString("password");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            Log.i("Return false1", "User invalid");
////            return false;
//        } catch (Exception e) {
//            e.getStackTrace();
//            Log.i("Return false2", "User invalid");
////            return false;
//        }
//
//        share.SaveStatus(FLag);
//
//        return FLag;
//    }


    protected void onPostExecute(String b) {
        // dismiss progress dialog and update ui
        Log.d("SQLite", "Begin to Show");
        MyContext.sendBroadcast(new Intent("Alarm fire"));
//        ShowMessages(GetMessagesfromDB());

    }


    @Override
    protected String doInBackground(Object... params) {


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

        InputStream is = null;
        String result = "";
        JSONObject jsonObject = null;

        try {

            // requests!

            String ur = "";
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
            SoapObject request = new SoapObject("http://192.168.1.13/", "CheckUser");
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
            HttpTransportSE httpTransport = new HttpTransportSE("http://192.168.1.13/Andr/WS.asmx");
//            Object response = null;
            Object response;
//            httpTransport.call("http://mpas.migtco.com/CheckUser", envelope);
            httpTransport.call("http://192.168.1.13/CheckUser", envelope);
            response = envelope.getResponse();


            SoapObject response1 = (SoapObject) response;
            SoapObject message = (SoapObject) response1.getProperty(0);
            SoapObject token = (SoapObject) response1.getProperty(1);
            SoapObject auth = (SoapObject) response1.getProperty(2);


            String Authresp = "";
            String Tokenresp = "";
//            SoapObject Messagesresp = new SoapObject("http://mpas.migtco.com/", "CheckUser");
            SoapObject Messagesresp = new SoapObject("http://192.168.1.13/", "CheckUser");

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
            boolean ff = false;
            while (index < message.getPropertyCount()) {
                share.SaveActivation(MyContext.getString(R.string.Active));
                FLag = "OK";
                ff = true;
                MessageObject temp;
                db = DatabaseHandler.getInstance(MyContext);
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
                IDs = IDs + Message.getProperty(0).toString() + ";";
                MIDs[index] = Message.getProperty(0).toString();

//                temp.setMessageID(Integer.valueOf(Message.getProperty(0).toString()));
//                temp.setMessageTitle(Message.getProperty(1).toString());
//                temp.setMessageBody(Message.getProperty(2).toString());
//                temp.setInsertDate(Message.getProperty(3).toString());
//                temp.setCritical(Boolean.valueOf(Message.getProperty(4).toString()));

                boolean b = Boolean.valueOf(Message.getProperty(4).toString());


                if (b) {
                    Intent i = new Intent(MyContext, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyContext.startActivity(i);
                    mNotificationManager.cancelAll();
                    return FLag;

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

//                                Intent notificationIntent = new Intent(MyContext, MainActivity.class);

//                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);


                        mNotificationManager.notify(temp.getMessageID(), mBuilder.build());
                    }
                }


                db.addMessage(temp);
                index++;
            }

            db = DatabaseHandler.getInstance(MyContext);

            if (ff) {
                String Status = "0";
                String plaintxt = "value1=" + IDs + ",value2=" + share.GetToken()
                        + ",value3=" + Status;


                plaintxt = new String(Base64.encode(plaintxt.getBytes(), Base64.DEFAULT));

                SoapObject requestDel = new SoapObject("http://192.168.1.13/", "Delivered");
                PropertyInfo Pinf = new PropertyInfo();
                Pinf.setName("Value");
                Pinf.setValue(plaintxt);
                Pinf.setType(String.class);
                requestDel.addProperty(Pinf);


                SoapSerializationEnvelope envelopeDel = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelopeDel.dotNet = true;

                envelopeDel.setOutputSoapObject(requestDel);


                httpTransport.call("http://192.168.1.13/Delivered", envelopeDel);
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
                    database = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("SendDelivered", true);
                    String[] MIDs = IDs.split(";");
                    for (int i = 0; i < MIDs.length; i++) {
                        database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                    }
                    database.close();
                } else if (response.toString().contains(MyContext.getString(R.string.Seen))) {
                    database = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("SendSeen", true);
                    values.put("SendDelivered", true);
                    String[] MIDs = IDs.split(";");
                    for (int i = 0; i < MIDs.length; i++) {
                        database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                    }
                    database.close();
                }


            }


            if (db.unSend(MyContext) == 1) {
//                DatabaseHandler d = DatabaseHandler.getInstance(MyContext);
                SQLiteDatabase d = db.getWritableDatabase();
                int f = 0;
                IDs = "";
                Cursor cursor = d.rawQuery("SELECT * from Messages WHERE SendDelivered = False;", null);
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

                SoapObject requestDel = new SoapObject("http://192.168.1.13/", "Delivered");
                PropertyInfo Pinf = new PropertyInfo();
                Pinf.setName("Value");
                Pinf.setValue(plaintxt);
                Pinf.setType(String.class);
                requestDel.addProperty(Pinf);


                SoapSerializationEnvelope envelopeDel = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelopeDel.dotNet = true;

                envelopeDel.setOutputSoapObject(requestDel);


                httpTransport.call("http://192.168.1.13/Delivered", envelopeDel);
                response = envelopeDel.getResponse();


                if (response.toString().contains(MyContext.getString(R.string.Delivered))) {
                    database = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("SendDelivered", true);
                    String[] MIDs = IDs.split(";");
                    for (int i = 0; i < MIDs.length; i++) {
                        database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                    }
                    database.close();
                }
            } else if (db.unSend(MyContext) == 2) {
//                DatabaseHandler d = DatabaseHandler.getInstance(MyContext);
                SQLiteDatabase d = db.getWritableDatabase();
                int f = 0;
                IDs = "";
                Cursor cursor = d.rawQuery("SELECT * from Messages WHERE SendDelivered = False;", null);
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

                SoapObject requestDel = new SoapObject("http://192.168.1.13/", "Delivered");
                PropertyInfo Pinf = new PropertyInfo();
                Pinf.setName("Value");
                Pinf.setValue(plaintxt);
                Pinf.setType(String.class);
                requestDel.addProperty(Pinf);


                SoapSerializationEnvelope envelopeDel = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelopeDel.dotNet = true;

                envelopeDel.setOutputSoapObject(requestDel);


                httpTransport.call("http://192.168.1.13/Delivered", envelopeDel);
                response = envelopeDel.getResponse();


                if (response.toString().contains(MyContext.getString(R.string.Delivered))) {
                    database = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("SendDelivered", true);
                    String[] MIDs = IDs.split(";");
                    for (int i = 0; i < MIDs.length; i++) {
                        database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                    }
                    database.close();
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        if ((FLag.equals(MyContext.getString(R.string.OK))) | (FLag.equals(MyContext.getString(R.string.Wait))))
            share.SaveStatus(FLag);

        return FLag;
    }


}
