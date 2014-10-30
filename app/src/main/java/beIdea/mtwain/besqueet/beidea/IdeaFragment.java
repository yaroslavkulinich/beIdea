package beIdea.mtwain.besqueet.beidea;

import android.app.Fragment;
import android.app.backup.BackupManager;
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
    EditText etIdea;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_idea, container, false);
        etIdea = (EditText) rootView.findViewById(R.id.etIdea);
        return rootView;
    }

    public void initDB(){
        dbHelper = new DBHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    @Override
    public void onDestroy(){
        String idea = etIdea.getText()+"";
        if(!idea.equals("")){
            insertIdeaToDB();
        }
        BackupManager bm = new BackupManager(getActivity());
        bm.dataChanged();
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
        etIdea.setText("");
        initDB();
    }


}
