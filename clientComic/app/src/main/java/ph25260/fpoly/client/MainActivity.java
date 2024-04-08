package ph25260.fpoly.client;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;

import io.socket.emitter.Emitter;
import ph25260.fpoly.client.config.SocketConfig;
import ph25260.fpoly.client.databinding.ActivityMainBinding;
import ph25260.fpoly.client.ui.comic.HomeFragment;
import ph25260.fpoly.client.ui.history.HistoryFragment;
import ph25260.fpoly.client.ui.user.UserFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Toolbar toolbar;
    FrameLayout frameLayout;
    public static final String CHANEL_ID = "ABC1234"; //tự đặt thành tên mới
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Tên chanel
                CharSequence name = "ten_chanel"; // tên hiển thị trong cài đặt notify của điện thoại
                // mo ta:
                String mota = "Mo ta";
                int do_uu_tien = NotificationManager.IMPORTANCE_DEFAULT;

                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .build();

                // đăng ký notify
                NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, do_uu_tien);

                channel.setDescription(mota);

                channel.setSound(uri, audioAttributes);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.mSocket.connect();
        socketConfig.mSocket.on("newComic", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postNotify("ComicSaga", args[0].toString());
                        Toast.makeText(MainActivity.this, "NOi dung: " + args[0]
                                , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


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
                    toolbar.setTitle("tài khoản");
                } else if (id == R.id.navigation_history) {
                    selectedFragment = new HistoryFragment();
                    toolbar.setTitle("Tìm kiếm");
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left);
                transaction.replace(R.id.nav_host_fragment_activity_main, selectedFragment);
                transaction.commit();
                return true;
            }
        });
    }


    void postNotify(String title, String content){
        // Khởi tạo layout cho Notify
        Object NotifyConfig = null;
        Notification customNotification = new NotificationCompat.Builder(MainActivity.this, CHANEL_ID)
                .setSmallIcon(android.R.drawable.ic_delete)
                .setContentTitle( title )
                .setContentText(content)
                .setAutoCancel(true)

                .build();
        // Khởi tạo Manager để quản lý notify
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);

        // Cần kiểm tra quyền trước khi hiển thị notify
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // Gọi hộp thoại hiển thị xin quyền người dùng
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(MainActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        // nếu đã cấp quyền rồi thì sẽ vượt qua lệnh if trên và đến đây thì hiển thị notify
        // mỗi khi hiển thị thông báo cần tạo 1 cái ID cho thông báo riêng
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        //lệnh hiển thị notify
        notificationManagerCompat.notify(id_notiy , customNotification);

    }

}