package org.besqueet.mtwain.beidea;

import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.halfbit.tinybus.Bus;
import com.halfbit.tinybus.TinyBus;
import com.halfbit.tinybus.wires.ShakeEventWire;

import org.besqueet.mtwain.beidea.controllers.RealmController;
import org.besqueet.mtwain.beidea.controllers.StringsController;
import org.besqueet.mtwain.beidea.ui.fragments.BeIdeaFragment;


public class BeIdeaActivity extends FragmentActivity {

    private Bus mBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("B", "ONCREATE   "+ RealmController.getPath(getApplicationContext()));
        setContentView(R.layout.activity_layout);
        mBus = TinyBus.from(this).wire(new ShakeEventWire());
        RealmController.initRealm(this);
        StringsController.initStrings(this);
        RealmController.loadIdeas();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,new BeIdeaFragment())
                .commit();
    }

    public void presentFragment(final Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,fragment)
                .addToBackStack("")
                .commit();
    }

    @Override
    protected void onStart() {
        mBus.register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mBus.unregister(this);
        requestBackup();
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    //визивається при зміні орієнтації
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void requestBackup()
    {
        BackupManager b = new BackupManager(this);
        b.dataChanged();
    }


    public void restore() {
        BackupManager b = new BackupManager(this);
        b.requestRestore(new RestoreObserver() {
            @Override
            public void restoreStarting(int numPackages) {
                super.restoreStarting(numPackages);
                Log.d("B","restoreStarting");
            }

            @Override
            public void onUpdate(int nowBeingRestored, String currentPackage) {
                super.onUpdate(nowBeingRestored, currentPackage);
                Log.d("B","onUpdate");
            }

            @Override
            public void restoreFinished(int error) {
                super.restoreFinished(error);
                Log.d("B","restoreFinished");
            }
        });
    }

}
