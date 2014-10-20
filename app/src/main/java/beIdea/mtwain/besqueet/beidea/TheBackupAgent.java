package beIdea.mtwain.besqueet.beidea;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;
import android.util.Log;

/**
 * Created by twain on 18.10.14.
 */
public class TheBackupAgent extends BackupAgentHelper {

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        Log.i("asa","BACKUPED");
        FileBackupHelper hosts = new FileBackupHelper(this,
                "../databases/" + DBHelper.DATABASE_NAME);
        addHelper(DBHelper.DATABASE_NAME, hosts);
    }

}
