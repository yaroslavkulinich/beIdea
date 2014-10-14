package beIdea.mtwain.besqueet.beidea;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by twain on 07.10.14.
 */
public class ListIdeaFragment extends Fragment {

    public ListIdeaFragment(){}

    private static final String LOG_TAG = "ListIdeaFragment";
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<IdeaRaw> ideas = new ArrayList<IdeaRaw>();
    StickyListHeadersListView stickyList;
    ListIdeaAdapter ideaAdapter;
    Button btn;


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
        btn = (Button)rootView.findViewById(R.id.btn);
        stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.lvIdea);
        ideaAdapter = new ListIdeaAdapter(getActivity(),ideas);
        stickyList.setAdapter(ideaAdapter);
        stickyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + position);
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ideas.remove(positionToRemove);
                        ideaAdapter.notifyDataSetChanged();
                        Log.d(LOG_TAG, "IDEAS SIZE: " + ideas.size());
                    }


                });
                adb.show();
                return false;
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

    public void removeIdeaFromList(int id) {
        Log.d(LOG_TAG, "--- Delete from table: ---");
        // удаляем по id
        int delCount = sqLiteDatabase.delete(dbHelper.TABLE_NAME, "id = " + id, null);
        Log.d(LOG_TAG, "deleted rows count = " + delCount);
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

}
