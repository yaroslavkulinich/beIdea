package beIdea.mtwain.besqueet.beidea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by twain on 18.10.14.
 */
public class DetailIdeaFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "DetailFragment";

    public DetailIdeaFragment(){}


    BeIdeaActivity activity;
    TextView tvIdea,tvTime;
    IdeaRaw ideaRaw;
    Button btnEdit,btnDelete;

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String date ="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        activity = (BeIdeaActivity)getActivity();
        dbHelper = new DBHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();

        View rootView = inflater.inflate(R.layout.fragment_detail_idea, container, false);
        tvIdea = (TextView)rootView.findViewById(R.id.tvIdea);
        tvTime = (TextView)rootView.findViewById(R.id.tvTime);
        Bundle bundle = getArguments();
        date = bundle.getString(DBHelper.DATE_RAW);
        tvIdea.setText(bundle.getString(DBHelper.IDEA_RAW));
        tvTime.setText(bundle.getString(DBHelper.TIME_RAW));
        btnDelete = (Button) rootView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        btnEdit = (Button) rootView.findViewById(R.id.btnEdit);
        Log.d("DetailFragment","!!!!!OnCreate");

        return rootView;
    }

    public void setIdea(String idea){
        tvIdea.setText(idea);
    }

    public void setTime(String time){
        tvTime.setText(time);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnDelete){
           removeIdeaFromList();
           activity.list_idea_fragment.refresh();
        }
    }

    public void removeIdeaFromList() {
        int delCount = sqLiteDatabase.delete(DBHelper.TABLE_NAME, DBHelper.ID_RAW+" = " + getID(date,tvTime.getText()+""), null);
        Log.d(LOG_TAG, "deleted rows count = " + delCount);
        dbHelper.close();
        getFragmentManager().popBackStack();
    }

    public String getID(String date,String time){
        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        Cursor c = sqLiteDatabase.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(DBHelper.ID_RAW);
            int dateIndex = c.getColumnIndex(DBHelper.DATE_RAW);
            int timeIndex = c.getColumnIndex(DBHelper.TIME_RAW);
            do {
                String s = c.getString(idIndex);
                if(date.equals(c.getString(dateIndex))){
                    if(time.equals(c.getString(timeIndex))){
                        return s;
                    }
                }
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        return null;
    }


}
