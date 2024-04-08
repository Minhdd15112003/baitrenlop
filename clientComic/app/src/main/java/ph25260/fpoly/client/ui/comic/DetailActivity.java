package ph25260.fpoly.client.ui.comic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.ComicService;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.adapter.CommentAdapter;
import ph25260.fpoly.client.config.ApiConfig;
import ph25260.fpoly.client.model.Comic;
import ph25260.fpoly.client.model.CommentObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ImageView imgDetailComic;
    private TextView tvDetailComicName;
    private TextView tvDetailDateComic;
    private TextView tvDetailAuthorComic;
    private TextView tvDetailDesComic;
    private Button btnReadComic;
    private EditText edtComment;
    private Button btnComment;

    private TextView tvDetailTheLoai;
    CommentAdapter commentAdapter;
    RecyclerView rvComment;
    private ArrayList<Comic> comicArrayList;
    private ArrayList<CommentObject> commentArrayList;
    ComicService comicService = ApiManager.getServer().create(ComicService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imgDetailComic = findViewById(R.id.imgDetailComic);
        tvDetailComicName = findViewById(R.id.tvDetailComicName);
        tvDetailDateComic = findViewById(R.id.tvDetailDateComic);
        tvDetailAuthorComic = findViewById(R.id.tvDetailAuthorComic);
        tvDetailDesComic = findViewById(R.id.tvDetailDesComic);
        btnReadComic = findViewById(R.id.btnReadComic);
        tvDetailTheLoai = findViewById(R.id.tvDetailTheLoai);
        edtComment = findViewById(R.id.edtComment);
        btnComment = findViewById(R.id.btnComment);
        rvComment = findViewById(R.id.rcvCommet);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        commentArrayList = new ArrayList<>();
        comicService.getDetailComic(id).enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvDetailComicName.setText(intent.getStringExtra("name"));
                    tvDetailAuthorComic.setText(intent.getStringExtra("author"));
                    tvDetailDesComic.setText(intent.getStringExtra("description"));
                    tvDetailDateComic.setText("[" + intent.getStringExtra("year") + "]");
                    tvDetailTheLoai.setText(intent.getStringExtra("cate"));
                    String imagePath = intent.getStringExtra("coverImage");
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Picasso.get().load(ApiConfig.BASE_URL + "/uploads/" + imagePath).into(imgDetailComic);
                    } else {
                        imgDetailComic.setImageResource(R.drawable.avatar);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Lỗi khi tải thông tin truyện", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                // Handle the failure case
                Toast.makeText(DetailActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        btnReadComic.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetailActivity.this, ReadComicActivity.class);
            intent1.putExtra("id", id);
            startActivity(intent1);
        });

        commentAdapter = new CommentAdapter(new ArrayList<CommentObject>(), this);
        rvComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvComment.setAdapter(commentAdapter);
        loadComment(id);
        btnComment.setOnClickListener(v -> {
            postComment(id);
            edtComment.setText("");
        });
    }

    private void loadComment(String id) {
        comicService.getDetailComic(id).enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentArrayList.clear();
                    commentArrayList.addAll(response.body().getCommentObjects());
                    commentAdapter.updateList(commentArrayList);
                } else {
                    Toast.makeText(DetailActivity.this, "Lỗi khi tải thông tin truyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                // Handle the failure case
                Toast.makeText(DetailActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postComment(String comicId) {
        String content = edtComment.getText().toString();
        if (content.isEmpty()) {
            edtComment.setError("Vui lòng nhập bình luận");
            return;
        }
        CommentObject commentObject = new CommentObject();
        SharedPreferences sharedPreferences = getSharedPreferences("User", getApplication().MODE_PRIVATE);
        String idUser = sharedPreferences.getString("id", "");
        String username = sharedPreferences.getString("username", "");
        String avatar = sharedPreferences.getString("avatar", "");
        comicService.commentComic(comicId, new CommentObject(content, idUser, username, commentObject.getDate(),avatar, commentObject.getComicId())).enqueue(new Callback<CommentObject>() {
            @Override
            public void onResponse(Call<CommentObject> call, Response<CommentObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DetailActivity.this, "Bình luận thành công", Toast.LENGTH_SHORT).show();
                    loadComment(comicId);
                } else {
                    Toast.makeText(DetailActivity.this, "Bình luận thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentObject> call, Throwable t) {
                Log.e("onFailure", "Request failed: ", t);
                if (t instanceof IOException) {
                    Toast.makeText(getApplicationContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
    }


