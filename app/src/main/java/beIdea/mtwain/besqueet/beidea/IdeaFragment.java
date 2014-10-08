package beIdea.mtwain.besqueet.beidea;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by twain on 07.10.14.
 */
public class IdeaFragment extends Fragment {

    public IdeaFragment(){}

    private static final String LOG_TAG = "IdeaFragment";

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<IdeaRaw> ideas = new ArrayList<IdeaRaw>();

    EditText etIdea;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ideas = getIdeasFromDB();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_idea, container, false);
        etIdea = (EditText) rootView.findViewById(R.id.etIdea);

        return rootView;
    }

    @Override
    public void onDestroy(){
        String idea = etIdea.getText()+"";
        if(!idea.equals("")){
            insertIdeaToDB();
        }
        super.onDestroy();
    }

    public void insertIdeaToDB(){
        Calendar c = Calendar.getInstance();
        String hour = c.get(Calendar.HOUR_OF_DAY)+"";
        if(hour.length()==1){
            hour = "0"+hour;
        }
        String minute = c.get(Calendar.MINUTE)+"";
        if(minute.length()==1){
            minute = "0"+minute;
        }
        String second = c.get(Calendar.SECOND)+"";
        if(second.length()==1){
            second = "0"+second;
        }

        String time = hour+":"+minute+":"+second;
        String date = c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
        String idea = etIdea.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(dbHelper.DATE_RAW, date);
        cv.put(dbHelper.TIME_RAW, time);
        cv.put(dbHelper.IDEA_RAW, idea);

        // вызываем метод вставки
        long rowID = sqLiteDatabase.insert(dbHelper.TABLE_NAME, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        Log.d(LOG_TAG,"Time: "+time);
        Log.d(LOG_TAG, "Date: "+date);
        Log.d(LOG_TAG,"Idea: "+idea);
        dbHelper.close();
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
