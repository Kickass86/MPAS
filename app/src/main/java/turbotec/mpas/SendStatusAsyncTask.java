package turbotec.mpas;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ZAMANI on 4/9/2017.
 */

public class SendStatusAsyncTask extends AsyncTask {


    private final Context MyContext;
    private SharedPreferenceHandler share;
    private DatabaseHandler db;
    private SQLiteDatabase database;


    public SendStatusAsyncTask(Context myContext) {
        MyContext = myContext;
        share = SharedPreferenceHandler.getInstance(MyContext);
        db = DatabaseHandler.getInstance(MyContext);
        database = db.getWritableDatabase();
    }

    protected void onPreExecute() {

    }


    @Override
    protected Boolean doInBackground(Object[] params) {

        int num = Integer.parseInt(params[0].toString());
        Object Status = params[1];
        String IDs = "";
        for (int i = 2; i < num; i++) {
            IDs = IDs + (params[i] + ";");
        }


        InputStream is = null;
        String result = "";
        JSONObject jsonObject = null;

        try {

            // requests!

            String plaintext = "value1=" + IDs + ",value2=" + share.GetToken()
                    + ",value3=" + Status;


            plaintext = new String(Base64.encode(plaintext.getBytes(), Base64.DEFAULT));

            SoapObject request = new SoapObject("http://192.168.1.13/", "Delivered");
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
            Object response;
//            httpTransport.call("http://mpas.migtco.com/CheckUser", envelope);
            httpTransport.call("http://192.168.1.13/Delivered", envelope);
            response = envelope.getResponse();

            if (response.toString().contains(MyContext.getString(R.string.Delivered))) {

                ContentValues values = new ContentValues();
                values.put("SendDelivered", true);
                String[] MIDs = IDs.split(";");
                for (int i = 0; i < MIDs.length; i++) {
                    database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                }
                database.close();
            } else if (response.toString().contains(MyContext.getString(R.string.Delivered))) {

                ContentValues values = new ContentValues();
                values.put("SendSeen", true);
                values.put("SendDelivered", true);
                String[] MIDs = IDs.split(";");
                for (int i = 0; i < MIDs.length; i++) {
                    database.update("Messages", values, "MessageID  = ?", new String[]{MIDs[i]});
                }
                database.close();
            }

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }


}
