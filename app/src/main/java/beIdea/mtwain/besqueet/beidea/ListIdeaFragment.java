package beIdea.mtwain.besqueet.beidea;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by twain on 07.10.14.
 */
public class ListIdeaFragment extends Fragment {

    public ListIdeaFragment(){}

    private static final String LOG_TAG = "ListIdeaFragment";
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<IdeaRaw> ideas = new ArrayList<IdeaRaw>();
    ListView lvIdea;
    ListIdeaAdapter ideaAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ideas = getIdeasFromDB();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_idea, container, false);
        lvIdea = (ListView) rootView.findViewById(R.id.lvIdea);
        ideaAdapter = new ListIdeaAdapter(getActivity(),ideas);
        lvIdea.setAdapter(ideaAdapter);
        return rootView;
    }

    public ArrayList<IdeaRaw> getIdeasFromDB(){
        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        Cursor c = sqLiteDatabase.query(dbHelper.TABLE_NAME, null, null, null, null, null, null);

        ArrayList<IdeaRaw> ideaList = new ArrayList<IdeaRaw>();

        if (c.moveToFirst()) {

            int idIndex = c.getColumnIndex(dbHelper.ID_RAW);
            int dateIndex = c.getColumnIndex(dbHelper.DATE_RAW);
            int timeIndex = c.getColumnIndex(dbHelper.TIME_RAW);
            int ideaIndex = c.getColumnIndex(dbHelper.IDEA_RAW);

            Log.d(LOG_TAG,dateIndex+" "+timeIndex+" "+ideaIndex);

            do {

                IdeaRaw raw = new IdeaRaw();
                raw.date = c.getString(dateIndex);
                raw.idea = c.getString(ideaIndex);
                raw.time = c.getString(timeIndex);

                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idIndex) +
                                ", date = " + c.getString(dateIndex) +
                                ", time = " + c.getString(timeIndex)+
                                ", idea = " + c.getString(ideaIndex));
                ideaList.add(raw);

            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        return ideaList;
    }
}
