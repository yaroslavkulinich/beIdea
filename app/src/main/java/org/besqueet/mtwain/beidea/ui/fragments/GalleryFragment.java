package org.besqueet.mtwain.beidea.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import org.besqueet.mtwain.beidea.Constants;
import org.besqueet.mtwain.beidea.R;
import org.besqueet.mtwain.beidea.ui.view.HackyViewPager;


public class GalleryFragment extends Fragment implements Constants {

    HackyViewPager imagePager;
    ArrayList<String> imagePaths = new ArrayList<>();
    String showPhotoPath = "";

    public GalleryFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        imagePaths = getArguments().getStringArrayList(BUNDLE_IMAGE_PATHS);
        showPhotoPath = getArguments().getString(BUNDLE_PATH);
        imagePager = (HackyViewPager) rootView.findViewById(R.id.imagePager);
        ImageAdapter imageAdapter = new ImageAdapter(getFragmentManager());
        imagePager.setAdapter(imageAdapter);
        imagePager.setCurrentItem(findPathIndex(showPhotoPath));
        return rootView;
    }

    public int findPathIndex(String path){
        for(int i=0;i<imagePaths.size();i++){
            if(path.equals(imagePaths.get(i))){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class ImageAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

        Fragment fragment1,fragment2,fragment3;

        public ImageAdapter(FragmentManager fm) {
            super(fm);
            fragment1 = new ImageFragment();
            fragment2 = new ImageFragment();
            fragment3 = new ImageFragment();
        }


        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString(BUNDLE_PATH,imagePaths.get(position));
            if(position==0){
                fragment1.setArguments(bundle);
                return fragment1;
            }else if(position==1){
                fragment2.setArguments(bundle);
                return fragment2;
            }else if(position==2){
                fragment3.setArguments(bundle);
                return fragment3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return imagePaths.size();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

}
