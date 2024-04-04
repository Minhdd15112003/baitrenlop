package ph25260.fpoly.client.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ph25260.fpoly.client.R;
import ph25260.fpoly.client.model.Comic;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {
    ArrayList<Comic> listComic;
    Context context;
    public ComicAdapter(ArrayList<Comic> listComic, Context context) {
        this.listComic = listComic;
        this.context = context;
    }

    @NonNull
    @Override
    public ComicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_list_truyen, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicAdapter.ViewHolder holder, int position) {
        holder.tvTenTruyen.setText(listComic.get(position).getName());
        holder.tvTacGia.setText(listComic.get(position).getAuthor());
        Picasso.get().load(listComic.get(position).getCoverImage()).into(holder.imgAnhTruyen);
        holder.tvTheLoai.setText(listComic.get(position).getCateID());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyen);
            tvTacGia = itemView.findViewById(R.id.tvTacGia);
            imgAnhTruyen = itemView.findViewById(R.id.imgAnhTruyen);
            tvTheLoai = itemView.findViewById(R.id.tvTheLoai);
        }
    }
}
