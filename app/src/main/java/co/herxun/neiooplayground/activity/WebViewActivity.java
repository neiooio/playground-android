package co.herxun.neiooplayground.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gc.materialdesign.views.ProgressBarDeterminate;

import co.herxun.neiooplayground.R;
import co.herxun.neiooplayground.utils.Constant;

public class WebViewActivity extends BaseActivity {
	private WebView webView;
	private ProgressBarDeterminate mProgressBarDeterminate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		setUpWebView();
		checkBackForward();
		checkBundle();
	}

	private void initView(){
		setContentView(R.layout.activity_web_view);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);

		mProgressBarDeterminate = (ProgressBarDeterminate)findViewById(R.id.web_progress_bar);
		mProgressBarDeterminate.setMax(100);
	}

	private void setUpWebView(){
		webView = (WebView)findViewById(R.id.web_webview);
		webView.setFocusable(true);
		webView.setFocusableInTouchMode(true);
		webView.setHapticFeedbackEnabled(true);
		webView.setClickable(true);
		webView.requestFocus(View.FOCUS_DOWN);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
				return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
			}

			@Override
			public void onProgressChanged(WebView view, int progress) {
				if (progress == 100) {
					mProgressBarDeterminate.setVisibility(View.GONE);
				} else {
					mProgressBarDeterminate.setVisibility(View.VISIBLE);
				}
				mProgressBarDeterminate.setProgress(progress);
			}
		});

		if (Build.VERSION.SDK_INT < 11) {
			webView.getSettings().setBuiltInZoomControls(true);
		} else {
			webView.getSettings().setDisplayZoomControls(false);
			webView.getSettings().setBuiltInZoomControls(true);
		}

		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				checkBackForward();
			}
		});
	}

	private void checkBundle(){
		if(getIntent().hasExtra(Constant.INTENT_EXTRA_KEY_WEB_URL)){
			String url = getIntent().getStringExtra(Constant.INTENT_EXTRA_KEY_WEB_URL);
			webView.loadUrl(url);
		}
	}

	private void checkBackForward() {
		invalidateOptionsMenu();
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,R.anim.push_up_out);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_webview_activity, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		eableMenuItem(menu.getItem(0),webView.canGoBack());
		eableMenuItem(menu.getItem(1),webView.canGoForward());

		return super.onPrepareOptionsMenu(menu);

	}

	private void eableMenuItem(MenuItem menuItem, boolean enable){
		Drawable d = menuItem.getIcon();
		if(enable){
			d.setAlpha(255);
			menuItem.setIcon(d);
			menuItem.setEnabled(true);
		}else{
			d.setAlpha(61);
			menuItem.setIcon(d);
			menuItem.setEnabled(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_back:
				webView.goBack();
				return true;
			case R.id.action_next:
				webView.goForward();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
