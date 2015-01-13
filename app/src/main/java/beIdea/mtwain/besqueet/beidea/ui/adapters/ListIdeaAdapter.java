package beIdea.mtwain.besqueet.beidea.ui.adapters;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import beIdea.mtwain.besqueet.beidea.R;
import beIdea.mtwain.besqueet.beidea.controllers.RealmController;
import beIdea.mtwain.besqueet.beidea.ui.Idea;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ListIdeaAdapter extends BaseAdapter implements StickyListHeadersAdapter {
   // ListIdeaFragment ctx;
    LayoutInflater lInflater;


    public ListIdeaAdapter(Fragment context) {
     //   ctx = (ListIdeaFragment) context;
        lInflater = (LayoutInflater) context.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return RealmController.getIdeas().size();
    }

    @Override
    public Object getItem(int position) {
        return RealmController.getIdeas().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int pos, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final int position = pos;
        ImageButton btnDelete,btnEdit;

        final Idea idea = getIdea(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.swipe_layout, parent, false);
            SwipeLayout swipeLayout =  (SwipeLayout)convertView.findViewById(R.id.swipe);
            btnDelete = (ImageButton) swipeLayout.findViewById(R.id.ibtnDelete);
            btnDelete.setTag(RealmController.getIdeas().get(position).getTime());
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("1","Position "+position+", TIME "+idea.getTime()+"  ANOTHER TIME"+RealmController.getIdeas().get(position).getTime());
                    for(int i=0;i<RealmController.getIdeas().size();i++){
                        Log.d("1",RealmController.getIdeas().get(i).getTime());
                    }

                }
            });
            btnEdit = (ImageButton) swipeLayout.findViewById(R.id.ibtnEdit);
            btnEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                   // Log.d("","Position "+position);
                    /*ImageButton b = (ImageButton) view;

                    ctx.removeIdeaFromList((String)b.getTag());*/
                }
            });
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
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

        holder.idea.setText(idea.getIdea());
        holder.time.setText(idea.getTime());
        return convertView;
    }

    Idea getIdea(int position) {
        return ((Idea) getItem(position));
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
        Idea idea = getIdea(position);
        //holder.date.setText(idea.getDate());

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //String [] ids = getIdea(position).getDate().split("/");
        String id = "";
        for(int i=0;i<3;i++){
           // id += ids[i];
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
