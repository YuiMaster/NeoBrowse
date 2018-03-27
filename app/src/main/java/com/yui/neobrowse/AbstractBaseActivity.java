package com.yui.neobrowse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author liaoyuhuan
 * @name ${PROJECT_NAME}
 * @class
 * @time 2018/3/26  14:30
 * @description
 */
public abstract class AbstractBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            initParam(savedInstanceState);
        }
        setContentView(getLayout());
        initView();
        initData();
    }

    /**
     * 界面间跳转传参
     *
     * @param bundle
     */
    protected void initParam(Bundle bundle) {

    }

    /**
     * 注意：layoutId 不允许为非法id
     *
     * @return
     */
    public abstract int getLayout();

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化View
     */
    protected void initView() {

    }


}
