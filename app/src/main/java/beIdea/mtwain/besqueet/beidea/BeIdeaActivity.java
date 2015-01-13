package beIdea.mtwain.besqueet.beidea;

import android.app.Activity;
import android.os.Bundle;

import com.halfbit.tinybus.Bus;
import com.halfbit.tinybus.TinyBus;
import com.halfbit.tinybus.wires.ShakeEventWire;

import beIdea.mtwain.besqueet.beidea.controllers.RealmController;
import beIdea.mtwain.besqueet.beidea.ui.fragments.BeIdeaFragment;


public class BeIdeaActivity extends Activity {

    private Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        mBus = TinyBus.from(this).wire(new ShakeEventWire());
        RealmController.initRealm(this);
        RealmController.loadIdeas();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container,new BeIdeaFragment())
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
}
