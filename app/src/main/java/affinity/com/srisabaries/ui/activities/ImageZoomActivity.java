package affinity.com.srisabaries.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import affinity.com.srisabaries.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageZoomActivity extends AppCompatActivity {

	@BindView(R.id.iv_back)
	ImageView ivBack;
	@BindView(R.id.photo_view)
	PhotoView photoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_zoom);
		ButterKnife.bind(this);

		ivBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Uri uri = Uri.parse(getIntent().getStringExtra("image"));
		Picasso.with(this).load(uri).into(photoView);
	}
}
