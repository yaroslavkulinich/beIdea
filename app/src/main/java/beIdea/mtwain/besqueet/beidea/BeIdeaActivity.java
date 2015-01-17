package beIdea.mtwain.besqueet.beidea;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.halfbit.tinybus.Bus;
import com.halfbit.tinybus.TinyBus;
import com.halfbit.tinybus.wires.ShakeEventWire;

import beIdea.mtwain.besqueet.beidea.controllers.RealmController;
import beIdea.mtwain.besqueet.beidea.controllers.StringsController;
import beIdea.mtwain.besqueet.beidea.ui.fragments.BeIdeaFragment;


public class BeIdeaActivity extends FragmentActivity {

    private Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        super.onStop();
    }

    //визивається при зміні орієнтації
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
