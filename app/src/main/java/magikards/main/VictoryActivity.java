package magikards.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import magikards.main.data.NewSticker;

public class VictoryActivity extends AppCompatActivity {

    private static final String TAG = "VictoryActivity";

    MediaPlayer mediaPlayer;

    private RecyclerView recyclerView;
    private StickerRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Bitmap> newStickers;
    ArrayList<NewSticker> newStickersIds;
    ArrayList<String> stickersToDownload;

    private FragmentManager fragmentManager;
    private SaveStickerTask saveStickerTask;

    //FirebaseDatabase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        fragmentManager = super.getSupportFragmentManager();

        newStickersIds = new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("accountsStickers").child(user.getUid());

        this.addVictoryBtnListener();
        this.drawStickers();
        mediaPlayer = MediaPlayer.create(this, R.raw.victory);

    }

    @Override
    public void onStart(){
        super.onStart();
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    private void addVictoryBtnListener() {
        Button button = (Button) this.findViewById(R.id.victoryButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickersToDownload = new ArrayList<>();
                for(NewSticker n : newStickersIds) {
                    databaseReference.push().setValue(n);
                }
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                finishActivity();
            }
        });
    }

    private void finishActivity() {
        this.startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private void drawStickers() {

        this.newStickers = new ArrayList<>();

        Random numGenerator = new Random();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fireRef = storage.getReference();

        final long ONE_MEGABYTE = 1024 * 1024;
        for(int i = 0; i < 5; i++) {
            // PEGA 5 FIGURINHAS
            int drawnStickerNumber = numGenerator.nextInt(151) + 1;
            this.newStickersIds.add(new NewSticker(drawnStickerNumber));
            StorageReference pathReference = fireRef.child("pokemon/"+ String.valueOf(drawnStickerNumber) +".png");
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap sticker = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    newStickers.add(sticker);
                    if(newStickers.size() == 5)
                        initRecyclerView();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: could not load sticker from storage.");
                }
            });
        }
    }

    private void initRecyclerView() {
        this.layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.recyclerView = (RecyclerView) findViewById(R.id.hrlist_recycler_view);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.adapter = new StickerRecyclerAdapter(this.newStickers);
        this.recyclerView.setAdapter(adapter);
    }

    public void downloadStickers() {

        ((FrameLayout) this.findViewById(R.id.framevictory)).setVisibility(View.VISIBLE);
        ((Button) this.findViewById(R.id.victoryButton)).setVisibility(View.GONE);
        ((RecyclerView) this.findViewById(R.id.hrlist_recycler_view)).setVisibility(View.GONE);

        saveStickerTask = new SaveStickerTask(this, this.fragmentManager, getFrameLayout());

        if(stickersToDownload.size() > 0) {
            DatabaseReference newReference = firebaseDatabase.getReference("StickersURL");
            newReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, String> urlsMap = (HashMap<String, String>) dataSnapshot.getValue();
                    Iterator it = urlsMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        String key = String.valueOf(pair.getKey()).substring(7);
                        for(String k : stickersToDownload) {
                            if(k.compareTo(key) == 0) {
                                String stickerURL = pair.getValue().toString().substring(5, pair.getValue().toString().length() - 1);
                                saveStickerTask.addStickerToTask(stickerURL, key);
                            }
                        }
                    }

                    try {
                        saveStickerTask.execute();
                    }
                    catch (IllegalStateException e) {
                        SaveStickerTask newTask = new SaveStickerTask((VictoryActivity) saveStickerTask.getActivity(), saveStickerTask.getFragmentManager(), getFrameLayout());
                        newTask.setUrls(saveStickerTask.getUrls());
                        newTask.setFileNames(saveStickerTask.getFileNames());
                        newTask.execute();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                }
            });

        }

    }

    public void returnViewToNormal() {
        this.finish();
    }

    public FrameLayout getFrameLayout(){
        return (FrameLayout) this.findViewById(R.id.framevictory);
    }

}
