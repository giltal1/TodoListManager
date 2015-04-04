package il.ac.huji.todolistmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;


public class AddNewTodoItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo_item);

        //Set DatePicker to start from tomorrow
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        //Set Buttons
        Button ok = (Button) findViewById(R.id.btnOK);
        Button cancel = (Button) findViewById(R.id.btnCancel);
        ok.setOnClickListener(onOKClick);
        cancel.setOnClickListener(onCancelClick);

    }

    private OnClickListener onOKClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //get title
            EditText titleView = (EditText) findViewById(R.id.edtNewItem);
            String title = titleView.getText().toString();
            if (title.isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.empty_text_item, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            //get date
            DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date dueDate = calendar.getTime();

            //return results in intent
            Intent intent = new Intent();
            intent.putExtra("title", title);
            intent.putExtra("date", dueDate);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private OnClickListener onCancelClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };

}
