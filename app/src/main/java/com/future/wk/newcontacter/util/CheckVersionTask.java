package com.future.wk.newcontacter.util;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;


import com.future.wk.newcontacter.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2015/12/21.
 */
    /*
 * 从服务器获取xml解析并进行比对版本号
 */
public class CheckVersionTask implements Runnable {
    private WifiManager mWifiManager;
    private WifiManager.WifiLock mWifiLock;
    private PowerManager.WakeLock wakeLock = null;
    private Context context;
    private String TAG = "CheckVersionTask.";
    private UpdataInfo info;
    private DownloadManager downloadManager;
    private SharedPreferences url_sp;
    private SharedPreferences.Editor url_editor;
    private final static int DOWNLOAD_APK = 1;
    private final static int DOWNLOAD_FAIL = 2;
    private final static int DOWNLOAD_TIMEOUT = 3;

    private final static String UPDATE_CHECK_URL = "http://android.myapp.com/myapp/detail.htm?apkName=com.wty.app.bluetoothcar";

    public CheckVersionTask(Context context){
        this.context = context;
        url_sp = this.context.getSharedPreferences("url", Context.MODE_PRIVATE);
        url_editor = url_sp.edit();
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiLock =	mWifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF,"wifilock");
    }

    public void run() {
        try {
            //包装成url的对象
            /*URL url = new URL(UPDATE_CHECK_URL);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            InputStream is =conn.getInputStream();
            info =  UpdataInfoParser.getUpdataInfo(is);*/
            info = new UpdataInfo();
            info.setVersion("2.0");
            info.setXml_url("www.baidu.com");
            Log.d(TAG,"getVersionName:"+getVersionName());

            if(Float.parseFloat(info.getVersion()) <= Float.parseFloat(getVersionName())){
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("版本号相同无需升级")
                        .show();
            }else{
                Log.d(TAG, "版本号不同 ,提示用户升级 ");
                Message msg = new Message();
                msg.what = DOWNLOAD_APK;
                handler.sendMessage(msg);
            }
            url_editor.putString("xmlUrl",info.getXml_url());
            url_editor.commit();
        } catch (Exception e) {
            // 待处理
            Message msg = new Message();
            msg.what = DOWNLOAD_FAIL;
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }

    /*
* 获取当前程序的版本号
*/
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD_APK:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case DOWNLOAD_TIMEOUT:
                    //服务器超时
                    Toast.makeText(context.getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOAD_FAIL:
                    //下载apk失败
                    Toast.makeText(context.getApplicationContext(), "下载新版本失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    protected void showUpdataDialog() {
        /*AlertDialog.Builder builer = new AlertDialog.Builder(context) ;
        builer.setTitle("版本升级");
        builer.setMessage(info.getTip());
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "下载apk,更新");
                downLoadApk();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        builer.setCancelable(false);
        builer.show();*/


        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("升级到最新版本？");
        dialog.setContentText(info.getTip());
        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Log.d(TAG, "下载apk,更新");
                sweetAlertDialog.cancel();
                downLoadApk();
            }
        });
        dialog.setCancelText("取消");
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        dialog.show();

    }

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd;	//进度条对话框
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    if (!mWifiLock.isHeld())
                        mWifiLock.acquire();
                    downloadManager = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
                    downLoadFile(info.getApk_url(), pd);
                    sleep(1000);
                    if (null!=mWifiLock&&mWifiLock.isHeld())
                        mWifiLock.release();
                    installApk();
//                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                    try {
                        if (null!=mWifiLock&&mWifiLock.isHeld())
                            mWifiLock.release();
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }}.start();
    }

   /* public void downLoadFile(final String url,final ProgressDialog pDialog) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            int length = (int) entity.getContentLength();   //获取文件大小
            pDialog.setMax(length);                            //设置进度条的总长度
            InputStream is = entity.getContent();
            FileOutputStream fileOutputStream = null;
            if (is != null) {
                File file = new File(
                        Environment.getExternalStorageDirectory(),
                        "HC_101.apk");
                fileOutputStream = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int ch = -1;
                int process = 0;
                while ((ch = is.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, ch);
                    process += ch;
                    pDialog.setProgress(process);       //这里就是关键的实时更新进度了！
                }

            }
            fileOutputStream.flush();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            pDialog.cancel();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static File downLoadFile(String path, ProgressDialog pd)
            throws Exception {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            String sdpath = Environment.getExternalStorageDirectory()
                    + File.separator + "esTobacco";
            File folder = new File(sdpath);
            if (!folder.exists())
                folder.mkdirs();
            File file = new File(sdpath, "tobacco.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }


    void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "HC_101.apk")),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
