package ph25260.fpoly.client.sigin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.UsersService;
import ph25260.fpoly.client.MainActivity;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtUsername;
    private EditText edtFullname;
    private EditText edtMatKhau;
    private EditText edtMatKhauCf;
    private Button btnDangKi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtFullname = findViewById(R.id.edtFullname);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtMatKhauCf = findViewById(R.id.edtMatKhauCf);
        btnDangKi = findViewById(R.id.btnDangNhap);

        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String username = edtUsername.getText().toString();
                String fullname = edtFullname.getText().toString();
                String password = edtMatKhau.getText().toString();
                String passwordCf = edtMatKhauCf.getText().toString();

                if (email.isEmpty() || username.isEmpty() || fullname.isEmpty() || password.isEmpty() || passwordCf.isEmpty()) {
                    edtEmail.setError("Vui lòng nhập email");
                    edtUsername.setError("Vui lòng nhập username");
                    edtFullname.setError("Vui lòng nhập fullname");
                    edtMatKhau.setError("Vui lòng nhập mật khẩu");
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(passwordCf)) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập đúng password", Toast.LENGTH_SHORT).show();
                    edtMatKhauCf.setError("Vui lòng nhập đúng mật khẩu");
                    return;
                }

                UsersService usersService = ApiManager.getServer().create(UsersService.class);
                User user = new User(email, username, fullname, password);
                Call<User> call = usersService.insertUser(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null) {
                            Toast.makeText(SignUpActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
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
        });
    }
}