package epicture.epimgur;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        view = this.findViewById(R.id.webview);
        view.setWebViewClient(new Callback());
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=762fa603b3aba63&response_type=token");
    }

    private class Callback extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("Epimgur", url);
            Uri uri = Uri.parse(url);
            switch (uri.getHost()) {
                case "www.getpostman.com":
                    Map<String, String> params = splitQuery(uri.getEncodedFragment());
                    User user = new User(params);
                    Intent homepage = new Intent(LoginActivity.this, MainActivity.class);
                    homepage.putExtra("USER", user);
                    startActivity(homepage);
                    finish();
                    return true;
                default:
                    view.loadUrl(url);
            }
            return true;
        }
    }

    public static Map<String, String> splitQuery(String url) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String[] pairs = url.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }
        return query_pairs;
    }
}
