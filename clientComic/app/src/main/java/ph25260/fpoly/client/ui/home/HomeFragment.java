package ph25260.fpoly.client.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.UsersService;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.adapter.ComicAdapter;
import ph25260.fpoly.client.databinding.FragmentHomeBinding;
import ph25260.fpoly.client.model.Comic;
import retrofit2.Call;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ImageView imgAvatar;
    private TextView tvLevel;
    private TextView tvUsername;
    private Spinner spCate;
    private RecyclerView rvListTruyen;
    ComicAdapter comicAdapter;
    ArrayList<Comic> listComic;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvLevel = view.findViewById(R.id.tvLevel);
        tvUsername = view.findViewById(R.id.tvUsername);
        spCate = view.findViewById(R.id.spCate);
        rvListTruyen = view.findViewById(R.id.rvListTruyen);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rvListTruyen.setLayoutManager(layoutManager);
        listComic = new ArrayList<>();
        comicAdapter = new ComicAdapter(listComic, getContext());
        rvListTruyen.setAdapter(comicAdapter);
        loadComic();
        return view;
    }

    private void loadComic() {
        UsersService usersService = ApiManager.getServer().create(UsersService.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}