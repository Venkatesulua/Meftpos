package com.mobileeftpos.android.eftpos.SplashScreen;


/**
 * Created by Prathap on 4/26/17.
 */

public class IncompatibleRatioException extends RuntimeException {

    private static final long serialVersionUID = 234608108593115395L;

    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}