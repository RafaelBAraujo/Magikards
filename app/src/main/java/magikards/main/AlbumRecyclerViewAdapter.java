package magikards.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import magikards.main.data.Pokemon;
import magikards.main.data.TipoPokemon;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    private ArrayList<Bitmap> images;
    private ArrayList<Pokemon> pokemons;

    public AlbumRecyclerViewAdapter(Context context, ArrayList<Bitmap> images, ArrayList<Pokemon> pokemons) {
        this.context = context;
        this.images = images;
        this.pokemons = pokemons;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_menu, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        if(images.get(position) == null) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown));
            holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.white));
            holder.id.setText(String.valueOf(pokemons.get(position).getPokedexNumber()));
            holder.nome.setText("?");
            holder.tipo.setText("");
        }
        else {
            switch (pokemons.get(position).getTipo().get(0).toString()) {
                case "Bug": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bug));
                    break;
                case "Dark": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.dark));
                    break;
                case "Dragon": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.dragon));
                    break;
                case "Electric": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.electric));
                    break;
                case "Fairy": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fairy));
                    break;
                case "Fighting": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fighting));
                    break;
                case "Fire": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fire));
                    break;
                case "Flying": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.flying));
                    break;
                case "Ghost": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost));
                    break;
                case "Grass": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.grass));
                    break;
                case "Ground": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ground));
                    break;
                case "Ice": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ice));
                    break;
                case "Normal": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.normal));
                    break;
                case "Poison": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.poison));
                    break;
                case "Psychic": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.psychic));
                    break;
                case "Rock": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.rock));
                    break;
                case "Steel": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.steel));
                    break;
                case "Water": holder.background.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.water));
                    break;
                default:
                    break;
            }

            System.out.println(pokemons.get(position).getTipo().toString());
            holder.image.setImageBitmap(images.get(position));
            holder.id.setText(String.valueOf(pokemons.get(position).getPokedexNumber()));
            String capitalName = pokemons.get(position).getNome().substring(0, 1).toUpperCase() + pokemons.get(position).getNome().substring(1);
            holder.nome.setText(capitalName);
            holder.tipo.setText("");

            for(int i = 0; i < pokemons.get(position).getTipo().size(); i++) {
                holder.tipo.setText(holder.tipo.getText().toString() + pokemons.get(position).getTipo().get(i).toString());

                if(i != pokemons.get(position).getTipo().size() - 1)
                    holder.tipo.setText(holder.tipo.getText().toString() + ", ");
            }

        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView id;
        TextView nome;
        TextView tipo;
        ImageView background;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            id = itemView.findViewById(R.id.id);
            nome = itemView.findViewById(R.id.nome);
            tipo = itemView.findViewById(R.id.tipo);
            background = itemView.findViewById(R.id.background);
        }

    }

}
