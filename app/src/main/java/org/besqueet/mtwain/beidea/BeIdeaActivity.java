package org.besqueet.mtwain.beidea;

import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import org.besqueet.mtwain.beidea.ui.fragments.IntroFragment;


public class BeIdeaActivity extends FragmentActivity {

    private Bus mBus;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "false";
    SharedPreferences mSettings;

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

        mSettings = getSharedPreferences(APP_PREFERENCES, 0);
        Boolean bool = mSettings.getBoolean(APP_PREFERENCES_COUNTER, true);
        if(bool.equals(true)){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new IntroFragment())
                    .commit();
            mSettings = getSharedPreferences(APP_PREFERENCES, 0);

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(APP_PREFERENCES_COUNTER, false);
            editor.apply();
        }
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
