package co.uk.gauntface.android.gauntfacehelperlib.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by mattgaunt on 08/01/2014.
 */
public class BasicWebView extends WebView {

    private GestureDetector mGestureDetector;

    /**public BasicWebView(Context context) {
        this(context, null);
    }**/

    /**
     * Normally I would use this(content, attrs, 0) but that caused some focusing issues.
     * hence using super to ensure we are using sane defaults
     *
     * @param context
     * @param attrs
     */
    public BasicWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialiseDefaults();
    }

    public BasicWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialiseDefaults();
    }

    private void initialiseDefaults() {
        WebSettings settings = getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        setWebViewClient(new WebViewClient());

        // Handle Double Click
        mGestureDetector = new GestureDetector(getContext(), new DoubleTapListener());
    }

    public void loadJavascript(String javascript) {
        loadJavascript(javascript, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(mGestureDetector.onTouchEvent(e)) {
            return true;
        }

        return super.onTouchEvent(e);
    }

    /**
     * This method is designed to hide how Javascript is injected into
     * the WebView.
     *
     * In KitKat the new evaluateJavascript method has the ability to
     * give you access to any return values via the ValueCallback object.
     *
     * The String passed into onReceiveValue() is a JSON string, so if you
     * execute a javascript method which return a javascript object, you can
     * parse it as valid JSON. If the method returns a primitive value, it
     * will be a valid JSON object, but you should use the setLenient method
     * to true and then you can use peek() to test what kind of object it is,
     *
     * @param javascript
     */
    public void loadJavascript(String javascript, final OnJSReturnValue listener) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In KitKat+ you should use the evaluateJavascript method
            evaluateJavascript(javascript, new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String s) {
                    if(listener == null) {
                        return;
                    }

                    JsonReader reader = new JsonReader(new StringReader(s));

                    // Must set lenient to parse single values
                    reader.setLenient(true);

                    try {
                        if (reader.peek() != JsonToken.NULL) {
                            listener.onReturnValue(reader);
                        } else {
                            listener.onReturnValue(null);
                        }
                    } catch (IOException e) {
                        Log.e("TAG", "MainActivity: IOException", e);
                        try {
                            reader.close();
                        } catch (IOException exception) {
                            // NOOP
                        }
                    }
                }
            });
        } else {
            /**
             * For pre-KitKat+ you should use loadUrl("javascript:<JS Code Here>");
             * To then call back to Java you would need to use addJavascriptInterface()
             * and have your JS call the interface
             **/
            loadUrl("javascript:" + javascript);
        }
    }

    public interface OnJSReturnValue {
        public void onReturnValue(JsonReader reader);
    }

    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            BasicWebView.this.zoomIn();

            return true;
        }
    }
}
