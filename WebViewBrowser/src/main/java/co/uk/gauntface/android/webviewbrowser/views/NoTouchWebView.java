package co.uk.gauntface.android.webviewbrowser.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import co.uk.gauntface.android.gauntfacehelperlib.views.BasicWebView;

/**
 * Created by mattgaunt on 08/01/2014.
 */
public class NoTouchWebView extends BasicWebView {

    public NoTouchWebView(Context context) {
        this(context, null);
    }

    public NoTouchWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoTouchWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onTouchEvent (MotionEvent event) {
        return false;
    }
}
