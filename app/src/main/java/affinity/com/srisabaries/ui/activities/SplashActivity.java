package affinity.com.srisabaries.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.network.ApiClient;
import affinity.com.srisabaries.network.IApiClient;
import affinity.com.srisabaries.network.ResGetToken;
import affinity.com.srisabaries.preference.PreferenceKeys;
import affinity.com.srisabaries.utils.AppUtilMethods;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    private static final long DURATION_SPLASH = 1500;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        super.initData();
        AppUtilMethods.generateFBKeyHash(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAppSharedPreference.getBoolean(PreferenceKeys.KEY_LOGGED_IN, false)) {
                    IApiClient client = ApiClient.getApiClient();
                    Call<ResGetToken> call = client.getAccesToken("Basic aW1fY2xpZW5ldF9zcmk6aW1fc2VjcmV0X3NyaQ==");
                    call.enqueue(new Callback<ResGetToken>() {
                        @Override
                        public void onResponse(Call<ResGetToken> call, Response<ResGetToken> response) {
                            ResGetToken users = response.body();

                            if (response.isSuccessful()) {
                                ResGetToken.LoginTokenDetails userData = users.getLoginTokenDetails();
                                String token = userData.getAccess_token();
                                String type = userData.getToken_type();

                                mAppSharedPreference.setString(PreferenceKeys.KEY_SERVICE_KEY, type+" "+token);
                                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                                finish();
                                //TokenWithSignUp();

                            } else {
                            }
                        }
                        @Override
                        public void onFailure(Call<ResGetToken> call, Throwable t) {
                            Toast.makeText(SplashActivity.this, "Check your internet connection...", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
                    finish();
                }
            }
        }, DURATION_SPLASH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initVariables() {
        mHandler = new Handler();
    }

    @Override
    public void onClick(View view) {
    }
}
