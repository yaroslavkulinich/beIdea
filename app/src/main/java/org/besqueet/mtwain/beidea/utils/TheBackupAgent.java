package org.besqueet.mtwain.beidea.utils;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.util.Log;

import org.besqueet.mtwain.beidea.Constants;
import org.besqueet.mtwain.beidea.controllers.RealmController;

public class TheBackupAgent extends BackupAgentHelper implements Constants {

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        Log.i("B", "BACKUPED   "+ RealmController.getPath(getApplicationContext()));
        FileBackupHelper hosts = new FileBackupHelper(this, RealmController.getPath(getApplicationContext()));
        addHelper(FILES_BACKUP_KEY, hosts);
    }

}
