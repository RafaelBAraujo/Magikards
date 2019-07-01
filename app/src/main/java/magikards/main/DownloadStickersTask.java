package magikards.main;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.ArrayList;

import magikards.main.data.Pokemon;

public class DownloadStickersTask extends AsyncTask<Void, Void, ArrayList<Bitmap>> {

    private static final String TAG = "DownloadStickersTask";

    private ArrayList<String> urls;
    private AlbumActivity activity;
    ArrayList<Pokemon> pokemons;

    public DownloadStickersTask(AlbumActivity activity) {
        this.urls = null;
        this.pokemons = null;
        this.activity = activity;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public void setPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(Void... voids) {

        Log.d(TAG, "doInBackground: Starting DownloadStickersTask");
        ArrayList<Bitmap> images = new ArrayList<>();
        for(String url : urls) {
            try {
                Log.d(TAG, "doInBackground: downloadingSticker");
                images.add(ImageLoader.baixarImagem(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return images;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostExecute(ArrayList<Bitmap> images) {

        Log.d(TAG, "onPostExecute: Finished downloads");
        FrameLayout frame = (FrameLayout) this.activity.findViewById(R.id.albumframe);
        frame.setVisibility(View.GONE);
        if(this.pokemons != null)
            this.activity.initRecyclerView(this.pokemons, images);
        else
            Log.e(TAG, "onPostExecute: POKEMONS IS NULL" );

    }

}