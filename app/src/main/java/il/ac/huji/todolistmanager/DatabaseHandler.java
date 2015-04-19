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
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoListManagerDB";
    private static final String TABLE_NAME = "TodoTable";
    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_DESCRIPTION + " TEXT NOT NULL, " +
                    KEY_DATE + " INTEGER);";

    public DatabaseHandler(Context context) {
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

    // Adding new item
    public void insertItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, item.getTitle());

        long valueDate = item.getDate() != null ? item.getDate().getTime(): null;
        values.put(KEY_DATE, valueDate);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Getting single item
    //public Item getItem(int id) {}

    // Getting all items
    public List<Item> getAllItems() {
        List<Item> itemsList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(1);
                Date date = !cursor.isNull(2) ? new Date(cursor.getLong(2)) : null;
                Item item = new Item(title, date);

                // Adding contact to list
                itemsList.add(item);
            } while (cursor.moveToNext());
        }

        // return items list
        if (itemsList == null) {
            return new ArrayList<Item>();
        }
        else {
            return itemsList;
        }

    }

    // Getting items count
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single item
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, item.getTitle());
        long valueDate = item.getDate() != null ? item.getDate().getTime(): null;
        values.put(KEY_DATE, valueDate);

        // updating row
        return db.update(TABLE_NAME, values, KEY_DESCRIPTION + " = ?",
                new String[] { item.getTitle() });
    }

    // Deleting single contact
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_DESCRIPTION + " = ?",
                new String[] { item.getTitle() });
        db.close();
    }

}
