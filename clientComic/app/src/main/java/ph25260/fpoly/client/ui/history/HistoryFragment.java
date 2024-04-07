package ph25260.fpoly.client.ui.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.ComicService;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.adapter.ComicAdapter;
import ph25260.fpoly.client.config.ApiConfig;
import ph25260.fpoly.client.model.Comic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    private ImageView imgAvatar;
    private TextView tvLevel;
    private TextView tvUsername;
    private EditText edtSearch;
    private RecyclerView rvListTimKiem;
    ComicAdapter comicAdapter;
    ArrayList<Comic> listComic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvLevel = view.findViewById(R.id.tvLevel);
        tvUsername = view.findViewById(R.id.tvUsername);
        edtSearch = view.findViewById(R.id.edtSearch);
        rvListTimKiem = view.findViewById(R.id.rvListTimKiem);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", getContext().MODE_PRIVATE);
        tvUsername.setText(sharedPreferences.getString("username", ""));
        String avatar = sharedPreferences.getString("avatar", "");
        if (!avatar.isEmpty()) {
            Picasso.get()
                    .load(ApiConfig.BASE_URL + "/uploads/" + avatar)
                    .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.avatar);
        }
        rvListTimKiem.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        comicAdapter = new ComicAdapter(listComic, getActivity());
        rvListTimKiem.setAdapter(comicAdapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    // Load dữ liệu mặc định nếu trường tìm kiếm rỗng
                    loadComic();
                } else {
                    searchComics(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void searchComics(String searchTerm) {
        ComicService comicService = ApiManager.getServer().create(ComicService.class);
        Call<List<Comic>> call = comicService.searchComics(searchTerm);
        call.enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listComic = (ArrayList<Comic>) response.body();
                    listComic.addAll(response.body());
                    comicAdapter = new ComicAdapter(listComic, getActivity());
                    rvListTimKiem.setAdapter(comicAdapter);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tìm kiếm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadComic() {
        ComicService usersService = ApiManager.getServer().create(ComicService.class);
        Call<List<Comic>> call = usersService.getComic();
        call.enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                if (response != null && response.isSuccessful()) {
                    listComic = (ArrayList<Comic>) response.body();
                    listComic.addAll(response.body());
                    comicAdapter = new ComicAdapter(listComic, getActivity());
                    rvListTimKiem.setAdapter(comicAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {
                Log.e("onFailure", "Request failed: ", t); // Always log the error
                if (t instanceof IOException) {
                    Toast.makeText(getContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}