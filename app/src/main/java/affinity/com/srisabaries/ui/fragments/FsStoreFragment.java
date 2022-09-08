package affinity.com.srisabaries.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.adapters.AutoCompleteSearchViewAdapter;
import affinity.com.srisabaries.adapters.FsStoreViewPagerAdapter;
import affinity.com.srisabaries.customviews.CustomTypefaceAutoCompleteTextView;
import affinity.com.srisabaries.models.request.ReqFsStore;
import affinity.com.srisabaries.models.response.ResFsStore;
import affinity.com.srisabaries.network.ApiClient;
import affinity.com.srisabaries.network.IApiClient;
import affinity.com.srisabaries.network.MethodFactory;
import affinity.com.srisabaries.network.NetworkConnection;
import affinity.com.srisabaries.network.ServiceConstants;
import affinity.com.srisabaries.preference.PreferenceKeys;
import affinity.com.srisabaries.ui.activities.FsStoreMenWomenActivity;
import affinity.com.srisabaries.ui.activities.FsStoreProductDetail;
import affinity.com.srisabaries.ui.activities.HomeActivity;
import affinity.com.srisabaries.utils.AppConstants;
import affinity.com.srisabaries.utils.AppDialog;
import affinity.com.srisabaries.utils.AppUtilMethods;
import affinity.com.srisabaries.utils.Logger;
import affinity.com.srisabaries.utils.ToastUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akash on 2/8/16.
 */
public class FsStoreFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.vp_products)
    ViewPager vpProducts;
    @BindView(R.id.ll_pager_indicator)
    LinearLayout llPagerIndicator;
    @BindView(R.id.iv_shop_men)
    ImageView ivShopMen;
    @BindView(R.id.iv_shop_women)
    ImageView ivShopWomen;
    @BindView(R.id.at_search)
    CustomTypefaceAutoCompleteTextView acSearchTextView;
    @BindView(R.id.iv_cross_search)
    ImageView ivCrossSearch;
    private FsStoreViewPagerAdapter fsStoreViewPagerAdapter;
    private ArrayList<ResFsStore.ProductBanner> bannerArrayList;
    private List<ImageView> mImageViewList;
    private ProgressDialog mProgressDialog;
    private ArrayList<HashMap<String, String>> mAutoCompleteAdapterList;
    private AutoCompleteSearchViewAdapter searchAdapter;
    private int currentPage = 0;
    private Handler handler;
    private Runnable update;
    private Timer timer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fs_store, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        ((HomeActivity) mActivity).setActionBarTitle(R.string.txt_fs_store);
        bannerArrayList = new ArrayList<>();
        vpProducts.setOffscreenPageLimit(4);
        ivCrossSearch.setOnClickListener(this);
        vpProducts.addOnPageChangeListener(this);
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setTag(R.id.tag_position, i);
            imageView.setImageResource(R.drawable.x_sd_page_indicator);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) getResources().getDimension(R.dimen.d_pad_pager_indicator);
            params.rightMargin = (int) getResources().getDimension(R.dimen.d_pad_pager_indicator);
            imageView.setLayoutParams(params);
            imageView.setOnClickListener(this);
            llPagerIndicator.addView(imageView);
            mImageViewList.add(imageView);

        }
        mImageViewList.get(0).setSelected(true);
        acSearchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivCrossSearch.setVisibility(View.VISIBLE);
                } else {
                    ivCrossSearch.setVisibility(View.GONE);
                    AppUtilMethods.hideKeyBoard(mActivity);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initVariables() {
        currentPage = vpProducts.getCurrentItem();
        acSearchTextView.setThreshold(1);
        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (currentPage == 4) {
                    currentPage = 0;
                }
                vpProducts.setCurrentItem(currentPage++, true);
                Logger.e("In thread FS Store");
            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 4000);

        acSearchTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, FsStoreProductDetail.class);
                intent.putExtra("productId", searchAdapter.getSelectedList(position).get("productId"));
                intent.putExtra("productPosition", position);
                startActivityForResult(intent, AppConstants.RC_FS_STORE_PRODUCT_DETAIL);


            }
        });
        ivShopMen.setOnClickListener(this);
        ivShopWomen.setOnClickListener(this);
        if (NetworkConnection.isNetworkConnected(mActivity)) {
            getFsStore();

        } else {
            ToastUtils.showShortToast(mActivity, getString(R.string.err_network_connection));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_shop_men:
                intent = new Intent(mActivity, FsStoreMenWomenActivity.class);
                intent.putExtra("FsStoreGender", AppConstants.GENDER_MALE);
                this.startActivityForResult(intent, AppConstants.RC_FROM_FS_STORE_MEN_WOMEN_PRODUCTS_LISTING);
                break;

            case R.id.et_search:
                break;
            case R.id.iv_shop_women:
                intent = new Intent(mActivity, FsStoreMenWomenActivity.class);
                intent.putExtra("FsStoreGender", AppConstants.GENDER_FEMALE);
                startActivityForResult(intent, AppConstants.RC_FROM_FS_STORE_MEN_WOMEN_PRODUCTS_LISTING);
                break;
            case R.id.iv_cross_search:
                acSearchTextView.setText("");
                ivCrossSearch.setVisibility(View.GONE);
                break;
        }
    }


    public void showProgressDialog() {
        mProgressDialog = AppDialog.showProgressDialog(mActivity);
        mProgressDialog.show();
    }

    /**
     * This method is used to hide the progress dialog
     *
     * @return void
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void setAutoCompleteAdapter(ResFsStore resFsStore) {
        mAutoCompleteAdapterList = new ArrayList<>();
        for (int i = 0; i < resFsStore.getFsProductData().size(); i++) {
            HashMap<String, String> row = new HashMap<String, String>();
            row.put("productId", resFsStore.getFsProductData().get(i).getProductID());
            row.put("productName", resFsStore.getFsProductData().get(i).getName());
            mAutoCompleteAdapterList.add(row);
        }
        searchAdapter = new AutoCompleteSearchViewAdapter(mActivity, android.R.layout.simple_dropdown_item_1line, mAutoCompleteAdapterList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setFsStoreCount();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Getting count of MyCart from server
     */
    private void setFsStoreCount() {
        IApiClient iApiClient = ApiClient.getApiClient();
        final ReqFsStore reqFsStore = new ReqFsStore();
        reqFsStore.setUserID(mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, mActivity.getString(R.string.txt_skip_user_id)));
        reqFsStore.setMethod(MethodFactory.GET_FS_STORE.getMethod());
        reqFsStore.setServiceKey(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
        Call<ResFsStore> resFsStoreCall = iApiClient.getFsStore(reqFsStore);
        resFsStoreCall.enqueue(new Callback<ResFsStore>() {
            @Override
            public void onResponse(Call<ResFsStore> call, Response<ResFsStore> response) {
                if (isAdded()) {
                    ResFsStore resFsStore = response.body();
                    if (resFsStore != null) {
                        if (resFsStore.getSuccess() == ServiceConstants.SUCCESS) {
                            TextView tvToolbarFavouriteCount = (TextView) mActivity.findViewById(R.id.tv_toolbar_cart_count);
                            tvToolbarFavouriteCount.setText(resFsStore.getCartCount());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResFsStore> call, Throwable t) {
            }
        });
    }

    /**
     * Getting Fs store product list data from server
     */
    private void getFsStore() {
        showProgressDialog();
        IApiClient iApiClient = ApiClient.getApiClient();
        final ReqFsStore reqFsStore = new ReqFsStore();
        reqFsStore.setUserID(mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, mActivity.getString(R.string.txt_skip_user_id)));
        reqFsStore.setMethod(MethodFactory.GET_FS_STORE.getMethod());
        reqFsStore.setServiceKey(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
        Call<ResFsStore> resFsStoreCall = iApiClient.getFsStore(reqFsStore);
        resFsStoreCall.enqueue(new Callback<ResFsStore>() {
            @Override
            public void onResponse(Call<ResFsStore> call, Response<ResFsStore> response) {
                dismissProgressDialog();
                if (isAdded()) {
                    ResFsStore resFsStore = response.body();
                    if (resFsStore != null) {
                        if (resFsStore.getSuccess() == ServiceConstants.SUCCESS) {
                            fsStoreViewPagerAdapter = new FsStoreViewPagerAdapter(((HomeActivity) mActivity).getSupportFragmentManager(), mActivity, resFsStore.getProductBanner());
                            vpProducts.setAdapter(fsStoreViewPagerAdapter);
                            Glide.with(mActivity)
                                    .load(resFsStore.getShopManUrl())
                                    .placeholder(R.drawable.no_image_available)
                                    .into(ivShopMen);
                            Glide.with(mActivity)
                                    .load(resFsStore.getShopWomanUrl())
                                    .placeholder(R.drawable.no_image_available)
                                    .into(ivShopWomen);
                            setAutoCompleteAdapter(resFsStore);
                            acSearchTextView.setAdapter(searchAdapter);
                            TextView tvToolbarFavouriteCount = (TextView) mActivity.findViewById(R.id.tv_toolbar_cart_count);
                            tvToolbarFavouriteCount.setText(resFsStore.getCartCount());

                        } else {
                            ToastUtils.showShortToast(mActivity, resFsStore.getErrstr());
                        }
                    } else {
                        ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResFsStore> call, Throwable t) {
                dismissProgressDialog();
                ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (ImageView imageView : mImageViewList) {
            imageView.setSelected(false);
        }
        mImageViewList.get(position).setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


}
