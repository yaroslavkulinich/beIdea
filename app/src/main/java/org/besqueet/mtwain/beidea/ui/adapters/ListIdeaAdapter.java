package org.besqueet.mtwain.beidea.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import org.besqueet.mtwain.beidea.R;
import org.besqueet.mtwain.beidea.controllers.RealmController;
import org.besqueet.mtwain.beidea.controllers.StringsController;
import org.besqueet.mtwain.beidea.ui.Idea;

import java.util.Calendar;

import io.realm.RealmResults;
import me.drakeet.materialdialog.MaterialDialog;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ListIdeaAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    LayoutInflater lInflater;
    RealmResults<Idea>ideas;
    Context context;


    public ListIdeaAdapter(Context context) {
        this.context = context;
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
            final SwipeLayout swipeLayout =  (SwipeLayout)convertView.findViewById(R.id.swipeCard);
            btnDelete = (ImageButton) swipeLayout.findViewById(R.id.ibtnDelete);
            btnDelete.setTag(RealmController.getIdeas().get(position).getTime());
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                    mMaterialDialog.setTitle("Delete idea ?");
                    mMaterialDialog.setMessage("Be careful! You won't be able to restore this idea.");//TODO: Change text and add to strings
                    mMaterialDialog.setPositiveButton("DELETE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            RealmController.deleteIdea(getIdea(position));
                            notifyDataSetChanged();
                            swipeLayout.close();
                            mMaterialDialog.dismiss();

                        }
                    });
                    mMaterialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            swipeLayout.close();
                            mMaterialDialog.dismiss();
                        }
                    });

                    mMaterialDialog.show();

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
        /*if((System.currentTimeMillis() - idea.getTimeInMill())<60000*60*24){
            holder.date.setText("Today");//TODO: додати в Strings
        }else if((System.currentTimeMillis() - idea.getTimeInMill())<60000*60*24*2){
            holder.date.setText("Yesterday");//TODO: додати в Strings
        }else{
            //TODO: додати перевірку на локалі та міняти місяць з числом
            String[]months = StringsController.getMonths();
            holder.date.setText(months[idea.getMonth()]+" "+idea.getDay());
        }*/
        /*Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        if ((today.monthDay - idea.getDay() == 0)){
            holder.date.setText("Today");
        }else{
            if ((today.monthDay - idea.getDay() == 1)){
                holder.date.setText("Yesterday");
            }else{
                if ((today.monthDay - idea.getDay() > 1)){
                    String[]months = StringsController.getMonths();
                    holder.date.setText(months[idea.getMonth()]+" "+idea.getDay());
                }else{
                    if ((today.monthDay - idea.getDay() < 0)){
                        String[]months = StringsController.getMonths();
                        holder.date.setText(months[idea.getMonth()]+" "+idea.getDay());
                    }
                }
            }
        }*/

        long currentTime = System.currentTimeMillis();
        long ideaTime = idea.getTimeInMill();

        Calendar calendarCurrentTime = Calendar.getInstance();
        Calendar calendarIdeaTime = Calendar.getInstance();

        calendarCurrentTime.setTimeInMillis(currentTime);
        calendarIdeaTime.setTimeInMillis(ideaTime);

        String currentDate = calendarCurrentTime.get(Calendar.DAY_OF_MONTH) + ":" + calendarCurrentTime.get(Calendar.MONTH) + ":" + calendarCurrentTime.get(Calendar.YEAR);
        String ideaDate = calendarIdeaTime.get(Calendar.DAY_OF_MONTH) + ":" + calendarIdeaTime.get(Calendar.MONTH) + ":" + calendarIdeaTime.get(Calendar.YEAR);
        if (currentDate.equals(ideaDate)){
            holder.date.setText("Today");
        }else{
            Calendar calendarDifference = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);

            long calendarTimeInMillis = calendar.getTimeInMillis();//23:59:59
            long ideaTime2 = idea.getTimeInMill();//idea time
            //long it3 = calendarCurrentTime.getTimeInMillis();//current time

            long differenceOfTime = calendarTimeInMillis-ideaTime2;
            calendarDifference.setTimeInMillis(differenceOfTime);
            if((differenceOfTime<60000*60*24*2)&&(differenceOfTime>0)){
                holder.date.setText("Yesterday");
            }else{
                    String[]months = StringsController.getMonths();
                    holder.date.setText(months[idea.getMonth()]+" "+idea.getDay());
            }
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
