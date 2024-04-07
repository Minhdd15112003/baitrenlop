package ph25260.fpoly.client.ui.comic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.ComicService;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.adapter.ReadComicAdapter;
import ph25260.fpoly.client.model.Comic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadComicActivity extends AppCompatActivity {
    private RecyclerView rvListReadComic;
    ReadComicAdapter readComicAdapter;
    ArrayList<String> listReadComic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comic);
        rvListReadComic = findViewById(R.id.rvListReadComic);
        Intent intent = getIntent();
        String comicId = intent.getStringExtra("id");

        listReadComic = new ArrayList<>();
        readComicAdapter = new ReadComicAdapter(listReadComic, getApplicationContext()); // Khởi tạo adapter ở đây
        rvListReadComic.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvListReadComic.setAdapter(readComicAdapter);

        loadComic(comicId);
    }

    private void loadComic(String comicId) {
        ComicService comicService = ApiManager.getServer().create(ComicService.class);
        comicService.readComic(comicId).enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Comic comic = response.body();
                    listReadComic.addAll(comic.getContentImages());
                    readComicAdapter.updateList(listReadComic);
                } else {
                    Toast.makeText(ReadComicActivity.this, "Error loading comic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                Log.e("onFailure", "Request failed: ", t); // Always log the error
                // Check error type
                if (t instanceof IOException) {
                    // Likely a network connectivity error
                    Toast.makeText(getApplicationContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                } else {
                    // Other potential errors (server-side, parsing, etc.)
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}