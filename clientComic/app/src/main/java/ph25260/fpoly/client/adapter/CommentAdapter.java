package ph25260.fpoly.client.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.ComicService;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.config.ApiConfig;
import ph25260.fpoly.client.model.Comic;
import ph25260.fpoly.client.model.CommentObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comic> comictArrayList; // Truy cập tới CommentObject
    private ArrayList<CommentObject> commentArrayList; // Truy cập tới CommentObject
    private Context context;
    ComicService comicService = ApiManager.getServer().create(ComicService.class);

    public CommentAdapter(ArrayList<CommentObject> commentArrayList, Context context) {
        this.commentArrayList = commentArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_comment, null);
        return new ViewHolder(view);
    }

    public void updateList(ArrayList<CommentObject> neLIst) {
        commentArrayList = neLIst;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        CommentObject comment = commentArrayList.get(position);
        holder.tvCommentAuthor.setText(comment.getUsername());
        holder.tvCommentText.setText(comment.getContent());
        holder.tvCommentDate.setText(comment.getDate());
        String avatar = comment.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            Picasso.get().load(ApiConfig.BASE_URL + "/uploads/" + avatar).into(holder.imgCommentAuthor);
        } else {
            holder.imgCommentAuthor.setImageResource(R.drawable.avatar);
        }

        if (comment.getUserId().equals(id)) {
            holder.imgDeleteComment.setVisibility(View.VISIBLE);
        } else {
            holder.imgDeleteComment.setVisibility(View.GONE);
        }

        holder.imgDeleteComment.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc chắn muốn xóa bình luận này không?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                String comicId = comment.getComicId();
                String commentId = comment.get_id();
                    deleteComment(comicId, commentId);
            });
            builder.setNegativeButton("Không", (dialog1, which1) -> {
                dialog1.dismiss();
            });
            builder.show();
        });
    }

    private void deleteComment(String comicId, String commentId) {
        comicService.deleteComment(comicId, commentId).enqueue(new Callback<CommentObject>() {
            @Override
            public void onResponse(Call<CommentObject> call, Response<CommentObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "xóa bình luận thành công", Toast.LENGTH_SHORT).show();
                    loadComment(comicId);
                } else {
                    Toast.makeText(context, "xóa bình luận thất bại!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentObject> call, Throwable t) {
                Log.e("onFailure", "Request failed: ", t); // Always log the error
                // Handle the failure case
                Toast.makeText(context, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComment(String id) {
        comicService.getDetailComic(id).enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentArrayList.clear();
                    commentArrayList.addAll(response.body().getCommentObjects());
                    updateList(commentArrayList);

                } else {
                    Toast.makeText(context, "Lỗi khi tải thông tin truyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                // Handle the failure case
                Toast.makeText(context, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // Trả về kích thước của commentObjects
        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCommentAuthor;
        TextView tvCommentAuthor;
        TextView tvCommentText;
        TextView tvCommentDate;
        ImageView imgDeleteComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCommentAuthor = itemView.findViewById(R.id.imgCommentAuthor);
            tvCommentAuthor = itemView.findViewById(R.id.tvCommentAuthor);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
            imgDeleteComment = itemView.findViewById(R.id.imgDeleteComment);

        }
    }

}
