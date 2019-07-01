package magikards.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import magikards.main.data.Pokemon;
import magikards.main.fragments.LoadingFragment;

public class SaveStickerTask extends AsyncTask<Void, Integer, ArrayList<Bitmap>> {

    private static final String TAG = "SaveStickerTask";

    private ArrayList<String> urls;
    private ArrayList<String> fileNames;
    private MainActivity mainActivity = null;
    private VictoryActivity victoryActivity = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LoadingFragment loadingFragment;
    private ProgressBar downloadProgress;
    private FrameLayout loadingFrame;

    private Button btnLogout;
    private Button btnPlayGame;
    private Button btnOpenAlbum;
    private Button btnTrade;

    public SaveStickerTask(MainActivity activity, FragmentManager fragmentManager, FrameLayout loadingFrame) {
        this.fragmentManager = fragmentManager;
        this.mainActivity = activity;
        this.urls = new ArrayList<>();
        this.fileNames = new ArrayList<>();
        this.loadingFrame = loadingFrame;

        this.loadingFragment = new LoadingFragment();
//        this.btnLogout = (Button) activity.findViewById(R.id.logoutbutton);
//        this.btnPlayGame = (Button) activity.findViewById(R.id.playGame);
//        this.btnOpenAlbum = (Button) activity.findViewById(R.id.openAlbum);
//        this.btnTrade = (Button) activity.findViewById(R.id.tradeMagikards);
    }

    public SaveStickerTask(VictoryActivity activity, FragmentManager fragmentManager, FrameLayout loadingFrame) {
        this.fragmentManager = fragmentManager;
        this.victoryActivity = activity;
        this.urls = new ArrayList<>();
        this.fileNames = new ArrayList<>();
        this.loadingFrame = loadingFrame;

        this.loadingFragment = new LoadingFragment();
//        this.btnLogout = (Button) activity.findViewById(R.id.logoutbutton);
//        this.btnPlayGame = (Button) activity.findViewById(R.id.playGame);
//        this.btnOpenAlbum = (Button) activity.findViewById(R.id.openAlbum);
//        this.btnTrade = (Button) activity.findViewById(R.id.tradeMagikards);
    }

    public void addStickerToTask(String url, String fileName) {
        this.urls.add(url);
        this.fileNames.add(fileName);
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public Activity getActivity() {
        if(mainActivity == null)
            return victoryActivity;
        else
            return mainActivity;
    }

    public void restoreActivityToNormal() {
        if(mainActivity == null)
            victoryActivity.returnViewToNormal();
        else {
            mainActivity.returnViewToNormal();
            mainActivity.removeDatabaseReferences();
        }
    }

    @Override
    protected void onPreExecute() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framemenu, loadingFragment, this.getClass().toString());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(Void... voids) {

        Log.d(TAG, "doInBackground: Starting SaveStickerTask");
        ArrayList<Bitmap> images = new ArrayList<>();
        int progress = (int) 95 / this.urls.size(); if(progress == 0) { progress = 1; }
        Log.e(TAG, "doInBackground: PROGRESS: " + String.valueOf(progress) );
        for(String url : this.urls) {

            try {
                Log.d(TAG, "doInBackground: downloadingSticker");
                images.add(ImageLoader.baixarImagem(url));
                publishProgress(progress);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return images;
    }

    @Override
    protected void onProgressUpdate(Integer... progress){
        if(this.loadingFragment.getProgressBar().getProgress() < 80){
            this.loadingFragment.setLoadingMessage("Downloading your stickers...");
        }
        this.loadingFragment.getProgressBar().setProgress(this.loadingFragment.getProgressBar().getProgress() + progress[0]);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostExecute(ArrayList<Bitmap> images) {

       int progress = (int) 20 / images.size();
       for(int i = 0; i < images.size(); i++) {
           File path = new File(this.getActivity().getApplicationContext().getFileStreamPath(fileNames.get(i) + ".png").getPath());
           try {
               FileOutputStream outputStream = new FileOutputStream(path.getAbsolutePath());
               images.get(i).compress(Bitmap.CompressFormat.PNG, 100, outputStream);
               publishProgress(progress);
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }
       }

       this.loadingFragment.getProgressBar().setProgress(100);
        Log.e(TAG, "onPostExecute: popping stack");
       fragmentManager.popBackStack();
       this.restoreActivityToNormal();

//        this.loadingFrame.setVisibility(View.GONE);
//        this.btnLogout.setVisibility(View.VISIBLE);
//        this.btnPlayGame.setVisibility(View.VISIBLE);
//        this.btnTrade.setVisibility(View.VISIBLE);
//        this.btnOpenAlbum.setVisibility(View.VISIBLE);


    }

}