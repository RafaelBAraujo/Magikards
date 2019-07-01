package magikards.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TradeStickersActivity extends AppCompatActivity implements TradeRecyclerViewAdapter.ItemClickListener{

    TradeRecyclerViewAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    Set<Integer> stickers;
    ArrayList<Integer> stickersID;
    Button tradeButton;
    Set<Integer> stickersPositions;
    ArrayList<Integer> selectedStickers;
    FrameLayout tradeFrameLayout;

    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_stickers);

        Toast.makeText(this, getString(R.string.tradeMessage), Toast.LENGTH_LONG).show();
        fragmentManager = super.getSupportFragmentManager();

        tradeFrameLayout = (FrameLayout) findViewById(R.id.tradeframe);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                intent.putIntegerArrayListExtra("selectedStickers", selectedStickers);
                startActivity(intent);
            }
        };

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int stickerSize = displayMetrics.widthPixels - this.dpToPx(12 * 6);

        this.tradeButton = (Button) findViewById(R.id.tradeReturnButton);
        this.tradeButton.setOnClickListener(listener);
        this.tradeButton.setVisibility(View.GONE);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference().child("accountsStickers").child(user.getUid());

        stickers = new HashSet<>();
        stickersID = new ArrayList<>();

        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Integer stickerID = Integer.parseInt(snapshot.getValue().toString().substring(11, snapshot.getValue().toString().length() - 1));
                    if(stickers.contains(stickerID)){
                        stickersID.add(stickerID);
                    }
                    else{
                        stickers.add(stickerID);
                    }

                }

                Collections.sort(stickersID);
                selectedStickers = new ArrayList<>();
                stickersPositions = new HashSet<>();

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tradeRecycler);
                int numOfColumns = 3;
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext() , numOfColumns));
                adapter = new TradeRecyclerViewAdapter(getApplicationContext(), stickerSize, stickersID);
                adapter.setClickListener(new TradeRecyclerViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if(stickersPositions.contains(position)) {
                            stickersPositions.remove(position);
                            selectedStickers.remove(new Integer(Integer.parseInt(adapter.getItem(position))));
                        }
                        else {
                            stickersPositions.add(position);
                            selectedStickers.add(Integer.parseInt(adapter.getItem(position)));
                        }

                        if(selectedStickers.size() > 0) {
                            tradeButton.setVisibility(View.VISIBLE);
                        }
                        else {
                            tradeButton.setVisibility(View.GONE);
                        }

                    }
                });
                recyclerView.setAdapter(adapter);

                firebaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();

        if(isFinishing()) {
            deleteBluetoothFile();
        }

    }

    private void deleteBluetoothFile() {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DCIM + "/magikards/"
                        );

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, "test3.txt");
        file.delete();

    }

    public ArrayList<Integer> getSelectedStickers() {
        return selectedStickers;
    }

    private int pxToDp(int px) {
        DisplayMetrics displayMetrics = this.getApplicationContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getApplicationContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void returnToMainMenu(){
        this.finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
    }

}
