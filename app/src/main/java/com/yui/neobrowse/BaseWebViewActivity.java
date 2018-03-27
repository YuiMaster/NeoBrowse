package com.yui.neobrowse;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author liaoyuhuan
 * @name ${PROJECT_NAME}
 * @class
 * @time 2018/3/26  14:30
 * @description *
 * 参考blog：
 * 关于webview最详细讲解（包含 h5 和android 交互） https://blog.csdn.net/u014045181/article/details/53308537
 * 最底部有webView的坑与解决方案
 */
public class BaseWebViewActivity extends AbstractBaseActivity {

    protected WebView webView;
    protected WebSettings webSettings;


    /**
     * 1 加载assets目录下的本地文件
     * file:///android_asset/JavaAndJavaScriptCall.html
     * 2 加载网络资源
     * http://www.baidu.com
     */
    protected String mBaseUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        super.initView();
        webView = findViewById(R.id.webview);
        webSettings = webView.getSettings();
    }

    @Override
    protected void initData() {
        super.initData();
    }


    /**
     * 配置webView
     */
    protected void initWebView() {
        /**
         * 调用setWebViewClient，就不会调用系统浏览器
         */
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /**
                 * 拦截url
                 * 在网页跳转时调用，这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
                 */
                view.loadUrl(url);
                return true;
            }


            /**
             * 开始加载网页时处理 如：显示“加载提示”的加载对话框
             *
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 网页加载完成时处理 如：让 加载对话框 消失
             *
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            /**
             * 加载网页失败时的处理 如：提示失败或显示新的界面
             * @param view
             * @param request
             * @param error
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            /**
             * 处理https请求，为WebView处理ssl证书设置WebView，
             * 默认不处理https请求，需要在WebViewClient子类中重写父类的onReceivedSslError函数
             *                handler.proceed();  // 接受信任所有网站的证书
             *                handler.cancel();   // 默认操作 不处理
             *                handler.handleMessage(null);  // 可做其他处理
             * @param view
             * @param handler
             * @param error
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受信任所有网站的证书
                // handler.cancel();   // 默认操作 不处理
                // handler.handleMessage(null);  // 可做其他处理
            }
        });


        /**
         * 辅助WebView处理JavaScript的对话框，图片
         */
        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 监听网页加载进度
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                setTitle("页面加载中，请稍候..." + newProgress + "%");
                setProgress(newProgress);
                if (newProgress == 100) {
                    /**
                     * 页面加载进度100%
                     */
                }
            }
        });


        /**
         * 加快HTML网页加载完成速度
         * webkit解析网页各节点，发现有外部样式文件或外部脚本文件时，会异步发起网络请求下载文件。
         * 网络情况较差的情况下，过多的网络请求，会造成带宽紧张，影响到css或js文件加载完成时间，造成页面空白。
         *
         *Android 4.4(API 19)
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        } else {
            /**
             * webview先不自动加载图片，页面finish之后再发起图片加载
             * 查看 public void onPageFinished(WebView view, String url)
             */
            webView.getSettings().setLoadsImagesAutomatically(false);
        }

        /**
         * 设置缓存方式,主要有以下几种
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据。
         * LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
         * LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level 11开始作用同LOAD_DEFAULT模式。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据。
         * LOAD_CACHE_ELSE_NETWORK：只要本地有，无论是否过期，或者no-cache，都使用缓存中
         * */
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        /**
         * 支持缩放
         * */
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        /**
         * 支持内容重新布局
         * */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        /**
         * 支持js
         * */
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSInterface(), "Android");


        /**
         * 加载页面
         * */
        webView.loadUrl("https://www.baidu.com/");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * webView.canGoBack()在webView含有一个可后退的浏览记录时返回true
         */
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 页面销毁，释放webview资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }
}
/**
 * 坑与解决方案
 * <p>
 * 1. WebViewClient.onPageFinished()。
 * 你永远无法确定当WebView调用这个方法的时候，网页内容是否真的加载完毕了。
 * 当前正在加载的网页产生跳转的时候这个方法可能会被多次调用，
 * StackOverflow上有比较具体的解释（How to listen for a Webview finishing loading a URL in Android?），
 * 但其中列举的解决方法并不完美。
 * 所以当你的WebView需要加载各种各样的网页并且需要在页面加载完成时采取一些操作的话，
 * 可能WebChromeClient.onProgressChanged()比WebViewClient.onPageFinished()都要靠谱一些。
 * <p>
 * 2. WebView后台耗电问题。
 * 当你的程序调用了WebView加载网页，WebView会自己开启一些线程（？），
 * 如果你没有正确地将WebView销毁的话，
 * 这些残余的线程（？）会一直在后台运行，由此导致你的应用程序耗电量居高不下。
 * 对此我采用的处理方式比较偷懒，简单又粗暴（不建议），
 * 即在Activity.onDestroy()中直接调用System.exit(0)，
 * 使得应用程序完全被移出虚拟机，这样就不会有任何问题了。
 * <p>
 * 3. 切换WebView闪屏问题。
 * 如果你需要在同一个ViewGroup中来回切换不同的WebView（包含了不同的网页内容）的话，你就会发现闪屏是不可避免的。
 * 这应该是Android硬件加速的Bug，如果关闭硬件加速这种情况会好很多，
 * 但无法获得很好的浏览体验，你会感觉网页滑动的时候一卡一卡的，不跟手。
 * <p>
 * 4. 在某些手机上，Webview有视频时，activity销毁后，视频资源没有被销毁，
 * 甚至还能听到在后台播放。
 * 即便是像刚才那样各种销毁webview也无济于事，
 * 解决办法：在onDestory之前修改url为空地址。
 * <p>
 * 5.WebView硬件加速导致页面渲染闪烁问题
 * 关于Android硬件加速 开始于Android 3.0 (API level 11),开启硬件加速后，WebView渲染页面更加快速，拖动也更加顺滑。
 * 但有个副作用就是容易会出现页面加载白块同时界面闪烁现象。
 * 解决这个问题的方法是设置WebView暂时关闭硬件加速，代码如下：
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
 * webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
 * }
 */
