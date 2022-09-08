package affinity.com.srisabaries.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Window;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Photo;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.listeners.OnPublishListener;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.interfaces.IOnShareResult;
import affinity.com.srisabaries.interfaces.IOnSocialDataFetched;
import affinity.com.srisabaries.models.SocialModel;
import affinity.com.srisabaries.network.ServiceConstants;

/**
 * Created by root on 6/14/16.
 * This class is a utility class for all social media used
 */
public class SocialNetworkUtils {
	private static SocialNetworkUtils sInstance;
	private Activity mActivity;
	private SimpleFacebook mSimpleFacebook;
	private ProgressDialog mProgressDialog;
	private int mFromWhere;
	private String mImagePath;
	private Bitmap mBitmap;

	private SocialNetworkUtils(Activity activity) {
		mActivity = activity;
		mFromWhere = -1;
	}

	public static synchronized SocialNetworkUtils getInstance(Activity activity) {
		if (sInstance == null) {
			sInstance = new SocialNetworkUtils(activity);
		}
		return sInstance;
	}

	public static CallbackManager callbackManager = null;
	public static boolean fbLoginManagerCalled = false;

	/**
	 * This method is used to log in the user with fb
	 *
	 * @param activity
	 * @param simpleFacebook
	 * @return void
	 */
	public void loginWithFb(Activity activity, SimpleFacebook simpleFacebook) {
		mActivity = activity;
		mFromWhere = AppConstants.FROM_LOGIN;
		mSimpleFacebook = simpleFacebook;
		// if (!mSimpleFacebook.isLogin()) {

		ArrayList<String> perms = new ArrayList<>();
		perms.add("public_profile");
		perms.add("email");
		LoginManager manager = LoginManager.getInstance();

		if (AccessToken.getCurrentAccessToken() != null) {
			manager.logOut();
		}

		if (callbackManager == null) {
			callbackManager = CallbackManager.Factory.create();
		}
		fbLoginManagerCalled = true;
		manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				getFbProfile(mActivity, mSimpleFacebook);
				fbLoginManagerCalled = false;
			}

			@Override
			public void onCancel() {
				dismissProgressDialog();
				fbLoginManagerCalled = false;
			}

			@Override
			public void onError(FacebookException error) {
				dismissProgressDialog();
				error.printStackTrace();
				fbLoginManagerCalled = false;
			}
		});
		manager.logInWithReadPermissions(activity, perms);
		// }
	}

	/**
	 * This method is used to log out the user from fb
	 *
	 * @param activity
	 * @param simpleFacebook
	 * @return void
	 */
	public void logoutFromFb(Activity activity, SimpleFacebook simpleFacebook) {
		mActivity = activity;
		mSimpleFacebook = simpleFacebook;
		if (mSimpleFacebook.isLogin()) {
			mSimpleFacebook.logout(onLogoutListener);
		}
	}

	/**
	 * This method is used to share data on fb
	 *
	 * @param activity
	 * @param simpleFacebook
	 * @param imagePath
	 * @return void
	 */
	public void shareImageOnFacebook(Activity activity, SimpleFacebook simpleFacebook, String imagePath, Bitmap bitmap) {
		mFromWhere = AppConstants.FROM_SHARE_IMAGE;
		mActivity = activity;
		mSimpleFacebook = simpleFacebook;
		mImagePath = imagePath;
		mBitmap = bitmap;
		if (mSimpleFacebook.isLogin()) {
			Photo.Builder builder = new Photo.Builder();
			builder.setName("WCART");
			if (imagePath != null)
				builder.setImage(BitmapFactory.decodeFile(imagePath));
			else
				builder.setImage(mBitmap);

			Photo photo = builder.build();
			simpleFacebook.publish(photo, true, onPublishListener);

		} else {
			mSimpleFacebook.login(onLoginListener);
		}
	}


	public void shareImageOnFacebook(Activity activity, SimpleFacebook simpleFacebook, Bitmap bitmap) {
		mFromWhere = AppConstants.FROM_SHARE_IMAGE;
		mActivity = activity;
		mSimpleFacebook = simpleFacebook;
		mBitmap = bitmap;
		if (mSimpleFacebook.isLogin()) {
			Photo.Builder builder = new Photo.Builder();
			builder.setName(mActivity.getString(R.string.app_name));
			builder.setImage(bitmap);
			Photo photo = builder.build();
			simpleFacebook.publish(photo, true, onPublishListener);

		} else {
			mSimpleFacebook.login(onLoginListener);
		}
	}

	public void shareTextOnFacebook(Activity activity, SimpleFacebook simpleFacebook, String text, Bitmap bitmap) {
		mFromWhere = AppConstants.FROM_SHARE_IMAGE;
		mActivity = activity;
		mSimpleFacebook = simpleFacebook;
		mBitmap = bitmap;
		//    com.facebook.GraphRequest graphRequest = GraphRequest.newUploadPhotoRequest(mSimpleFacebook.getAccessToken()
		if (mSimpleFacebook.isLogin()) {
//            ShareOpenGraphContent shareOpenGraphContent = new ShareOpenGraphContent.Builder()
			SharePhoto sharePhoto = new SharePhoto.Builder()
					.setBitmap(mBitmap)
					.setCaption(text)
					.setUserGenerated(true)
					.build();


			SharePhotoContent content = new SharePhotoContent.Builder()
					.addPhoto(sharePhoto)
					.build();
//
			ShareDialog shareDialog = new ShareDialog(activity);
			if (ShareDialog.canShow(SharePhotoContent.class)) {
//                SharePhotoContent content = new SharePhotoContent.Builder()
//                        .setPhotos(photos)
//                        .build();

				shareDialog.show(content);
			}

//            Photo.Builder builder = new Photo.Builder();
//            builder.setName(mActivity.getString(R.string.app_name));
//            builder.setImage(BitmapFactory.decodeFile(imagePath));
//            Photo photo = builder.build();
//            simpleFacebook.publish(photo, true, onPublishListener);

//            Feed feed = new Feed.Builder()
//                    .setMessage("Clone it out...")
//                    .setName("Simple Facebook for Android")
//                    .setCaption("Code less, do the same.")
//                    .setDescription("The Simple Facebook library project makes the life much easier by coding less code for being able to login, publish feeds and open graph stories, invite friends and more.")
//                    .setPicture("https://raw.github.com/sromku/android-simple-facebook/master/Refs/android_facebook_sdk_logo.png")
//                    .setLink("https://github.com/sromku/android-simple-facebook")
//                    .build();
//            mSimpleFacebook.publish(feed, true, onPublishListener);

		} else {
			mSimpleFacebook.login(onLoginListener);
		}
	}

	/**
	 * This method is used to share data on fb
	 *
	 * @param activity
	 * @param simpleFacebook
	 * @return void
	 */
	public void getFbProfile(Activity activity, SimpleFacebook simpleFacebook) {
		Properties.Builder pBuilder = new Properties.Builder();
		pBuilder.add(Properties.NAME);
		pBuilder.add(Properties.FIRST_NAME);
		pBuilder.add(Properties.LAST_NAME);
		pBuilder.add(Properties.EMAIL);
		pBuilder.add(Properties.GENDER);
		pBuilder.add(Properties.BIRTHDAY);
		pBuilder.add(Properties.ID);
		PictureAttributes pictureAttributes = PictureAttributes.createPictureAttributes();
		pictureAttributes.setHeight((int) mActivity.getResources().getDimension(R.dimen.d_hw_profile_pic));
		pictureAttributes.setWidth((int) mActivity.getResources().getDimension(R.dimen.d_hw_profile_pic));
		pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
		pBuilder.add(Properties.PICTURE, pictureAttributes);
		mSimpleFacebook.getProfile(pBuilder.build(), onProfileListener);
	}

	/**
	 * This method is used to show the progress dialog
	 *
	 * @return void
	 */
	private void showProgressDialog() {
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mProgressDialog.setMessage(mActivity.getResources().getString(R.string.dialog_please_wait));
		mProgressDialog.setCancelable(false);
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

	private OnLogoutListener onLogoutListener = new OnLogoutListener() {

		@Override
		public void onLogout() {
			Logger.e("You are logged out from fb");
		}

	};

	private OnLoginListener onLoginListener = new OnLoginListener() {
		@Override
		public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
			switch (mFromWhere) {
				case AppConstants.FROM_LOGIN:
					getFbProfile(mActivity, mSimpleFacebook);
					break;

				case AppConstants.FROM_SHARE_IMAGE:
					shareImageOnFacebook(mActivity, mSimpleFacebook, mImagePath, mBitmap);
					break;

				default:
					break;
			}
			dismissProgressDialog();
		}

		@Override
		public void onCancel() {
			dismissProgressDialog();
		}

		@Override
		public void onException(Throwable throwable) {
			dismissProgressDialog();
		}

		@Override
		public void onFail(String reason) {
			dismissProgressDialog();
		}
	};

	private OnProfileListener onProfileListener = new OnProfileListener() {
		public void onComplete(Profile profile) {
			dismissProgressDialog();
			if (mActivity instanceof IOnSocialDataFetched) {
				final SocialModel socialModel = new SocialModel();
				if (profile.getId() != null && !profile.getId().equalsIgnoreCase("null")) {
					socialModel.setId(profile.getId());
				}
				if (profile.getName() != null && !profile.getName().equalsIgnoreCase("null")) {
					socialModel.setName(profile.getName());
				} else if (profile.getFirstName() != null && !profile.getFirstName().equalsIgnoreCase("null")) {
					socialModel.setName(profile.getFirstName());
				} else if (profile.getLastName() != null && !profile.getLastName().equalsIgnoreCase("null")) {
					socialModel.setName(profile.getLastName());
				} else {
					socialModel.setName("");
				}
				if (profile.getGender() != null && !profile.getGender().equalsIgnoreCase("null")) {
					if (profile.getGender().equalsIgnoreCase("female")) {
						socialModel.setGender(ServiceConstants.GENDER_FEMALE);
					} else if (profile.getGender().equalsIgnoreCase("male")) {
						socialModel.setGender(ServiceConstants.GENDER_MALE);
					} else if (profile.getGender().equalsIgnoreCase("others")) {
						socialModel.setGender(ServiceConstants.GENDER_OTHERS);
					}
				}
				if (profile.getBirthday() != null && !profile.getBirthday().equalsIgnoreCase("null")) {
					try {
						SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
						SimpleDateFormat reqFormat = new SimpleDateFormat("yyyy-MM-dd");
						String dob = reqFormat.format(format.parse(profile.getBirthday()));
						socialModel.setDob(dob);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					socialModel.setDob("");
				}
				if (profile.getEmail() != null && !profile.getEmail().equalsIgnoreCase("null")) {

					socialModel.setEmail(profile.getEmail());
				} else {
					socialModel.setEmail("");
				}
				if (profile.getPicture() != null && !profile.getPicture().equalsIgnoreCase("null")) {
					socialModel.setPicture(profile.getPicture());
				} else {
					socialModel.setPicture("");
				}

				((IOnSocialDataFetched) mActivity).onSocialDataFetched(socialModel);
			}
		}

		public void onException(Throwable throwable) {
			dismissProgressDialog();
		}

		public void onFail(String reason) {
			dismissProgressDialog();
		}

		public void onThinking() {
			showProgressDialog();
		}
	};

	private OnPublishListener onPublishListener = new OnPublishListener() {
		@Override
		public void onComplete(String response) {
			dismissProgressDialog();
			if (mActivity instanceof IOnShareResult) {
				((IOnShareResult) mActivity).onShareSuccess();
			}
		}

		public void onException(Throwable throwable) {
			dismissProgressDialog();
			if (mActivity instanceof IOnShareResult) {
				((IOnShareResult) mActivity).onShareFailure(throwable.getMessage());
			}
		}

		public void onFail(String reason) {
			dismissProgressDialog();
			if (mActivity instanceof IOnShareResult) {
				((IOnShareResult) mActivity).onShareFailure(reason);
			}
		}

		public void onThinking() {
			showProgressDialog();
		}
	};
}