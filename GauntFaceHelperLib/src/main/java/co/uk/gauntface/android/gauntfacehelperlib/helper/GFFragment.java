package co.uk.gauntface.android.gauntfacehelperlib.helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public abstract class GFFragment extends Fragment {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ensure the app uses the correct context
        mContext = getActivity().getApplicationContext();
    }

    /**
     * Prevent us from having to remember this boiler plate code
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getFragmentLayoutId(), container, false);
        initViews(v, container, savedInstanceState);

        return v;
    }

    public Context getApplicationContext() {
        return mContext;
    }

    public void hideKeyboard(EditText editText) {
        //InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public abstract int getFragmentLayoutId();
    public abstract void initViews(View view, ViewGroup container,
                                   Bundle savedInstanceState);

}
