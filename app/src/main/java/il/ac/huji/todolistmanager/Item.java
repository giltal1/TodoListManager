package il.ac.huji.todolistmanager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gil on 02/04/2015.
 */
public class Item {

    private final String FORMAT = "dd/MM/yyyy";

    private String title;
    private Date date;

    public Item(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(date);
    }
}
