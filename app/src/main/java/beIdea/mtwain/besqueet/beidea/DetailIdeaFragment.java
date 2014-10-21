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
           activity.list_idea_fragment.removeIdeaFromList(date,tvTime.getText()+"");
        }
    }




}
