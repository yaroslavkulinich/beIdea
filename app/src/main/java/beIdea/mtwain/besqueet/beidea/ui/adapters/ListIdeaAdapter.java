package beIdea.mtwain.besqueet.beidea.ui.adapters;

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
import beIdea.mtwain.besqueet.beidea.controllers.StringsController;
import beIdea.mtwain.besqueet.beidea.ui.Idea;
import io.realm.RealmResults;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ListIdeaAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    LayoutInflater lInflater;
    RealmResults<Idea>ideas;


    public ListIdeaAdapter(Context context) {
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ideas = RealmController.getIdeas();
        ideas.sort("timeInMill",false);//TODO: додати в Constants
    }

    @Override
    public int getCount() {
        return ideas.size();
    }

    @Override
    public Object getItem(int position) {
        return ideas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ImageButton btnDelete;
        final Idea idea = getIdea(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.idea_card_view, parent, false);
            SwipeLayout swipeLayout =  (SwipeLayout)convertView.findViewById(R.id.swipeCard);
            btnDelete = (ImageButton) swipeLayout.findViewById(R.id.ibtnDelete);
            btnDelete.setTag(RealmController.getIdeas().get(position).getTime());
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Log.d("1","Position "+position+", TIME "+idea.getTime()+"  ANOTHER TIME"+RealmController.getIdeas().get(position).getTime());
                    /*for(int i=0;i<RealmController.getIdeas().size();i++){
                        Log.d("1", RealmController.getIdeas().get(i).getTime());
                    }*/
                    Log.d("B","Click "+position);
                }
            });
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

            holder.idea = (TextView) convertView.findViewById(R.id.tvIdea);
            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.idea.setText(idea.getIdea());
        holder.title.setText(idea.getTitle());
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
        if((System.currentTimeMillis() - idea.getTimeInMill())<60000*60*24){
            holder.date.setText("Today");//TODO: додати в Strings
        }else if((System.currentTimeMillis() - idea.getTimeInMill())<60000*60*24*2){
            holder.date.setText(idea.getTime());//TODO: додати в Strings
        }else{
            //TODO: додати перевірку на локалі та міняти місяць з числом
            String[]months = StringsController.getMonths();
            holder.date.setText(months[idea.getMonth()]+" "+idea.getDay());
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        Idea idea = ideas.get(position);
        String currentIndexDate = idea.getDay()+idea.getMonth()+idea.getYear()+"";
      return Long.parseLong(currentIndexDate);
    }

    class HeaderViewHolder {
        TextView date;
    }

    class ViewHolder {
        TextView idea;
        TextView title;
    }
}
