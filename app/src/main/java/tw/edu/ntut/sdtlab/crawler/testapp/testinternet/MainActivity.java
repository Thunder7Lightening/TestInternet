package tw.edu.ntut.sdtlab.crawler.testapp.testinternet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
//    private ProgressDialog progressDialog;
    private Bitmap bitmap = null;
    private Button nextPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextPageButton = (Button) findViewById(R.id.nextPageButton);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage();
            }
        });

        String urlStr = "https://yt3.ggpht.com/a/AGF-l7_oYC9CmWYaF2AX56gvmKUfjk9s_bThHHIUmA=s900-mo-c-c0xffffffff-rj-k-no";
        checkInternetConenction();
        downloadImage(urlStr);
    }

    private void goToNextPage() {
        Intent intent = new Intent(this, FailureActivity.class);
        startActivity(intent);
    }

    private void downloadImage(final String urlStr) {
//        progressDialog = ProgressDialog.show(this, "", "Downloading Image from " + urlStrs[0]);

        new Thread() {
            public void run() {
                while(true){
                    String url = urlStr;
                    InputStream in;

                    Message msg = Message.obtain();
//                    msg.what = 1;

                    try {
                        in = openHttpConnection(url);
                        bitmap = BitmapFactory.decodeStream(in);
                        in.close();
                    }catch (IOException | NullPointerException e1) {
                        e1.printStackTrace();
                    }
                    Bundle b = new Bundle();
                    b.putParcelable("bitmap", bitmap);
                    msg.setData(b);
                    messageHandler.sendMessage(msg);
                }

            }
        }.start();
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }

            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageBitmap((Bitmap) (msg.getData().getParcelable("bitmap")));
//            progressDialog.dismiss();
        }
    };

    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
