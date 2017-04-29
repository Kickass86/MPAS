package turbotec.mpas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import android.database.Cursor;

//import java.util.ArrayList;
//import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MPAS";
    // Contacts table name
    private static final String TABLE_NAME = "Messages";
    // Contacts Table Columns names
    private static final String MESSAGE_ID = "MessageID";
    //    private static final String USER_ID = "UserID";
    private static final String MESSAGE_Title = "MessageTitle";
    private static final String MESSAGE_BODY = "MessageBody";
    private static final String INSERT_DATE = "InsertDate";
    private static final String Critical = "Critical";
    private static final String Seen = "Seen";
    private static final String SendSeen = "SendSeen";
    private static final String SendDelivered = "SendDelivered";
    //    private static Context MyContext = null;
    private static DatabaseHandler instance;


    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        MyContext = context;
    }

    public static DatabaseHandler getInstance(Context mContext) {
        if (instance == null) {
            instance = new DatabaseHandler(mContext);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                MESSAGE_ID + " INTEGER PRIMARY KEY," +
                MESSAGE_Title + " TEXT," + MESSAGE_BODY + " TEXT," + INSERT_DATE +
                " TEXT," + Critical + " Boolean," + Seen + " Boolean," + SendDelivered + " Boolean," + SendSeen + " Boolean)";
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);

    }

    public boolean CheckExist(int messageID, Context MyContext) {

        DatabaseHandler d = DatabaseHandler.getInstance(MyContext);
        SQLiteDatabase db = d.getWritableDatabase();
        boolean f = false;
        Cursor cursor = db.rawQuery("SELECT * from Messages WHERE MessageID =" + messageID + ";", null);
        if (cursor.getCount() > 0)
            f = true;
        cursor.close();

        return f;

    }


    public int unSend(Context MyContext) {

        DatabaseHandler d = DatabaseHandler.getInstance(MyContext);
        SQLiteDatabase db = d.getWritableDatabase();
        int f = 0;
        Cursor cursor = db.rawQuery("SELECT * from Messages WHERE " + SendDelivered + " = 0;", null);
        if (cursor.getCount() > 0)
            f = 1;
        cursor.close();

        cursor = db.rawQuery("SELECT * from Messages WHERE " + SendSeen + " = 0 AND " + Seen + " = 1;", null);
        if (cursor.getCount() > 0)
            f = 2;
        cursor.close();

        return f;

    }



    public void addMessage(MessageObject messageObject) {
        SQLiteDatabase db = this.getWritableDatabase();

//        if (CheckExist(messageObject.getMessageID())) {
        ContentValues values = new ContentValues();
        values.put(MESSAGE_ID, messageObject.getMessageID());
        values.put(MESSAGE_Title, messageObject.getMessageTitle());
        values.put(MESSAGE_BODY, messageObject.getMessageBody());
        values.put(INSERT_DATE, messageObject.getInsertDate());
        values.put(Critical, messageObject.getCritical());
        values.put(Seen, false);
        values.put(SendSeen, false);
        values.put(SendDelivered, false);


        // Inserting Row
        db.insert(TABLE_NAME, null, values);
//        }
        db.close(); // Closing database connection
    }


//    public List<MessageObject> getAllMessages(String DeviceID, String UserID) {
//
//        List<MessageObject> messageList = new ArrayList<>();
//        String selectQuery;
//
//        selectQuery = "SELECT  " + MESSAGE_ID + ", " + USER_ID + ", " + MESSAGE_Title +
//                ", " + MESSAGE_BODY + ", " + INSERT_DATE + ", " + Delivered +
//                " FROM " + TABLE_NAME + " WHERE " + USER_ID + " = '" + UserID +
//                "' AND " + DeviceID + " = '" + DeviceID + "' " + " ORDER BY " +
//                INSERT_DATE + " DESC;";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        try {
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    do {
//                        MessageObject messageObject = new MessageObject();
//                        messageObject.setMessageID(cursor.getInt(0));
//                        messageObject.setUserID(cursor.getString(1));
//                        messageObject.setMessageTitle(cursor.getString(2));
//                        messageObject.setMessageBody(cursor.getString(3));
//                        messageObject.setInsertDate(cursor.getString(4));
//                        messageObject.setDelivered(Integer.valueOf(cursor.getString(5)));
//
//                        // Adding contact to list
//
//                        messageList.add(messageObject);
//
//
//                    } while (cursor.moveToNext());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        cursor.close();
//        return messageList;
//
//
//    }

//    public List<MessageObject> getAllMessages() {
//
//        List<MessageObject> messageList = new ArrayList<>();
//        String selectQuery;
//
//        selectQuery = "SELECT  " + MESSAGE_ID + ", " + USER_ID + ", " + MESSAGE_Title +
//                ", " + MESSAGE_BODY + ", " + INSERT_DATE + ", " + Delivered +
//                " FROM " + TABLE_NAME + " " + " ORDER BY " +
//                INSERT_DATE + " DESC;";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        try {
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    do {
//                        MessageObject messageObject = new MessageObject();
//                        messageObject.setMessageID(cursor.getInt(0));
//                        messageObject.setUserID(cursor.getString(1));
//                        messageObject.setMessageTitle(cursor.getString(2));
//                        messageObject.setMessageBody(cursor.getString(3));
//                        messageObject.setInsertDate(cursor.getString(4));
//                        messageObject.setDelivered(Integer.valueOf(cursor.getString(5)));
//
//                        // Adding contact to list
//
//                        messageList.add(messageObject);
//
//
//                    } while (cursor.moveToNext());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        cursor.close();
//        return messageList;
//
//
//    }


}
