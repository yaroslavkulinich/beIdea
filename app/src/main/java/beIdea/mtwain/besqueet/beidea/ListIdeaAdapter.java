package beIdea.mtwain.besqueet.beidea;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.daimajia.swipe.SwipeLayout;

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
    Button btn;
    BeIdeaActivity a;

    ListIdeaAdapter(Context context, ArrayList<IdeaRaw> raws) {
        ctx = context;
        a =(BeIdeaActivity) context;
        ideas = raws;

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
        final IdeaRaw p = getIdeaRaw(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.swipe_layout, parent, false);
            SwipeLayout swipeLayout =  (SwipeLayout)convertView.findViewById(R.id.swipe);
//set show mode.
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//set drag edge.
            swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);

            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout swipeLayout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout swipeLayout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });
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
        String [] ids = getIdeaRaw(position).date.split("/");
        String id = "";
        for(int i=0;i<3;i++){
            id += ids[i];
        }
      return Long.parseLong(id);
    }

    class HeaderViewHolder {
        TextView date;
    }

    class ViewHolder {
        TextView idea;
        TextView time;
    }
}
