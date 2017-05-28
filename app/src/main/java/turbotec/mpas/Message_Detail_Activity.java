package turbotec.mpas;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import android.app.IntentService;
//import android.content.Intent;
//import android.support.annotation.Nullable;

public class Message_Detail_Activity extends AppCompatActivity {

    private static DatabaseHandler db;
    private final Context context = this;
    //    SQLiteDatabase database;
    private Button DelBut;
    private Integer ID = 1;
//    private String[] arg;

    public Message_Detail_Activity() {

    }


//    public class SaveState extends IntentService {
//
//
//        public SaveState() {
//            super("State Save in SQLite");
//        }
//
//        @Override
//        protected void onHandleIntent(@Nullable Intent intent) {
//
//            SQLiteDatabase database = db.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put("Seen", true);
////            database = db.getWritableDatabase();
//            database.update("Messages", values, "MessageID  = ?", new String[]{String.valueOf(ID)});
//            database.close();
//
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail_layout);
        db = DatabaseHandler.getInstance(this);
        Bundle b = getIntent().getExtras();
        String Title = "";
        String Body = "";
        Boolean Critical = false;
        Boolean isSeen = false;
        Boolean isSendSeen = false;

        if (b != null) {
            Title = b.getString(getString(R.string.Title));
            Body = b.getString(getString(R.string.Body));
            Critical = b.getBoolean(getString(R.string.Critical));
            isSeen = b.getBoolean(getString(R.string.Seen));
            isSendSeen = b.getBoolean(getString(R.string.SendSeen));
            ID = b.getInt(getString(R.string.ID));


            TextView t1 = (TextView) findViewById(R.id.titledetail2);
            TextView t2 = (TextView) findViewById(R.id.bodydetail2);
            ImageView i1 = (ImageView) findViewById(R.id.statedetail2);
            ImageView i2 = (ImageView) findViewById(R.id.Critical2);
            t1.setText(Title);
            t2.setText(Body);
            i1.setImageResource(R.mipmap.seen);
            if (Critical) {
                i2.setImageResource(R.mipmap.critical);
            }
            if (!isSeen) {

//                Intent in = new Intent(this, SaveState.class);
//                startService(in);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SQLiteDatabase database = db.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("Seen", true);
                        database.update("Messages", values, "MessageID  = ?", new String[]{String.valueOf(ID)});
//                        database.close();

                    }


                }).start();


//                DatabaseUpdateOperation d = new DatabaseUpdateOperation();
//                try {
//                    d.execute("").get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
            }
        }


        if (!isSendSeen) {
            int z = 3;
            String[] data = new String[]{z + "", "1", String.valueOf(ID)};

            SendStatusAsyncTask taskstate = new SendStatusAsyncTask(this);

            taskstate.execute(data);
        }
        DelBut = (Button) findViewById(R.id.button2);

        DelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

//                                SQLiteDatabase database = db.getWritableDatabase();
//                                arg = new String[]{String.valueOf(ID)};
//                                database.delete("Messages", "MessageID  = ?", new String[]{String.valueOf(ID)});
//                                database.close();
                                DatabaseDeleteOperation ff = new DatabaseDeleteOperation();
                                ff.execute("");
                                finish();

                            }
                        })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();

                                    }
                                })
                        .setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);
                AlertDialog adialog = alertDialogBuilder.create();
                adialog.show();


            }
        });


    }


//    private class DatabaseUpdateOperation extends AsyncTask<String, String, String> {
//
////        @Override
////        protected void onProgressUpdate(String... values) {
////            super.onProgressUpdate(values);
////
////        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
////            String tablename = params[0];
////            String WhereClause = params[1];
//
//            SQLiteDatabase database = db.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put("Seen", true);
////            database = db.getWritableDatabase();
//            database.update("Messages", values, "MessageID  = ?", new String[]{String.valueOf(ID)});
//            database.close();
//
//            return null;
//        }
//
////        @Override
////        protected void onPostExecute(String result) {
////            super.onPostExecute(result);
////            // do something with data here-display it or send to mainactivity
////        }
//    }


    private class DatabaseDeleteOperation extends AsyncTask<String, String, String> {

//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//
//        }

        @Override
        protected String doInBackground(String... params) {

//            String tablename = params[0];
//            String WhereClause = params[1];

            SQLiteDatabase database = db.getWritableDatabase();
//            arg = new String[]{String.valueOf(ID)};
            database.delete("Messages", "MessageID  = ?", new String[]{String.valueOf(ID)});
            database.close();
            finish();

            return null;
        }

//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            // do something with data here-display it or send to mainactivity
//        }
    }


}
