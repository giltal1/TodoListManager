package il.ac.huji.todolistmanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Gil on 14/03/2015.
 */
public class ItemAdapter extends ArrayAdapter<String> {

    public ItemAdapter(Context context, int resource, List<String> tasks) {
        super(context, resource, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.activity_todo_list_manager_list_item, null);
        TextView tv = (TextView) view.findViewById(R.id.list_todo_item);
        int color = position % 2 == 0 ? Color.RED : Color.BLUE;
        tv.setTextColor(color);
        tv.setTextSize(30);
        String item = getItem(position);
        tv.setText(item.toString());
        return view;
    }
}