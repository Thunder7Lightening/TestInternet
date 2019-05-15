package tw.edu.ntut.sdtlab.crawler.testapp.testinternet;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    WebView t_webView1;
    Button requestYahooPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t_webView1 = (WebView) findViewById(R.id.webView1);
        requestYahooPageButton = (Button) findViewById(R.id.requestYahooPageButton);

        WebSettings webSettings = t_webView1.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(true);
        t_webView1.setWebViewClient(new WebViewClient());

        requestYahooPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t_webView1.loadUrl("https://tw.yahoo.com/");
            }
        });
    }

}
