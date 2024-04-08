package ph25260.fpoly.client.ui.comic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.ComicService;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.adapter.ComicAdapter;
import ph25260.fpoly.client.config.ApiConfig;
import ph25260.fpoly.client.databinding.FragmentHomeBinding;
import ph25260.fpoly.client.model.Cate;
import ph25260.fpoly.client.model.Comic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ImageView imgAvatar;
    private TextView tvLevel;
    private TextView tvUsername;

    private RecyclerView rvListTruyen;
    ComicAdapter comicAdapter;
    ArrayList<Comic> listComic;
    private Spinner spCate;
    ArrayList<Cate> cateArrayList;
    String selectedCateId;
    private SwipeRefreshLayout resetData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvLevel = view.findViewById(R.id.tvLevel);
        tvUsername = view.findViewById(R.id.tvUsername);
        spCate = view.findViewById(R.id.spCate);
        rvListTruyen = view.findViewById(R.id.rvListTruyen);
        resetData = view.findViewById(R.id.resetData);

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
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        rvListTruyen.setLayoutManager(layoutManager);
        listComic = new ArrayList<>();
        cateArrayList = new ArrayList<>();
        comicAdapter = new ComicAdapter(listComic, getActivity());
        rvListTruyen.setAdapter(comicAdapter);
        loadComic();
        loadCategory();
        resetData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listComic.clear();
                loadComic();
            }
        });
        return view;
    }
    private void loadCategory() {
        ComicService usersService = ApiManager.getServer().create(ComicService.class);
        Call<List<Cate>> call = usersService.getAllCate();
        call.enqueue(new Callback<List<Cate>>() {
            @Override
            public void onResponse(Call<List<Cate>> call, Response<List<Cate>> response) {
                if (response != null && response.isSuccessful()) {
                    cateArrayList = (ArrayList<Cate>) response.body();
                    ArrayList<String> listCate = new ArrayList<>();

                   listCate.add("Tất cả");
                    for (Cate cate : cateArrayList) {
                        listCate.add(cate.getCate());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listCate);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCate.setAdapter(adapter);
                    spCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0){
                                listComic.clear();
                                loadComic();
                            }else{
                                listComic.clear();
                                selectedCateId = cateArrayList.get(position - 1).get_id();
                                loadComicByCate(selectedCateId);
                            }
                        }

                        private void loadComicByCate(String selectedCateId) {
                            ComicService usersService = ApiManager.getServer().create(ComicService.class);
                            Call<List<Comic>> call = usersService.getCategoryById(selectedCateId);
                            call.enqueue(new Callback<List<Comic>>() {
                                @Override
                                public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                                    if (response != null && response.isSuccessful()) {
                                        listComic = (ArrayList<Comic>) response.body();
                                        comicAdapter = new ComicAdapter(listComic, getActivity());
                                        rvListTruyen.setAdapter(comicAdapter);
                                        resetData.setRefreshing(false);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Comic>> call, Throwable t) {
                                    resetData.setRefreshing(false);
                                    Log.e("onFailure", "Request failed: ", t); // Always log the error
                                    // Check error type
                                    if (t instanceof IOException) {
                                        // Likely a network connectivity error
                                        Toast.makeText(getContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Other potential errors (server-side, parsing, etc.)
                                        Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Cate>> call, Throwable t) {
                Log.e("onFailure", "Request failed: ", t);
                if (t instanceof IOException) {
                    Toast.makeText(getContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
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
                    rvListTruyen.setAdapter(comicAdapter);
                    resetData.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {
                resetData.setRefreshing(false);
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