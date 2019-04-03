package epicture.epimgur;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private User user;
    private TabLayout tablayout;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getIntent().getSerializableExtra("USER");
        Log.d("Epimgur", user.access_token);
        setContentView(R.layout.activity_main);

        // Init fragments resources
        tablayout = findViewById(R.id.tablayout);
        viewpager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Adding Fragments
        adapter.AddFragment(new FragmentUploads(), "Uploads");
        adapter.AddFragment(new FragmentFavorites(), "Favorites");
        adapter.AddFragment(new FragmentTrending(), "Trending");

        // Adapter Setup
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);

    }

}
