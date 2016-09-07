package com.example.zs.pager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.myaccount.AboutUsActivity;
import com.example.zs.myaccount.FeedbackActivity;
import com.example.zs.myaccount.InitializeActivity;
import com.example.zs.myaccount.LoginActivity;
import com.example.zs.myaccount.MyBalanceActivity;
import com.example.zs.myaccount.QuestionActivity;
import com.example.zs.myaccount.R;
import com.example.zs.myaccount.ShareAppActivity;
import com.example.zs.utils.MyHttpUtils;
import com.example.zs.view.OwnerItem;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 钟云婷 on 2016/9/2.
 */
//该类实现“我的”页面
public class OwnerPager extends BasePager {

    private static final String TAG = "OwnerPager";
    private OwnerItem oi_ownerpager_login;
    private OwnerItem oi_ownerpager_balance;
    private OwnerItem oi_ownerpager_share;
    private OwnerItem oi_ownerpager_feedback;
    private OwnerItem oi_ownerpager_update;
    private OwnerItem oi_ownerpager_initialize;
    private OwnerItem oi_ownerpager_commonQuestion;
    private OwnerItem oi_ownerpager_aboutUs;
    final String VERSION_IN_SERVER_PATH = "http://10.0.2.2:8080/MyAccount/version.json";//version.json在服务器中的地址
    private static final int MSG_OK = 1;
    private static final int MSG_ERROR_NOTFOUND = -1;
    private static final int MSG_ERROR_SERVERINNER = -2;
    private static final int MSG_ERROR_URL = -3;
    private static final int MSG_ERROR_IO = -4;
    private static final int MSG_ERROR_JSON = -5;
    private int CURRENT_VERSION;
    private String downloadurl;
    private ProgressBar pb_ownerpager_downloadapk;

    public OwnerPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        //填充OwnerPager页面
        View OwnerPagerView = View.inflate(mActivity, R.layout.ownerpager_main,null);
        //找出OwnerPagerView中的所有控件
        oi_ownerpager_login = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_login);
        oi_ownerpager_balance = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_balance);
        oi_ownerpager_share = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_share);
        oi_ownerpager_feedback = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_feedback);
        oi_ownerpager_update = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_update);
        oi_ownerpager_initialize = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_initialize);
        oi_ownerpager_commonQuestion = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_commonQuestion);
        oi_ownerpager_aboutUs = (OwnerItem) OwnerPagerView.findViewById(R.id.oi_ownerpager_aboutUs);

        TextView tv_ownerpager_version = (TextView) OwnerPagerView.findViewById(R.id.tv_ownerpager_version);
        pb_ownerpager_downloadapk = (ProgressBar) OwnerPagerView.findViewById(R.id.pb_ownerpager_downloadapk);

        tv_ownerpager_version.setText("v"+getVersionName());


        initLogin();        //登录
        initMyBalance();    //我的余额
        initShareApp();     //分享App
        initClearALl();     //初始化，最初状态
        initFeedback();     //意见反馈
        initUpdate();       //检测更新
        initQuestion();     //常见问题
        initAboutUs();      //关于我们

        return OwnerPagerView;
    }

    //初始化 登录 条目，添加点击事件
    private void initLogin() {
        Log.i(TAG,"initLogin coming!");
        oi_ownerpager_login.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
    }

    //初始化 我的余额 条目，添加点击事件
    private void initMyBalance() {
        Log.i(TAG,"initMyBalance coming!");
        oi_ownerpager_balance.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, MyBalanceActivity.class));
            }
        });
    }

    //初始化 “初始化”条目，添加点击事件
    private void initClearALl() {
        Log.i(TAG,"initClearALl coming!");
        oi_ownerpager_initialize.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, InitializeActivity.class));
            }
        });
    }

    //初始化 常见问题 条目，添加点击事件
    private void initQuestion() {
        Log.i(TAG,"initQuestion coming!");
        oi_ownerpager_commonQuestion.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, QuestionActivity.class));
            }
        });
    }

    //初始化 关于我们 条目，添加点击事件
    private void initAboutUs() {
        Log.i(TAG,"initAboutUs coming!");
        oi_ownerpager_aboutUs.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, AboutUsActivity.class));
            }
        });
    }

    //初始化 意见反馈 条目，添加点击事件
    private void initFeedback() {
        Log.i(TAG,"initFeedback coming!");
        oi_ownerpager_feedback.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, FeedbackActivity.class));
            }
        });
    }

    //初始化 分享App 条目，添加点击事件
    private void initShareApp() {
        Log.i(TAG,"initShareApp coming!");
        oi_ownerpager_share.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                mActivity.startActivity(new Intent(mActivity, ShareAppActivity.class));
            }
        });
    }


    //初始化 检测更新 条目，添加点击事件
    private void initUpdate() {
        Log.i(TAG,"initUpdate coming!");
        oi_ownerpager_update.setMyOwnerItemOnClickListener(new OwnerItem.MyOwnerItemOnClickListener() {
            //为oi_ownerpager_balance条目设置自定义的监听，当该条目被点击测会调用onItemClick()，然后跳转到MyBalance页面
            @Override
            public void onItemClick() {
                Log.i(TAG,"更新App");
                //检测服务器App与本地App的版本是否一致
                //1 获取服务器App版本
                checkVersionFromServer();
                //2 获取本地App版本
                getVersionCode();
                //3 比较App在服务器版本与本地版本是否一致

            }
        });
    }

    /**
     * 起一个线程获取服务器中App的版本，返回消息
     */
    private void checkVersionFromServer() {
        //起一个线程联网
        new Thread(new Runnable() {
            @Override
            public void run() {
                //String urlpath = "http://10.0.2.2/MobileManager/version.json";
                Message msg = new Message();
                try {
                    URL url = new URL(VERSION_IN_SERVER_PATH);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.connect();

                    int responseCode = httpURLConnection.getResponseCode();
                    if(responseCode==200){
                        //确认请求返回
                        //获取返回的json文件
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String jsonString = MyHttpUtils.getStringFromInputStream(inputStream);
                        Log.i(TAG,jsonString);

                        //解析json文件
                        JSONObject json = new JSONObject(jsonString);

                        String new_version = json.getString("new_version");
                        String description = json.getString("description");
                        downloadurl = json.getString("download_url");

                        String[] info = {new_version,description};

                        msg.what = MSG_OK;
                        msg.obj = info;

                    }else{
                        //当连接不成功，响应不成功时
                        if(responseCode==404){
                            msg.what = MSG_ERROR_NOTFOUND;
                        }else if(responseCode==500){
                            msg.what = MSG_ERROR_SERVERINNER;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = MSG_ERROR_URL;//当返回信息是MSG_ERROR_URL，则做出响应的处理
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = MSG_ERROR_IO;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = MSG_ERROR_JSON;
                }finally {
                    //不管是否响应成功，都处理返回来的信息（可以使正确的或错误的）
                    myHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * 匿名内部类：在获取了服务器版本后，比较本地与服务器的版本
     */
    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch(msg.what){
                case MSG_OK:
                    String[] info = (String[]) msg.obj;
                    int versionCode_from_server = Integer.parseInt(info[0]);
                    CURRENT_VERSION = getVersionCode();

                    //如果服务器的版本大于当前的版本，则提示用户去更新
                    if(versionCode_from_server>CURRENT_VERSION){
                        askUserToUpdate(info[1]);//将详细信息一并传递
                    }else{
                        //版本已经是最新的，直接进入主页面
                        Toast.makeText(mActivity, "已经是最新版本了", Toast.LENGTH_SHORT).show();

                    }
                    break;
                case MSG_ERROR_IO:
                case MSG_ERROR_URL:
                case MSG_ERROR_JSON:
                case MSG_ERROR_NOTFOUND:
                case MSG_ERROR_SERVERINNER:
                    Toast.makeText(mActivity, "errorCode="+msg.what, Toast.LENGTH_SHORT).show();
                    break;
            }

            super.handleMessage(msg);
        }
    };

    /**
     * 弹一个Dialog框询问用户是否更新
     * @param description
     */
    private void askUserToUpdate(String description) {
        new AlertDialog.Builder(mActivity)
                .setTitle("发现新版本，是否要更新？")
                .setMessage(description)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //下载服务器上的新版本，然后去安装
                        downloaAPK();
                    }
                })
                .setNegativeButton("否",null)
                .show();
    }

    /**
     * 使用Xutils去下载apk文件
     */
    private void downloaAPK() {
        HttpUtils httpUtils = new HttpUtils();

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String target = sdcard + "/myaccount.apk";
        pb_ownerpager_downloadapk.setVisibility(View.VISIBLE);
        Log.i(TAG,"target ="+target);
        httpUtils.download(downloadurl,target , new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                pb_ownerpager_downloadapk.setVisibility(View.INVISIBLE);
                File apkFile = responseInfo.result;
                Log.i(TAG, apkFile.getTotalSpace() + "");//获取apk文件的大小
                installAPK(apkFile);
            }

            //当下载进度每次有更新的时候都会调用
            @Override
            public void onLoading(long total, long current, boolean isUploading) {

                pb_ownerpager_downloadapk.setMax((int) total);
                pb_ownerpager_downloadapk.setProgress((int) current);

                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 负责安装应用的app叫做PackageInstaller
     * @param apkFile
     */
    private void installAPK(File apkFile) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        mActivity.startActivityForResult(intent,100);
    }

    /**
     * 获取本地App版本
     * @return
     */
    private int getVersionCode() {
        int versionCode = -1;
        //PackageManager可以去获取任意的app信息
        PackageManager packageManager = mActivity.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    "com.example.zs.myaccount",0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取本地App版本名称
     * @return
     */
    private String getVersionName() {
        String versionName = "";
        //PackageManager可以去获取任意的app的信息
        PackageManager packageManager = mActivity.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.mobilemanager.zyt.mobilemanager",0);
            versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    @Override
    public void initData() {

    }
}
