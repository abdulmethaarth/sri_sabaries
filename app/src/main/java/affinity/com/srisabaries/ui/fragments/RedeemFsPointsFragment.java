package affinity.com.srisabaries.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.models.request.ReqFsPoints;
import affinity.com.srisabaries.models.response.ResGetFsPoints;
import affinity.com.srisabaries.network.ApiClient;
import affinity.com.srisabaries.network.IApiClient;
import affinity.com.srisabaries.network.MethodFactory;
import affinity.com.srisabaries.network.NetworkConnection;
import affinity.com.srisabaries.network.ServiceConstants;
import affinity.com.srisabaries.preference.PreferenceKeys;
import affinity.com.srisabaries.ui.activities.HomeActivity;
import affinity.com.srisabaries.utils.AppDialog;
import affinity.com.srisabaries.utils.ToastUtils;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akash on 26/8/16.
 */
public class RedeemFsPointsFragment extends BaseFragment {
    @BindView(R.id.tv_option_one_text)
    TextView tvOptionOneText;
    Boolean expandOptionOne = false;
    Boolean expandOptionTwo = false;
    Boolean expandOptionThree = false;
    @BindView(R.id.tv_option_one)
    TextView tvOptionOne;
    @BindView(R.id.tv_option_two)
    TextView tvOptionTwo;
    @BindView(R.id.ll_option_one)
    LinearLayout llOptionOne;
    @BindView(R.id.ll_option_two)
    LinearLayout llOptionTwo;
    @BindView(R.id.tv_current_fs_points_balance)
    affinity.com.srisabaries.customviews.CustomTypefaceTextView tv_current_fs_points;
    @BindView(R.id.btn_start_redeeming)
    affinity.com.srisabaries.customviews.CustomTypefaceButton btnStartRedeeming;
    ResGetFsPoints mResGetFsPoints;

    @Override
    protected void initViews() {
        ((HomeActivity) mActivity).setActionBarTitle(R.string.title_redeem_fs_points);
    }

    @Override
    protected void initVariables() {
        SpannableString spannableString = new SpannableString(mActivity.getString(R.string.txt_visit_fs_store));
        Drawable d = getResources().getDrawable(R.drawable.fs_bag_black);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        spannableString.setSpan(span, spannableString.toString().indexOf("@"), spannableString.toString().indexOf("@") + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvOptionOneText.setText(spannableString);
        tvOptionOne.setOnClickListener(this);
        tvOptionTwo.setOnClickListener(this);
        //tvOptionThree.setOnClickListener(this);
        llOptionOne.setOnClickListener(this);
        llOptionTwo.setOnClickListener(this);
        // llOptionThree.setOnClickListener(this);
        btnStartRedeeming.setOnClickListener(this);
        llOptionOne.setVisibility(View.GONE);
        llOptionTwo.setVisibility(View.GONE);
        //llOptionThree.setVisibility(View.GONE);
        if (NetworkConnection.isNetworkConnected(mActivity)) {
            getFSPoints();
        } else {
            ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
        }

    }

    private void getFSPoints() {
        AppDialog.showProgressDialog(mActivity, true);
        IApiClient client = ApiClient.getApiClient();
        ReqFsPoints reqFsPoints = new ReqFsPoints();
        reqFsPoints.setUserID(mAppSharedPreference.getString(PreferenceKeys.KEY_USER_ID, ""));
        reqFsPoints.setServiceKey(mAppSharedPreference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
        reqFsPoints.setMethod(MethodFactory.GET_FS_STORE_POINTS.getMethod());
        Call<ResGetFsPoints> resGetFsPoints = client.getFsStorePoints(reqFsPoints);
        resGetFsPoints.enqueue(new Callback<ResGetFsPoints>() {
            @Override
            public void onResponse(Call<ResGetFsPoints> call, Response<ResGetFsPoints> response) {
                AppDialog.showProgressDialog(mActivity, false);
                mResGetFsPoints = response.body();
                if (mResGetFsPoints != null) {
                    if (mResGetFsPoints.getSuccess() == ServiceConstants.SUCCESS) {
                        if (mResGetFsPoints.getTotalPoints().equals("")) {
                            tv_current_fs_points.setText("0");
                        } else {
                            tv_current_fs_points.setText(mResGetFsPoints.getTotalPoints());
                        }
                    } else {
                        tv_current_fs_points.setText("0");
                        ToastUtils.showShortToast(mActivity, mResGetFsPoints.getErrstr());
                    }
                } else {
                    ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
                }
            }

            @Override
            public void onFailure(Call<ResGetFsPoints> call, Throwable t) {
                AppDialog.showProgressDialog(mActivity, false);
                ToastUtils.showShortToast(mActivity, R.string.err_network_connection);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_option_one:
                if (expandOptionOne) {
                    llOptionOne.setVisibility(View.GONE);
                    expandOptionOne = false;
                    expandOptionTwo = false;
                    expandOptionThree = false;
                } else {
                    llOptionOne.setVisibility(View.VISIBLE);
                    llOptionTwo.setVisibility(View.GONE);
                    //llOptionThree.setVisibility(View.GONE);
                    expandOptionOne = true;
                    expandOptionTwo = false;
                    expandOptionThree = false;
                }

                break;
            case R.id.tv_option_two:
                if (expandOptionTwo) {
                    llOptionTwo.setVisibility(View.GONE);
                    expandOptionTwo = false;
                    expandOptionOne = false;
                    expandOptionThree = false;

                } else {
                    llOptionOne.setVisibility(View.GONE);
                    llOptionTwo.setVisibility(View.VISIBLE);
                    //llOptionThree.setVisibility(View.GONE);
                    expandOptionTwo = true;
                    expandOptionThree = false;
                    expandOptionOne = false;
                }

                break;
            case R.id.btn_start_redeeming:
              ((HomeActivity) mActivity).startHomeFragment();
               /* ((HomeActivity) mActivity).setVisibilityBottomLayout(true);*/  // testing command for bottom bar view hiding
                break;

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_redeem_fs_points, container, false);

    }
}
