package il.ac.huji.todolistmanager;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Gil on 20/04/2015.
 */
public class ParseDB {

    private final String FORMAT = "dd/MM/yyyy";

    public ParseDB(Context context) {
        Parse.enableLocalDatastore(context);
        Resources resources = context.getResources();
        String appID = resources.getString(R.string.parseAppID);
        String clientID = resources.getString(R.string.parseClientID);
        Parse.initialize(context, appID, clientID);
        ParseUser.enableAutomaticUser();
    }

    public void insert(Item item) {
        ParseObject parseItem = new ParseObject("todo");
        parseItem.put("title", item.getTitle());
        parseItem.put("date", item.getDateAsString());

        //parseItem.setACL(new ParseACL(ParseUser.getCurrentUser()));
        parseItem.saveInBackground();
    }

    public void delete(Item item) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("todo");
        query.whereEqualTo("title", item.getTitle());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> todoList, ParseException e) {
                if (e == null && todoList.size() > 0) {
                    for (ParseObject object : todoList) {
                        object.deleteInBackground();
                    }
                } else {
                    Log.e("ParseDB", "Find item to delete failed", e);
                }
            }
        });
    }
}
