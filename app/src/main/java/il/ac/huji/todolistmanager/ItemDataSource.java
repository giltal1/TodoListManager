package il.ac.huji.todolistmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ulamadm on 19/04/2015.
 */
public class ItemDataSource {

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

        long valueDate = item.getDate() != null ? item.getDate().getTime(): null;
        values.put(MySQLiteHelper.KEY_DATE, valueDate);

        db.insert(MySQLiteHelper.TABLE_NAME, null, values);
    }

    // Getting single item
    public Item getItem(int id) {
        Cursor cursor = db.query(MySQLiteHelper.TABLE_NAME, allColumns,
                MySQLiteHelper.KEY_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        String title = cursor.getString(1);
        Date date = !cursor.isNull(2) ? new Date(cursor.getLong(2)) : null;
        Item item = new Item(title, date);
        cursor.close();
        return item;
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
                Date date = !cursor.isNull(2) ? new Date(cursor.getLong(2)) : null;
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

//    // Updating single item
//    public int updateItem(Item item) {
//        ContentValues values = new ContentValues();
//        values.put(MySQLiteHelper.KEY_DESCRIPTION, item.getTitle());
//        long valueDate = item.getDate() != null ? item.getDate().getTime(): null;
//        values.put(MySQLiteHelper.KEY_DATE, valueDate);
//
//        // updating row
//        return db.update(MySQLiteHelper.TABLE_NAME, values, MySQLiteHelper.KEY_DESCRIPTION + " = ?",
//                new String[] { item.getTitle() });
//    }

    // Deleting single contact
    public void deleteItem(Item item) {
        db.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.KEY_DESCRIPTION + " = ?",
                new String[] { item.getTitle() });
        //db.close();
    }
}
