package com.mobileeftpos.android.eftpos.scanner;

import android.app.Activity;
 import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
 import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.sunmi.scan.Config;
import com.sunmi.scan.Image;
import com.sunmi.scan.ImageScanner;
import com.sunmi.scan.Symbol;
import com.sunmi.scan.SymbolSet;


/**
 * Created by Prathap on 4/26/17.
 */

public class ZBarScannerActivity extends Fragment implements Camera.PreviewCallback, ZBarConstants {

    private static final String TAG = "ZBarScannerActivity";
    private CameraPreview mPreview;
    private Camera mCamera;
    private ImageScanner mScanner;
    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;
    private Context context;

    static {
        System.loadLibrary("iconv");

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        if (!isCameraAvailable()) {
            // Cancel request if there is no rear-facing camera.
            cancelRequest();
            return;
        }

        // Hide the window title.
        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAutoFocusHandler = new Handler();

        // Create and configure the ImageScanner;
        setupScanner();

        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mPreview = new CameraPreview(getActivity(), this, autoFocusCB);
        getActivity().setContentView(mPreview);
    }

    public void setupScanner() {
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);

        int[] symbols = getActivity(). getIntent().getIntArrayExtra(SCAN_MODES);
        if (symbols != null) {
            mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
            for (int symbol : symbols) {
                mScanner.setConfig(symbol, Config.ENABLE, 1);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open();
        if (mCamera == null) {
            // Cancel request if mCamera is null.
            cancelRequest();
            return;
        }

        mPreview.setCamera(mCamera);
        mPreview.showSurfaceView();

        mPreviewing = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();

            // According to Jason Kuang on http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
            // there might be surface recreation problems when the device goes to sleep. So lets just hide it and
            // recreate on resume
            mPreview.hideSurfaceView();

            mPreviewing = false;
            mCamera = null;
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void cancelRequest() {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(ERROR_INFO, "Camera unavailable");
        getActivity().setResult(Activity.RESULT_CANCELED, dataIntent);
        getActivity().finish();
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();

        Image barcode = new Image(size.width, size.height, "Y800");
        barcode.setData(data);

        int result = mScanner.scanImage(barcode);

        if (result != 0) {
            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mPreviewing = false;
            SymbolSet syms = mScanner.getResults();
            for (Symbol sym : syms) {
                String symData = sym.getData();
                if (!TextUtils.isEmpty(symData)) {
                    Intent dataIntent = new Intent();
                    dataIntent.putExtra(SCAN_RESULT, symData);
                    dataIntent.putExtra(SCAN_RESULT_TYPE, sym.getType());
                    getActivity().setResult(Activity.RESULT_OK, dataIntent);
                    getActivity().finish();
                    break;
                }
            }
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null && mPreviewing) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}
