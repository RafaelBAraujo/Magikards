package magikards.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import magikards.main.data.Pokemon;

import static android.support.constraint.Constraints.TAG;


public class GetStickersURLSTask extends AsyncTask<String, Void, ArrayList<String>> {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth auth;
    private AlbumActivity activity;
    private Map<String, String> urlsMap;
    private ArrayList<String> urls;

    public GetStickersURLSTask(AlbumActivity context) {

        this.activity = context;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.mFirebaseDatabaseReference = this.firebaseDatabase.getReference("StickersURL");
        this.urls = new ArrayList<>();

        this.auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            signInAnonymously();
        }

    }

    @Override
    protected ArrayList<String> doInBackground(String... keys) {


        mFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    urlsMap = (Map<String, String>) dataSnapshot.getValue();

                    Iterator it = urlsMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        String url = pair.getValue().toString().substring(5, pair.getValue().toString().length() - 1);
                        System.out.println(url);
                        urls.add(url);
                        it.remove();
                    }
                }
                catch (Exception e){
                    Log.e(TAG, "onDataChange: " + e.getMessage());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: error at getting URLs");
            }
        });

        return urls;
    }

    @Override
    protected void onPostExecute(ArrayList<String> urls) {

        Log.d(TAG, "onPostExecute: Starting download of images");
        //DownloadStickersTask downloadStickersTask = new DownloadStickersTask(this.activity, urls);
        //downloadStickersTask.execute();

    }

    private void signInAnonymously() {
        auth.signInAnonymously().addOnSuccessListener(activity, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

}

class PokemonURL {

    private String url;

    public PokemonURL() {

    }

    public PokemonURL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}