package ph25260.fpoly.client.API;

import java.util.List;

import ph25260.fpoly.client.model.Comic;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ComicService {

    @GET("/getComic")
    Call<List<Comic>> getComic();
}
