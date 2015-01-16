package beIdea.mtwain.besqueet.beidea;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;

import com.halfbit.tinybus.Bus;
import com.halfbit.tinybus.TinyBus;
import com.halfbit.tinybus.wires.ShakeEventWire;

import beIdea.mtwain.besqueet.beidea.controllers.RealmController;
import beIdea.mtwain.besqueet.beidea.controllers.StringsController;
import beIdea.mtwain.besqueet.beidea.ui.ragments.BeIdeaFragment;


public class BeIdeaActivity extends Activity {

    private Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        mBus = TinyBus.from(this).wire(new ShakeEventWire());
        RealmController.initRealm(this);
        StringsController.initStrings(this);
        RealmController.loadIdeas();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container,new BeIdeaFragment())
                .commit();
    }

    public void presentFragment(final Fragment fragment){

        getFragmentManager()
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
