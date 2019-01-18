package com.zysdk.vulture.clib.corel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zysdk.vulture.clib.CommonConst;
import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.inter.IActivityLifeCycle;
import com.zysdk.vulture.clib.inter.IPermissionListener;
import com.zysdk.vulture.clib.utils.AppActivityManager;
import com.zysdk.vulture.clib.utils.CommonUtils;
import com.zysdk.vulture.clib.utils.DisplayUtils;
import com.zysdk.vulture.clib.utils.EmptyUtils;
import com.zysdk.vulture.clib.utils.IntentUtils;
import com.zysdk.vulture.clib.utils.LogListener;
import com.zysdk.vulture.clib.utils.LoggerUtils;
import com.zysdk.vulture.clib.utils.ResourceUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by zhiyang on 2018/2/23.
 */

public abstract class BaseActivity extends SupportActivity implements IActivityLifeCycle, View
        .OnClickListener, LogListener {
    protected Context mContext;
    protected LayoutInflater layoutInflater;
    protected int screenWidth;
    protected int screenHeigth;
    protected Unbinder unbinder;
    protected BaseInternalHandler mH = new BaseInternalHandler(this) {
        @Override
        protected void processMessage(Message pMessage) {
            onMessageGoing(pMessage);
        }
    };
    private IPermissionListener mListener;
    private int mReqPermission;
    private String mPermissionTip;
    // 真实的手机屏幕高度:手机像素高度+标题+34
    protected int realScreenHeight;

    protected void onMessageGoing(Message message) {

    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        if (bundle != null) {
            //如果系统回收Activity,但是系统却保留了Fragment,当Activity被重新初始化,此时,系统保存的Fragment 的getActivity为空，
            //所以要移除旧的Fragment,重新初始化新的Fragment
            String FRAGMENTS_TAG = "android:support:fragments";
            bundle.remove(FRAGMENTS_TAG);
        }
        beforeCreate();
        super.onCreate(bundle);

        LoggerUtils.loge(this, "onCreate");
        AppActivityManager.getInstance().addActivity(this);//新建时添加到栈
        if (getContentId() != 0) {
            mContext = this;
            beforeSetContentView();
            //沉浸式状态栏处理
            if (hasSupportTransStatusBar()) {
                setTranslucentStatus(true);
                if (getStatusbarColor() != 0)
                    setSystemBarTintDrawable(getResources().getDrawable(getStatusbarColor()));
            }
        }

        setContentView(getContentId());
        unbinder = ButterKnife.bind(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeigth = displayMetrics.heightPixels;
        realScreenHeight = DisplayUtils.getScreenHeightWithDecorations();

        LoggerUtils.loge(DisplayUtils.getScreenHeight() + "");
        LoggerUtils.loge(DisplayUtils.getScreenWH()[0] + " , " + DisplayUtils.getScreenWH()[1]);
        LoggerUtils.loge(DisplayUtils.getStatusBarHeight() + "");
        LoggerUtils.loge(DisplayUtils.getScreenHeightWithDecorations() + "");

        LoggerUtils.loge(getClass().getName() + ", onCreate context.isFinishing() = " +
                isFinishing() + "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            LoggerUtils.loge(getClass().getName() + ", onCreate context.isDestroyed() = " +
                    isDestroyed() + "");
        }
        LoggerUtils.loge(getClass().getName() + ", onCreate context.isTaskRoot = " + isTaskRoot()
                + "");

        layoutInflater = LayoutInflater.from(mContext);
        // TODO: 2018/4/6 andorid 23以上版本检查运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasCheckPermission()
                    && (getClass().getSimpleName().equals("MainActivity")
                    || getClass().getSimpleName().equals("SplashActivity")
                    || getClass().getSimpleName().equals("WelcomeActivity")
                    || getClass().getSimpleName().equals("StartActivity")
                    || getClass().getSimpleName().equals("GuideActivity"))) {
                checkSDCardPermission(getPermissonCallBack());
            }
        }

        if (CheckUtils.hasPublish()) {
            initData();
            beforeSubContentInit();
            initView();
            addListener();
        }
    }

    @Override
    public void beforeCreate() {

    }

    /**
     * 是否支持沉浸式状态栏
     */
    protected boolean hasSupportTransStatusBar() {
        return false;
    }

    /**
     * set status bar translucency
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    /**
     * 获取需要设置的状态栏颜色
     */
    protected int getStatusbarColor() {
        return R.color.aliceblue;
    }

    /**
     * use SytemBarTintManager
     */
    protected void setSystemBarTintDrawable(Drawable tintDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            if (tintDrawable != null) {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }
    }

    protected boolean hasCheckPermission() {
        return true;
    }

    protected void checkSDCardPermission(IPermissionListener IPermissionListener) {
        checkPermission(CommonConst.PERMISSION.PERMISSION_WRITE_EXTERNAL_STORAGE, "SD卡权限",
                IPermissionListener, CommonConst.PERMISSION.REQ_SDCARD_PERMISSION);
    }

    protected abstract IPermissionListener getPermissonCallBack();

    /**
     * 检查权限
     */
    public void checkPermission(String permission, final String tips, IPermissionListener
            listener, int reqPermission) {
        this.mReqPermission = reqPermission;
        this.mPermissionTip = tips;
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
                showPermissionDenyDialog(tips);

            } else {
                Logger.e("申请权限");
                // 申请授权。
                ActivityCompat.requestPermissions(this, new String[]{permission}, reqPermission);
            }
        } else {
            Logger.e("SD权限已授权");
            switch (reqPermission) {
                case CommonConst.PERMISSION.REQ_SDCARD_PERMISSION:
                case CommonConst.PERMISSION.REQ_CAMERA_PERMISSION:
                    callListener(1, reqPermission);
                    break;
            }
        }
    }

    /**
     * 显示权限拒绝的dialog
     */
    protected void showPermissionDenyDialog(String tips) {
        // TODO: 2018/2/2  用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("温馨提示")
                .setMessage("你拒绝了" + tips + "的权限申请，将会造成" + tips + "相关功能无法使用")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2018/2/1 跳转到应用详情
                        CommonUtils.goAppDetailSetting();
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
    }

    protected void callListener(int result, int code) {
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
    public void beforeSetContentView() {

    }

    @Override
    public void beforeSubContentInit() {

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
        mH.destory();
        AppActivityManager.getInstance().removeActivity(this);

        LoggerUtils.loge(getClass().getName() + ", onDestroy context.isFinishing() = " +
                isFinishing() + "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            LoggerUtils.loge(getClass().getName() + ", onDestroy context.isDestroyed() = " +
                    isDestroyed() + "");
        }
        LoggerUtils.loge(getClass().getName() + ", onDestroy context.isTaskRoot = " + isTaskRoot()
                + "");

        super.onDestroy();
        LoggerUtils.loge(this, "onDestroy");
        if (unbinder != null) {
            unbinder.unbind();
        }
        release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LoggerUtils.loge(this, "onSaveInstanceState");
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        int result = 0;
        switch (requestCode) {
            case CommonConst.PERMISSION.REQ_SDCARD_PERMISSION:
                result = showPermissionResult(grantResults, "SD卡写入");
                callListener(result, CommonConst.PERMISSION.REQ_SDCARD_PERMISSION);
                break;
            case CommonConst.PERMISSION.REQ_CAMERA_PERMISSION:
                result = showPermissionResult(grantResults, "摄像头");
                callListener(result, CommonConst.PERMISSION.REQ_CAMERA_PERMISSION);
                break;
            default:
                result = showPermissionResult(grantResults, mPermissionTip);
                callListener(result, mReqPermission);
                break;
        }
    }

    protected int showPermissionResult(@NonNull int[] grantResults, String str) {
        int result = -1;
        if (grantResults.length > 0 && grantResults[0] == CommonConst.PERMISSION
                .RESULT_PERMISSION_GRANT) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onHomeMenuClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 默认是关闭，子类可以通过重写，进行选择性事件操作
     */
    protected void onHomeMenuClick() {
        finish();
    }

    /**
     * 显示提示对话框
     */
    protected void showTipsDialog(String content, DialogInterface.OnClickListener confirmListener) {
        showTipsDialogWithTitle(null, content, ResourceUtils.getStr(R.string.str_dialog_confirm),
                confirmListener, null, null);
    }

    /**
     * 显示提示对话框（带标题）
     */
    protected void showTipsDialogWithTitle(String title, String content, String confirmText,
                                           DialogInterface.OnClickListener confirmListener,
                                           String cancelText,
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
     */
    protected void showTipsDialog(String content, String confirmText, DialogInterface
            .OnClickListener confirmListener,
                                  String cancelText, DialogInterface.OnClickListener
                                          cancelListener) {
        showTipsDialogWithTitle("", content, confirmText, confirmListener, cancelText,
                cancelListener);
    }

    /**
     * 显示提示对话框（带标题）
     */
    protected void showTipsDialogWithTitle(String title, String content, DialogInterface
            .OnClickListener confirmListener,
                                           DialogInterface.OnClickListener cancelListener) {
        showTipsDialogWithTitle(title, content,
                ResourceUtils.getStr(R.string.str_dialog_confirm),
                confirmListener, ResourceUtils.getStr(R.string.str_dialog_cancel),
                cancelListener);
    }

    public void setOnClick(int viewID) {
        findViewById(viewID).setOnClickListener(this);
    }

    protected void checkCameraPermission(IPermissionListener IPermissionListener) {
        checkPermission(CommonConst.PERMISSION.PERMISSION_CAMERA, "CAMERA", IPermissionListener,
                CommonConst.PERMISSION.REQ_CAMERA_PERMISSION);
    }

    protected boolean checkSelfPermission(String permission, int requestCode) {
        LoggerUtils.loge("checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    protected void forward(String aciton) {
        forward(aciton, null);
    }

    protected void forward(String aciton, Bundle bundle) {
        IntentUtils.forward(aciton, bundle);
    }

    protected void forwardForResult(Class<? extends Activity> activityCls, int reqCode) {
        forwardForResult(activityCls, null, reqCode);
    }

    protected void forwardForResult(Class<? extends Activity> activityCls, Bundle bundle, int
            reqCode) {
        IntentUtils.forwardForResult(activityCls, bundle, reqCode);
    }

    protected void forwardForResult(Intent intent, int reqCode) {
        IntentUtils.forwardForResult(intent, reqCode);
    }

    protected void forwardForResult(String aciton, int reqCode) {
        forwardForResult(aciton, null, reqCode);
    }

    protected void forwardForResult(String aciton, Bundle bundle, int reqCode) {
        IntentUtils.forwardForResult(aciton, bundle, reqCode);
    }

    protected void forward(Class<? extends Activity> cls) {
        forward(cls, null);
    }

    protected static void forward(Class<? extends Activity> activityCls, Bundle bundle) {
        IntentUtils.forward(activityCls, bundle);
    }
}
