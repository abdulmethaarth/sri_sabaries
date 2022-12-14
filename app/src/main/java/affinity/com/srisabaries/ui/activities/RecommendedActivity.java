package affinity.com.srisabaries.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.adapters.EndlessRecyclerOnScrollListener;
import affinity.com.srisabaries.adapters.RecommendedRecyclerAdapter;
import affinity.com.srisabaries.models.request.ReqProductsCategory;
import affinity.com.srisabaries.models.response.ResGetProductsCategories;
import affinity.com.srisabaries.models.response.ResProductsCategoryData;
import affinity.com.srisabaries.network.ApiClient;
import affinity.com.srisabaries.network.IApiClient;
import affinity.com.srisabaries.network.MethodFactory;
import affinity.com.srisabaries.network.NetworkConnection;
import affinity.com.srisabaries.network.ServiceConstants;
import affinity.com.srisabaries.preference.PreferenceKeys;
import affinity.com.srisabaries.utils.AppDialog;
import affinity.com.srisabaries.utils.AppUtilMethods;
import affinity.com.srisabaries.utils.GridSpacingItemDecoration;
import affinity.com.srisabaries.utils.ToastUtils;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedActivity extends BaseActivity implements View.OnClickListener {
	@BindView(R.id.rv_Recommended)
	RecyclerView rvRecommended;
	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.iv_back)
	ImageView ivBack;
	@BindView(R.id.et_search)
	EditText etSearch;
	@BindView(R.id.iv_cross_search)
	ImageView ivCrossSearch;
	private int mPageindex = 1;
	private RecommendedRecyclerAdapter mRecommendedRecyclerAdapter;
	private List<ResProductsCategoryData> mResProductsCategoryData = new ArrayList<>();
	GridLayoutManager gridLayoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommended);
		super.initData();
	}

	@Override
	protected void initViews() {
		tvTitle.setText("CATEGORIES");
		etSearch.setHint("Search by Product Categories");
		gridLayoutManager = new GridLayoutManager(RecommendedActivity.this, 2);
		//rvRecommended.addItemDecoration(new GridSpacingItemDecoration(2,30, true));
		rvRecommended.setLayoutManager(gridLayoutManager);
		mResProductsCategoryData = new ArrayList<ResProductsCategoryData>();
		mRecommendedRecyclerAdapter = new RecommendedRecyclerAdapter(RecommendedActivity.this, mResProductsCategoryData);
		setAdapter();
		if (NetworkConnection.isNetworkConnected(this)) {
			getProductsCategories("", 0, mPageindex, false);
		} else {
			ToastUtils.showShortToast(this, R.string.err_network_connection);
		}

		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					AppUtilMethods.hideKeyBoard(RecommendedActivity.this);
					if (NetworkConnection.isNetworkConnected(RecommendedActivity.this)) {
						getProductsCategories(etSearch.getText().toString(), 0, 1, true);
					} else {
						ToastUtils.showShortToast(RecommendedActivity.this, getResources().getString(R.string.err_network_connection));
					}
				}
				return false;
			}
		});

		//set listeners
		ivBack.setOnClickListener(this);
		ivCrossSearch.setOnClickListener(this);
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// mRecommendedRecyclerAdapter.getFilter().filter(s.toString());
				if (s.length() > 0) {
					ivCrossSearch.setVisibility(View.VISIBLE);
					getProductsCategories(etSearch.getText().toString(), 0, 1, true);
				} else {
					ivCrossSearch.setVisibility(View.GONE);
					mPageindex = 1;
					if (NetworkConnection.isNetworkConnected(RecommendedActivity.this)) {
						getProductsCategories(etSearch.getText().toString(), 0, 1, true);
					} else {
						ToastUtils.showShortToast(RecommendedActivity.this, getResources().getString(R.string.err_network_connection));
					}
				}
//                if (s.length() >= 0) {
//                    if (NetworkConnection.isNetworkConnected(RecommendedActivity.this)) {
//                        getProductsCategories(s.toString(), 1);
//                    } else {
//                        ToastUtils.showShortToast(RecommendedActivity.this, R.string.err_network_connection);
//                    }
//                }
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	/**
	 * Setting adapters and setting onScroll Listeners for paging
	 */
	private void setAdapter() {
		rvRecommended.setAdapter(mRecommendedRecyclerAdapter);
		rvRecommended.setOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
			@Override
			public void onLoadMore(int current_page) {
				Log.d("onloadmore", "Load More data, Current Page = " + current_page);
				mPageindex += 1;
				getProductsCategories(etSearch.getText().toString(), 0, mPageindex, false);
			}
		});
	}

	/**
	 * Getting products categories from server
	 *
	 * @param searchTerm
	 * @param from
	 * @param pageindex
	 * @param isFromSearch
	 */
	private void getProductsCategories(final String searchTerm, int from, final int pageindex, final boolean isFromSearch) {
		if (from == 0) {
			AppDialog.showProgressDialog(RecommendedActivity.this, true);
		}
		IApiClient client = ApiClient.getApiClient();
		ReqProductsCategory reqProductsCategory = new ReqProductsCategory();
		reqProductsCategory.setPage(String.valueOf(pageindex));
		reqProductsCategory.setMethod(MethodFactory.GET_PRODUCT_CATEGORIES.getMethod());
		reqProductsCategory.setServiceKey(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
		reqProductsCategory.setUserID(mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, ""));
		reqProductsCategory.setSearch(searchTerm);
		Call<ResGetProductsCategories> resGetProductsCategoriesCall = client.getProductsCategory(/*reqProductsCategory*/ mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ""));
		resGetProductsCategoriesCall.enqueue(new Callback<ResGetProductsCategories>() {
			@Override
			public void onResponse(Call<ResGetProductsCategories> call, Response<ResGetProductsCategories> response) {
				AppDialog.showProgressDialog(RecommendedActivity.this, false);
				ResGetProductsCategories resGetProductsCategories = response.body();
				if (resGetProductsCategories != null) {
					if (resGetProductsCategories.getSuccess() == ServiceConstants.SUCCESS) {
						if (isFromSearch)
							mResProductsCategoryData.clear();
						mResProductsCategoryData.addAll(resGetProductsCategories.getProductCatData());
						if (!TextUtils.isEmpty(searchTerm) || pageindex == 1)
							setAdapter();
						else
							mRecommendedRecyclerAdapter.notifyDataSetChanged();
					} else {
						if (isFromSearch) {
							mResProductsCategoryData.clear();
							mResProductsCategoryData.addAll(resGetProductsCategories.getProductCatData());
							mRecommendedRecyclerAdapter.notifyDataSetChanged();
						}
					}
				} else {
					ToastUtils.showShortToast(RecommendedActivity.this, R.string.err_network_connection);
				}
			}

			@Override
			public void onFailure(Call<ResGetProductsCategories> call, Throwable t) {
				AppDialog.showProgressDialog(RecommendedActivity.this, false);
				ToastUtils.showShortToast(RecommendedActivity.this, R.string.err_network_connection);
			}
		});

	}

	@Override
	protected void initVariables() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back:
				AppUtilMethods.hideKeyBoard(this, v);
				AppUtilMethods.hideKeyBoard(this);
				AppUtilMethods.hideKeyBoard(this, etSearch);
				finish();
				break;
			case R.id.iv_cross_search:
				etSearch.setText("");
				ivCrossSearch.setVisibility(View.GONE);
				AppUtilMethods.hideKeyBoard(this);
				mPageindex = 1;
				if (NetworkConnection.isNetworkConnected(RecommendedActivity.this)) {
					getProductsCategories(etSearch.getText().toString(), 0, 1, true);
				} else {
					ToastUtils.showShortToast(RecommendedActivity.this, getResources().getString(R.string.err_network_connection));
				}
				break;
		}
	}
}
