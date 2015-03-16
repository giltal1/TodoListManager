package il.ac.huji.todolistmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TodoListManagerActivity extends ActionBarActivity {

    final Context context = this;
    private List<String> items;
    private ArrayAdapter<String> adapter;
    ListView itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        //ListView
        itemsList = (ListView) findViewById(R.id.items_list);
        items = new ArrayList<String>();
        adapter = new ItemAdapter(this, android.R.layout.simple_list_item_1, items);
        itemsList.setAdapter(adapter);
        itemsList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);

                //Custom Dialog Title
                TextView title = new TextView(context);
                title.setText(itemsList.getItemAtPosition(position).toString());
                title.setBackgroundColor(Color.GRAY);
                title.setPadding(10, 30, 10, 30);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);
                builder.setCustomTitle(title);

                //Delete Button
                builder.setPositiveButton("Delete Item", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.show();
                return true;
            }
        });
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
            return addItem();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean addItem() {
        EditText text = (EditText) findViewById(R.id.edit_new_item);
        if (text.getText().toString().isEmpty()) {
            return false;
        }
        String newItem = text.getText().toString();
        adapter.insert(newItem, 0);
        text.setText("");
        return true;
    }

}
