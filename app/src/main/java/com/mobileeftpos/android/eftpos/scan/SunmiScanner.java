package com.mobileeftpos.android.eftpos.scan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileeftpos.android.eftpos.R;
import com.mobileeftpos.android.eftpos.SupportClasses.Constants;
import com.mobileeftpos.android.eftpos.activity.AlipayActivity;
import com.mobileeftpos.android.eftpos.activity.BaseScannerActivity;
import com.mobileeftpos.android.eftpos.activity.FullScannerActivity;
import com.sunmi.scan.Config;
import com.sunmi.scan.Image;
import com.sunmi.scan.ImageScanner;
import com.sunmi.scan.Symbol;
import com.sunmi.scan.SymbolSet;


public class SunmiScanner extends BaseScannerActivity implements SurfaceHolder.Callback {
	private Camera mCamera;
	private SurfaceHolder mHolder;
	private SurfaceView surface_view;
	private ImageScanner scanner;// declare a scanner
	private Handler autoFocusHandler;
	private AsyncDecode asyncDecode;
	SoundUtils soundUtils;
	private boolean vibrate;
	public int decode_count = 0;
    private TextView txtManualentry;
	private FinderView finder_view;
    EditText alertinputField;
    AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_sunmi_scan_finder);
        setupToolbar();
        txtManualentry=(TextView)findViewById(R.id.manual_txtview);
        txtManualentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

		init();
	}

	private void init() {
		surface_view = (SurfaceView) findViewById(R.id.surface_view);
		finder_view = (FinderView) findViewById(R.id.finder_view);
 		mHolder = surface_view.getHolder();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mHolder.addCallback(this);
		scanner = new ImageScanner();// create scanner 
		scanner.setConfig(0, Config.X_DENSITY, 2);// Line scanning interval
		scanner.setConfig(0, Config.Y_DENSITY, 2);// Column scanning interval
		scanner.setConfig(0, Config.ENABLE_MULTILESYMS, 0);// Whether to identify several code,0 means identify one，1 means identify multiple code
		scanner.setConfig(0, Config.ENABLE_INVERSE, 0);// Whether to identify inverse code
		scanner.setConfig(Symbol.PDF417, Config.ENABLE, 0);// Disable decode PDF417 code, default enable.

		autoFocusHandler = new Handler();
		asyncDecode = new AsyncDecode();
		decode_count = 0;
	}

    public void showCustomDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this,R.style.full_screen_dialog);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_layout, null);
        dialogBuilder.setView(dialogView);

        alertinputField = (EditText) dialogView.findViewById(R.id.alert_et);
        Button alertButton = (Button) dialogView.findViewById(R.id.alert_btn);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alertinputField.getText().toString()!=null && alertinputField.getText().toString().length()>0){

                    Intent i = new Intent(SunmiScanner.this, AlipayActivity.class);
                    i.putExtra(Constants.QRCODE.BARCODE_INTENT_RESULT_KEY, alertinputField.getText().toString());
                    setResult(111, i);
                    alertDialog.dismiss();
                    finish();
                }
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		if (mHolder.getSurface() == null) {
			return;
		}
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
		}
		try {
			//camera preview resolution and  image zooming settings, nonobligatory
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(800, 480); // set preview resolution
			// parameters.set("zoom", String.valueOf(27 / 10.0));// Image magnification factor of 2 . 7
			mCamera.setParameters(parameters);
			// //////////////////////////////////////////
			mCamera.setDisplayOrientation(90);// vertical showing
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setPreviewCallback(previewCallback);
			mCamera.startPreview();
			mCamera.autoFocus(autoFocusCallback);
		} catch (Exception e) {
			Log.d("DBG", "Error starting camera preview: " + e.getMessage());
		}
	}

	/**
	 * preview data
	 */
	PreviewCallback previewCallback = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			if (asyncDecode.isStoped()) {
				Camera.Parameters parameters = camera.getParameters();
				Size size = parameters.getPreviewSize();// getting the preview resolution

				//creating decode image , transform to source data,the image has been  rotated 90 degrees
				Image source = new Image(size.width, size.height, "Y800");
				Rect scanImageRect = finder_view.getScanImageRect(size.height,
						size.width);
				//image has been rotated 90 degrees, crop the image 
				source.setCrop(scanImageRect.top, scanImageRect.left,scanImageRect.height(), scanImageRect.width());
				source.setData(data);// filling data
				asyncDecode = new AsyncDecode();
				asyncDecode.execute(source);// decoding the code asynchronously
			}
		}
	};

	private class AsyncDecode extends AsyncTask<Image, Void, Void> {
		private boolean stoped = true;
		private String str = "";

		@Override
		protected Void doInBackground(Image... params) {
			stoped = false;
			StringBuilder sb = new StringBuilder();
			Image src_data = params[0];// getting the srouce data
            String result = null;
			long startTimeMillis = System.currentTimeMillis();

			// decoding ，return value 0 means failure，>0 means successful
			int nsyms = scanner.scanImage(src_data);

			long endTimeMillis = System.currentTimeMillis();
			long cost_time = endTimeMillis - startTimeMillis;

			if (nsyms != 0) {
				playBeepSoundAndVibrate();// play prompt tone after scanning

				SymbolSet syms = scanner.getResults();// getting the result
				for (Symbol sym : syms) {
                    result=sym.getResult();
				}
			}
			str = result;
			return null;
		}

	

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			stoped = true;
			if (null == str || str.equals("")) {
			} else {
                Intent i = new Intent(SunmiScanner.this, AlipayActivity.class);
				i.putExtra(Constants.QRCODE.BARCODE_INTENT_RESULT_KEY, str);
				setResult(AlipayActivity.ALIPAY_CONSTANT,i);
				finish();
			}
		}

		public boolean isStoped() {
			return stoped;
		}
	}

	/**
	 *auto-focusing callback
	 */
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};
	/**
	creating image,width is the preview image's width and  height is the preview image's height,generally speaking,high generally speaking also means clearer image,but slower decoding speed.cause the decoding arithmetic need the the original data and the default format of the preview image is YCbCr_420_SP,you must transform the fromat, parameter "Y800" is the format of image what you want transform.
	*/
	// auto-focus
	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (null == mCamera || null == autoFocusCallback) {
				return;
			}
			mCamera.autoFocus(autoFocusCallback);
		}
	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();
		} catch (Exception e) {
			mCamera = null;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private void initBeepSound() {
		if (soundUtils == null) {
			soundUtils = new SoundUtils(this, SoundUtils.RING_SOUND);
			soundUtils.putSound(0, R.raw.beep);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initBeepSound();
		vibrate = false;
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	/*
	 * private final OnCompletionListener beepListener = new
	 * OnCompletionListener() { public void onCompletion(MediaPlayer
	 * mediaPlayer) { mediaPlayer.seekTo(0); } };
	 */

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (soundUtils != null) {
			soundUtils.playSound(0, SoundUtils.SINGLE_PLAY);
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

}
