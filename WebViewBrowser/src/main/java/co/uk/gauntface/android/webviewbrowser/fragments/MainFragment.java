package co.uk.gauntface.android.webviewbrowser.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import co.uk.gauntface.android.gauntfacehelperlib.helper.GFFragment;
import co.uk.gauntface.android.webviewbrowser.R;

public class MainFragment extends GFFragment implements View.OnClickListener {

    private EditText mEditText;
    private Button mLinkGoButton;
    private WebView mWebView;

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
        if(!urlString.startsWith("http")) {
            urlString = "http://"+urlString;
        }

        mWebView.loadUrl(urlString);
    }
}
