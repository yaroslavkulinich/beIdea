package beIdea.mtwain.besqueet.beidea;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by twain on 07.10.14.
 */
public class ListIdeaFragment extends Fragment {

    public ListIdeaFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_idea, container, false);
        return rootView;
    }
}
