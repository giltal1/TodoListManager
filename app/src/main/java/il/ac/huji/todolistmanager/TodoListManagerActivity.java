package il.ac.huji.todolistmanager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.util.Date;
import java.util.List;

public class TodoListManagerActivity extends ActionBarActivity {

    private static final int REQ_ADD_NEW_TODO_ITEM = 1;
    private final String CALL = "tel:";
    private final String SMS = "smsto:";
    private final String MAIL = "mailto:";

    private String callPrefix = null;
    private String messagePrefix = null;
    private String mailPrefix = null;

    private final Context context = this;
    private ItemDataSource dataSource;
    private List<Item> items;
    private ArrayAdapter<Item> adapter;
    private ListView itemsList;
    private ParseDB parse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        callPrefix = getResources().getString(R.string.menu_call);
        messagePrefix = getResources().getString(R.string.menu_message);
        mailPrefix = getResources().getString(R.string.menu_mail);

        //Init parse
        parse = new ParseDB(context);

        //Open DB
        dataSource = new ItemDataSource(this);
        dataSource.open();
        items = dataSource.getAllItems();

        //ListView
        itemsList = (ListView) findViewById(R.id.lstTodoItems);
        adapter = new ItemAdapter(this, android.R.layout.simple_list_item_1, items);
        itemsList.setAdapter(adapter);
        registerForContextMenu(itemsList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_todo_list_context, menu);

        //Set Header
        AdapterContextMenuInfo adapterInfo = (AdapterContextMenuInfo) menuInfo;
        Item item = adapter.getItem(adapterInfo.position);
        menu.setHeaderTitle(item.getTitle());

        //Choose which menu items to show
        if (item.getTitle().startsWith(callPrefix)) {
            MenuItem callItem = menu.findItem(R.id.menuItemCall);
            callItem.setTitle(item.getTitle());
            menu.removeItem(R.id.menuItemMessage);
            menu.removeItem(R.id.menuItemMail);
        }
        else if(item.getTitle().startsWith(messagePrefix)) {
            MenuItem messageItem = menu.findItem(R.id.menuItemMessage);
            messageItem.setTitle(item.getTitle());
            menu.removeItem(R.id.menuItemCall);
            menu.removeItem(R.id.menuItemMail);

        }
        else if(item.getTitle().startsWith(mailPrefix)) {
            MenuItem mailItem = menu.findItem(R.id.menuItemMail);
            mailItem.setTitle(item.getTitle());
            menu.removeItem(R.id.menuItemCall);
            menu.removeItem(R.id.menuItemMessage);
        }
        else {
            menu.removeItem(R.id.menuItemCall);
            menu.removeItem(R.id.menuItemMessage);
            menu.removeItem(R.id.menuItemMail);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuItem.getMenuInfo();
        Item item = adapter.getItem(info.position);
        switch (menuItem.getItemId()) {
            case R.id.menuItemDelete:
                return removeItem(item);
            case R.id.menuItemCall:
                return actionOnItem(CALL, item);
            case R.id.menuItemMessage:
                return actionOnItem(SMS, item);
            case R.id.menuItemMail:
                return actionOnItem(MAIL, item);
            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    private boolean removeItem(Item item) {
        parse.delete(item);
        dataSource.deleteItem(item);
        adapter.remove(item);
        adapter.notifyDataSetChanged();
        return true;
    }

    private boolean actionOnItem(String action, Item item) {
        Intent intent = null;
        String data;
        switch (action) {
            case CALL:
                data = item.getTitle().substring(callPrefix.length());
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(CALL + data));
                break;
            case SMS:
                data = item.getTitle().substring(messagePrefix.length());
                intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse(SMS + data));
                break;
            case MAIL:
                data = item.getTitle().substring(mailPrefix.length());
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(MAIL + data));
                break;
        }
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplicationContext(), AddNewTodoItemActivity.class);
            startActivityForResult(intent, REQ_ADD_NEW_TODO_ITEM);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ADD_NEW_TODO_ITEM) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                Date date = (Date) data.getExtras().get("date");
                Item item = new Item(title, date);
                parse.insert(item);
                dataSource.insertItem(item);
                adapter.insert(item, 0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

}
