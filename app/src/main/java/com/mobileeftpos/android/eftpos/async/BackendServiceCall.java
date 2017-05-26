package com.mobileeftpos.android.eftpos.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.mobileeftpos.android.eftpos.app.EftposApp;
import com.mobileeftpos.android.eftpos.utils.AppLog;
import com.mobileeftpos.android.eftpos.utils.AppUtil;
import com.mobileeftpos.android.eftpos.utils.NetworkConnectionState;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prathap on 2/18/2017.
 * <p>
  */

public class BackendServiceCall {

    public Context context;
    private static final String TAG = BackendServiceCall.class.getSimpleName();
    private OnServiceCallCompleteListener listener;
    private boolean isProgressDialogShow;
    private ProgressDialog pDialog;

    private static final String DEVICE_OFFLINE_MESSAGE = "Please check your device settings to " +
            "ensure you have a working internet connection. Or there was an issue connecting to " +
            "Server,please try again later.";

    public void setOnServiceCallCompleteListener(OnServiceCallCompleteListener listener) {
        this.listener = listener;
    }

    public BackendServiceCall(Context context, boolean isProgressDialogShow) {
        this.context = context;
        this.isProgressDialogShow = isProgressDialogShow;
    }

    public static boolean isOnline(Context context) {

//		Runtime runtime = Runtime.getRuntime();
//		try {
//			Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//			int exitValue = ipProcess.waitFor();
//			return (exitValue == 0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

        return true;
    }

//	public static boolean isOnline(Context context) {
//		ConnectivityManager cm =
//				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//		boolean isConnected = activeNetwork != null &&
//				activeNetwork.isConnectedOrConnecting();
//		return isConnected;
//	}

	/* public void makeJSONObjectGetRequest(String url, final Request.Priority priority) {

		if (AppUtil.getNetworkState() == NetworkConnectionState.CONNECTED && isOnline(context)) {
			// Tag used to cancel the request
			String tag_json_obj = "json_obj_req";
			if (isProgressDialogShow) {
				pDialog = new ProgressDialog(context);
				pDialog.setMessage("Loading...");
				pDialog.show();
			}

			final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
					url, null,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							AppLog.d(TAG, response.toString());
							if (isProgressDialogShow) {
								pDialog.dismiss();
							}
							listener.onJSONObjectResponse(response);
						}
					}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());
					// hide the progress dialog
					if (isProgressDialogShow) {
						pDialog.dismiss();
					}
					listener.onErrorResponse(error);
				}
			}) {
				@Override
				public Priority getPriority() {
					return priority;
				}
			};

			// Adding request to request queue
			EftposApp.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			throw new RuntimeException(DEVICE_OFFLINE_MESSAGE);
		}
	}

	public void makeJSONObejctPostRequestMultipart(final String url, final HashMap<String, String>
			postParam, final File imageFile, final String filename, final String fileField, final Request.Priority priority) {
		if (AppUtil.getNetworkState() == NetworkConnectionState.CONNECTED && isOnline(context)) {
			// Tag used to cancel the request
			String tag_json_obj = "json_obj_req";
			if (isProgressDialogShow) {
				pDialog = new ProgressDialog(context);
				pDialog.setMessage("Loading...");
				pDialog.show();
			}
			VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
				@Override
				public void onResponse(NetworkResponse response) {
					if (isProgressDialogShow) {
						pDialog.dismiss();
					}
					String resultResponse = new String(response.data);
					try {
						JSONObject result = new JSONObject(resultResponse);
						AppLog.d("MediaSent Response", result + "");
						listener.onJSONObjectResponse(result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());
					if (isProgressDialogShow) {
						pDialog.dismiss();
					}
					listener.onErrorResponse(error);
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
					return postParam;
				}

				@Override
				protected Map<String, DataPart> getByteData() {
					Map<String, DataPart> params = new HashMap<>();
					// file name could found file base or direct access from real path
					// for now just get bitmap data from ImageView
					params.put(fileField, new DataPart(filename, getFileDataFromDrawable(imageFile), "image/jpeg"));
					return params;
				}

				@Override
				public Priority getPriority() {
					return priority;
				}
			};

			EftposApp.getInstance().addToRequestQueue(multipartRequest, tag_json_obj);

		} else {
			throw new RuntimeException(DEVICE_OFFLINE_MESSAGE);
		}
	}


	public void makeJSONObjectPostRequest(String url, final HashMap<String, String> postParam, final Request.Priority priority) {
		if (AppUtil.getNetworkState() == NetworkConnectionState.CONNECTED && isOnline(context)) {
			// Tag used to cancel the request
			String tag_json_obj = "json_obj_req";

			if (isProgressDialogShow) {
				pDialog = new ProgressDialog(context);
				pDialog.setMessage("Loading...");
				pDialog.show();
			}

			StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
					url,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							AppLog.d(TAG, response.toString());
							if (isProgressDialogShow) {
								pDialog.dismiss();
							}
							listener.onJSONObjectResponse(getJSONObject(response));
						}

					}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d(TAG, "Error: " + error.getMessage());
					if (isProgressDialogShow) {
						pDialog.dismiss();
					}
					listener.onErrorResponse(error);
				}
			}) {


				@Override
				protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
					return postParam;
				}

				@Override
				public Priority getPriority() {
					return priority;
				}
			};
			// Adding request to request queue
			EftposApp.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			throw new RuntimeException(DEVICE_OFFLINE_MESSAGE);
		}
	}

	public JSONObject getJSONObject(String stringOject) {
		try {
			return new JSONObject(stringOject);
		} catch (JSONException ex) {
			return null;
		}
	}

	public static void startImageLoader(final ImageView imageView, String imageURL) {
		if (imageURL != null && !imageURL.equalsIgnoreCase("null") && imageURL.startsWith("http")) {
			ImageLoader imageLoader = EftposApp.getInstance().getImageLoader();

			// If you are using normal ImageView
			imageLoader.get(imageURL, new ImageLoader.ImageListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					AppLog.d(TAG, "Image Load Error: " + error.getMessage());
				}

				@Override
				public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
					if (response.getBitmap() != null) {
						// load image into imageview
						imageView.setImageBitmap(response.getBitmap());
					}
				}
			});
		}
	}

	public static void startImageLoader(final ImageView imageView, String imageURL, int
			loading_icon, int error_icon) {
		if (imageURL != null && !imageURL.equalsIgnoreCase("null") && imageURL.startsWith("http")) {
			ImageLoader imageLoader = RecipeApp.getInstance().getImageLoader();

			// If you are using normal ImageView
			imageLoader.get(imageURL, new ImageLoader.ImageListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					AppLog.d(TAG, "Image Load Error: " + error.getMessage());
				}

				@Override
				public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
					if (response.getBitmap() != null) {
						// load image into imageview
						imageView.setImageBitmap(response.getBitmap());
					}
				}
			}, loading_icon, error_icon);
		}
	}


	public static void startImageLoader(final NetworkImageView imgNetWorkView, String imageURL) {
		if (imageURL != null && !imageURL.equalsIgnoreCase("null") && imageURL.startsWith("http")) {
			ImageLoader imageLoader = RecipeApp.getInstance().getImageLoader();

			// If you are using NetworkImageView
			imgNetWorkView.setImageUrl(imageURL, imageLoader);
		}
	}

	public static void startImageLoader(final NetworkImageView imgNetWorkView, String imageURL, int
			loading_icon, int error_icon) {
		if (imageURL != null && !imageURL.equalsIgnoreCase("null") && imageURL.startsWith("http")) {
			ImageLoader imageLoader = RecipeApp.getInstance().getImageLoader();

			// If you are using normal ImageView
			imageLoader.get(imageURL, new ImageLoader.ImageListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					AppLog.d(TAG, "Image Load Error: " + error.getMessage());
				}

				@Override
				public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
					if (response.getBitmap() != null) {
						// load image into imgNetWorkView
						imgNetWorkView.setImageBitmap(response.getBitmap());
					}
				}
			}, loading_icon, error_icon);
		}
	}

	public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true; //Deprecated API 21

		return BitmapFactory.decodeFile(photoPath, bmOptions);
	}

	public byte[] getFileDataFromDrawable(File imagePath) {


		Bitmap bitmap = resizeBitmap(imagePath + "", 200, 200);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	public static void startImageLoader(final NotificationCompat.Builder notificationBuilder, final String message, String imageURL) {
		if (imageURL != null && !imageURL.equalsIgnoreCase("null") && imageURL.startsWith("http")) {

			try {
				URL url = new URL(imageURL);
				Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(image);
				s.setSummaryText(message);
				notificationBuilder.setStyle(s);
			} catch(IOException e) {
				System.out.println(e);
			}

		}
	}*/

}
