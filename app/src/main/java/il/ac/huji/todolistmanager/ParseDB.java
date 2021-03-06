package il.ac.huji.todolistmanager;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.Parse;
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
        Resources resources = context.getResources();
        String appID = resources.getString(R.string.parseAppID);
        String clientID = resources.getString(R.string.parseClientID);
        Parse.initialize(context, appID, clientID);
        ParseUser.enableAutomaticUser();
    }

    public void insert(Item item) {
        ParseObject parseItem = new ParseObject("Todo");
        parseItem.put("title", item.getTitle());
        parseItem.put("date", item.getDateAsString());
        parseItem.saveInBackground();
    }

    public void delete(Item item) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Todo");
        query.whereEqualTo("title", item.getTitle());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.e("ParseDB", "Find item to delete failed");
                } else {
                    object.deleteInBackground();
                }
            }
        });
    }
}
