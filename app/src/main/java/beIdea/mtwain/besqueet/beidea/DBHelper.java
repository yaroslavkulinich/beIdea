package beIdea.mtwain.besqueet.beidea;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by twain on 07.10.14.
 */
public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    private static final String LOG_TAG = "DBHelper";
    private Context ctx;
    public static SQLiteDatabase sqldb;

    private static final String DATABASE_NAME = "mydb.db";
    public static final String TABLE_NAME = "mytable";
    public static final String DATE_RAW = "date";
    public static final String TIME_RAW = "time";
    public static final String IDEA_RAW = "idea";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqldb = sqLiteDatabase;
        Log.d(LOG_TAG, "---onCreate DataBase---");
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" ("+
                "id integer primary key autoincrement,"+
                DATE_RAW+" text,"+
                TIME_RAW+" text,"+
                IDEA_RAW+" text"+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }
}
