package epicture.epimgur;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentTrending extends Fragment {

    private String sort = "top";
    private String query = "";
    private View view;
    private ListView list;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ImgurAPI imgurAPI = retrofit.create(ImgurAPI.class);

    public FragmentTrending() { }

    public void getTrending() {
        Call<TrendingResponse> call;
        if (!query.isEmpty()) {
            call = imgurAPI.searchGallery("Client-ID 762fa603b3aba63", sort, query);
        } else {
            call = imgurAPI.getTrending("Client-ID 762fa603b3aba63", sort);
        }

        call.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("Epimgur", response.message());
                    return;
                }
                list.setAdapter(new PostAdapter(getActivity(), response.body().data));
            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                Log.e("Epimgur", t.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.trending_fragment, container, false);
        list = view.findViewById(R.id.listview_trending);

        // Callback when clicking search icon
        ImageButton searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchField = view.findViewById(R.id.search_text);
                query = searchField.getText().toString().toLowerCase();
                getTrending();
            }
        });

        // Callback when clicking ok on the keyboard
        EditText searchtext = view.findViewById(R.id.search_text);
        searchtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    EditText searchField = view.findViewById(R.id.search_text);
                    query = searchField.getText().toString().toLowerCase();
                    getTrending();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = getActivity().getCurrentFocus();
                    if (view == null) {
                        view = new View(getActivity());
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        });

        // Callback when selecting sort filter
        final Spinner dropDownmenu = view.findViewById(R.id.dropdown_menu);
        dropDownmenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sort = dropDownmenu.getSelectedItem().toString().toLowerCase();
                getTrending();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        return view;
    }

}
