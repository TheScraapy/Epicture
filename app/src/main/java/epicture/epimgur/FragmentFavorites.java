package epicture.epimgur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentFavorites extends Fragment {

    public FragmentFavorites() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.favorites_fragment, container, false);
        final ListView list = view.findViewById(R.id.listview_favorites);
        User user = (User) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("USER");

        // Get Favorites images
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImgurAPI imgurAPI = retrofit.create(ImgurAPI.class);
        Call<FavoritesResponse> call = imgurAPI.getFavorites("Bearer " + user.access_token);

        call.enqueue(new Callback<FavoritesResponse>() {
            @Override
            public void onResponse(Call<FavoritesResponse> call, Response<FavoritesResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("Epimgur", response.message());
                    return;
                }
                list.setAdapter(new PostAdapter(getActivity(), response.body().data));
            }

            @Override
            public void onFailure(Call<FavoritesResponse> call, Throwable t) {
                Log.e("Epimgur", t.getMessage());
            }
        });

        return view;
    }

}
