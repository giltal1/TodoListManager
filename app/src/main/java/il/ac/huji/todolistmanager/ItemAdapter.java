package il.ac.huji.todolistmanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public ItemAdapter(Context context, int resource, List<Item> tasks) {
        super(context, resource, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.activity_todo_list_manager_list_item, null);

        TextView title = (TextView) view.findViewById(R.id.txtTodoTitle);
        TextView date = (TextView) view.findViewById(R.id.txtTodoDueDate);
        Item item = getItem(position);

        title.setText(item.getTitle());
        if (item.getDate() != null) {
            date.setText(df.format(item.getDate()));
            Date today = new Date();
            if (item.getDate().getTime() < today.getTime()) {
                date.setTextColor(Color.RED);
                title.setTextColor(Color.RED);
            }
        }
        else {
            date.setText("No due date");
        }

        return view;
    }
}