package beIdea.mtwain.besqueet.beidea.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import beIdea.mtwain.besqueet.beidea.R;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageFragment extends Fragment {

    PhotoViewAttacher mAttacher;
    ImageView image;
    public ImageFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        image = (ImageView) rootView.findViewById(R.id.imageFull);
        String path = getArguments().getString("path");
        Log.d("B","Load: "+image);
        Picasso.with(getActivity())
                .load(new File(path))
                .placeholder(R.drawable.ic_launcher)//TODO:Додати картинку
                .error(R.drawable.ic_launcher)//TODO:Додати картинку помилки
                .into(image);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
