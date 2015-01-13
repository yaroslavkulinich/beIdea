package beIdea.mtwain.besqueet.beidea.ui.fragments;

import android.app.Fragment;
import android.app.backup.BackupManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slidinglayer.SlidingLayer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import beIdea.mtwain.besqueet.beidea.R;
import beIdea.mtwain.besqueet.beidea.controllers.RealmController;
import beIdea.mtwain.besqueet.beidea.ui.Idea;
import beIdea.mtwain.besqueet.beidea.ui.adapters.IdeaCardArrayAdapter;
import beIdea.mtwain.besqueet.beidea.ui.adapters.ListIdeaAdapter;
import beIdea.mtwain.besqueet.beidea.ui.cards.BaseIdeaCard;
import beIdea.mtwain.besqueet.beidea.ui.cards.IdeaHeaderInnerCard;
import io.realm.RealmResults;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.prototypes.CardSection;
import it.gmariotti.cardslib.library.prototypes.SectionedCardAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class BeIdeaFragment extends Fragment implements View.OnClickListener {

    SlidingUpPanelLayout slidingUpPanelLayout;
    RelativeLayout dragLayout;
    EditText etIdea,etTitle;
    SlidingLayer slidingLayer;
    StickyListHeadersListView stickyList;
    ListIdeaAdapter ideaAdapter;
    TextView tvDetailIdea,tvDetailDate,tvDetailTime;

    static String title,idea = "";
    boolean state = false;

    public BeIdeaFragment(){}

    @Override
    public void onResume() {
        super.onResume();
        etTitle.setText(title);
        etIdea.setText(idea);
    }

    @Override
    public void onPause() {
        super.onPause();
        title = etTitle.getText().toString();
        idea = etIdea.getText().toString();
    }

    @Override
    public void onDestroy(){
        BackupManager bm = new BackupManager(getActivity());
        bm.dataChanged();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beidea, container, false);
        etIdea = (EditText) rootView.findViewById(R.id.etIdea);
        etTitle = (EditText) rootView.findViewById(R.id.etTitle);
        slidingUpPanelLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
        dragLayout = (RelativeLayout) rootView.findViewById(R.id.dragLayout);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("", "onPanelSlide, offset " + slideOffset);
                if(!state){
                    if(slideOffset > 0.8){
                        setUpDragLayout();
                    }
                }else{
                    if(slideOffset < 0.3){
                        etIdea.clearFocus();
                        setDownDragLayout();
                        hideKeyboard();
                    }
                }
            }
            @Override
            public void onPanelExpanded(View panel) {
                Log.i("", "onPanelExpanded");
                etIdea.requestFocus();
            }
            @Override
            public void onPanelCollapsed(View panel) {

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
        /*final View rootView = inflater.inflate(R.layout.fragment_list_idea, container, false);

        stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.lvIdea);
        ideaAdapter = new ListIdeaAdapter(this);
        slidingLayer = (SlidingLayer) rootView.findViewById(R.id.slidingLayer);
        tvDetailIdea = (TextView) slidingLayer.findViewById(R.id.tvDetailIdea);
        tvDetailDate = (TextView) slidingLayer.findViewById(R.id.tvDetailDate);
        tvDetailTime = (TextView) slidingLayer.findViewById(R.id.tvDetailTime);

        slidingLayer.setShadowWidthRes(R.dimen.shadow_width);
        slidingLayer.setOffsetWidth(1);
        slidingLayer.setCloseOnTapEnabled(true);
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_LEFT);

        stickyList.setAdapter(ideaAdapter);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                slidingLayer.openLayer(true);
                tvDetailIdea.setText(RealmController.getIdeas().get(i).getIdea());
                tvDetailDate.setText(RealmController.getIdeas().get(i).getDate());
                tvDetailTime.setText(RealmController.getIdeas().get(i).getTime());
            }
        });*/
       /*final View rootView = inflater.inflate(R.layout.fragment_list_idea, container, false);

        stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.lvIdea);
        ideaAdapter = new ListIdeaAdapter(this);
        slidingLayer = (SlidingLayer) rootView.findViewById(R.id.slidingLayer);
        tvDetailIdea = (TextView) slidingLayer.findViewById(R.id.tvDetailIdea);
        tvDetailDate = (TextView) slidingLayer.findViewById(R.id.tvDetailDate);
        tvDetailTime = (TextView) slidingLayer.findViewById(R.id.tvDetailTime);

        slidingLayer.setShadowWidthRes(R.dimen.shadow_width);
        slidingLayer.setOffsetWidth(1);
        slidingLayer.setCloseOnTapEnabled(true);
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_LEFT);

        stickyList.setAdapter(ideaAdapter);
        stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                slidingLayer.openLayer(true);
                tvDetailIdea.setText(RealmController.getIdeas().get(i).getIdea());
                tvDetailDate.setText(RealmController.getIdeas().get(i).getDate());
                tvDetailTime.setText(RealmController.getIdeas().get(i).getTime());
            }
        });*/

        ArrayList<Card> cards = new ArrayList<>();
        RealmResults<Idea> ideas = RealmController.getIdeas();
        Idea idea;
        for(int i=0; i<ideas.size(); i++){
            idea = ideas.get(i);
        }

        BaseIdeaCard card = new BaseIdeaCard(getActivity());
        IdeaHeaderInnerCard ideaHeaderInnerCard = new IdeaHeaderInnerCard(getActivity());
        ideaHeaderInnerCard.setTopHeader("top header");
        card.addCardHeader(ideaHeaderInnerCard);
        card.text = "main content of card";
        cards.add(card);
        IdeaCardArrayAdapter ideaCardArrayAdapter = new IdeaCardArrayAdapter(getActivity(),cards);
        CardListView listView = (CardListView) rootView.findViewById(R.id.card_idea_list);
        // Define your sections
        List<CardSection> sections =  new ArrayList<>();
        sections.add(new CardSection(0,"Section 1"));
        CardSection[] dummy = new CardSection[sections.size()];

        //Define your Sectioned adapter
        SectionedCardAdapter mAdapter = new SectionedCardAdapter(getActivity(), ideaCardArrayAdapter);
        mAdapter.setCardSections(sections.toArray(dummy));

        if (listView!=null){
            listView.setExternalAdapter(mAdapter,ideaCardArrayAdapter);
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                if(!etIdea.getText().toString().equals("")) {
                    Calendar c = Calendar.getInstance();

                    String hour = c.get(Calendar.HOUR_OF_DAY) + "";
                    if (hour.length() == 1) hour = "0" + hour;
                    String minute = c.get(Calendar.MINUTE) + "";
                    if (minute.length() == 1) minute = "0" + minute;
                    String second = c.get(Calendar.SECOND) + "";
                    if (second.length() == 1) second = "0" + second;

                    String time = hour + ":" + minute + ":" + second;

                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                    String idea = etIdea.getText().toString();
                    String title = etTitle.getText().toString();

                    RealmController.addIdea(idea, title, month, year, day, dayOfWeek, time);
                    Log.d("B", "Saving: " + title + "|" + month + "|" + year + "|" + day + "|" + dayOfWeek + "|" + time);

                }
                clearFields();
                slidingUpPanelLayout.collapsePanel();
                break;

            case R.id.btnCancel:
                clearFields();
                slidingUpPanelLayout.collapsePanel();
                break;
        }
    }

    public void clearFields(){
        etTitle.setText("");
        etIdea.setText("");
    }

    private void setDownDragLayout(){
        if(dragLayout!=null) {
            dragLayout.removeAllViews();
            LayoutInflater ltInflater = getActivity().getLayoutInflater();
            View view = ltInflater.inflate(R.layout.down_state_layout, dragLayout, false);
            dragLayout.addView(view);
            state = false;
            dragLayout.setBackgroundResource(R.drawable.rounded_corner_up);
        }
    }

    private void setUpDragLayout(){
        if(dragLayout!=null) {
            dragLayout.removeAllViews();
            LayoutInflater ltInflater = getActivity().getLayoutInflater();
            View view = ltInflater.inflate(R.layout.up_state_layout, dragLayout, false);
            TextView cancel = (TextView) view.findViewById(R.id.btnCancel);
            cancel.setOnClickListener(this);
            TextView save = (TextView) view.findViewById(R.id.btnSave);
            save.setOnClickListener(this);
            dragLayout.addView(view);
            state = true;
            dragLayout.setBackgroundResource(R.drawable.rounded_corner_down);
        }
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dragLayout.getWindowToken(), 0);
    }

}
