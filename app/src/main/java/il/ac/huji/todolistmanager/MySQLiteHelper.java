package il.ac.huji.todolistmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gil on 19/04/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "TodoTable";
    public static final String KEY_ID = "id";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "date";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoListManagerDB";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_DESCRIPTION + " TEXT NOT NULL, " +
                    KEY_DATE + " INTEGER);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and create table again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
