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

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by twain on 09.10.14.
 */
public class ListIdeaAdapter extends BaseAdapter implements StickyListHeadersAdapter {
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
        ViewHolder holder;
        // используем созданные, но не используемые view
        IdeaRaw p = getIdeaRaw(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.idea_raw_item, parent, false);
            holder.idea = (TextView) convertView.findViewById(R.id.tvRawIdea);
            holder.time = (TextView) convertView.findViewById(R.id.tvRawTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.idea.setText(p.idea);
        holder.time.setText(p.time);

        return convertView;
    }

    IdeaRaw getIdeaRaw(int position) {
        return ((IdeaRaw) getItem(position));
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = lInflater.inflate(R.layout.idea_header, parent, false);
            holder.date = (TextView) convertView.findViewById(R.id.tvHeader);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        IdeaRaw p = getIdeaRaw(position);

        holder.date.setText(p.date);
        return convertView;

    }

    @Override
    public long getHeaderId(int position) {
        getIdeaRaw(position);
      return getIdeaRaw(position).date.subSequence(0,2).toString().charAt(0);
    }

    class HeaderViewHolder {
        TextView date;
    }

    class ViewHolder {
        TextView idea;
        TextView time;
    }
}
