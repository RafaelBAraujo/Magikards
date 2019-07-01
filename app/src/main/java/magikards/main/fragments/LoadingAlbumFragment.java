package magikards.main.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import magikards.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingAlbumFragment extends Fragment {


    public LoadingAlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View lview = inflater.inflate(R.layout.fragment_loading_album, container, false);



        return  lview;
    }

}
