package affinity.com.srisabaries.utils;

import android.app.Activity;
import android.app.ProgressDialog;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.models.request.ReqShareAndInvite;
import affinity.com.srisabaries.models.response.ResShareAndInvite;
import affinity.com.srisabaries.network.ApiClient;
import affinity.com.srisabaries.network.IApiClient;
import affinity.com.srisabaries.network.MethodFactory;
import affinity.com.srisabaries.network.ServiceConstants;
import affinity.com.srisabaries.preference.AppSharedPreference;
import affinity.com.srisabaries.preference.PreferenceKeys;
import affinity.com.srisabaries.ui.activities.LoginActivity;
import affinity.com.srisabaries.ui.activities.ShareActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 7/21/16.
 */
public class ServiceUtils {

	/**
	 * This method is used to show the progress dialog
	 *
	 * @return void
	 */
	private static void showProgressDialog(ProgressDialog dialog) {
		if (dialog != null) {
			dialog.show();
		}
	}

	/**
	 * This method is used to hide the progress dialog
	 *
	 * @return void
	 */
	private static void dismissProgressDialog(ProgressDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * Method used to call the get user addresses web service
	 */
	public static void shareAndInvite(final Activity activity, final ProgressDialog dialog, final AppSharedPreference preference, String type, String shareUserId) {
		if (shareUserId == null) {
			shareUserId = preference.getString(PreferenceKeys.KEY_USER_ID, "");
		}
		AppDialog.showProgressDialog(activity, true);
		IApiClient client = ApiClient.getApiClient();
		final ReqShareAndInvite reqShareAndInvite = new ReqShareAndInvite();
		reqShareAndInvite.setServiceKey(preference.getString(PreferenceKeys.KEY_SERVICE_KEY, ServiceConstants.SERVICE_KEY));
		reqShareAndInvite.setMethod(MethodFactory.SHARE_AND_INVITE.getMethod());
		reqShareAndInvite.setUserID(shareUserId);
		reqShareAndInvite.setType(type);
		Call<ResShareAndInvite> resShareAndInviteCall = client.shareAndInvite(reqShareAndInvite);
		resShareAndInviteCall.enqueue(new Callback<ResShareAndInvite>() {
			@Override
			public void onResponse(Call<ResShareAndInvite> call, Response<ResShareAndInvite> response) {
				if (dialog != null) {
					AppDialog.showProgressDialog(activity, false);
					ResShareAndInvite resShareAndInvite = response.body();
					if (resShareAndInvite != null) {
						if (resShareAndInvite.getSuccess() == ServiceConstants.SUCCESS) {
							if (activity instanceof LoginActivity)
								ToastUtils.showShortToast(activity, resShareAndInvite.getErrstr());
							preference.setString(PreferenceKeys.KEY_POINTS, resShareAndInvite.getTotalPoints());
							if (activity instanceof ShareActivity)
								activity.finish();
						} else {
							ToastUtils.showShortToast(activity, resShareAndInvite.getErrstr());
						}
					} else {
						ToastUtils.showShortToast(activity, R.string.err_network_connection);
					}
				} else {
					ResShareAndInvite resShareAndInvite = response.body();
					if (resShareAndInvite != null) {
						if (resShareAndInvite.getSuccess() == ServiceConstants.SUCCESS) {
							preference.setString(PreferenceKeys.KEY_POINTS, resShareAndInvite.getTotalPoints());
						}
					}
				}
			}

			@Override
			public void onFailure(Call<ResShareAndInvite> call, Throwable t) {
				AppDialog.showProgressDialog(activity, false);
				ToastUtils.showShortToast(activity, R.string.err_network_connection);
			}
		});
	}
}
