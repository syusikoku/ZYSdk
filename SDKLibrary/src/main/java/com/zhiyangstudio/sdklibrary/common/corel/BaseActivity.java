package com.zhiyangstudio.sdklibrary.common.corel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import com.example.common.utils.StatusBarUtils;
import com.example.utils.LogListener;
import com.example.utils.LoggerUtils;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by example on 2018/2/23.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, LogListener {
    protected static final int REQ_SDCARD_PERMISSION = 0x110;
    protected static final int REQ_CAMERA_PERMISSION = 0x111;

    private Unbinder unbinder;
    protected Context mContext;
    protected LayoutInflater layoutInflater;
    private PermissionListener mListener;
    protected int screenWidth;
    protected int screenHeigth;

    protected abstract int getContentViewId();

    protected abstract void initView();

    protected abstract void addListener();

    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoggerUtils.loge(this, "onCreate");
        if (getContentViewId() != 0) {
            preprocess();
            if (hasSupportTransStatusBar()) {
                StatusBarUtils.setStatusBarAndBottomBarTranslucent(this);
            }
            setContentView(getContentViewId());
            mContext = this;
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            screenWidth = displayMetrics.widthPixels;
            screenHeigth = displayMetrics.heightPixels;
            layoutInflater = LayoutInflater.from(mContext);

            unbinder = ButterKnife.bind(this);

            if (hasCheckPermission()
                    && getClass().getSimpleName().equals("MainActivity")
                    && hasCheckPermission()) {
                checkSDCardPermission(getPermissonCallBack());
            }
            initView();
            addListener();
            initData();
        } else {
            throw new IllegalArgumentException("请使用合法的布局文件");
        }
    }

    /**
     * 是否支持沉浸式状态栏
     */
    protected boolean hasSupportTransStatusBar() {
        return false;
    }

    protected void preprocess() {

    }

    protected boolean hasCheckPermission() {
        return true;
    }

    protected abstract PermissionListener getPermissonCallBack();

    protected void preBuildUi() {

    }

    public void setOnClick(int viewID) {
        findViewById(viewID).setOnClickListener(this);
    }

    protected void checkSDCardPermission(PermissionListener permissionListener) {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD卡权限",
                permissionListener, REQ_SDCARD_PERMISSION);
    }

    protected void checkCameraPermission(PermissionListener permissionListener) {
        checkPermission(Manifest.permission.CAMERA, "CAMERA", permissionListener, REQ_CAMERA_PERMISSION);
    }

    public void checkPermission(String permission, final String tips, PermissionListener
            listener, int reqPermission) {
        this.mListener = listener;
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            Logger.e(tips + "权限未授权");
            // TODO: 2018/2/2 没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Logger.e("拒绝过" + tips + "的权限，提示用户");
                // TODO: 2018/2/2  用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                builder.setTitle("温馨提示")
                        .setMessage("你拒绝了" + tips + "的权限申请，将会造成" + tips + "相关功能无法使用")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: 2018/2/1 跳转到应用详情
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Logger.e("申请权限");
                // 申请授权。
                ActivityCompat.requestPermissions(this, new String[]{permission}, reqPermission);
            }
        } else {
            Logger.e("SD权限已授权");
            switch (reqPermission) {
                case REQ_SDCARD_PERMISSION:
                case REQ_CAMERA_PERMISSION:
                    callListener(1, reqPermission);
                    break;
            }
        }
    }

    protected void forward(Class<? extends Activity> activityCls) {
        if (activityCls == null) {
            return;
        }

        startActivity(new Intent(this, activityCls));
    }

    protected void forward(Class<? extends Activity> activityCls, Bundle bundle) {
        if (activityCls == null) {
            return;
        }

        Intent intent = new Intent(this, activityCls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_SDCARD_PERMISSION:
                int result = showPermissionResult(grantResults, "SD卡写入");
                callListener(result, REQ_SDCARD_PERMISSION);
                break;
            case REQ_CAMERA_PERMISSION:
                int resut = showPermissionResult(grantResults, "摄像头");
                callListener(resut, REQ_CAMERA_PERMISSION);
                break;
        }
    }

    private void callListener(int result, int code) {
        if (result == -1) {
            if (mListener != null) {
                mListener.onDeny(code);
            }
        } else {
            if (mListener != null) {
                mListener.onGrant(code);
            }
        }
    }

    private int showPermissionResult(@NonNull int[] grantResults, String str) {
        int result = -1;
        if (grantResults.length > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
            // TODO: 2018/2/2 权限允许了
            Logger.e(str + "权限允许了");
            result = 1;
        } else {
            // TODO: 2018/2/2 权限拒绝了
            Logger.e(str + "权限拒绝了");
        }
        return result;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        LoggerUtils.loge(this, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoggerUtils.loge(this, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoggerUtils.loge(this, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoggerUtils.loge(this, "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LoggerUtils.loge(this, "onNewIntent");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LoggerUtils.loge(this, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LoggerUtils.loge(this, "onRestoreInstanceState");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoggerUtils.loge(this, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoggerUtils.loge(this, "onDestroy");
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public interface PermissionListener extends LogListener {
        void onGrant(int code);

        void onDeny(int code);
    }
}
