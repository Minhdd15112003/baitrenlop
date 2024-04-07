package ph25260.fpoly.client.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ph25260.fpoly.client.R;
import ph25260.fpoly.client.config.ApiConfig;
import ph25260.fpoly.client.model.Comic;

public class ReadComicAdapter extends RecyclerView.Adapter<ReadComicAdapter.ViewHolder>{
    // Thay đổi kiểu dữ liệu
    ArrayList<String> listReadComic;
    Context context;

    public ReadComicAdapter(ArrayList<String> listReadComic, Context context) {
        this.listReadComic = listReadComic;
        this.context = context;
    }

    public void updateList(ArrayList<String> newList) {
        listReadComic = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReadComicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_read_comic, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadComicAdapter.ViewHolder holder, int position) {
        String url = listReadComic.get(position);

        Log.e(TAG, "onBindViewHolder: " + url );

        // Sử dụng Picasso để tải ảnh
        Picasso.get().load(ApiConfig.BASE_URL + "/uploads/" + url).into(holder.imgReadComic);

    }

    @Override
    public int getItemCount() {
        if (listReadComic != null) {
            return listReadComic.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgReadComic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgReadComic = itemView.findViewById(R.id.imgReadComic);
        }
    }
}
