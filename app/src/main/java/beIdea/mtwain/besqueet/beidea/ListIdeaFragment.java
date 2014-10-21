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


import java.util.ArrayList;
import java.util.Collections;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by twain on 07.10.14.
 */
public class ListIdeaFragment extends Fragment  {

    public ListIdeaFragment(){}

    public static final String LOG_TAG = "ListIdeaFragment";
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<IdeaRaw> ideas = new ArrayList<IdeaRaw>();
    StickyListHeadersListView stickyList;
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

        stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.lvIdea);
        ideaAdapter = new ListIdeaAdapter(getActivity(),ideas);
        stickyList.setAdapter(ideaAdapter);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString(DBHelper.IDEA_RAW, ideas.get(i).idea);
                bundle.putString(DBHelper.TIME_RAW, ideas.get(i).time);
                bundle.putString(DBHelper.DATE_RAW, ideas.get(i).date);
                DetailIdeaFragment detailFragment = new DetailIdeaFragment();
                detailFragment.setArguments(bundle);
                slideDetail(detailFragment);
            }
        });
        /*stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Fragment detailFragment = new DetailIdeaFragment();
                FragmentTransaction ft  = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, detailFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/

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
        Collections.reverse(ideaList);
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
    }

   /* public void slideMenu(Fragment f) {
        int slide_in_up = R.animator.slide_in_up;
        int slide_out_up = R.animator.slide_out_up;

        getActivity().getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(slide_in_up,slide_out_up)
                .replace(R.id.container, f)
                .addToBackStack(null)
                .commit();
    }*/

    public void slideDetail(Fragment f) {
        int rightIn = R.animator.slide_right_in;
        int rightOut = R.animator.slide_right_out;
        int leftIn = R.animator.slide_left_in;
        int leftOut = R.animator.slide_left_out;
        getActivity().getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(rightIn, leftOut, leftIn, rightOut)
                .add(R.id.container, f)
                .addToBackStack(null)
                .commit();
    }


    public void removeIdeaFromList(String date,String time) {
        dbHelper = new DBHelper(getActivity());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int delCount = sqLiteDatabase.delete(DBHelper.TABLE_NAME, DBHelper.ID_RAW+" = " + getID(date,time), null);
        Log.d(LOG_TAG, "deleted rows count = " + delCount);
        refresh();
        getFragmentManager().popBackStack();
        dbHelper.close();
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


    public void refresh() {
            ideas = getIdeasFromDB();
            ideaAdapter.ideas = ideas;
            ideaAdapter.notifyDataSetChanged();
    }
}
