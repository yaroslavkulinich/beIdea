package org.besqueet.mtwain.beidea.ui.fragments;

import android.app.Activity;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slidinglayer.SlidingLayer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.besqueet.mtwain.beidea.BeIdeaActivity;
import org.besqueet.mtwain.beidea.Constants;
import org.besqueet.mtwain.beidea.controllers.RealmController;
import org.besqueet.mtwain.beidea.ui.Idea;
import org.besqueet.mtwain.beidea.ui.adapters.ListIdeaAdapter;
import org.besqueet.mtwain.beidea.utils.Utilites;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import org.besqueet.mtwain.beidea.R;
import io.realm.Realm;
import me.drakeet.materialdialog.MaterialDialog;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class BeIdeaFragment extends Fragment implements Constants,View.OnClickListener {

    LinearLayout generalImageContainer,detailImageContainer;
    SlidingUpPanelLayout slidingUpPanelLayout;
    FloatingActionsMenu actionButton;
    FloatingActionButton btnTakePhoto,btnGetPicture,btnEditIdea,btnSaveIdea;
    RelativeLayout dragLayout;
    EditText etIdea,etTitle;
    SlidingLayer showIdeaLayer,saveButtonLayer;
    ListIdeaAdapter listIdeaAdapter;
    EditText etDetailIdea, etDetailTitle;
    ImageView add_detail_image;


    private static final int SELECT_PHOTO = 1;//TODO: to constants
    private static final int REQUEST_IMAGE_CAPTURE = 2 ;//TODO: to constants
    private static final int SELECT_PHOTO_DETAIL = 3;//TODO: to constants
    private static final int REQUEST_IMAGE_CAPTURE_DETAIL = 4 ;//TODO: to constants

    static String title,idea,currentPicturePath = "";
    boolean state = false;
    boolean editingEnable = false;
    Idea editingIdea;
    int editingIdeaIndex;

    ArrayList<String>imagePaths = new ArrayList<>();
    ArrayList<String>editingImages = new ArrayList<>();

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
        generalImageContainer = (LinearLayout) rootView.findViewById(R.id.general_image_container);
        detailImageContainer = (LinearLayout) rootView.findViewById(R.id.detail_image_container);
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
               // Log.i("", "onPanelSlide, offset " + slideOffset);
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
                //Log.i("", "onPanelExpanded");
                etIdea.requestFocus();
            }
            @Override
            public void onPanelCollapsed(View panel) {

            }
            @Override
            public void onPanelAnchored(View panel) {
                /*Log.i("", "onPanelAnchored");*/
            }

            @Override
            public void onPanelHidden(View panel) {
                /*Log.i("", "onPanelHidden");*/
            }
        });

        saveButtonLayer = (SlidingLayer) rootView.findViewById(R.id.saveButtonLayer);
        saveButtonLayer.setCloseOnTapEnabled(false);
        saveButtonLayer.setSlidingEnabled(false);

        showIdeaLayer = (SlidingLayer) rootView.findViewById(R.id.slidingLayer);
        showIdeaLayer.setShadowWidthRes(R.dimen.shadow_width);
        showIdeaLayer.setOffsetWidth(1);
        showIdeaLayer.setCloseOnTapEnabled(false);
        showIdeaLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);
        showIdeaLayer.setOnInteractListener(new SlidingLayer.OnInteractListener() {
            @Override
            public void onOpened() {
            }

            @Override
            public void onOpen() {

            }
            @Override
            public void onClosed() {
                editingEnable = false;
                editingImages = new ArrayList<>();
                etDetailIdea.setEnabled(false);
                etDetailTitle.setEnabled(false);
                showSaveDetailButton(false);
            }
            @Override
            public void onClose() {

            }
        });
        etDetailIdea = (EditText) showIdeaLayer.findViewById(R.id.tvDetailIdea);
        etDetailTitle = (EditText) showIdeaLayer.findViewById(R.id.tvDetailTitle);
        btnEditIdea = (FloatingActionButton) rootView.findViewById(R.id.editIdea);
        btnEditIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDetail();
            }
        });
        btnSaveIdea = (FloatingActionButton) saveButtonLayer.findViewById(R.id.saveIdea);
        btnSaveIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetail();
            }
        });

        StickyListHeadersListView cardListView = (StickyListHeadersListView) rootView.findViewById(R.id.card_idea_list);
        listIdeaAdapter = new ListIdeaAdapter(getActivity());
        cardListView.setAdapter(listIdeaAdapter);
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editingIdeaIndex = i;
                showIdea(i);
            }
        });
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
                File image = Utilites.generatePicturePath();
                if (image != null) {
                    makePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                    currentPicturePath = image.getAbsolutePath();
                }
                startActivityForResult(makePhotoIntent, REQUEST_IMAGE_CAPTURE);
                actionButton.collapse();
            }
        });

        add_detail_image = (ImageView) showIdeaLayer.findViewById(R.id.add_image);
        add_detail_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1
                );
                arrayAdapter.add("Take photo");
                arrayAdapter.add("Gallery");
                ListView listView = new ListView(getActivity());
                listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (8 * scale + 0.5f);
                listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
                listView.setDividerHeight(0);
                listView.setAdapter(arrayAdapter);

                final MaterialDialog alert = new MaterialDialog(getActivity())
                        .setTitle("Add Image")
                        .setContentView(listView);
                alert.setPositiveButton(
                        "Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        }
                );
                alert.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i==0){
                            Intent makePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File image = Utilites.generatePicturePath();
                            if (image != null) {
                                makePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                                currentPicturePath = image.getAbsolutePath();
                            }
                            startActivityForResult(makePhotoIntent, REQUEST_IMAGE_CAPTURE_DETAIL);
                            alert.dismiss();
                        }else if(i==1){
                            Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
                            imagePickerIntent.setType("image/*");
                            startActivityForResult(imagePickerIntent,SELECT_PHOTO_DETAIL);
                            alert.dismiss();
                        }
                    }
                });
            }
        });
        return rootView;
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
                case REQUEST_IMAGE_CAPTURE_DETAIL:
                    File fileDetail = new File(currentPicturePath);
                    Boolean bDetail = fileDetail.exists();
                    if (bDetail) {
                        putImageIntoDetail(currentPicturePath,true);
                    }
                    break;
                case SELECT_PHOTO_DETAIL:
                    Uri selectedImageDetail = data.getData();
                    String imagePathDetail = Utilites.getPath(getActivity(), selectedImageDetail);
                    putImageIntoDetail(imagePathDetail,true);
                    break;

            }
        }
    }

    public void putImageIntoDetail(final String path,boolean once){
        if(once) {
            if (editingImages.contains(path)) {
                return;
            }
            editingImages.add(path);
        }
        LayoutInflater lInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View photoView = lInflater.inflate(R.layout.photo_view, detailImageContainer, false);
        ImageView image = (ImageView) photoView.findViewById(R.id.imgImage);
        ImageView imageRemove = (ImageView) photoView.findViewById(R.id.imgRemove);
        detailImageContainer.addView(photoView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new GalleryFragment();
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_PATH, path);
                bundle.putStringArrayList(BUNDLE_IMAGE_PATHS, editingImages);
                fragment.setArguments(bundle);
                ((BeIdeaActivity) getActivity()).presentFragment(fragment);
            }
        });
        imageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editingImages.remove(path);
                photoView.setVisibility(View.GONE);
                add_detail_image.setVisibility(View.VISIBLE);
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
        if(!editingEnable){
            imageRemove.setVisibility(View.GONE);
            add_detail_image.setVisibility(View.INVISIBLE);
        }else{
            add_detail_image.setVisibility(View.VISIBLE);
        }

        if(editingImages.size()>=3){
            add_detail_image.setVisibility(View.GONE);
        }

    }

    public void showSaveDetailButton(boolean show){
        if(show){
            saveButtonLayer.openLayer(true);
        }else{
            saveButtonLayer.closeLayer(true);
        }

    }

    public void putImage(final String path){
        if(imagePaths.contains(path)) return;
        imagePaths.add(path);
        LayoutInflater lInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View photoView = lInflater.inflate(R.layout.photo_view, generalImageContainer, false);
        ImageView image = (ImageView) photoView.findViewById(R.id.imgImage);
        ImageView imageRemove = (ImageView) photoView.findViewById(R.id.imgRemove);
        generalImageContainer.addView(photoView);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new GalleryFragment();
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_PATH, path);
                bundle.putStringArrayList(BUNDLE_IMAGE_PATHS, imagePaths);
                fragment.setArguments(bundle);
                ((BeIdeaActivity) getActivity()).presentFragment(fragment);
            }
        });
        imageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePaths.remove(path);
                photoView.setVisibility(View.GONE);
                actionButton.setVisibility(View.VISIBLE);
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
        if(imagePaths.size()==3){
            actionButton.setVisibility(View.INVISIBLE);
            //TODO:додати функцію видалення - в ній зробити кнопку видимою
        }
    }

    public void showIdea(int position){
        detailImageContainer.removeAllViews();
        showIdeaLayer.openLayer(true);
        editingIdea = RealmController.getIdeas().get(position);
        etDetailIdea.setText(editingIdea.getIdea());
        etDetailTitle.setText(editingIdea.getTime());
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<String>>(){}.getType();
        editingImages = gson.fromJson(editingIdea.getImages(), collectionType);
        for(int i=0;i<editingImages.size();i++){
            putImageIntoDetail(editingImages.get(i),false);
        }
        if(!editingEnable){
            add_detail_image.setVisibility(View.GONE);
        }else{
            add_detail_image.setVisibility(View.VISIBLE);
        }

    }


    public void editDetail(){

        if(!editingEnable){
            editingEnable = true;
            etDetailIdea.setEnabled(true);
            etDetailTitle.setEnabled(true);
            showIdea(editingIdeaIndex);
            etDetailIdea.requestFocus();
            etDetailIdea.setSelection(etDetailIdea.getText().length(), etDetailIdea.getText().length());
            showSaveDetailButton(true);

        }else {
            etDetailIdea.setEnabled(false);
            etDetailTitle.setEnabled(false);
            editingEnable = false;
            showSaveDetailButton(false);
            showIdea(editingIdeaIndex);

        }
    }

    public void saveDetail(){
        Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();
        editingIdea.setIdea(etDetailIdea.getText() + "");
        editingIdea.setTitle(etDetailTitle.getText() + "");
        editingIdea.setImages(new Gson().toJson(editingImages));
        realm.commitTransaction();
        saveButtonLayer.closeLayer(true);
        etDetailIdea.setEnabled(false);
        etDetailTitle.setEnabled(false);
        editingEnable = false;
        showIdeaLayer.closeLayer(true);
        listIdeaAdapter.notifyDataSetChanged();
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
                    Gson gson = new Gson();
                    String images = gson.toJson(imagePaths);
                    RealmController.addIdea(idea, title, month, year, day, dayOfWeek, time,c.getTimeInMillis(),images);
                    Log.d("B", "Saving: " + title + "|" + month + "|" + year + "|" + day + "|" + dayOfWeek + "|" + time);
                    ((BeIdeaActivity)getActivity()).requestBackup();
                }
                clearFields();
                imagePaths.clear();
                listIdeaAdapter.notifyDataSetChanged();
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
        generalImageContainer.removeAllViews();
        actionButton.setVisibility(View.VISIBLE);
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
