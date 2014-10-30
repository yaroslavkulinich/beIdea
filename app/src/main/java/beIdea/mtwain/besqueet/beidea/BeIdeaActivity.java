package beIdea.mtwain.besqueet.beidea;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.backup.BackupManager;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.RelativeLayout;

import com.actionbarsherlock.internal.nineoldandroids.view.animation.AnimatorProxy;
import com.slidinglayer.SlidingLayer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class BeIdeaActivity extends Activity {

    private static final String LOG_TAG = "BeIdeaActivity";
    IdeaFragment idea_fragment;
    ListIdeaFragment list_idea_fragment;
    RelativeLayout main_container,second_container;
    SlidingUpPanelLayout slideup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        idea_fragment = new IdeaFragment();
        list_idea_fragment = new ListIdeaFragment();
        slideup = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        slideup.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("", "onPanelSlide, offset " + slideOffset);
                if (slideOffset > 0.5) {


                } else {
                    idea_fragment.etIdea.clearFocus();

                }
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i("", "onPanelExpanded");

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i("", "onPanelCollapsed");
                idea_fragment.etIdea.requestFocus();
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i("", "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i("", "onPanelHidden");
            }
        });

        main_container = (RelativeLayout) findViewById(R.id.main_container);
        second_container = (RelativeLayout) findViewById(R.id.second_container);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_container,idea_fragment)
                .commit();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.second_container,list_idea_fragment)
                .commit();
    }

}
