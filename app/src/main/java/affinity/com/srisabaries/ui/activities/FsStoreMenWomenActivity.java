package affinity.com.srisabaries.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.adapters.EndlessRecyclerOnScrollListenerGrid;
import affinity.com.srisabaries.adapters.FsStoreProductsListAdapter;
import affinity.com.srisabaries.interfaces.IOnItemClickListener;
import affinity.com.srisabaries.models.request.ReqFsStoreProductsList;
import affinity.com.srisabaries.models.request.ReqSetFavouriteProduct;
import affinity.com.srisabaries.models.response.ResBase;
import affinity.com.srisabaries.models.response.ResFsStoreProductsList;
import affinity.com.srisabaries.network.ApiClient;
import affinity.com.srisabaries.network.IApiClient;
import affinity.com.srisabaries.network.MethodFactory;
import affinity.com.srisabaries.network.NetworkConnection;
import affinity.com.srisabaries.network.ServiceConstants;
import affinity.com.srisabaries.preference.PreferenceKeys;
import affinity.com.srisabaries.utils.AppConstants;
import affinity.com.srisabaries.utils.AppDialog;
import affinity.com.srisabaries.utils.AppUtilMethods;
import affinity.com.srisabaries.utils.CameraAndGalleryUtils;
import affinity.com.srisabaries.utils.GridSpacingItemDecoration;
import affinity.com.srisabaries.utils.PermissionUtils;
import affinity.com.srisabaries.utils.ToastUtils;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FsStoreMenWomenActivity extends BaseActivity implements IOnItemClickListener {
    public int mGender;
    public ArrayList<Integer> mSelectedRangeForFSProducts = new ArrayList<>();
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_fs_store_product_list)
    RecyclerView mRvFsStoreProductList;
    @BindView(R.id.lnrNoData)
    LinearLayout lnrNoData;
    @BindView(R.id.tv_toolbar_favourite_count)
    TextView tvToolbarFavouriteCount;
    @BindView(R.id.rl_toolbar_filter)
    RelativeLayout rlToolbarFilter;
    @BindView(R.id.rl_toolbar_favourite)
    RelativeLayout rlToolbarFavourite;
    @BindView(R.id.rl_toolbar_mycart)
    RelativeLayout rlToolbarMyCart;
    @BindView(R.id.iv_toolbar_filter)
    ImageView ivToolbarFilter;
    @BindView(R.id.iv_toolbar_filter_active)
    ImageView ivFilterIndicator;
    @BindView(R.id.iv_cross_search)
    ImageView ivCrossSearch;
    GridLayoutManager gridLayoutManager;
    private List<String> mProductFiltersList;
    private List<String> mSelectedCategoryList = new ArrayList<>();
    private ArrayList<ResFsStoreProductsList.FsCatDatum> mCategoryList = new ArrayList<>();
    private List<Integer> mRangeList;
    private ResFsStoreProductsList mResFsStoreProductsList;
    private FsStoreProductsListAdapter mFsStoreProductsListAdapter;
    private List<ResFsStoreProductsList.FsProductDatum> mResFsStoreProductsListData = new ArrayList<>();
    private String mShareBody;
    private Bitmap mBitmap;
    private int mPageindex = 1;
    private String mMinPrice = "";
    private String mMaxPrice = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.RC_FS_STORE_PRODUCT_DETAIL && resultCode == RESULT_OK) {
            int pos = data.getExtras().getInt("productPosition");
            String productId = data.getExtras().getString("productId");
            int favourite = data.getExtras().getInt("productPositionFavourite");
            if (mResFsStoreProductsListData != null) {
                if (mResFsStoreProductsListData.get(pos).getFavourite() != favourite) {
                    String fav = tvToolbarFavouriteCount.getText().toString().trim();
                    int favCount = Integer.parseInt(fav.isEmpty() ? "0" : fav);
                    if (favourite == ServiceConstants.FAVOURITE) {
                        favCount++;
                    } else {
                        favCount--;
                    }
                    tvToolbarFavouriteCount.setText(String.valueOf(favCount));
                    if (favCount > 0) {
                        tvToolbarFavouriteCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToolbarFavouriteCount.setVisibility(View.VISIBLE);
                    }

                }
                for (int i = 0; i < mResFsStoreProductsListData.size(); i++) {
                    if (mResFsStoreProductsListData.get(i).getProductID().equals(productId)) {
                        mResFsStoreProductsListData.get(i).setFavourite(favourite);
                    }
                }
                //mResFsStoreProductsListData.get(pos).setFavourite(favourite);
                mFsStoreProductsListAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == AppConstants.RC_FAVORITES /*&& resultCode == RESULT_OK*/) {
            if (NetworkConnection.isNetworkConnected(FsStoreMenWomenActivity.this)) {
                int minPrice = mRangeList.get(0);
                int maxPrice = mRangeList.get(1);
                String minRange;
                String maxRange;
                if (minPrice == AppConstants.FILTER_MIN && maxPrice == AppConstants.FILTER_MAX) {
                    minRange = "";
                    maxRange = "";
                } else {
                    minRange = String.valueOf(minPrice);
                    maxRange = String.valueOf(maxPrice);
                }

             /*  mPageindex=1;
               Log.v("mPage_Log",mPageindex+"");*/
              //  getFsStoreProducts(etSearch.getText().toString(), mGender, minRange, maxRange, mPageindex, true);
            } else {
                ToastUtils.showShortToast(FsStoreMenWomenActivity.this, getString(R.string.err_network_connection));
            }
        }

        //super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_store_men_women);
        mShareBody = getString(R.string.txt_share_body);
        super.initData();


    }

    @Override
    protected void initViews() {
        rlToolbarFilter.setVisibility(View.VISIBLE);
        //rlToolbarMyCart.setVisibility(View.VISIBLE);
        rlToolbarFavourite.setVisibility(View.VISIBLE);
        rlToolbarFavourite.setOnClickListener(this);
        ivCrossSearch.setOnClickListener(this);
        if (getIntent().getIntExtra("FsStoreGender", 1) == AppConstants.GENDER_MALE) {
            tvTitle.setText(R.string.txt_shop_men_fs_store_title);
        } else if(getIntent().getIntExtra("FsStoreGender", 1) == AppConstants.GENDER_FEMALE){
            tvTitle.setText(R.string.txt_shop_women_fs_store_title);
        }else{
            tvTitle.setText(R.string.txt_shop_accessories_fs_store_title);
        }
        gridLayoutManager = new GridLayoutManager(FsStoreMenWomenActivity.this, 2);
        mRvFsStoreProductList.setLayoutManager(gridLayoutManager);
        mRvFsStoreProductList.addItemDecoration(new GridSpacingItemDecoration(2, 5, false));
        mFsStoreProductsListAdapter = new FsStoreProductsListAdapter(FsStoreMenWomenActivity.this, mResFsStoreProductsListData);
        mFsStoreProductsListAdapter.setOnItemClickListener(this);
        //setAdapter();
        if (NetworkConnection.isNetworkConnected(FsStoreMenWomenActivity.this)) {
            if (getIntent().getIntExtra("FsStoreGender", 1) == AppConstants.GENDER_MALE) {
                mGender = AppConstants.GENDER_MALE;
                getFsStoreProducts("", mGender, "", "", mPageindex, false);
            } else if(getIntent().getIntExtra("FsStoreGender", 1) == AppConstants.GENDER_FEMALE) {
                mGender = AppConstants.GENDER_FEMALE;
                getFsStoreProducts("", mGender, "", "", mPageindex, false);
            }else{
                mGender = AppConstants.ACCSESSORIES;
                getFsStoreProducts("", mGender, "", "", mPageindex, false);
            }
        } else {
            ToastUtils.showShortToast(FsStoreMenWomenActivity.this, getString(R.string.err_network_connection));
        }


        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AppUtilMethods.hideKeyBoard(FsStoreMenWomenActivity.this);
                    if (NetworkConnection.isNetworkConnected(FsStoreMenWomenActivity.this)) {
                        getFsStoreProducts(etSearch.getText().toString(), mGender, mMaxPrice, mMinPrice, 1, true);
                    } else {
                        ToastUtils.showShortToast(FsStoreMenWomenActivity.this, getResources().getString(R.string.err_network_connection));
                    }
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //mFsStoreProductsListAdapter.getFilter().filter(charSequence.toString());
                if (charSequence.length() > 0) {
                    ivCrossSearch.setVisibility(View.VISIBLE);
                } else {
                    ivCrossSearch.setVisibility(View.GONE);
                    AppUtilMethods.hideKeyBoard(FsStoreMenWomenActivity.this);
                    mPageindex = 1;
                    /*mMinPrice = "";
                    mMaxPrice = "";*/
                    if (NetworkConnection.isNetworkConnected(FsStoreMenWomenActivity.this)) {

                        getFsStoreProducts("", mGender, mMaxPrice, mMinPrice, 1, true);
                    } else {
                        ToastUtils.showShortToast(FsStoreMenWomenActivity.this, getResources().getString(R.string.err_network_connection));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * setting adapters and onScroll listeners for paging
     */
    private void setAdapter() {
        if (!mResFsStoreProductsListData.isEmpty()){
            mRvFsStoreProductList.setVisibility(View.VISIBLE);
            lnrNoData.setVisibility(View.GONE);
        }else{
            mRvFsStoreProductList.setVisibility(View.GONE);
            lnrNoData.setVisibility(View.VISIBLE);
        }
        mRvFsStoreProductList.setAdapter(mFsStoreProductsListAdapter);
        mRvFsStoreProductList.addOnScrollListener(new EndlessRecyclerOnScrollListenerGrid(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d("onloadmore", mPageindex+" Load More data, Current Page = " + current_page);
                //mPageindex += 1;
                if (mPageindex!=current_page){
                mPageindex = current_page;
                getFsStoreProducts(etSearch.getText().toString(), mGender, mMaxPrice, mMinPrice, mPageindex, false);}
            }
        });
    }

    /**
     * Sharing product
     *
     * @param bitmap
     * @param shareBody
     */
    public void shareProduct(Bitmap bitmap, String shareBody) {
        mBitmap = bitmap;
        if (PermissionUtils.hasStoragePermission(this)) {
            String shareSubject = getString(R.string.app_name);
            String imagePath = AppConstants.FITSTREET_DIRECTORY_PATH + AppConstants.FITSTREET_PRODUCT_IMAGE_PATH + +System.currentTimeMillis() + AppConstants.EXTN_JPG;
            CameraAndGalleryUtils.saveImageToFile(bitmap, imagePath);
            AppUtilMethods.openImageShareDialog(this, shareSubject, mShareBody, imagePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.RC_REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    shareProduct(mBitmap, mShareBody);
                } else {
                    // Permission Denied
                    ToastUtils.showShortToast(this, getString(R.string.err_permission_denied));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * setting favourites/unfavourites to/from server
     *
     * @param position
     * @param methodName
     */
    public void setFavouriteUnFavourite(final int position, final String methodName) {
        AppDialog.showProgressDialog(this, true);
        IApiClient client = ApiClient.getApiClient();
        ReqSetFavouriteProduct reqSetFavouriteProduct = new ReqSetFavouriteProduct();
        reqSetFavouriteProduct.setProductID(mResFsStoreProductsListData.get(position).getProductID());
        reqSetFavouriteProduct.setUserID(mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, ""));
        reqSetFavouriteProduct.setMethod(methodName);
        reqSetFavouriteProduct.setServiceKey(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
        Call<ResBase> resBaseCall = client.setFavouriteProduct(reqSetFavouriteProduct);
        resBaseCall.enqueue(new Callback<ResBase>() {
            @Override
            public void onResponse(Call<ResBase> call, Response<ResBase> response) {
                AppDialog.showProgressDialog(FsStoreMenWomenActivity.this, false);
                ResBase resBase = response.body();
                if (resBase != null) {
                    if (resBase.getSuccess() == ServiceConstants.SUCCESS) {
                        String fav = tvToolbarFavouriteCount.getText().toString().trim();
                        int favCount = Integer.parseInt(fav.isEmpty() ? "0" : fav);
                        if (methodName.equals(MethodFactory.UNFAVOURITE_PRODUCT.getMethod().toString())) {
                            ToastUtils.showShortToast(FsStoreMenWomenActivity.this, getString(R.string.txt_success_unfavourite_product));
                            mResFsStoreProductsListData.get(position).setFavourite(ServiceConstants.UNFAVOURITE);
                            mFsStoreProductsListAdapter.notifyDataSetChanged();
                            tvToolbarFavouriteCount.setText(String.valueOf(favCount - 1));
                            if ((favCount - 1) > 0)
                                tvToolbarFavouriteCount.setVisibility(View.VISIBLE);
                            else
                                tvToolbarFavouriteCount.setVisibility(View.VISIBLE);
                        } else {
                            mResFsStoreProductsListData.get(position).setFavourite(ServiceConstants.FAVOURITE);
                            ToastUtils.showShortToast(FsStoreMenWomenActivity.this, getString(R.string.txt_success_set_favourite_product));
                            mFsStoreProductsListAdapter.notifyDataSetChanged();
                            tvToolbarFavouriteCount.setText(String.valueOf(favCount + 1));
                            if ((favCount + 1) > 0)
                                tvToolbarFavouriteCount.setVisibility(View.VISIBLE);
                            else
                                tvToolbarFavouriteCount.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtils.showShortToast(FsStoreMenWomenActivity.this, resBase.getErrstr());
                    }
                } else {
                    ToastUtils.showShortToast(FsStoreMenWomenActivity.this, R.string.err_network_connection);
                }
            }

            @Override
            public void onFailure(Call<ResBase> call, Throwable t) {
                AppDialog.showProgressDialog(FsStoreMenWomenActivity.this, false);
                ToastUtils.showShortToast(FsStoreMenWomenActivity.this, R.string.err_network_connection);

            }
        });
    }

    /**
     * Method used to handle visibility of filter indicator
     *
     * @param visibility
     */
    public void setFilterIndicatorVisibility(int visibility) {
        ivFilterIndicator.setVisibility(visibility);
    }

    @Override
    protected void initVariables() {
        ivBack.setOnClickListener(this);
        rlToolbarFilter.setOnClickListener(this);
        mRangeList = new ArrayList<Integer>(2);
        mProductFiltersList = Arrays.asList(getResources().getStringArray(R.array.arr_filter_fs_product));
        mRangeList.add(AppConstants.FILTER_MIN);
        mRangeList.add(AppConstants.FILTER_MAX);
    }

    /**
     * Getting Fs Store products from server
     *
     * @param searchTerm
     * @param gender
     * @param maxRange
     * @param minRange
     * @param pageindex
     * @param isFromFilters
     */
    public void getFsStoreProducts(final String searchTerm, int gender, String maxRange, String minRange, final int pageindex, final boolean isFromFilters) {
        mPageindex =pageindex;
        mMaxPrice=maxRange;
        mMinPrice=minRange;
        Log.v("gs_store_log gender",gender+" minrange "+minRange+" maxrange "+maxRange+" pageindex "+pageindex+" isFromFilters "+isFromFilters
               +" userID "+mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, getString(R.string.txt_skip_user_id))+" servicekey "+mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
        AppDialog.showProgressDialog(FsStoreMenWomenActivity.this, true);
        IApiClient apiClient = ApiClient.getApiClient();
        final ReqFsStoreProductsList reqFsStoreProductsList = new ReqFsStoreProductsList();
        reqFsStoreProductsList.setServiceKey(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
        reqFsStoreProductsList.setMethod(MethodFactory.GET_FS_STORE_PRODUCTS_LIST.getMethod());
        reqFsStoreProductsList.setUserID(mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, getString(R.string.txt_skip_user_id)));
        reqFsStoreProductsList.setGender(gender);
        reqFsStoreProductsList.setSearch(searchTerm);
        reqFsStoreProductsList.setPage(String.valueOf(pageindex));
        ReqFsStoreProductsList.FilterType filterType = new ReqFsStoreProductsList.FilterType();
        ReqFsStoreProductsList.PriceRange priceRange = new ReqFsStoreProductsList.PriceRange();
        priceRange.setMax(maxRange);
        priceRange.setMin(minRange);
        filterType.setPriceRange(priceRange);
        filterType.setProCatID(mSelectedCategoryList);
        reqFsStoreProductsList.setFilterType(filterType);
        apiClient.getFsStoreProductsList(reqFsStoreProductsList);
        final Gson gson = new Gson();
        Log.v("get_para_log",gson.toJson(reqFsStoreProductsList)+" ");
        Call<ResFsStoreProductsList> resFsStoreProductsListCall = apiClient.getFsStoreProductsList(reqFsStoreProductsList);
        resFsStoreProductsListCall.enqueue(new Callback<ResFsStoreProductsList>() {
            @Override
            public void onResponse(Call<ResFsStoreProductsList> call, Response<ResFsStoreProductsList> response) {
                Log.v("get_para_log2",gson.toJson(response)+" ");
                mResFsStoreProductsList = new ResFsStoreProductsList();
                AppDialog.showProgressDialog(FsStoreMenWomenActivity.this, false);
                mResFsStoreProductsList = response.body();
                if (mResFsStoreProductsList != null) {
                    if (mResFsStoreProductsList.getSuccess() == ServiceConstants.SUCCESS) {

                        if (mCategoryList.isEmpty()){
                            mCategoryList.addAll(mResFsStoreProductsList.getFsCatData());
                        }


                        if (isFromFilters)
                            mResFsStoreProductsListData.clear();
                        mResFsStoreProductsListData.addAll(mResFsStoreProductsList.getFsProductData());
                        tvToolbarFavouriteCount.setText(mResFsStoreProductsList.getFavCount());
                        if (!TextUtils.isEmpty(searchTerm) || pageindex == 1)
                            setAdapter();
                        else
                            mFsStoreProductsListAdapter.notifyDataSetChanged();
                    } else {
                        if (isFromFilters)
                            mResFsStoreProductsListData.clear();
                        mResFsStoreProductsListData.addAll(mResFsStoreProductsList.getFsProductData());
                        mFsStoreProductsListAdapter.notifyDataSetChanged();
                        Log.v("get_para_log3",isFromFilters+" "+mResFsStoreProductsListData.isEmpty());
                        if (!mResFsStoreProductsListData.isEmpty()){
                            mRvFsStoreProductList.setVisibility(View.VISIBLE);
                            lnrNoData.setVisibility(View.GONE);
                        }else{
                            mRvFsStoreProductList.setVisibility(View.GONE);
                            lnrNoData.setVisibility(View.VISIBLE);
                        }
                        //mResFsStoreProductsListData.clear();
                        //tvToolbarFavouriteCount.setText(mResFsStoreProductsList.getFavCount());
                        //tvToolbarFavouriteCount.setVisibility(View.GONE);
                        //ToastUtils.showShortToast(FsStoreMenWomenActivity.this, mResFsStoreProductsList.getErrstr());
                    }

                } else {
                    ToastUtils.showShortToast(FsStoreMenWomenActivity.this, R.string.err_network_connection);

                }
            }

            @Override
            public void onFailure(Call<ResFsStoreProductsList> call, Throwable t) {
                AppDialog.showProgressDialog(FsStoreMenWomenActivity.this, false);
                ToastUtils.showShortToast(FsStoreMenWomenActivity.this, R.string.err_network_connection);
            }
        });


    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rl_toolbar_filter:
                if (mCategoryList.isEmpty()) {
                    if (NetworkConnection.isNetworkConnected(this)) {
                        if (mResFsStoreProductsList != null) {
                            for (int i = 0; i < mResFsStoreProductsList.getFsCatData().size(); i++) {
                                mCategoryList.add(mResFsStoreProductsList.getFsCatData().get(i));
                            }
                            AppDialog.showFSProductFiltersDialog(this, mGender, ivToolbarFilter.getMeasuredWidth(), ivToolbarFilter.getMeasuredHeight(), etSearch.getText().toString().trim(), mProductFiltersList,
                                    mCategoryList, mSelectedCategoryList, mRangeList, mPageindex);
                        }
                    } else {
                        ToastUtils.showShortToast(this, R.string.err_network_connection);
                    }
                } else {
                    AppDialog.showFSProductFiltersDialog(this, mGender, ivToolbarFilter.getMeasuredWidth(), ivToolbarFilter.getMeasuredHeight(), etSearch.getText().toString().trim(), mProductFiltersList,
                            mCategoryList, mSelectedCategoryList, mRangeList, mPageindex);

                }
                break;
            case R.id.rl_toolbar_favourite:
                if (mAppSharedPreference.getBoolean(PreferenceKeys.KEY_LOGGED_IN, false)) {
                    if (mAppSharedPreference.getString(PreferenceKeys.KEY_IS_EMAIL_VERIFIED, "0").equalsIgnoreCase("1")) {
                        Intent intent = new Intent(this, FavouritesActivity.class);
                        intent.putExtra(AppConstants.TAG_FROM_WHERE, AppConstants.FROM_PRODUCTS);
                        startActivityForResult(intent, AppConstants.RC_FAVORITES);
                    } else {
                        AppDialog.showVerifyEmailAlertDialog(this);
                    }
                } else {
                    AppDialog.showLoginAlertDialog(this);
                }
                break;

            case R.id.iv_cross_search:
                etSearch.setText("");
                ivCrossSearch.setVisibility(View.GONE);
                AppUtilMethods.hideKeyBoard(this);
                break;

        }
    }

    @Override
    public void onItemClick(View view, int position, int tag) {
        Intent intent = new Intent(this, FsStoreProductDetail.class);
        ResFsStoreProductsList.FsProductDatum fsProductDatum = (ResFsStoreProductsList.FsProductDatum) view.getTag();
        intent.putExtra("productId", fsProductDatum.getProductID());
        intent.putExtra("productPosition", position);
        startActivityForResult(intent, AppConstants.RC_FS_STORE_PRODUCT_DETAIL);
    }
}
