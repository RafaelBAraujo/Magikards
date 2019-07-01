package magikards.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import magikards.main.data.NewSticker;
import magikards.main.fragments.LoadingAlbumFragment;
import magikards.main.fragments.LoadingFragment;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";

    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;

    private GoogleApiClient googleApiClient;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference synchronizeReference;
    private DatabaseReference downloadReference;
    private ValueEventListener downloadListener;
    private ValueEventListener synchronizeListener;

    private SaveStickerTask saveStickerTask;
    private ArrayList<String> stickersToDownload;

    private String pathToBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(TAG, "onCreate: ON CREATE MAIN" );
        fragmentManager = super.getSupportFragmentManager();

        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        //idTextView = (TextView) findViewById(R.id.idTextView);
        pathToBluetooth = null;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    setUserData(user);
                }else{
                    goLogInScreen();
                }

            }
        };


    }

    private void setUserData(FirebaseUser user){

        synchronizeAccountData(user.getUid());
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        //idTextView.setText(user.getUid());
        Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);

    }

    private void synchronizeAccountData(String userUID) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        synchronizeReference = firebaseDatabase.getReference().child("accountsStickers").child(userUID);

        this.stickersToDownload = new ArrayList<>();
        this.saveStickerTask = new SaveStickerTask(this, this.fragmentManager, this.getFrameLayout());

        checkSentFromBluetooth();

        synchronizeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "synchronizeAccountData: synchronizing data");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String stickerID = snapshot.getValue().toString().substring(11, snapshot.getValue().toString().length() - 1);
                    File stickerImage = new File(getApplicationContext().getFileStreamPath(stickerID + ".png").getPath());

                    if(!stickerImage.exists())
                        stickersToDownload.add(stickerID);
                }
                synchronizeReference.removeEventListener(this);
                downloadStickers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }

        });

    }

    private void checkSentFromBluetooth() {

        if(this.pathToBluetooth == null)
            this.pathToBluetooth = searchForBluetoothFolder();

        final File path = new File(this.pathToBluetooth+"/test4.txt");

        if(path.exists()) {
            Log.d(TAG, "checkSentFromBluetooth: reading new traded stickers.");
            StringBuilder text = new StringBuilder();
            ArrayList<NewSticker> newStickers = new ArrayList<>();

            try {
                BufferedReader br = new BufferedReader(new FileReader(path));
                String line;

                while ((line = br.readLine()) != null) {
                    newStickers.add(new NewSticker(Integer.parseInt(line)));
                }
                br.close();
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }

            for(NewSticker n : newStickers) {
                this.synchronizeReference.push().setValue(n);
            }

            path.delete();

        }

    }

    public void downloadStickers() {
        
        ((FrameLayout) this.findViewById(R.id.framemenu)).setVisibility(View.VISIBLE);
        ((Button) this.findViewById(R.id.logoutbutton)).setVisibility(View.GONE);
        ((Button) this.findViewById(R.id.playGame)).setVisibility(View.GONE);
        ((Button) this.findViewById(R.id.tradeMagikards)).setVisibility(View.GONE);
        ((Button) this.findViewById(R.id.openAlbum)).setVisibility(View.GONE);

        if(stickersToDownload.size() > 0) {
            Log.e(TAG, "downloadStickers: DOWNLOADING FROM MAIN");
            downloadReference = firebaseDatabase.getReference("StickersURL");
            downloadReference.addValueEventListener(new ValueEventListener() {
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

                    stickersToDownload.clear();
                    downloadReference.removeEventListener(this);
                    try {
                        saveStickerTask.execute();
                    }
                    catch (IllegalStateException e) {
                        SaveStickerTask newTask = new SaveStickerTask((MainActivity) saveStickerTask.getActivity(), saveStickerTask.getFragmentManager(), getFrameLayout());
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
        else {
            ((FrameLayout) this.findViewById(R.id.framemenu)).setVisibility(View.GONE);
            ((Button) this.findViewById(R.id.logoutbutton)).setVisibility(View.VISIBLE);
            ((Button) this.findViewById(R.id.playGame)).setVisibility(View.VISIBLE);
            ((Button) this.findViewById(R.id.tradeMagikards)).setVisibility(View.VISIBLE);
            ((Button) this.findViewById(R.id.openAlbum)).setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(View view) {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    goLogInScreen();
                }else{
                    Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void revoke(View view) {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    goLogInScreen();
                }else{
                    Toast.makeText(getApplicationContext(), R.string.not_revoke, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    protected void onStop(){
        super.onStop();

        if(firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }

    }

    public void playGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        removeDatabaseReferences();
        startActivity(intent);
        this.finish();

    }

    public void openAlbum(View view) {
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }

    public void tradeMagikards(View view) {
        Intent intent = new Intent(this, TradeStickersActivity.class);
        startActivity(intent);
    }

    public FrameLayout getFrameLayout(){
        return (FrameLayout) this.findViewById(R.id.framemenu);
    }

    public void removeDatabaseReferences() {

        try {
            Log.d(TAG, "removeDatabaseReferences: removing reference listeners");
            this.downloadReference.removeEventListener(downloadListener);
            this.synchronizeReference.removeEventListener(synchronizeListener);
        }
        catch (Exception e) {
            Log.e(TAG, "removeDatabaseReferences: " + e.getMessage() );
        }
    }

    public void returnViewToNormal() {
        ((FrameLayout) this.findViewById(R.id.framemenu)).setVisibility(View.GONE);
        ((Button) this.findViewById(R.id.logoutbutton)).setVisibility(View.VISIBLE);
        ((Button) this.findViewById(R.id.playGame)).setVisibility(View.VISIBLE);
        ((Button) this.findViewById(R.id.tradeMagikards)).setVisibility(View.VISIBLE);
        ((Button) this.findViewById(R.id.openAlbum)).setVisibility(View.VISIBLE);
    }

    public List<File> folderSearchBT(File src, String folder)
            throws FileNotFoundException {

        List<File> result = new ArrayList<File>();

        File[] filesAndDirs = src.listFiles();
        if(filesAndDirs == null){
            return result;
        }

        List<File> filesDirs = Arrays.asList(filesAndDirs);

        for (File file : filesDirs) {
            result.add(file); // always add, even if directory
            if (!file.isFile()) {
                List<File> deeperList = folderSearchBT(file, folder);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    public String searchForBluetoothFolder() {

        String splitchar = "/";
        File root = Environment.getExternalStorageDirectory();
        List<File> btFolder = null;
        String bt = "bluetooth";
        try {
            btFolder = folderSearchBT(root, bt);
        } catch (FileNotFoundException e) {
            Log.e("FILE: ", e.getMessage());
        }

        for (int i = 0; i < btFolder.size(); i++) {

            String g = btFolder.get(i).toString();

            String[] subf = g.split(splitchar);

            String s = subf[subf.length - 1].toUpperCase();

            boolean equals = s.equalsIgnoreCase(bt);

            if (equals)
                return g;
        }
        return null; // not found
    }

}

    /*
        private void handleSignInResult(GoogleSignInResult result) {
            if(result.isSuccess()){

                GoogleSignInAccount account = result.getSignInAccount();

                nameTextView.setText(account.getDisplayName());
                emailTextView.setText(account.getEmail());
                idTextView.setText(account.getId());

                Glide.with(this).load(account.getPhotoUrl()).into(photoImageView);

            }else{
                goLogInScreen();
            }
        }
    */
