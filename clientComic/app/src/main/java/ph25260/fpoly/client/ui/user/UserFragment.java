package ph25260.fpoly.client.ui.user;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.UsersService;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.Until.CustomDialog;
import ph25260.fpoly.client.config.ApiConfig;
import ph25260.fpoly.client.model.User;
import ph25260.fpoly.client.sigin.SignInActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    private ImageView imgAvatar;
    private LinearLayout btnNotification;
    private LinearLayout btnUpdateUser;
    private LinearLayout btnDeleteUser;
    private TextView txtEmail;
    private TextView txtUsername;
    private TextView txtFullname;
    private ImageView previewimg;
    private Button btnDangXuat;
    private TextView txtFullnameHeader;
    private ActivityResultLauncher<Intent> launcher;
    private String pathImgWaitToUpload;
    UsersService usersService = ApiManager.getServer().create(UsersService.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        txtFullnameHeader = view.findViewById(R.id.txtFullnameHeader);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        btnNotification = view.findViewById(R.id.btnNotification);
        btnUpdateUser = view.findViewById(R.id.btnUpdateUser);
        btnDeleteUser = view.findViewById(R.id.btnDeleteUser);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtFullname = view.findViewById(R.id.txtFullname);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);

        // Đăng ký ActivityResultLauncher ngay ở đây.
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri selectedImageUri = data.getData();
                pathImgWaitToUpload = selectedImageUri.getPath();
                Log.d(TAG, "onCreateView: " + selectedImageUri.getPath());
                Picasso.get().load(selectedImageUri).into(previewimg);
            }
        });


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        usersService.getUserByID(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    txtEmail.setText(user.getEmail());
                    txtFullnameHeader.setText(user.getFullname());
                    txtUsername.setText(user.getUsername());
                    txtFullname.setText(user.getFullname());
                    String imagePath = user.getAvatar();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Picasso.get().load(new ApiConfig().BASE_URL + "/uploads/" + imagePath).into(imgAvatar);
                    } else {
                        imgAvatar.setImageResource(R.drawable.avatar);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("onFailure", "Request failed: ", t); // Always log the error

                // Check error type
                if (t instanceof IOException) {
                    // Likely a network connectivity error
                    Toast.makeText(getActivity(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                } else {
                    // Other potential errors (server-side, parsing, etc.)
                    Toast.makeText(getActivity(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }

            }
        });
        updateUser(id);
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xác nhận xóa tài khoản");
                builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    String id = sharedPreferences.getString("id", "");
                    Call<User> call = usersService.deleteUser(id);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), SignInActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "Xóa tài khoản thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                            Log.e("onFailure", "Request failed: ", t); // Always log the error

                            // Check error type
                            if (t instanceof IOException) {
                                // Likely a network connectivity error
                                Toast.makeText(getActivity(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other potential errors (server-side, parsing, etc.)
                                Toast.makeText(getActivity(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                });
                builder.setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<User> call = usersService.logOut();
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), SignInActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Đăng xuất thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("onFailure", "Request failed: ", t); // Always log the error

                        // Check error type
                        if (t instanceof IOException) {
                            // Likely a network connectivity error
                            Toast.makeText(getActivity(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                        } else {
                            // Other potential errors (server-side, parsing, etc.)
                            Toast.makeText(getActivity(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }


    public void updateUser(String id) {
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.item_update_user);
                ImageView imgUpdateAvatar = dialog.findViewById(R.id.avatar_image_update);
                previewimg = imgUpdateAvatar;
                EditText txtEmailUpdate = dialog.findViewById(R.id.txtEmailUpdate);
                EditText txtUsernameUpdate = dialog.findViewById(R.id.txtUsernameUpdate);
                EditText txtFullnameUpdate = dialog.findViewById(R.id.txtFullnameUpdate);
                EditText txtPasswordUpdate = dialog.findViewById(R.id.txtPasswordUpdate);
                Button btnUpdateUser = dialog.findViewById(R.id.btnUpdateUser);
                txtEmailUpdate.setText(sharedPreferences.getString("email", ""));
                txtUsernameUpdate.setText(sharedPreferences.getString("username", ""));
                txtFullnameUpdate.setText(sharedPreferences.getString("fullname", ""));
                txtPasswordUpdate.setText(sharedPreferences.getString("password", ""));
                String imagePath = sharedPreferences.getString("avatar", "");
                if (imagePath != null && !imagePath.isEmpty()) {
                    Picasso.get().load(new ApiConfig().BASE_URL + "/uploads/" + imagePath).into(imgUpdateAvatar);
                    Picasso.get().load(new ApiConfig().BASE_URL + "/uploads/" + imagePath).into(previewimg);
                } else {
                    imgAvatar.setImageResource(R.drawable.avatar);
                }
                imgUpdateAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImagePicker.with(getActivity())
                                .crop()                //Crop image(Optional), Check Customization for more option
                                .compress(1024)        //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .createIntent(new Function1<Intent, Unit>() {
                                    @Override
                                    public Unit invoke(Intent intent) {
                                        launcher.launch(intent);
                                        return null;
                                    }
                                });
                    }
                });
                btnUpdateUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = sharedPreferences.getString("id", "");
                        String email = txtEmailUpdate.getText().toString();
                        String username = txtUsernameUpdate.getText().toString();
                        String fullname = txtFullnameUpdate.getText().toString();
                        String password = txtPasswordUpdate.getText().toString();

                        if (pathImgWaitToUpload != null) {

                            File file = new File(pathImgWaitToUpload);
                            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("Avatar", file.getName(), requestBody);

                            Call<User> call = usersService.updateUserAvatar(body, id);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {

                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });

                            pathImgWaitToUpload = null;
                        }


                        usersService.updateUser(id, new User(email, username, fullname, password)).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.body() != null && response.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                    reloadData();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.e("onFailure", "Request failed: ", t); // Always log the error

                                // Check error type
                                if (t instanceof IOException) {
                                    // Likely a network connectivity error
                                    Toast.makeText(getActivity(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Other potential errors (server-side, parsing, etc.)
                                    Toast.makeText(getActivity(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                new CustomDialog().decorBackground(dialog);
                dialog.show();
            }
        });
    }

    private void reloadData() {
        UsersService usersService = ApiManager.getServer().create(UsersService.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        Call<User> call = usersService.getUserByID(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("email", user.getEmail());
                    edit.putString("username", user.getUsername());
                    edit.putString("fullname", user.getFullname());
                    edit.putString("password", user.getPassword());
                    edit.putString("avatar", user.getAvatar());
                    edit.commit();
                    edit.apply();
                    txtEmail.setText(user.getEmail());
                    txtFullnameHeader.setText(user.getFullname());
                    txtUsername.setText(user.getUsername());
                    txtFullname.setText(user.getFullname());
                    String imagePath = user.getAvatar();
                    if (imagePath != null && !imagePath.isEmpty()) {
                        Picasso.get().load(new ApiConfig().BASE_URL + "/uploads/" + imagePath).into(imgAvatar);
                        Picasso.get().load(new ApiConfig().BASE_URL + "/uploads/" + imagePath).into(previewimg);
                    } else {
                        imgAvatar.setImageResource(R.drawable.avatar);
                    }

                } else {
                    Toast.makeText(getActivity(), "Không thể tải dữ liệu mới", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("onFailure", "Request failed: ", t); // Always log the error

                // Check error type
                if (t instanceof IOException) {
                    // Likely a network connectivity error
                    Toast.makeText(getActivity(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                } else {
                    // Other potential errors (server-side, parsing, etc.)
                    Toast.makeText(getActivity(), "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}