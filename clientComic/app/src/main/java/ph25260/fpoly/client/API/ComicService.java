package ph25260.fpoly.client.API;

import java.util.List;

import okhttp3.MultipartBody;
import ph25260.fpoly.client.model.Cate;
import ph25260.fpoly.client.model.Comic;
import ph25260.fpoly.client.model.CommentObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ComicService {

    @GET("/getComic")
    Call<List<Comic>> getComic();

    @GET("/getDetailComic/{id}")
    Call<Comic> getDetailComic(@Path("id") String id);

    @GET("/getDetailComic/{id}/readComic")
    Call<Comic> readComic(@Path("id") String id);

    @GET("/getCategoryById/{id}")
    Call<List<Comic>> getCategoryById(@Path("id") String id);

    @GET("/getAllCate")
    Call<List<Cate>> getAllCate();

    @Multipart
    @POST("/getDetailComic/{id}/commentComic")
    Call<CommentObject> commentAvatarComic(@Part MultipartBody.Part avatar,@Path("id") String id);

    @POST("/getDetailComic/{id}/commentComic")
    Call<CommentObject> commentComic(@Path("id") String id, @Body CommentObject commentObject);

    @GET("/deleteComment/{comicId}/{commentId}")
    Call<CommentObject> deleteComment(@Path("comicId") String comicId, @Path("commentId") String commentId);

    @GET("/searchComic")
    Call<List<Comic>> searchComics(@Query("q") String keyword);

}
