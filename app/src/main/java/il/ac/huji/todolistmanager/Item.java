package il.ac.huji.todolistmanager;

import java.util.Date;

/**
 * Created by Gil on 02/04/2015.
 */
public class Item {

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
}
