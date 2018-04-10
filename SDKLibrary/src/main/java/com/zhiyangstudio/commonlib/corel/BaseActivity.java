package com.zhiyangstudio.commonlib.corel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.inter.ILifecycle;
import com.zhiyangstudio.commonlib.utils.EmptyUtils;
import com.zhiyangstudio.commonlib.utils.LogListener;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;
import com.zhiyangstudio.commonlib.utils.StatusBarUtils;
import com.zhiyangstudio.commonlib.utils.UiUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by zhiyang on 2018/2/23.
 */

public abstract class BaseActivity extends SupportActivity implements ILifecycle, View.OnClickListener,
        LogListener {
    protected static final int REQ_SDCARD_PERMISSION = 0x110;
    protected static final int REQ_CAMERA_PERMISSION = 0x111;
    protected Context mContext;
    protected LayoutInflater layoutInflater;
    protected int screenWidth;
    protected int screenHeigth;
    protected Unbinder unbinder;
    private PermissionListener mListener;

    private ProgressDialog loadingDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        if (bundle != null) {
            //如果系统回收Activity,但是系统却保留了Fragment,当Activity被重新初始化,此时,系统保存的Fragment 的getActivity为空，
            //所以要移除旧的Fragment,重新初始化新的Fragment
            String FRAGMENTS_TAG = "android:support:fragments";
            bundle.remove(FRAGMENTS_TAG);
        }
        super.onCreate(bundle);

        LoggerUtils.loge(this, "onCreate");
        if (getContentId() != 0) {
            preProcess();
            if (hasSupportTransStatusBar()) {
                StatusBarUtils.setStatusBarAndBottomBarTranslucent(this);
            }

            mContext = this;
            setContentView(getContentId());
            unbinder = ButterKnife.bind(this);

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            screenWidth = displayMetrics.widthPixels;
            screenHeigth = displayMetrics.heightPixels;
            layoutInflater = LayoutInflater.from(mContext);

            // TODO: 2018/4/6 andorid 23以上版本检查运行时权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (hasCheckPermission()
                        && getClass().getSimpleName().equals("MainActivity")
                        && hasCheckPermission()) {
                    checkSDCardPermission(getPermissonCallBack());
                }
            }
            initView();
            doExtOpts();
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

    protected boolean hasCheckPermission() {
        return true;
    }

    protected void checkSDCardPermission(PermissionListener permissionListener) {
        checkPermission(CommonConst.PERMISSION_WRITE_EXTERNAL_STORAGE, "SD卡权限",
                permissionListener, REQ_SDCARD_PERMISSION);
    }

    protected abstract PermissionListener getPermissonCallBack();

    protected void doExtOpts() {

    }

    public void checkPermission(String permission, final String tips, PermissionListener
            listener, int reqPermission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

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

    @Override
    protected void onStart() {
        super.onStart();
        LoggerUtils.loge(this, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoggerUtils.loge(this, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoggerUtils.loge(this, "onDestroy");
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LoggerUtils.loge(this, "onSaveInstanceState");
    }

    public void setOnClick(int viewID) {
        findViewById(viewID).setOnClickListener(this);
    }

    protected void checkCameraPermission(PermissionListener permissionListener) {
        checkPermission(CommonConst.PERMISSION_CAMERA, "CAMERA", permissionListener, REQ_CAMERA_PERMISSION);
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
    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        LoggerUtils.loge(this, "onPause");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LoggerUtils.loge(this, "onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoggerUtils.loge(this, "onResume");
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

    private int showPermissionResult(@NonNull int[] grantResults, String str) {
        int result = -1;
        if (grantResults.length > 0 && grantResults[0] == CommonConst.RESULT_PERMISSION_GRANT) {
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LoggerUtils.loge(this, "onRestoreInstanceState");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoggerUtils.loge(this, "onRestart");
    }

    /**
     * 显示提示对话框
     *
     * @param content         内容
     * @param confirmListener 确定按钮点击事件
     */
    protected void showTipsDialog(String content, DialogInterface.OnClickListener confirmListener) {
        showTipsDialogWithTitle(null, content, UiUtils.getStr(R.string.str_dialog_confirm),
                confirmListener, null, null);
    }

    /**
     * 显示提示对话框（带标题）
     *
     * @param title           标题
     * @param content         内容
     * @param confirmText     确定按钮文字
     * @param confirmListener 确定按钮点击事件
     * @param cancelText      取消按钮文字
     * @param cancelListener  取消按钮点击事件
     */
    protected void showTipsDialogWithTitle(String title, String content, String confirmText,
                                           DialogInterface.OnClickListener confirmListener, String cancelText,
                                           DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (EmptyUtils.isNotEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(content);
        builder.setPositiveButton(confirmText, confirmListener);
        if (EmptyUtils.isNotEmpty(cancelText)) {
            builder.setNegativeButton(cancelText, cancelListener);
        }
        builder.create().show();
    }

    /**
     * 显示提示对话框
     *
     * @param content         内容
     * @param confirmText     确定按钮文字
     * @param confirmListener 确定按钮点击事件
     * @param cancelText      取消按钮文字
     * @param cancelListener  取消按钮点击事件
     */
    protected void showTipsDialog(String content, String confirmText, DialogInterface.OnClickListener confirmListener,
                                  String cancelText, DialogInterface.OnClickListener cancelListener) {
        showTipsDialogWithTitle("", content, confirmText, confirmListener, cancelText, cancelListener);
    }

    /**
     * 显示提示对话框（带标题）
     *
     * @param title           标题
     * @param content         内容
     * @param confirmListener 确定按钮点击事件
     * @param cancelListener  取消按钮点击事件
     */
    protected void showTipsDialogWithTitle(String title, String content, DialogInterface.OnClickListener confirmListener,
                                           DialogInterface.OnClickListener cancelListener) {
        showTipsDialogWithTitle(title, content,
                UiUtils.getStr(R.string.str_dialog_confirm),
                confirmListener, UiUtils.getStr(R.string.str_dialog_cancel),
                cancelListener);
    }

    /**
     * 显示带消息的进度框
     *
     * @param title 提示
     */
    protected void showLoadingDialog(String title) {
        createLoadingDialog();
        loadingDialog.setMessage(title);
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    /**
     * 创建LodingDialog
     */
    private void createLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    /**
     * 显示进度框
     */
    protected void showLoadingDialog() {
        createLoadingDialog();
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    /**
     * 隐藏进度框
     */
    protected void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public interface PermissionListener extends LogListener {
        void onGrant(int code);

        void onDeny(int code);
    }
}
