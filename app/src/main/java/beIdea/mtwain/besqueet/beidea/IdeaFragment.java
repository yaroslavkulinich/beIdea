package beIdea.mtwain.besqueet.beidea;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by twain on 07.10.14.
 */
public class IdeaFragment extends Fragment {

    public IdeaFragment(){}

    private static final String LOG_TAG = "IdeaFragment";

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    EditText etIdea;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbHelper = new DBHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();

        View rootView = inflater.inflate(R.layout.fragment_idea, container, false);
        etIdea = (EditText) rootView.findViewById(R.id.etIdea);
        etIdea.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

        });
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
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.DATE_RAW, "Date");
        cv.put(dbHelper.DATE_RAW, "Time");
        cv.put(dbHelper.DATE_RAW, "Idea");
        // вызываем метод вставки
        long rowID = sqLiteDatabase.insert(dbHelper.TABLE_NAME, null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        dbHelper.close();
    }
}
