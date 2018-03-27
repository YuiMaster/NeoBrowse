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
    public JSInterface() {
    }

    @JavascriptInterface
    public void showToast(Context context, String data) {
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
    }
}
