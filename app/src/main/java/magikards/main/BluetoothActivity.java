package magikards.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import magikards.main.data.NewSticker;

import static android.support.constraint.Constraints.TAG;

public class BluetoothActivity extends AppCompatActivity {

    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;

    String path;

    private static final String[] INITIAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int INITIAL_REQUEST = 1337;

    private static final int REQUEST_WRITE_STORAGE = INITIAL_REQUEST + 4;

    TextView textView_FileName;
    String pathToBluetooth;

    ArrayList<Integer> selectedStickers;
    ArrayList<Integer> stickersToDelete;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        this.selectedStickers = getIntent().getIntegerArrayListExtra("selectedStickers");

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference().child("accountsStickers").child(user.getUid());

        stickersToDelete = new ArrayList<>();
        for(Integer id : this.selectedStickers){
            stickersToDelete.add(id);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String stickerID = snapshot.getValue().toString().substring(11, snapshot.getValue().toString().length() - 1);
                    if(stickersToDelete.contains(new Integer(Integer.parseInt(stickerID)))) {
                        snapshot.getRef().removeValue();
                        stickersToDelete.remove(new Integer(Integer.parseInt(stickerID)));
                    }
                }

                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: failed to remove stickers from firebase.");
            }
        });

        if (!canAccessLocation() || !canAccessCamera() || !canAccessWriteStorage() || !canAccessReadStorage() || !canAccessReadContacts() || !canAccessWriteContacts()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }
        }

        sendViaBluetooth();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case REQUEST_WRITE_STORAGE:
                if (canAccessWriteStorage()) {
                    //reload my activity with permission granted or use the features what required the permission
                    System.out.println("permission grantedddd");

                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void sendViaBluetooth() {


        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
        } else {
            enableBluetooth();
        }
    }

    public void getFile() {
        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mediaIntent.setType("*/*"); //set mime type as per requirement
        startActivityForResult(mediaIntent, 1001);
    } /** USELESS, THIS IS FUCKING USELESS **/

    public void enableBluetooth() {
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("*/*");

            writeToFile(this.selectedStickers);
            final File path =
                    Environment.getExternalStoragePublicDirectory
                            (
                                    //Environment.DIRECTORY_PICTURES
                                    Environment.DIRECTORY_DCIM + "/magikards/"
                            );


            final File f = new File(path, "test4.txt");

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));

            PackageManager pm = this.getPackageManager();
            List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);

            if (appsList.size() > 0) {
                String packageName = null;
                String className = null;
                boolean found = false;

                for (ResolveInfo info : appsList) {
                    packageName = info.activityInfo.packageName;
                    if (packageName.equals("com.android.bluetooth")) {
                        className = info.activityInfo.name;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(this, "Bluetooth havn't been found",
                            Toast.LENGTH_LONG).show();
                } else {
                    intent.setClassName(packageName, className);
                    startActivity(intent);
                    this.finish();
                }
            }
        } else if (requestCode == 1001
                && resultCode == Activity.RESULT_OK) {
            Uri uriPath = data.getData();
            Log.d("", "Video URI= " + uriPath);

            path = getPath(this, uriPath);// "/mnt/sdcard/FileName.mp3"
            System.out.println("pathhhh " + path);
            textView_FileName.setText(path);

        } else {
            Toast.makeText(this, "Bluetooth is cancelled", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void writeToFile(ArrayList<Integer> selectedStickers)
    {
        // Get the directory for the user's public pictures directory.
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

        final File file = new File(path, "test4.txt");

        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            for(Integer id : selectedStickers) {
                myOutWriter.append(String.valueOf(id) + "\n");
            }

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private boolean canAccessWriteStorage() {
        return (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean canAccessReadStorage() {
        return (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    private boolean canAccessReadContacts() {
        return (hasPermission(Manifest.permission.READ_CONTACTS));
    }

    private boolean canAccessWriteContacts() {
        return (hasPermission(Manifest.permission.WRITE_CONTACTS));
    }

    private boolean canAccessCamera() {
        return (hasPermission(Manifest.permission.CAMERA));
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public List<File> folderSearchBT(File src, String folder)
            throws FileNotFoundException {

        List<File> result = new ArrayList<File>();

        File[] filesAndDirs = src.listFiles();
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