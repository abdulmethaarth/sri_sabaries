package affinity.com.srisabaries;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.stetho.Stetho;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
/**
 * Created by root on 6/9/16.
 */


@ReportsCrashes(formUri = "",
		mailTo = "abdulmethaarth@gmail.com")

public class CartApplication extends Application {
	private static final String APP_NAMESPACE = "affinity.com.srisabaries";
	private static final String APP_SECRET = "2b43355c3d09e26cb51870b73fcf67bf";
	private static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		setUPFacebook();
		ACRA.init(this);

		Stetho.initializeWithDefaults(this);

		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
	}
	public static Context getGlobalContext() {
		return context;
	}
	public void setUPFacebook() {
		// initialize facebook configuration
		Permission[] permissions = new Permission[]{
				Permission.PUBLIC_PROFILE,
				Permission.USER_PHOTOS,
				Permission.USER_BIRTHDAY,
				Permission.PUBLISH_ACTION};
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder().
				setAppId(getString(R.string.app_id)).
				setAppSecret(APP_SECRET).
				setNamespace(APP_NAMESPACE).setPermissions(permissions).
				setDefaultAudience(DefaultAudience.FRIENDS).
				setAskForAllPermissionsAtOnce(false).
				setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK).build();

		SimpleFacebook.setConfiguration(configuration);
	}
}
