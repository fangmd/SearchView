package com.adouble.searchview;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by double on 2016/10/31.
 */

public class SearchView extends FrameLayout implements View.OnFocusChangeListener, TextWatcher, View.OnClickListener {

    private static final String TAG = SearchView.class.getSimpleName();
    private boolean DEBUG;

    private ImageView mIVConfirm;
    private EditText mET;
    private LinearLayout mLLHint;
    private com.adouble.searchview.OnSearchListener mOnSearchListener;
    private boolean mHasFocus;
    private String mETContent;
    private ImageView mIVClear;


    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.search_view, this);
        mIVConfirm = ((ImageView) view.findViewById(R.id.iv_search_view_confirm));
        mET = ((EditText) view.findViewById(R.id.et_search));
        mLLHint = ((LinearLayout) view.findViewById(R.id.ll_search_view_place_holder));
        mIVClear = ((ImageView) view.findViewById(R.id.iv_search_view_clear));

        mET.setOnFocusChangeListener(this);
        mET.addTextChangedListener(this);
        mET.setOnClickListener(this);

        mIVConfirm.setOnClickListener(this);
        mIVClear.setOnClickListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mHasFocus = hasFocus;
        if (DEBUG) Log.d(TAG, "onFocusChange:  " + hasFocus);
        mLLHint.setVisibility(hasFocus ? GONE : VISIBLE);

        saveAndClearETContent();

    }

    private void saveAndClearETContent() {
        mETContent = mET.getText().toString();// 保存了没有加载

        mET.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String s1 = s.toString();
        mIVConfirm.setVisibility(TextUtils.isEmpty(s1) ? GONE : VISIBLE);
        mIVClear.setVisibility(TextUtils.isEmpty(s1) ? GONE : VISIBLE);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.et_search) {
//            mLLHint.setVisibility(GONE);
        } else if (i == R.id.iv_search_view_confirm) {
            if (mOnSearchListener != null) {
                mOnSearchListener.onSearch(mET.getText().toString());
            }
            hideSoftKeyBoard();
        } else if (i == R.id.iv_search_view_clear) {
            clearFocus();
        }


    }

    private void hideSoftKeyBoard() {
        // Check if no view has focus:
        Activity activity = getActivity();
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        mOnSearchListener = onSearchListener;
    }

    public boolean isHasFocus() {
        return mHasFocus;
    }

    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    @Override
    public void clearFocus() {
        super.clearFocus();
        mET.clearFocus();
    }


    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
