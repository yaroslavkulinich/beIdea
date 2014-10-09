package beIdea.mtwain.besqueet.beidea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by twain on 09.10.14.
 */
public class ListIdeaAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<IdeaRaw> ideas;

    ListIdeaAdapter(Context context, ArrayList<IdeaRaw> raws) {
        ctx = context;
        ideas = raws;
        Collections.reverse(ideas);
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return ideas.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return ideas.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.idea_raw_item, parent, false);
        }

        IdeaRaw p = getIdeaRaw(position);
        ((TextView) view.findViewById(R.id.tvRawIdea)).setText(p.idea);
        ((TextView) view.findViewById(R.id.tvRawTime)).setText(p.time);

        return view;
    }

    IdeaRaw getIdeaRaw(int position) {
        return ((IdeaRaw) getItem(position));
    }

}
