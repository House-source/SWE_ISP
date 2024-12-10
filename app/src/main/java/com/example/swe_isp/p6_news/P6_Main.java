package com.example.swe_isp.p6_news;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import com.example.swe_isp.R;

public class P6_Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p6_main);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isConnected() == false && mMobile.isConnected() == false) {
            showErrorView();
        }
        else {
            System.out.println("Connected");
            setContentView(R.layout.p6_main);

            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            FileDownloader news = new FileDownloader("http://branko-cirovic.appspot.com/etcapp/news/news.xml", P6_Main.this);
            news.setOnResultsListener(new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Intent newsScreen = new Intent(getApplicationContext(), NewsActivity.class);
                    newsScreen.putExtra("xmlData", output);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    startActivity(newsScreen);
                }
            });
            news.execute();
        }
    }

    private void showErrorView() {
        setContentView(R.layout.p6_error_layout);
        TextView errorView = (TextView) findViewById(R.id.errorMessage);
        errorView.setText("App cannot connect to network. Check network settings and try again.");
    }
}


