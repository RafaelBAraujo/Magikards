package magikards.main.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import magikards.main.AlbumActivity;
import magikards.main.GameActivity;
import magikards.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View lview = inflater.inflate(R.layout.fragment_main, container, false);



        lview.findViewById(R.id.openAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AlbumActivity.class));
            }
        });

        lview.findViewById(R.id.playGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), GameActivity.class));
                getActivity().finish();

            }
        });

        return lview;
    }



}
