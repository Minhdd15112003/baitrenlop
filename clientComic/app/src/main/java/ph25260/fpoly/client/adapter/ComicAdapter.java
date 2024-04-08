package ph25260.fpoly.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ph25260.fpoly.client.R;
import ph25260.fpoly.client.config.ApiConfig;
import ph25260.fpoly.client.model.Comic;
import ph25260.fpoly.client.ui.comic.DetailActivity;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {
    ArrayList<Comic> listComic;
    Context context;

    public ComicAdapter(ArrayList<Comic> listComic, Context context) {
        this.listComic = listComic;
        this.context = context;
    }

    public void updateData(ArrayList<Comic> listComic) {
        listComic = new ArrayList<>(listComic);
        notifyDataSetChanged(); // Cập nhật lại toàn bộ dữ liệu hiển thị
    }


    @NonNull
    @Override
    public ComicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.itemlistruyen, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicAdapter.ViewHolder holder, int position) {
        holder.tvTenTruyen.setText(listComic.get(position).getName());
//        holder.tvTacGia.setText(listComic.get(position).getAuthor());
        Picasso.get().load(ApiConfig.BASE_URL + "/uploads/" + listComic.get(position).getCoverImage()).into(holder.imgAnhTruyen);
//        holder.tvTheLoai.setText(listComic.get(position).getCateID().getCate());
        holder.detailComicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DetailActivity.class);
                intent.putExtra("id", listComic.get(position).get_id());
                intent.putExtra("name", listComic.get(position).getName());
                intent.putExtra("author", listComic.get(position).getAuthor());
                intent.putExtra("description", listComic.get(position).getDescription());
                intent.putExtra("year", listComic.get(position).getYear());
                intent.putExtra("cate", listComic.get(position).getCateID().getCate());
                intent.putExtra("coverImage", listComic.get(position).getCoverImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listComic != null)
            return listComic.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenTruyen;
        TextView tvTacGia;
        TextView tvTheLoai;
        ImageView imgAnhTruyen;
        CardView detailComicView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyen);
            tvTacGia = itemView.findViewById(R.id.tvTacGia);
            imgAnhTruyen = itemView.findViewById(R.id.imgAnhTruyen);
            tvTheLoai = itemView.findViewById(R.id.tvTheLoai);
            detailComicView = itemView.findViewById(R.id.detailComicView);
        }
    }
}
