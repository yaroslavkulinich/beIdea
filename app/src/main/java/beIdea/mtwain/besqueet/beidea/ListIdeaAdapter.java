package beIdea.mtwain.besqueet.beidea;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
    ListIdeaFragment ctx;
    LayoutInflater lInflater;
    Button btn;
    BeIdeaActivity a;

    ListIdeaAdapter(Fragment context) {
        ctx = (ListIdeaFragment) context;
        lInflater = (LayoutInflater) ctx.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return ctx.ideas.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return ctx.ideas.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView( int pos, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final int position = pos;
        ImageButton ibtnDelete,ibtnEdit;
        // используем созданные, но не используемые view
        final IdeaRaw p = getIdeaRaw(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.swipe_layout, parent, false);
            SwipeLayout swipeLayout =  (SwipeLayout)convertView.findViewById(R.id.swipe);
//set show mode.
            ibtnDelete = (ImageButton) swipeLayout.findViewById(R.id.ibtnDelete);
            ibtnDelete.setTag(ctx.ideas.get(position).id);
            ibtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("1","Position "+position+", TIME "+p.time+"  ANOTHER TIME"+ctx.ideas.get(position).time);
                    for(int i=0;i<ctx.ideas.size();i++){
                        Log.d("1",ctx.ideas.get(i).time);
                    }

                }
            });
            ibtnEdit = (ImageButton) swipeLayout.findViewById(R.id.ibtnEdit);
            ibtnEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                   // Log.d("","Position "+position);
                    /*ImageButton b = (ImageButton) view;

                    ctx.removeIdeaFromList((String)b.getTag());
*/
                }
            });
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
