package beIdea.mtwain.besqueet.beidea.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.slidinglayer.SlidingLayer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import beIdea.mtwain.besqueet.beidea.BeIdeaActivity;
import beIdea.mtwain.besqueet.beidea.Constants;
import beIdea.mtwain.besqueet.beidea.R;
import beIdea.mtwain.besqueet.beidea.controllers.RealmController;
import beIdea.mtwain.besqueet.beidea.ui.adapters.ListIdeaAdapter;
import beIdea.mtwain.besqueet.beidea.utils.Utilites;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class BeIdeaFragment extends Fragment implements Constants,View.OnClickListener {

    LinearLayout horizontalLinearLayout;
    SlidingUpPanelLayout slidingUpPanelLayout;
    FloatingActionsMenu actionButton;
    FloatingActionButton btnTakePhoto,btnGetPicture;
    RelativeLayout dragLayout;
    EditText etIdea,etTitle;
    SlidingLayer slidingLayer;
    StickyListHeadersListView stickyList;
    ListIdeaAdapter ideaAdapter;
    TextView tvDetailIdea,tvDetailDate,tvDetailTime;

    private static final int SELECT_PHOTO = 1;//TODO: to constants
    private static final int REQUEST_IMAGE_CAPTURE = 2 ;//TODO: to constants

    static String title,idea,currentPicturePath = "";
    boolean state = false;

    ArrayList<String>imagePaths = new ArrayList<>();

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
        horizontalLinearLayout = (LinearLayout) rootView.findViewById(R.id.horizontalLinearLayout);
        actionButton = (FloatingActionsMenu) rootView.findViewById(R.id.actionButton);
        btnTakePhoto = (FloatingActionButton) rootView.findViewById(R.id.takePhoto);
        btnGetPicture = (FloatingActionButton) rootView.findViewById(R.id.getPicture);
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

        StickyListHeadersListView cardListView = (StickyListHeadersListView) rootView.findViewById(R.id.card_idea_list);
        ListIdeaAdapter listIdeaAdapter = new ListIdeaAdapter(getActivity());
        cardListView.setAdapter(listIdeaAdapter);
        btnGetPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
                imagePickerIntent.setType("image/*");
                startActivityForResult(imagePickerIntent,SELECT_PHOTO);
                actionButton.collapse();
            }
        });
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent makePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File image = generatePicturePath();
                if (image != null) {
                    makePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                    currentPicturePath = image.getAbsolutePath();
                }
                startActivityForResult(makePhotoIntent, REQUEST_IMAGE_CAPTURE);
                actionButton.collapse();
            }
        });
        return rootView;
    }

    public static File generatePicturePath() {
        try {
            File storageDir = getAlbumDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            return new File(storageDir, "IMG_" + timeStamp + ".jpg");
        } catch (Exception ignored) {

        }
        return null;
    }

    private static File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "beIdea");//TODO:вказати потрібний шлях
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()){
                        return null;
                    }
                }
            }
        }
        return storageDir;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_PHOTO:
                    Uri selectedImage = data.getData();
                    String imagePath = Utilites.getPath(getActivity(), selectedImage);
                    putImage(imagePath);
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    File file = new File(currentPicturePath);
                    Boolean b = file.exists();
                    if (b) {
                        putImage(currentPicturePath);
                    }
                    break;
            }
        }
    }

    public void putImage(final String path){

        LayoutInflater lInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView image = (ImageView) lInflater.inflate(R.layout.photo_view, horizontalLinearLayout, false);
        horizontalLinearLayout.addView(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment imageFragment = new ImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("path",path);//TODO:add to Constants
                imageFragment.setArguments(bundle);
                ((BeIdeaActivity)getActivity()).presentFragment(imageFragment);
            }
        });
        File file = new File(path);
        Boolean b = file.exists();
        if (b){
            Picasso.with(getActivity())
                    .load(new File(path))
                    .resize(50, 50)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)//TODO:Додати картинку
                    .error(R.drawable.ic_launcher)//TODO:Додати картинку помилки
                    .into(image);
        }else {
            Log.d("B","File doesn't exists");
        }

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

                    RealmController.addIdea(idea, title, month, year, day, dayOfWeek, time,c.getTimeInMillis());
                    Log.d("B", "Saving: " + title + "|" + month + "|" + year + "|" + day + "|" + dayOfWeek + "|" + time);
                }
                clearFields();
                imagePaths.clear();
                slidingUpPanelLayout.collapsePanel();
                break;

            case R.id.btnCancel:
                clearFields();
                imagePaths.clear();
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
