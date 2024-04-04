package ph25260.fpoly.client.API;

import ph25260.fpoly.client.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersService {

    @POST("/loginUser")
    Call<User> login(@Body User user);

    @POST("/insertUser")
    Call<User> insertUser(@Body User user);

    @POST("/updateUser/{id}")
    Call<User> updateUser(@Path("id") String id, @Body User user);

     @GET("/deleteUser/{id}")
    Call<User> deleteUser(@Path("id") String id);

    @GET("/logOut")
    Call<User> logOut();

    @GET("/getUserByID/{id}")
    Call<User> getUserByID(@Path("id") String id);
}
