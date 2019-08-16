package com.gao.wechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.gao.wechat.R;

public class WebViewActivity extends AppCompatActivity {
    public static final String ACTION = "com.gao.wechat.intent.action.activity.WebViewActivity";
    // Intent 的参数
    public static final String WEB_CONTENT = "content";
    public static final String WEB_TYPE = "web_type";

    // WEB_TYPE 可以填入的类型
    public static final String CONTENT_URL = "url";
    public static final String CONTENT_TEXT = "text";

    private WebView webView;
    private Toolbar toolbar;

    private String content;
    private String contentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initWebData();
        initToolbar();

        webView = findViewById(R.id.web_view);
        showContent();
    }

    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    public void loadData(String str) {
        String encoded = Base64.encodeToString(str.getBytes(),Base64.NO_PADDING);
        webView.loadData(encoded, "text/html", "base64");
    }

    private void initWebData() {
        Intent intent = getIntent();
        if (intent != null) {
            contentType = intent.getStringExtra(WEB_TYPE);
            content = intent.getStringExtra(WEB_CONTENT);
        }
    }

    private void showContent() {
        switch (contentType) {
            case CONTENT_TEXT:
                // 加载纯文本
                loadData(content);
                setToolbarTitle("扫描结果");
                break;
            case CONTENT_URL:
                // 加载指定URL的网页
                loadUrl(content);
                setToolbarTitle(webView.getTitle());
                break;
            default:
                // 其他情况
                loadData(content);
                break;
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.web_view_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
        }
    }

    private void setToolbarTitle(String text) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 按下返回按钮
                finish();
                return true;
            case R.id.web_view_info:
                // 在浏览器中打开
                openInBrowser(content);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openInBrowser(String text) {
        Uri uri = Uri.parse(text);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
