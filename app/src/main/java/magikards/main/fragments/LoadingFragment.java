package magikards.main.fragments;


import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import magikards.main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView loadingMessage;

    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View lview = inflater.inflate(R.layout.fragment_loading, container, false);

        this.progressBar = (ProgressBar) lview.findViewById(R.id.progressBarLoading);
        this.loadingMessage = (TextView) lview.findViewById(R.id.loadingMessage);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/game_font.ttf");
        loadingMessage.setTypeface(face);

        Drawable progressBarDrawable = progressBar.getProgressDrawable().mutate();
        progressBarDrawable.setColorFilter(this.getResources().getColor(R.color.progressBar),
                PorterDuff.Mode.SRC_IN);
        this.progressBar.setProgressDrawable(progressBarDrawable);

        return lview;
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public TextView getLoadingMessage() { return this.loadingMessage; }

    public void setLoadingMessage(String message) {
        this.loadingMessage.setText(message);
    }
}
