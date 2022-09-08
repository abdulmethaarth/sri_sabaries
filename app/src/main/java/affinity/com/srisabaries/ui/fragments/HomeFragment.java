package affinity.com.srisabaries.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.adapters.EndlessRecyclerOnScrollListener;
import affinity.com.srisabaries.adapters.TabPagerAdapter;
import affinity.com.srisabaries.customviews.CustomTypefaceTextView;
import affinity.com.srisabaries.customviews.WrapContentViewPager;
import affinity.com.srisabaries.imagechooser.ImageChooser;
import affinity.com.srisabaries.models.request.ReqProductsCategory;
import affinity.com.srisabaries.models.request.ReqUserCalorieDistance;
import affinity.com.srisabaries.models.response.ResUserCalorieDistance;
import affinity.com.srisabaries.network.ApiClient;
import affinity.com.srisabaries.network.IApiClient;
import affinity.com.srisabaries.network.MethodFactory;
import affinity.com.srisabaries.network.NetworkConnection;
import affinity.com.srisabaries.network.ServiceConstants;
import affinity.com.srisabaries.preference.PreferenceKeys;
import affinity.com.srisabaries.ui.activities.HomeActivity;
import affinity.com.srisabaries.ui.activities.NotificationActivity;
import affinity.com.srisabaries.ui.activities.RecommendedActivity;
import affinity.com.srisabaries.ui.activities.ShareActivity;
import affinity.com.srisabaries.ui.activities.TrendingActivity;
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

/**
 * Created by akash on 17/6/16.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tab_layoutCat)
    TabLayout tabLayoutCat;
    @BindView(R.id.view_pager)
    WrapContentViewPager viewPager;

    @BindView(R.id.view_pagerCat)
    WrapContentViewPager viewPagerCat;


   /* @BindView(R.id.iv_image_at_top)
    ImageView ivImageAtTop;*/
  /* @BindView(R.id.viewflip)
   AdapterViewFlipper adapterViewFlipping;*/
    @BindView(R.id.iv_navigation_menu)
    ImageView ivNavigationSlideMenu;
    @BindView(R.id.toolbar)
    Toolbar customToolbar;
    @BindView(R.id.iv_upload)
    ImageView ivUpload;
  /*  @BindView(R.id.btn_view_all)
    CustomTypefaceButton btnViewAll;*/
    @BindView(R.id.iv_notification)
    ImageView ivNotification;
    @BindView(R.id.iv_cart)
    ImageView iv_cartIMG;
    /*@BindView(R.id.tv_distance_unit)
    TextView tvDistanceUnit;*/
    @BindView(R.id.rl_fitness_info)
    RelativeLayout rlFitnessInfo;
   /* @BindView(R.id.tv_points)
    TextView tvPoints;*/
   /* @BindView(R.id.tv_distance)
    CustomTypefaceTextView tvDistance;
    @BindView(R.id.tv_calories)
    CustomTypefaceTextView tvCalories;*/
   @BindView(R.id.et_search)
   EditText etSearch;
    @BindView(R.id.iv_cross_search)
    ImageView ivCrossSearch;
    @BindView(R.id.tv_title)
    CustomTypefaceTextView tvTitle;
    @BindView(R.id.tv_notification_count)
    CustomTypefaceTextView tvNotificationCount;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.ToDoList)
    NestedScrollView ToDoList;

    private int COUPONS_ACTIVITY = 3001;
    private int RECOMMENDED_ACTIVITY = 3002;
    private int TRENDING_ACTIVITY = 3003;
    private int width;
    private int height;
    private ProgressDialog mProgressDialog;
    private String mImagePath;
    private String mCurrentTab;
    private ResUserCalorieDistance mResGetFsPoints;
    private SearchRecyclerAdapter mRecommendedRecyclerAdapter;
    private List<ResAllProductsData> mResProductsCategoryData = new ArrayList<>();
    private int mPageindex = 1;
    @BindView(R.id.rv_Recommended)
    RecyclerView rvRecommended;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    LinearLayoutManager linearLayoutManager;
    TextView view_all_cat,view_all_latest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        return view;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getInt(AppConstants.TAG_FROM_WHERE, 0) == 1) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openShareActivity();
                    }
                }, 100);
            }
        }
    }

   /* @OnClick({R.id.rlMen,R.id.rlWomen*//*,R.id.rlAccessories*//*})
    public void OnClickView(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.rlMen:
                intent = new Intent(mActivity, FsStoreMenWomenActivity.class);
                intent.putExtra("FsStoreGender", AppConstants.GENDER_MALE);
                this.startActivity(intent);
                break;
            case R.id.rlWomen:
                intent = new Intent(mActivity, FsStoreMenWomenActivity.class);
                intent.putExtra("FsStoreGender", AppConstants.GENDER_FEMALE);
                startActivity(intent);
                break;
            *//*case R.id.rlAccessories:
                intent = new Intent(mActivity, FsStoreMenWomenActivity.class);
                intent.putExtra("FsStoreGender", AppConstants.ACCSESSORIES);
                startActivity(intent);
                break;*//*
        }
    }*/

    @Override
    protected void initViews() {

        view_all_cat = (TextView)getActivity().findViewById(R.id.view_all_cat);
        view_all_latest = (TextView)getActivity().findViewById(R.id.view_all_latest);
        view_all_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecommendedActivity.class);
                startActivity(intent);
            }
        });

        view_all_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrendingActivity.class);
                startActivity(intent);
            }
        });
        ivNavigationSlideMenu.setOnClickListener(this);
        /*((HomeActivity) mActivity).setVisibilityBottomLayout(true);*/ // testing command for bottom bar view hiding
        ((AppCompatActivity) getActivity()).setSupportActionBar(customToolbar);//TRENDING
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(mActivity, R.color.c_hint_et_login_sign_up), ContextCompat.getColor(mActivity, R.color.c_dark_grey));
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getFragmentManager());
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayoutCat.addTab(tabLayoutCat.newTab());//ALLCATPRODUCTS
        tabLayoutCat.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutCat.setTabTextColors(ContextCompat.getColor(mActivity, R.color.c_hint_et_login_sign_up), ContextCompat.getColor(mActivity, R.color.c_dark_grey));
        TabPagerAdapterAllCat tabPagerAdapterCat = new TabPagerAdapterAllCat(getFragmentManager());
        viewPagerCat.setAdapter(tabPagerAdapterCat);
        tabLayoutCat.setupWithViewPager(viewPagerCat);


       // adapterViewFlipping.getLayoutParams().height = (int) (height / (2.5));

       // btnViewAll.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        tabLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        tabLayoutCat.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int statusBarHeight = getStatusBarHeight();
        int topImageHeight = (int) (height / (2.5));
        int bottomTabsHeight = (int) getResources().getDimension(R.dimen.d_bottom_layout_height);
       // int viewAllHeight = btnViewAll.getMeasuredHeight();
        int tabLayoutHeight = tabLayout.getMeasuredHeight();
        int tabLayoutHeights = tabLayoutCat.getMeasuredHeight();
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        //int viewPagerHeight = screenHeight - (statusBarHeight + topImageHeight + bottomTabsHeight + viewAllHeight + tabLayoutHeight);
        LinearLayout.LayoutParams pagerParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
        LinearLayout.LayoutParams pagerParam = (LinearLayout.LayoutParams) viewPagerCat.getLayoutParams();
       // pagerParams.height = viewPagerHeight;
        mCurrentTab = (viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem())).toString();
        mCurrentTab = (viewPagerCat.getAdapter().getPageTitle(viewPagerCat.getCurrentItem())).toString();

       /* if (!mAppSharedPreference.getString(PreferenceKeys.KEY_HOME_IMAGE, "").isEmpty()) {
            Glide.with(mActivity)
                    .load(mAppSharedPreference.getString(PreferenceKeys.KEY_HOME_IMAGE, ""))
                    .centerCrop()
                    .placeholder(R.drawable.img_placeholder)
                    .into(ivImageAtTop);
        }*/
        mCurrentTab = (viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem())).toString();
        mCurrentTab = (viewPagerCat.getAdapter().getPageTitle(viewPagerCat.getCurrentItem())).toString();
        AppUtilMethods.overrideFonts(mActivity, tabLayout);
        AppUtilMethods.overrideFonts(mActivity, tabLayoutCat);

        //set listeners
        tabLayout.setOnTabSelectedListener(this);
        tabLayoutCat.setOnTabSelectedListener(this);
       // btnViewAll.setOnClickListener(this);
        ivUpload.setOnClickListener(this);
        tabLayout.setOnTabSelectedListener(this);
        tabLayoutCat.setOnTabSelectedListener(this);
        ivNotification.setOnClickListener(this);
        iv_cartIMG.setOnClickListener(this);
        tvNotificationCount.setOnClickListener(this);
        ivCrossSearch.setOnClickListener(this);
      //  btnViewAll.setOnClickListener(this);
        updateUserData();

        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvRecommended.addItemDecoration(new GridSpacingItemDecoration(1,30, true));
        rvRecommended.setLayoutManager(linearLayoutManager);
        mResProductsCategoryData = new ArrayList<ResAllProductsData>();
        mRecommendedRecyclerAdapter = new SearchRecyclerAdapter(mActivity, mResProductsCategoryData);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AppUtilMethods.hideKeyBoard(mActivity);
                    if (NetworkConnection.isNetworkConnected(mActivity)) {
                        getProducts(etSearch.getText().toString(), 0, 1, true);
                        rvRecommended.setVisibility(View.VISIBLE);
                    } else {
                        ToastUtils.showShortToast(mActivity, getResources().getString(R.string.err_network_connection));
                    }
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // mRecommendedRecyclerAdapter.getFilter().filter(s.toString());
                if (s.length() > 0) {
                    ivCrossSearch.setVisibility(View.VISIBLE);
                    if (NetworkConnection.isNetworkConnected(mActivity)) {
                        getProducts(etSearch.getText().toString(), 0, 1, true);
                        rvRecommended.setVisibility(View.VISIBLE);
                    } else {
                        ToastUtils.showShortToast(mActivity, getResources().getString(R.string.err_network_connection));
                        rvRecommended.setVisibility(View.GONE);
                    }
                } else {
                    ivCrossSearch.setVisibility(View.GONE);
                    rvRecommended.setVisibility(View.GONE);
                    mPageindex = 1;
                    if (NetworkConnection.isNetworkConnected(mActivity)) {
                        getProducts(etSearch.getText().toString(), 0, 1, true);
                    } else {
                        ToastUtils.showShortToast(mActivity, getResources().getString(R.string.err_network_connection));
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

    private void getProducts(final String searchTerm, int from, final int pageindex, final boolean isFromSearch) {
        if (from == 0) {
            AppDialog.showProgressDialog(mActivity, true);
        }
        IApiClient client = ApiClient.getApiClient();
        ReqProductsCategory reqProductsCategory = new ReqProductsCategory();
        reqProductsCategory.setPage(String.valueOf(pageindex));
        reqProductsCategory.setMethod(MethodFactory.GET_AllPRODUCT.getMethod());
        reqProductsCategory.setServiceKey(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
        reqProductsCategory.setUserID(mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, ""));
        reqProductsCategory.setSearch(searchTerm);
        Call<ResGetAllProductsDatas> resGetProductsCategoriesCall = client.getAllProductsData(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ""));
        resGetProductsCategoriesCall.enqueue(new Callback<ResGetAllProductsDatas>() {
            @Override
            public void onResponse(Call<ResGetAllProductsDatas> call, Response<ResGetAllProductsDatas> response) {
                AppDialog.showProgressDialog(mActivity, false);
                ResGetAllProductsDatas resGetProductsCategories = response.body();
                if (resGetProductsCategories != null) {
                    if (resGetProductsCategories.getSuccess() == ServiceConstants.SUCCESS) {
                        if (isFromSearch)
                            mResProductsCategoryData.clear();
                        mResProductsCategoryData.addAll(resGetProductsCategories.getAllProductData());
                        if (!TextUtils.isEmpty(searchTerm) || pageindex == 1)
                            setAdapter();
                        else
                            mRecommendedRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        if (isFromSearch) {
                            mResProductsCategoryData.clear();
                            mResProductsCategoryData.addAll(resGetProductsCategories.getAllProductData());
                            mRecommendedRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                   // ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
                }
            }

            @Override
            public void onFailure(Call<ResGetAllProductsDatas> call, Throwable t) {
                AppDialog.showProgressDialog(mActivity, false);
                ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        rvRecommended.setVisibility(View.GONE);
        etSearch.setText("");
    }
    private void setAdapter() {

        rvRecommended.setAdapter(mRecommendedRecyclerAdapter);
        /*ViewCompat.setNestedScrollingEnabled(rvRecommended, true);
        rvRecommended.setNestedScrollingEnabled(true);*/
        rvRecommended.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d("onloadmore", "Load More data, Current Page = " + current_page);
                mPageindex += 1;
                getProducts(etSearch.getText().toString(), 0, mPageindex, false);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void initVariables() {
        if (NetworkConnection.isNetworkConnected(mActivity))
            getFSPoints();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserData();
        rvRecommended.setVisibility(View.GONE);
       etSearch.setText("");
       // getBanners();
        //getAllCatPro();
    }

    /**
     * Updating user distance,fs-points and calories
     */
    public void updateUserData() {
       /* tvDistanceUnit.setText(mAppSharedPreference.getString(PreferenceKeys.KEY_DISTANCE_UNIT, ""));
        tvPoints.setText(mAppSharedPreference.getString(PreferenceKeys.KEY_POINTS, "0"));
        tvCalories.setText(mAppSharedPreference.getString(PreferenceKeys.KEY_CALORIES, "0"));
        tvDistance.setText(mAppSharedPreference.getString(PreferenceKeys.KEY_DISTANCE, "0.0"));*/
    }

    /**
     * Getting user distance,fs-points and calories from server
     */
    private void getFSPoints() {
        IApiClient client = ApiClient.getApiClient();
        ReqUserCalorieDistance reqUserCalorieDistance = new ReqUserCalorieDistance();
        reqUserCalorieDistance.setUserID(mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, ""));
        reqUserCalorieDistance.setServiceKey(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
        reqUserCalorieDistance.setMethod(MethodFactory.GET_USER_CALORIE_DISTANCE.getMethod());
        Call<ResUserCalorieDistance> resGetFsPoints = client.getUserCalorieDistance(reqUserCalorieDistance);
        if (mResGetFsPoints == null) {
            resGetFsPoints.enqueue(new Callback<ResUserCalorieDistance>() {
                @Override
                public void onResponse(Call<ResUserCalorieDistance> call, Response<ResUserCalorieDistance> response) {
                    mResGetFsPoints = response.body();
                    if (mResGetFsPoints != null) {
                        if (mResGetFsPoints.getSuccess() == ServiceConstants.SUCCESS) {
                            if (mResGetFsPoints.getTotalPoints().equals("")) {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_POINTS, "0");
                            } else {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_POINTS, mResGetFsPoints.getTotalPoints());
                            }
                            if (mResGetFsPoints.getCalories().equals("")) {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_CALORIES, "0");
                            } else {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_CALORIES, mResGetFsPoints.getCalories());
                            }
                            if (mResGetFsPoints.getDistance().equals("")) {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_DISTANCE, "0");
                            } else {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_DISTANCE, String.format(Locale.getDefault(), "%.1f", Float.parseFloat(mResGetFsPoints.getDistance())));
                            }
                            if (mResGetFsPoints.getNotificationCount().equals("")) {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_POINTS, "0");
                            } else {
                                if (!TextUtils.isEmpty(mResGetFsPoints.getNotificationCount()) && Integer.parseInt(mResGetFsPoints.getNotificationCount())>0){
                                    tvNotificationCount.setText(mResGetFsPoints.getNotificationCount());
                                    tvNotificationCount.setVisibility(View.VISIBLE);
                                }else{
                                    tvNotificationCount.setText("");
                                    tvNotificationCount.setVisibility(View.GONE);
                                }
                            }
                            if (mResGetFsPoints.getIsFitbitConnect().equals("1")) {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_CONNECTED_APP, AppConstants.FITBIT_CONNECTED);
                            }
                            if (mResGetFsPoints.getIsRunkeeperConnect().equals("1")) {
                                mAppSharedPreference.setString(PreferenceKeys.KEY_CONNECTED_APP, AppConstants.RUNKEEPER_CONNECTED);
                            }
                            mAppSharedPreference.setString(PreferenceKeys.KEY_IS_EMAIL_VERIFIED, mResGetFsPoints.getEmailStatus());
                        }
                    }
                    updateUserData();
                }

                @Override
                public void onFailure(Call<ResUserCalorieDistance> call, Throwable t) {
                    AppDialog.showProgressDialog(mActivity, false);
                    ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction;
        switch (v.getId()) {
            case R.id.iv_navigation_menu:
                ((HomeActivity) mActivity).showDrawer();
                break;
           /* case R.id.iv_fs_store_bottom:  // testing command for bottom bar view hiding
                if (mAppSharedPreference.getBoolean(PreferenceKeys.KEY_LOGGED_IN, false)) {
                    if (mAppSharedPreference.getString(PreferenceKeys.KEY_IS_EMAIL_VERIFIED, "0").equalsIgnoreCase("1")) {

                        //NavigationToolbarFavourite.setVisibility(View.VISIBLE);
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        ChallengesFragment challengesFragment = new ChallengesFragment();
                        //mCurrentFragment = favouriteFragment;
                        fragmentTransaction.replace(R.id.container_body, challengesFragment);
                        fragmentTransaction.commit();
                        ((HomeActivity) getActivity()).mCurrentFragment = challengesFragment;
                        //Toolbar.setVisibility(View.VISIBLE);
                    } else {
                        AppDialog.showVerifyEmailAlertDialog(mActivity);
                    }
                } else {
                    AppDialog.showLoginAlertDialog(mActivity);
                }
                break;*/
            case R.id.iv_notification:
                if (mAppSharedPreference.getBoolean(PreferenceKeys.KEY_LOGGED_IN, false)) {
                    Intent intent;
                    if (mAppSharedPreference.getString(PreferenceKeys.KEY_IS_EMAIL_VERIFIED, "0").equalsIgnoreCase("1")) {
                        intent = new Intent(mActivity, NotificationActivity.class);
                        startActivity(intent);
                    } else {
                        AppDialog.showVerifyEmailAlertDialog(mActivity);
                    }
                } else {
                    AppDialog.showLoginAlertDialog(mActivity);
                }
                break;

                case R.id.iv_cart:
                if (mAppSharedPreference.getBoolean(PreferenceKeys.KEY_LOGGED_IN, false)) {
                    Intent intent;
                    if (mAppSharedPreference.getString(PreferenceKeys.KEY_IS_EMAIL_VERIFIED, "0").equalsIgnoreCase("1")) {
                        /*intent = new Intent(mActivity, MyCartFragmentNew.class);
                        startActivity(intent);*/
                        ((HomeActivity) mActivity).showMyCart();
                        ((HomeActivity) mActivity).adapterflip();
                    } else {
                        AppDialog.showVerifyEmailAlertDialog(mActivity);
                    }
                } else {
                    AppDialog.showLoginAlertDialog(mActivity);
                }
                break;
           /* case R.id.tv_notification_count:
                if (mAppSharedPreference.getBoolean(PreferenceKeys.KEY_LOGGED_IN, false)) {
                    Intent intent;
                    if (mAppSharedPreference.getString(PreferenceKeys.KEY_IS_EMAIL_VERIFIED, "0").equalsIgnoreCase("1")) {
                        intent = new Intent(mActivity, NotificationActivity.class);
                        startActivity(intent);
                    } else {
                        AppDialog.showVerifyEmailAlertDialog(mActivity);
                    }
                } else {
                    AppDialog.showLoginAlertDialog(mActivity);
                }
                break;*/
            case R.id.iv_upload:
                if (mAppSharedPreference.getBoolean(PreferenceKeys.KEY_LOGGED_IN, false)) {
                    if (mAppSharedPreference.getString(PreferenceKeys.KEY_IS_EMAIL_VERIFIED, "0").equalsIgnoreCase("1")) {
                        ((HomeActivity) mActivity).setmFromWhere(AppConstants.FROM_RESET_IMAGE);
                        if (PermissionUtils.hasStoragePermission(mActivity))
                            new ImageChooser(mActivity, this).openMediaSelector();
                    } else {
                        AppDialog.showVerifyEmailAlertDialog(mActivity);
                    }
                } else {
                    AppDialog.showLoginAlertDialog(mActivity);
                }
                break;
            /*case R.id.btn_view_all:
                if (mAppSharedPreference.getBoolean(PreferenceKeys.KEY_LOGGED_IN, false)) {
                    if (mAppSharedPreference.getString(PreferenceKeys.KEY_IS_EMAIL_VERIFIED, "0").equalsIgnoreCase("1")) {
                        if (mCurrentTab.equals(getString(R.string.txt_tabs_title_coupons))) {
                            Intent intent1 = new Intent(getActivity(), CouponsActivity.class);
                            startActivityForResult(intent1, COUPONS_ACTIVITY);
                            break;
                        } else if (mCurrentTab.equals(getString(R.string.txt_tabs_title_recommended))) {
                            Intent intent1 = new Intent(getActivity(), RecommendedActivity.class);
                            startActivityForResult(intent1, RECOMMENDED_ACTIVITY);
                            break;
                        } else if (mCurrentTab.equals(getString(R.string.txt_tabs_title_trending))) {
                            Intent intent1 = new Intent(getActivity(), TrendingActivity.class);
                            startActivityForResult(intent1, TRENDING_ACTIVITY);
                            break;
                        }
                    } else {
                        AppDialog.showVerifyEmailAlertDialog(mActivity);
                    }
                } else {
                    AppDialog.showLoginAlertDialog(mActivity);
                }
                break;*/
            case R.id.iv_cross_search:
                etSearch.setText("");
                ivCrossSearch.setVisibility(View.GONE);
                rvRecommended.setVisibility(View.GONE);
                AppUtilMethods.hideKeyBoard(mActivity);
               // mPageindex = 1;
               /* if (NetworkConnection.isNetworkConnected(mActivity)) {
                    getProducts(etSearch.getText().toString(), 0, 1, true);
                } else {
                    ToastUtils.showShortToast(mActivity, getResources().getString(R.string.err_network_connection));
                }
                break;*/

        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        mCurrentTab = (viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem())).toString();

        viewPagerCat.setCurrentItem(tab.getPosition());
        mCurrentTab = (viewPagerCat.getAdapter().getPageTitle(viewPagerCat.getCurrentItem())).toString();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    /**
     * This method is used to open the camera
     *
     * @param intent
     * @return void
     */
    public void getImageFromCamera(Intent intent) {
        CameraAndGalleryUtils.getImageFromCamera(intent, mActivity);
    }

    /**
     * This method is used to open the gallery
     *
     * @param intent
     * @return void
     */
    public void getImageFromGallery(Intent intent) {
        CameraAndGalleryUtils.getImageFromGallery(intent, mActivity);
    }

    /**
     * Method used to reset home image
     */
    public void resetImage() {
        if (NetworkConnection.isNetworkConnected(mActivity)) {
          //  resetHomeScreenImage();
        } else {
            ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
        }
    }

    /**
     * This method is used to set the profile image
     *
     * @return void
     */
  /*  public void setProfileImage(String imagePath) {
        mImagePath = imagePath;
        if (NetworkConnection.isNetworkConnected(mActivity)) {
            Glide.with(mActivity)
                    .load(mImagePath)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.img_placeholder)
                    .into(ivImageAtTop);
            updateHomeScreenImage();
        } else {
            ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
        }
    }*/

    /**
     * Method used to choose image
     */
    public void selectImage() {
        new ImageChooser(mActivity, this).openMediaSelector();
    }

    /**
     * Method used to open share activity to share image on Facebook
     */
    public void openShareActivity() {
        rlFitnessInfo.setDrawingCacheEnabled(true);
        rlFitnessInfo.buildDrawingCache(true);
        rlFitnessInfo.setDrawingCacheQuality(RelativeLayout.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(rlFitnessInfo.getDrawingCache());

        if (bitmap != null) {
            //String imagePath = AppConstants.FITSTREET_DIRECTORY_PATH + AppConstants.FITSTREET_FITNESS_IMAGE_PATH;
            //CameraAndGalleryUtils.saveImageToFile(bitmap, imagePath);
            Intent intent = new Intent(mActivity, ShareActivity.class);
            //intent.putExtra(AppConstants.TAG_IMAGE_PATH, imagePath);
            //intent.putExtra(AppConstants.TAG_HEIGHT, (int) (height / (2.5)));
            startActivity(intent);
        }
        rlFitnessInfo.setDrawingCacheEnabled(false);
    }

    /**
     * This method is used to show the progress dialog
     *
     * @return void
     */
    private void showProgressDialog() {
        mProgressDialog = AppDialog.showProgressDialog(getActivity());
        mProgressDialog.show();
    }

    /**
     * This method is used to hide the progress dialog
     *
     * @return void
     */
    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Method used to call the update profile web service
     */
    /*private void updateHomeScreenImage() {
        showProgressDialog();
        IApiClient client = ApiClient.getApiClient();
        File imageFile = new File(mImagePath);
        Call<ResUpdateProfile> resUpdateProfileCall = client.updateSettings(RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), MethodFactory.UPDATE_SETTINGS.getMethod()),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY)),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, "")),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), ""),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), ""),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), ""),
                MultipartBody.Part.createFormData("homeScreen", imageFile.getName(), RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), imageFile)));
        resUpdateProfileCall.enqueue(new Callback<ResUpdateProfile>() {
            @Override
            public void onResponse(Call<ResUpdateProfile> call, Response<ResUpdateProfile> response) {
                dismissProgressDialog();
                ResUpdateProfile resUpdateProfile = response.body();
                if (resUpdateProfile != null) {
                    if (resUpdateProfile.getSuccess() == ServiceConstants.SUCCESS) {
                        mAppSharedPreference.setString(PreferenceKeys.KEY_HOME_IMAGE, resUpdateProfile.getImgUrl());
                        if (!mAppSharedPreference.getString(PreferenceKeys.KEY_HOME_IMAGE, "").isEmpty()) {
                            Glide.with(mActivity)
                                    .load(mAppSharedPreference.getString(PreferenceKeys.KEY_HOME_IMAGE, ""))
                                    .centerCrop()
                                    .placeholder(R.drawable.img_placeholder)
                                    .into(ivImageAtTop);
                        }
                    } else {
                        ToastUtils.showShortToast(mActivity, resUpdateProfile.getErrstr());
                    }
                } else {
                    ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
                }
            }

            @Override
            public void onFailure(Call<ResUpdateProfile> call, Throwable t) {
                dismissProgressDialog();
                ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
            }
        });
    }*/

    /**
     * Method used to call the reset home screen image service
     */
   /* private void resetHomeScreenImage() {
        showProgressDialog();
        IApiClient client = ApiClient.getApiClient();
        Call<ResUpdateProfile> resUpdateProfileCall = client.updateSettingsWithoutImage(RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), MethodFactory.UPDATE_SETTINGS.getMethod()),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY)),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, "")),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), ""),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), ""),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), "1"),
                RequestBody.create(MediaType.parse(ServiceConstants.TYPE_MULTIPART), ""));
        resUpdateProfileCall.enqueue(new Callback<ResUpdateProfile>() {
            @Override
            public void onResponse(Call<ResUpdateProfile> call, Response<ResUpdateProfile> response) {
                dismissProgressDialog();
                ResUpdateProfile resUpdateProfile = response.body();
                if (resUpdateProfile != null) {
                    if (resUpdateProfile.getSuccess() == ServiceConstants.SUCCESS) {
                        mAppSharedPreference.setString(PreferenceKeys.KEY_HOME_IMAGE, resUpdateProfile.getImgUrl());
                        if (!mAppSharedPreference.getString(PreferenceKeys.KEY_HOME_IMAGE, "").isEmpty()) {
                            Glide.with(mActivity)
                                    .load(mAppSharedPreference.getString(PreferenceKeys.KEY_HOME_IMAGE, ""))
                                    .centerCrop()
                                    .placeholder(R.drawable.img_placeholder)
                                    *//*.into(ivImageAtTop)*//*;
                        }
                    } else {
                        ToastUtils.showShortToast(mActivity, resUpdateProfile.getErrstr());
                    }
                } else {
                    ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
                }
            }

            @Override
            public void onFailure(Call<ResUpdateProfile> call, Throwable t) {
                dismissProgressDialog();
                ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
            }
        });
    }*/

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
           // searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    Toast.makeText(mActivity, "sfsfsfsf", Toast.LENGTH_SHORT).show();
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
*/

}
