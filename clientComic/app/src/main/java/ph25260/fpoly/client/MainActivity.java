package ph25260.fpoly.client;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ph25260.fpoly.client.databinding.ActivityMainBinding;
import ph25260.fpoly.client.ui.history.HistoryFragment;
import ph25260.fpoly.client.ui.home.HomeFragment;
import ph25260.fpoly.client.ui.user.UserFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Toolbar toolbar;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        toolbar = binding.toolbar;
        frameLayout = binding.navHostFragmentActivityMain;
        navView.setItemIconTintList(null);
        setActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, new HomeFragment()).commit();
            toolbar.setTitle("Home");
            navView.setSelectedItemId(R.id.navigation_home);
        }
        binding.navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                int id = menuItem.getItemId();
                if (id == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                    toolbar.setTitle("Home");
                } else if (id == R.id.navigation_user) {
                    selectedFragment = new UserFragment();
                    toolbar.setTitle("User");
                } else if (id == R.id.navigation_history) {
                    selectedFragment = new HistoryFragment();
                    toolbar.setTitle("History");
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left);
                transaction.replace(R.id.nav_host_fragment_activity_main, selectedFragment);
                transaction.commit();
                return true;
            }
        });
    }
}