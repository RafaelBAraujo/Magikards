package magikards.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;

public class TradeRecyclerViewAdapter  extends RecyclerView.Adapter<TradeRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "TradeRecyclerViewAdapt";

    ArrayList<Integer> stickersID;
    int stickersSize;
    Context context;
    ItemClickListener clickListener;

    public TradeRecyclerViewAdapter(Context context, int stickersSize, ArrayList<Integer> stickersID) {
        this.context = context;
        this.stickersSize = stickersSize;
        this.stickersID = stickersID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.traderecycler_item, parent, false);
        TradeRecyclerViewAdapter.ViewHolder holder = new TradeRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.sticker.getLayoutParams().width = this.stickersSize;
        holder.sticker.getLayoutParams().height = this.stickersSize;
        holder.sticker.requestLayout();

        File pathToSticker = new File(this.context.getFileStreamPath(String.valueOf(this.stickersID.get(position)) + ".png").getPath());
        if(pathToSticker.exists()){
            try {
                Bitmap sticker = BitmapFactory.decodeStream(new FileInputStream(pathToSticker));
                holder.sticker.setImageBitmap(sticker);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.stickersID.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ImageView sticker;
        ImageView selectedBorders;

        public ViewHolder(View itemView) {
            super(itemView);
            this.sticker = (ImageView) itemView.findViewById(R.id.tradesticker);
            this.selectedBorders = (ImageView) itemView.findViewById(R.id.selectBorders);
            this.sticker.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if(clickListener != null) clickListener.onItemClick(v, getAdapterPosition());

            if(selectedBorders.getVisibility() == View.GONE)
                selectedBorders.setVisibility(View.VISIBLE);
            else
                selectedBorders.setVisibility(View.GONE);

            return true;
        }
    }

    String getItem(int id) {
        return String.valueOf(this.stickersID.get(id));
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
