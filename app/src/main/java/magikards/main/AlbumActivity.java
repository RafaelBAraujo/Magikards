package magikards.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import magikards.main.data.Pokemon;
import magikards.main.data.TipoPokemon;
import magikards.main.fragments.LoadingAlbumFragment;


public class AlbumActivity extends AppCompatActivity {

    private static final String TAG = "AlbumActivity";

    private RecyclerView recyclerView;
    private HorizontalAdapter horizontalAdapter;
    private LruCache<String, Bitmap> mMemoryCache;
    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mFirebaseDatabaseReference;
    private HashMap<String, String> urlsMap;
    private ArrayList<String> urls;
    private ArrayList<Pokemon> pokemons;
    DownloadStickersTask downloadStickersTask;
    private ArrayList<Bitmap> stickers;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        fragmentManager = super.getSupportFragmentManager();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fireRef = storage.getReference();

        this.pokemons = this.pickPokemon(this);
        Collections.sort(pokemons, new Comparator<Pokemon>() {
            @Override
            public int compare(Pokemon o1, Pokemon o2) {
                if(o1.getPokedexNumber() < o2.getPokedexNumber())
                    return -1;
                else
                    return 1;
            }
        });

        stickers = new ArrayList<>();
        for(int i = 1; i <= 151; i++) {
            File pathToSticker = new File(this.getApplicationContext().getFileStreamPath(String.valueOf(i) + ".png").getPath());
            if(pathToSticker.exists()) {
                try {
                    Bitmap sticker = BitmapFactory.decodeStream(new FileInputStream(pathToSticker));
                    stickers.add(sticker);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
                stickers.add(null);
            }
        }

        initRecyclerView(pokemons, stickers);

    }

    public void addBitmapToCache(String key, Bitmap bitmap){
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key){
        Bitmap bitmap;
        try {
            bitmap = mMemoryCache.get(key);
        }
        catch (NullPointerException e) {
            bitmap = null;
        }
        return bitmap;
    }

    public void initRecyclerView(ArrayList<Pokemon> pokemons, ArrayList<Bitmap> images) {
        Log.d(TAG, "initRecyclerView: init RecyclerView");
        RecyclerView recyclerView = findViewById(R.id.horizontal_recycler_view);
        AlbumRecyclerViewAdapter adapter = new AlbumRecyclerViewAdapter(this, images, pokemons);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<Pokemon> pickPokemon(Context appContext) {

        Pokemon pokemon = null;
        ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();


        InputStream pokemonInputStream = appContext.getResources().openRawResource(R.raw.species);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(pokemonInputStream))) {

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            String everything = sb.toString();

            JSONArray pokeJson = new JSONArray(everything);

            Random random = new Random();
            Integer randPokeIndex = random.nextInt(pokeJson.length());

            int x = 0;
            while ( x < 151) {

                JSONObject randPoke = pokeJson.getJSONObject(x);

                String pokeNumber = randPoke.getString("national_pokedex_number");
                String randPokeName = randPoke.getString("name");
                int ataque = randPoke.getJSONObject("baseStats").getInt("attack");
                int hp = randPoke.getJSONObject("baseStats").getInt("hp");
                int defesa = randPoke.getJSONObject("baseStats").getInt("defense");
                JSONArray types = randPoke.getJSONArray("types");

                ArrayList<TipoPokemon> enumTipos = new ArrayList<>();
                for (int i = 0; i < types.length(); i++) {
                    enumTipos.add(pokemonTypeToEnum(types.getString(i)));
                }

                pokemons.add(new Pokemon(Integer.parseInt(pokeNumber), randPokeName, hp, enumTipos, 100, null, ataque, defesa));
                x++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return pokemons;

    }

    private static TipoPokemon pokemonTypeToEnum(String type) {

        switch (type) {

            case "grass":
                return TipoPokemon.Grass;

            case "dragon":
                return TipoPokemon.Dragon;

            case "ice":
                return TipoPokemon.Ice;

            case "fighting":
                return TipoPokemon.Fighting;

            case "fire":
                return TipoPokemon.Fire;

            case "flying":
                return TipoPokemon.Flying;

            case "ghost":
                return TipoPokemon.Ghost;

            case "ground":
                return TipoPokemon.Ground;

            case "electric":
                return TipoPokemon.Electric;

            case "normal":
                return TipoPokemon.Normal;

            case "poison":
                return TipoPokemon.Poison;

            case "psychic":
                return TipoPokemon.Psychic;

            case "rock":
                return TipoPokemon.Rock;

            case "water":
                return TipoPokemon.Water;

            default:
                return TipoPokemon.Normal;

        }

    }

}
class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


    ArrayList<Pokemon> pokemons;
    Context context;
    int fotoPokemon = 1;
    private AlbumActivity aA = new AlbumActivity();


    public HorizontalAdapter(ArrayList<Pokemon> pokemons, Context context) {
        this.pokemons = pokemons;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtViewId;
        TextView txtViewNome;
        TextView txtViewTipo;
        int fotoPokemon = 1;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            txtViewId = (TextView) view.findViewById(R.id.id);
            txtViewNome = (TextView) view.findViewById(R.id.nome);
            txtViewTipo = (TextView) view.findViewById(R.id.tipo);

        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        int second = position * 3 - 3;
        String TAG = String.valueOf(second);
        holder.imageView.setTag(TAG);


        Bitmap bitmap = aA.getBitmapFromMemCache(TAG);
        if(bitmap == null) {
//            MySimpleAsyncTask mySimpleAsyncTask = new MySimpleAsyncTask(holder.imageView, TAG, "Pokemon" + String.valueOf(fotoPokemon), aA);
//            mySimpleAsyncTask.execute(second);
            fotoPokemon++;
        }
        else{
            holder.imageView.setImageBitmap(bitmap);
        }
        String aux = String.valueOf(position + 1);
        holder.txtViewId.setText(aux);
        holder.txtViewNome.setText(pokemons.get(position).getNome().toString());
        holder.txtViewTipo.setText(pokemons.get(position).getTipo().toString());


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String list = pokemons.get(position).toString();
                //Toast.makeText(AlbumActivity.this, list, Toast.LENGTH_SHORT).show();
            }

        });

    }


    @Override
    public int getItemCount()
    {
        return pokemons.size();
    }
}
