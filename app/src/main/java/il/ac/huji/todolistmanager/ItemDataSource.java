package il.ac.huji.todolistmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gil on 19/04/2015.
 */
public class ItemDataSource {

    private final String FORMAT = "dd/MM/yyyy";

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.KEY_ID, MySQLiteHelper.KEY_DESCRIPTION,
            MySQLiteHelper.KEY_DATE };

    public ItemDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Adding new item
    public void insertItem(Item item) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_DESCRIPTION, item.getTitle());
        values.put(MySQLiteHelper.KEY_DATE, item.getDateAsString());
        db.insert(MySQLiteHelper.TABLE_NAME, null, values);
    }

    // Deleting item
    public void deleteItem(Item item) {
        db.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.KEY_DESCRIPTION + " = ?",
                new String[] { item.getTitle() });
    }

    // Getting all items
    public List<Item> getAllItems() {
        List<Item> itemsList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(1);
                Date date = null;
                String dateAsString = !cursor.isNull(2) ? cursor.getString(2) : null;
                DateFormat formatter = new SimpleDateFormat(FORMAT);
                try {
                    date = formatter.parse(dateAsString);
                }
                catch (ParseException e) {
                    Log.e("ItemDataSource", "can't convert string to date", e);
                }
                Item item = new Item(title, date);
                // Adding contact to list
                itemsList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
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
        String countQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

}
