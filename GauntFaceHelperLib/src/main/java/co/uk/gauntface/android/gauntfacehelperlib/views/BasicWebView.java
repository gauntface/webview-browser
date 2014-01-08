package co.uk.gauntface.android.gauntfacehelperlib.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by mattgaunt on 08/01/2014.
 */
public class BasicWebView extends WebView {

    public BasicWebView(Context context) {
        this(context, null);
    }

    public BasicWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
    }
}
