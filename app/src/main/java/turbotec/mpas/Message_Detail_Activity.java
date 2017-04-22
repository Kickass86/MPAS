package turbotec.mpas;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class Message_Detail_Activity extends AppCompatActivity {

    private static DatabaseHandler db;
    final Context context = this;
    private Button DelBut;
    private Integer ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__detail);
        db = DatabaseHandler.getInstance(this);
        Bundle b = getIntent().getExtras();
        String Title = "";
        String Body = "";
        Boolean Critical = false;

        if (b != null) {
            Title = b.getString(getString(R.string.Title));
            Body = b.getString(getString(R.string.Body));
            Critical = b.getBoolean(getString(R.string.Critical));
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
            ContentValues values = new ContentValues();
            values.put("Seen", true);
            SQLiteDatabase database = db.getWritableDatabase();
            database.update("Messages", values, "MessageID  = ?", new String[]{String.valueOf(ID)});
            database.close();
        }

        int z = 3;
        String[] data = new String[]{z + "", "1", String.valueOf(ID)};

        SendStatusAsyncTask taskstate = new SendStatusAsyncTask(this);

        try {
            Object d = taskstate.execute(data).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        DelBut = (Button) findViewById(R.id.button2);

        DelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SQLiteDatabase database = db.getWritableDatabase();
                                database.delete("Messages", "MessageID  = ?", new String[]{String.valueOf(ID)});
                                database.close();
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

}
