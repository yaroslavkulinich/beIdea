package org.besqueet.mtwain.beidea.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.besqueet.mtwain.beidea.controllers.RealmController;
import org.besqueet.mtwain.beidea.ui.Idea;
import io.realm.RealmResults;

/**
 * Created by mtwain on 13.01.15.
 */
public class IdeaListAdapter extends BaseAdapter {

    Context context;
    RealmResults<Idea> ideas;
    LayoutInflater lInflater;

    public IdeaListAdapter(Context context){
        ideas = RealmController.getIdeas();
        this.context = context;
        lInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return RealmController.getIdeas().size();
    }

    @Override
    public Object getItem(int i) {
        ideas.sort("timeInMill", false);
        ideas.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        return null;
    }
}
