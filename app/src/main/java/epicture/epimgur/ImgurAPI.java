package epicture.epimgur;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ImgurAPI {

    @GET("3/account/me/images")
    Call<UploadsResponse> getUploads(
            @Header("Authorization") String accessToken
    );

    @GET("3/account/me/favorites")
    Call<FavoritesResponse> getFavorites(
            @Header("Authorization") String accessToken
    );

    @GET("3/gallery/hot/{sort}")
    Call<TrendingResponse> getTrending(
            @Header("Authorization") String clientId,
            @Path("sort") String sort
    );

    @GET("3/gallery/search/{sort}")
    Call<TrendingResponse> searchGallery(
            @Header("Authorization") String clientId,
            @Path("sort") String sort,
            @Query("q") String query
    );

    @Multipart
    @POST("3/image")
    Call<UploadsResponse> uploadImage(
            @Header("Authorization") String clientId,
            @Header("Authorization") String accessToken,
            @Part("image") String base64img
    );
}
