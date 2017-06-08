package com.mobileeftpos.android.eftpos.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.IntentCompat;
import com.mobileeftpos.android.eftpos.activity.LoginActivity;
import com.mobileeftpos.android.eftpos.sharedpreference.SharedPreferenceStore;
import org.json.JSONObject;
import java.io.IOException;
 import java.util.List;



/**
 * Created by Prathap on 4/26/17.
 */

public class AppUtil {

    public static int ITEMS_PER_AD = 8;

    /**
     * networkState: This is global variable contain network state.
     */
    private static NetworkConnectionState networkState;

    public static void setNetworkState(NetworkConnectionState networkConnectionState) {
        networkState = networkConnectionState;
    }

    public static NetworkConnectionState getNetworkState() {
        return networkState;
    }


    public static final String DATA_KEY_NOTIFICATION_TITLE = "title";
    public static final String DATA_KEY_NOTIFICATION_MESSAGE = "message";
    public static final String DATA_KEY_NOTIFICATION_TYPE = "notificationType";
    public static final String DATA_KEY_NOTIFICATION_RECIPECODE = "recipeCode";
    public static final String DATA_KEY_NOTIFICATION_RECIPEIMAGE = "recipeImage";
    public static final String BROADCAST_NOTIFICATION = "broadcast_notification";

    public static final String GCM_REPEAT_TAG = "repeat|[7200,1800]";

    public static String getUserID() {
        String userId = null;
        try {
            JSONObject userInfo = new JSONObject(SharedPreferenceStore.getEncryptedSharedPref("LoginInfo"));
            JSONObject userDetails = userInfo.getJSONObject("users");
            userId = userDetails.getString("userId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static boolean checkApplicationRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }
        return isActivityFound;
    }

    public static void closeUserSession(Context context) {
        SharedPreferenceStore.deleteSharedPreference("LoginInfo");
        SharedPreferenceStore.deleteSharedPreference("LoginStatus");

        if (checkApplicationRunning(context)) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat
                    .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * playNotificationSound: This method will play default notification sound.
     */
    public static void playNotificationSound(Context context) {
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, defaultRingtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/*public static void displayUserNotLoggedInDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
				.setTitle("Authentic Recipe")
				.setMessage("This feature is locked. To unlocked this feature please login with app. \n Are you sure want to login? ")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
						AppUtil.closeUserSession(activity);
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				})
				.setIcon(R.drawable.fail)
				.show();
	}

	public static void displayNotificationDialog(Activity activity, Intent intent) {

		String notificationType = intent.getStringExtra(AppUtil.DATA_KEY_NOTIFICATION_TYPE);
		Notifications newNotification = new Notifications();
		newNotification.setNotificationType(notificationType);
		newNotification.setTime(System.currentTimeMillis());
		newNotification.setTitle(intent.getStringExtra(AppUtil.DATA_KEY_NOTIFICATION_TITLE));
		newNotification.setMessage(intent.getStringExtra(AppUtil.DATA_KEY_NOTIFICATION_MESSAGE));
		newNotification.setUserId(AppUtil.getUserID());
		if (notificationType.equalsIgnoreCase("Recipe")) {
			newNotification.setRecipeCode(intent.getStringExtra(AppUtil.DATA_KEY_NOTIFICATION_RECIPECODE));
			newNotification.setRecipeImage(intent.getStringExtra(AppUtil.DATA_KEY_NOTIFICATION_RECIPEIMAGE));

			///gksjfglkjf

			final Dialog dialog = new Dialog(activity);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.notifaction_dialog);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(true);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			final TextView titel = (TextView) dialog.findViewById(R.id.notifactionTitel);
			final TextView message = (TextView) dialog.findViewById(R.id.notifactionMessage);
			final ImageView image = (ImageView) dialog.findViewById(R.id.bg_image);
			final CircularImageView avatar = (CircularImageView) dialog.findViewById(R.id.avatar);
			titel.setText(newNotification.getTitle());
			message.setText(newNotification.getMessage());
			avatar.setImageResource(R.mipmap.recipe_icon2);
			BackendServiceCall.startImageLoader(image, newNotification.getRecipeImage());
			dialog.show();
		} else {
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
			builder.setMessage(newNotification.getMessage())
					.setTitle(newNotification.getTitle())
					.setCancelable(false)
					.setNegativeButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			android.app.AlertDialog alert = builder.create();
			alert.show();
		}
	}

	public static void displaySignUpDialog(final Activity activity, final TextView _success) {
		new AlertDialog.Builder(activity)
				.setTitle("Authentic Recipe")
				.setMessage("User signup process completed successfully.\nWe have sent username " +
						("and password to your registered email id. Please check your Email in " +
								"inbox/spam folder.\nAlso you need to activate your account by on" +
								" tapping \" Confirm\" button."))
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
						new CountDownTimer(2800, 1000) {

							public void onTick(long millisUntilFinished) {
							}

							public void onFinish() {
								CircularAnim.fullActivity(activity, _success)
										.colorOrImageRes(android.R.color.white)
										.go(new CircularAnim.OnAnimationEndListener() {
											@Override
											public void onAnimationEnd() {
												activity.finish();
												ActivityStack.getInstance().remove("SignUpActivityNew");
											}
										});
							}
						}.start();
					}
				})

				.setIcon(R.drawable.checked)
				.show();
	}

	public static void doAutoLogin(Activity activity) {
		try {

			String loginUrl = ServerURL.getLoginUrl();

			HashMap<String, String> loginParams = new HashMap<>();
			loginParams.put("userName", SharedPreferenceStore.getEncryptedSharedPref("LoginUserName"));
			loginParams.put("userPassword", SharedPreferenceStore.getEncryptedSharedPref
					("LoginPassword"));
			loginParams.put("userDeviceId", SharedPreferenceStore.getEncryptedSharedPref
					("UserDeviceId"));
			BackendServiceCall serviceCall = new BackendServiceCall(activity,
					false);
			serviceCall.setOnServiceCallCompleteListener
					(new OnServiceCallCompleteListener() {
						@Override
						public void onJSONObjectResponse(JSONObject jsonObject) {
							try {
								if (jsonObject.getString("status").equalsIgnoreCase("true")) {

									SharedPreferenceStore.setEncryptedSharedPref("LoginInfo", jsonObject + "");
									SharedPreferenceStore.setEncryptedSharedPref("LoginStatus", "true");
									JSONObject userDetails = jsonObject.getJSONObject("users");
									String userName = userDetails.getString("userName");
									SharedPreferenceStore.setEncryptedSharedPref("UserName", userName);
								}
							} catch (JSONException e) {

							}
						}

						@Override
						public void onJSONArrayResponse(JSONArray jsonArray) {

						}

						@Override
						public void onErrorResponse(VolleyError error) {

						}
					});
			serviceCall.makeJSONObjectPostRequest(loginUrl, loginParams, Request.Priority
					.IMMEDIATE);
		} catch (RuntimeException e) {
			Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}*/


}
