package affinity.com.srisabaries.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.utils.AppConstants;
import butterknife.BindView;

public class WebViewActivity extends BaseActivity {

	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.iv_back)
	ImageView ivBack;
	@BindView(R.id.wv_url)
	WebView wvUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		super.initData();
	}

	@Override
	protected void initViews() {
		Intent intent = getIntent();
		tvTitle.setText(intent.getIntExtra(AppConstants.TAG_TITLE_RESOURCE_ID, 0));
		wvUrl.getSettings().setJavaScriptEnabled(true);
		wvUrl.setWebViewClient(new MyWebViewClient());
		String url = intent.getStringExtra(AppConstants.TAG_URL);
		if (url.endsWith(".pdf")) {
//          wvUrl.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
			wvUrl.loadUrl("https://docs.google.com/viewer?url=" + url);
		} else
			wvUrl.loadUrl(url);

		//set listeners
		ivBack.setOnClickListener(this);
	}

	@Override
	protected void initVariables() {
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.iv_back:
				finish();
				break;
		}
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
			builder.setMessage(R.string.notification_error_ssl_cert_invalid);
			builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.proceed();
				}
			});
			builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.cancel();
				}
			});
			final AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
}
