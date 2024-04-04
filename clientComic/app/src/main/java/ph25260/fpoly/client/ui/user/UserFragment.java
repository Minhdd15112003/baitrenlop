package ph25260.fpoly.client.ui.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.ResponseBody;
import ph25260.fpoly.client.API.ApiManager;
import ph25260.fpoly.client.API.UsersService;
import ph25260.fpoly.client.R;
import ph25260.fpoly.client.Until.CustomDialog;
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
    private Button btnDangXuat;
    private TextView txtFullnameHeader;
    private static final int REQUEST_CODE_CHOOSE_FROM_GALLERY = 101;
    private static final int REQUEST_CODE_TAKE_PHOTO = 102;

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
        UsersService usersService = ApiManager.getServer().create(UsersService.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        usersService.getUserByID(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    txtEmail.setText(user.getEmail());
                    txtFullnameHeader.setText(user.getFullname());
                    txtUsername.setText(user.getUsername());
                    txtFullname.setText(user.getFullname());
                    Picasso.get().load(user.getAvatar()).into(imgAvatar);
                    Log.d("acccccc", "onResponse: "+ user.getAvatar());
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
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.item_update_user);
                ImageView avatarImageUpdate = dialog.findViewById(R.id.avatar_image_update);
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
                Picasso.get().load(imagePath).into(imgAvatar);
                avatarImageUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialogChoseAvatar = new Dialog(getActivity());
                        dialogChoseAvatar.setContentView(R.layout.item_chose_avatar);
                        ImageView imgPhoto;
                        ImageView imgGallery;
                        imgPhoto = dialogChoseAvatar.findViewById(R.id.imgPhoto);
                        imgGallery = dialogChoseAvatar.findViewById(R.id.imgGallery);
                        imgPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
                                } else {
                                    Toast.makeText(getActivity(), "Không tìm thấy ứng dụng camera", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        imgGallery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                startActivityForResult(intent, REQUEST_CODE_CHOOSE_FROM_GALLERY);
                            }
                        });

                        new CustomDialog().decorBackground(dialogChoseAvatar);
                        dialogChoseAvatar.show();
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
                        usersService.updateUser(id, new User(email, username, fullname, password)).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.body() != null && response.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", email);
                                    editor.putString("username", username);
                                    editor.putString("fullname", fullname);
                                    editor.putString("password", password);
                                    editor.apply();
                                    editor.commit();
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
    private void reloadData() {
        UsersService usersService = ApiManager.getServer().create(UsersService.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        Call<User> call = usersService.getUserByID(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    txtEmail.setText(user.getEmail());
                    txtFullnameHeader.setText(user.getFullname());
                    txtUsername.setText(user.getUsername());
                    txtFullname.setText(user.getFullname());
                    String imagePath = user.getAvatar();
                    Picasso.get().load(imagePath).into(imgAvatar);
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