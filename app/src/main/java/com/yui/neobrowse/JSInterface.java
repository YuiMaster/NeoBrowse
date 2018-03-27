package com.yui.neobrowse;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * 提供给JS调用的方法
 *
 * @author liaoyuhuan
 * @name ${PROJECT_NAME}
 * @class
 * @time 2018/3/26  15:37
 * @description *
 */
public class JSInterface {
    private Context mContext;

    public JSInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void showToast(String data) {
        Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
    }
}
