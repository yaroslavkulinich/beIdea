package beIdea.mtwain.besqueet.beidea;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.slidinglayer.SlidingLayer;

import java.util.ArrayList;
import java.util.Collections;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by twain on 07.10.14.
 */
public class ListIdeaFragment extends Fragment  {

    private SlidingLayer slidingLayer;

    public ListIdeaFragment(){}

    public static final String LOG_TAG = "ListIdeaFragment";
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    ArrayList<IdeaRaw> ideas = new ArrayList<IdeaRaw>();
    StickyListHeadersListView stickyList;
    ListIdeaAdapter ideaAdapter;
    RelativeLayout dragLayout;
    TextView tvDetailIdea,tvDetailDate,tvDetailTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
        ideas = getIdeasFromDB();
    }
    @Override
    public void onDestroy(){
        if(!sqLiteDatabase.isOpen())
        dbHelper.close();

        super.onDestroy();
    }

    public void initDB(){
        dbHelper = new DBHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list_idea, container, false);

        stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.lvIdea);
        ideaAdapter = new ListIdeaAdapter(this);

        slidingLayer = (SlidingLayer) rootView.findViewById(R.id.slidingLayer1);
        tvDetailIdea = (TextView) slidingLayer.findViewById(R.id.tvDetailIdea);
        tvDetailDate = (TextView) slidingLayer.findViewById(R.id.tvDetailDate);
        tvDetailTime = (TextView) slidingLayer.findViewById(R.id.tvDetailTime);

        slidingLayer.setShadowWidthRes(R.dimen.shadow_width);
        slidingLayer.setOffsetWidth(1);
        slidingLayer.setCloseOnTapEnabled(true);
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_LEFT);

        dragLayout = (RelativeLayout) rootView.findViewById(R.id.dragLayout);


        stickyList.setAdapter(ideaAdapter);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("","CLICK");

                slidingLayer.openLayer(true);
                tvDetailIdea.setText(ideas.get(i).idea);
                tvDetailDate.setText(ideas.get(i).date);
                tvDetailTime.setText(ideas.get(i).time);
                //slidingLayer.addView(new Button(this));
                /*Bundle bundle = new Bundle();
                bundle.putString(DBHelper.IDEA_RAW, ideas.get(i).idea);
                bundle.putString(DBHelper.TIME_RAW, ideas.get(i).time);
                bundle.putString(DBHelper.DATE_RAW, ideas.get(i).date);
                DetailIdeaFragment detailFragment = new DetailIdeaFragment();
                detailFragment.setArguments(bundle);
                slideDetail(detailFragment);*/
            }
        });

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
                raw.id = c.getString(idIndex);
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
        //Collections.reverse(ideaList);
        dbHelper.close();
        return ideaList;
    }

    public void updateIdea(String idea,String id){
        Log.d(LOG_TAG, "--- Update table: ---");
        ContentValues cv = new ContentValues();
        // подготовим значения для обновления
        cv.put(dbHelper.IDEA_RAW, idea);
                // обновляем по id
        int updCount = sqLiteDatabase.update(dbHelper.TABLE_NAME, cv, "id = ?",
                new String[] { id });
        Log.d(LOG_TAG, "updated rows count = " + updCount);
        dbHelper.close();
        initDB();
    }

    public void removeIdeaFromList(String id) {
        initDB();
        int delCount = sqLiteDatabase.delete(DBHelper.TABLE_NAME, DBHelper.ID_RAW+" = " + id, null);
        Log.d(LOG_TAG, "deleted rows count = " + delCount);
        refresh();
        //getFragmentManager().popBackStack();
        dbHelper.close();
    }

    public String getID(String date,String time){

        Cursor c = sqLiteDatabase.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex(DBHelper.ID_RAW);
            int dateIndex = c.getColumnIndex(DBHelper.DATE_RAW);
            int timeIndex = c.getColumnIndex(DBHelper.TIME_RAW);
            do {
                String s = c.getString(idIndex);
                Log.d("1", "--- Remove ID: "+s);
                Log.d("1", date+" AND "+c.getString(dateIndex));
                if(date.equals(c.getString(dateIndex))){
                    Log.d("1", "YES");
                    Log.d("1", time+" AND "+c.getString(timeIndex));
                    if(time.equals(c.getString(timeIndex))){
                        Log.d("1", "HAAA YEAH");
                        Log.d("1",s+" deleted!!!");
                        return s;
                    }
                }
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");

        return null;
    }


    public void refresh() {
            ideas = getIdeasFromDB();
            ideaAdapter.notifyDataSetChanged();
    }
}
