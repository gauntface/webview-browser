package co.uk.gauntface.android.webviewbrowser.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import co.uk.gauntface.android.gauntfacehelperlib.helper.GFFragment;
import co.uk.gauntface.android.gauntfacehelperlib.views.BasicWebView;
import co.uk.gauntface.android.webviewbrowser.C;
import co.uk.gauntface.android.webviewbrowser.R;

public class MainFragment extends GFFragment implements View.OnClickListener {

    private static final int ENTER_URL = 0;
    private static final int LOADING_SPINNER = 1;
    private static final int ABOUT = 2;
    private static final int NO_PAGE = -1;

    private FrameLayout mFullscreenContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private EditText mEditText;
    private Button mLinkGoButton;
    private WebView mWebView;
    private BasicWebView mMessageWebView;
    private int mCurrentMessageState = NO_PAGE;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initViews(View view, ViewGroup container, Bundle savedInstanceState) {
        mEditText = (EditText) view.findViewById(R.id.fragment_main_url_edittext);
        mLinkGoButton = (Button) view.findViewById(R.id.fragment_main_go_btn);
        mWebView = (WebView) view.findViewById(R.id.fragment_main_webview);
        mMessageWebView = (BasicWebView) view.findViewById(R.id.fragment_main_message_webview);
        mFullscreenContainer = (FrameLayout) view.findViewById(R.id.fragment_main_fullscreen_video);

        mFullscreenContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCustomViewCallback != null) {
                    mCustomViewCallback.onCustomViewHidden();
                    mFullscreenContainer.setVisibility(View.INVISIBLE);

                    mCustomViewCallback = null;
                }
            }
        });

        mLinkGoButton.setOnClickListener(this);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_GO) {
                    displayLinkInPage();
                }
                return false;
            }
        });

        mMessageWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                Log.v(C.TAG, "MessageWebView: onPageFinished() url = " + url);
                showMessagePane(mCurrentMessageState);
            }
        });

        mMessageWebView.loadUrl(getString(R.string.init_url));
        mMessageWebView.setBackgroundColor(Color.TRANSPARENT);
        showMessagePane(ENTER_URL);

        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                if(url == null || url.length() == 0 || url.equals("about:blank")) {
                    return false;
                } else {
                    mEditText.setText(url);
                }
                return false;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showMessagePane(LOADING_SPINNER);
            }

            public void onPageFinished(WebView view, String url) {
                Log.v(C.TAG, "MainWebView: onPageFinished() url = " + url);
                if(url == null || url.length() == 0 || url.equals("about:blank")) {
                    showMessagePane(ENTER_URL);
                } else {
                    showMessagePane(NO_PAGE);
                }
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                Log.v("VideoViewWebView", "getDefaultVideoPoster()");
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.ic_launcher);
                return icon;
            }

            @Override
            public View getVideoLoadingProgressView() {
                Log.v("VideoViewWebView", "getVideoLoadingProgressView()");
                return null;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                Log.v("VideoViewWebView", "onShowCustomView()");
                if (view instanceof FrameLayout){
                    FrameLayout frame = (FrameLayout) view;

                    mCustomViewCallback = callback;

                    mFullscreenContainer.removeAllViews();
                    mFullscreenContainer.addView(frame);
                    mFullscreenContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onHideCustomView() {
                Log.v("VideoViewWebView", "onHideCustomView()");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_main_go_btn:
                hideKeyboard(mEditText);
                displayLinkInPage();
                break;
        }
    }

    private void displayLinkInPage() {
        String urlString = mEditText.getText().toString();
        if(urlString == null || urlString.length() == 0) {
            mWebView.loadUrl("about://blank");
            return;
        }
        if(!urlString.startsWith("http")) {
            urlString = "http://"+urlString;
        }

        mWebView.loadUrl(urlString);
        //mWebView.requestFocus(View.FOCUS_DOWN);
    }

    private void showMessagePane(int messageType) {
        int newState = messageType;
        switch(messageType) {
            case ENTER_URL:
                mMessageWebView.setVisibility(View.VISIBLE);
                mMessageWebView.loadJavascript("showEnterUrl();");
                break;
            case LOADING_SPINNER:
                mMessageWebView.setVisibility(View.VISIBLE);
                mMessageWebView.loadJavascript("showLoadingSpinner();");
                break;
            case ABOUT:
                mMessageWebView.setVisibility(View.VISIBLE);
                mMessageWebView.loadJavascript("showAbout();");
                break;
            case NO_PAGE:
            default:
                mMessageWebView.setVisibility(View.GONE);
                newState = NO_PAGE;
                mMessageWebView.loadJavascript("showNoPane();");
                break;
        }

        mCurrentMessageState = newState;
    }
}
