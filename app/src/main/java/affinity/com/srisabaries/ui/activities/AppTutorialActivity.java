package affinity.com.srisabaries.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.adapters.TutorialPageAdapter;
import affinity.com.srisabaries.customviews.AppTutorialViewPager;
import affinity.com.srisabaries.ui.fragments.AppTutorialFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppTutorialActivity extends BaseActivity {

	@BindView(R.id.vp_app_tutorial)
	AppTutorialViewPager vpAppTutorial;
	@BindView(R.id.ll_pager_indicator)
	LinearLayout llPagerIndicator;
	@BindView(R.id.view_green)
	View viewGreen;
	@BindView(R.id.iv_next_row)
	ImageView ivNextRow;
	@BindView(R.id.iv_prev_row)
	ImageView ivPrevRow;
	private List<Fragment> mTutorialFragmentList;
	private int mPage = 0;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(AppTutorialActivity.this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		setResult(RESULT_OK);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_tutorial);
		ButterKnife.bind(this);
		vpAppTutorial.setAdapter(new TutorialPageAdapter(getSupportFragmentManager(), getTutorialFragments()));
		vpAppTutorial.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		ivNextRow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mPage < 4) {
					mPage++;
					vpAppTutorial.setCurrentItem(mPage);
				} else {
					Intent intent = new Intent(AppTutorialActivity.this, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
					setResult(RESULT_OK);
				}
			}
		});
		ivPrevRow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mPage >= 1) {
					mPage--;
					vpAppTutorial.setCurrentItem(mPage);
				}
			}
		});
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void initVariables() {

	}

	/**
	 * Setting Tutorial Fragments in View-Pager
	 *
	 * @return
	 */
	private List<Fragment> getTutorialFragments() {
		mTutorialFragmentList = new ArrayList<Fragment>();
		mTutorialFragmentList.add(AppTutorialFragment.newInstance(R.drawable.app_tutorial_1, R.string.txt_blank, R.string.txt_blank));
		mTutorialFragmentList.add(AppTutorialFragment.newInstance(R.drawable.app_tutorial_2, R.string.txt_blank, R.string.txt_blank));
		mTutorialFragmentList.add(AppTutorialFragment.newInstance(R.drawable.app_tutorial_3, R.string.txt_blank, R.string.txt_blank));
		mTutorialFragmentList.add(AppTutorialFragment.newInstance(R.drawable.app_tutorial_4, R.string.txt_blank, R.string.txt_blank));
		mTutorialFragmentList.add(AppTutorialFragment.newInstance(R.drawable.app_tutorial_5, R.string.txt_blank, R.string.txt_blank));
		return mTutorialFragmentList;
	}

	@Override
	public void onClick(View view) {

	}
}
