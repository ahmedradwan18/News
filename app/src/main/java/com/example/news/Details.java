package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import okhttp3.internal.Util;

public class Details extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private ImageView imageView;
    private TextView appBar_Title, appBar_SubTitle, date, time, title;
    private boolean isHideTolbarView = false;
    private FrameLayout dateBehavoir;
    private LinearLayout titleAppBar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl, mImg, mTitle, mDte, mSource, mAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        dateBehavoir = findViewById(R.id.date_behavior);
        titleAppBar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        appBar_Title = findViewById(R.id.title_on_appbar);
        appBar_SubTitle = findViewById(R.id.subtitle_on_appbar);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        Intent intent = getIntent();

        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        mDte = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());
        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        appBar_Title.setText(mSource);
        appBar_SubTitle.setText(mUrl);
        date.setText(Utils.DateFormat(mDte));
        title.setText(mTitle);

        String author = null;
        if (mAuthor != null && mAuthor != "") {
            mAuthor = "\u2022" + mAuthor;
        } else mAuthor = "";
        time.setText(mSource + author + "\u2022" + Utils.DateToTimeFormat(mDte));

        initWebView(mUrl);


    }


    private void initWebView(String url) {

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentAge = (float) Math.abs(i) / (float) maxScroll;

        if (percentAge == 1f && isHideTolbarView) {
            dateBehavoir.setVisibility(View.GONE);
            titleAppBar.setVisibility(View.VISIBLE);
            isHideTolbarView = !isHideTolbarView;

        } else if (percentAge < 1f && isHideTolbarView) {


            dateBehavoir.setVisibility(View.VISIBLE);
            titleAppBar.setVisibility(View.GONE);
            isHideTolbarView = !isHideTolbarView;


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.view_web) {
            Intent i = new Intent((Intent.ACTION_VIEW));
            i.setData(Uri.parse(mUrl));
            startActivity(i);
            return true;
        }
        else if(id==R.id.share){
            try {

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT,mSource);
                String body=mTitle+"\n"+mUrl+"\n"+"Share from the new apps"+"\n";
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(intent.createChooser(intent,("share with :")));


            }
            catch (Exception e){
                Toast.makeText(this, "sorry can't be share...", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
