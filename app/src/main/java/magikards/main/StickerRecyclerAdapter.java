package magikards.main;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StickerRecyclerAdapter extends RecyclerView.Adapter<StickerRecyclerAdapter.ViewHolder>{


    ArrayList<Bitmap> newStickers;

    public StickerRecyclerAdapter(ArrayList<Bitmap> newStickers) {
        this.newStickers = newStickers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap sticker = this.newStickers.get(position);
        holder.newSticker.setImageBitmap(sticker);
    }

    @Override
    public int getItemCount() {
        return this.newStickers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView newSticker;

        public ViewHolder(View itemView) {
            super(itemView);
            newSticker = (ImageView) itemView.findViewById(R.id.newStickerImage);
        }
    }

}
