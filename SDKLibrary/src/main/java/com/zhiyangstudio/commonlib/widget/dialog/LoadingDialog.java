package com.zhiyangstudio.commonlib.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyangstudio.commonlib.R;

/**
 * Created by zhiyang on 2018/5/19.
 */

public class LoadingDialog extends Dialog {

    private static LoadingDialog loadingDialog;

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    public LoadingDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        View loadingView = findViewById(R.id.iv_loading);
        if (loadingView != null && loadingView instanceof ImageView) {
            ImageView ivLoading = (ImageView) loadingView;
            AnimationDrawable animationDrawable = (AnimationDrawable) ivLoading.getBackground();
            animationDrawable.start();
        }
    }

    public void setMessage(CharSequence mesag) {
        if (mesag != null && mesag.length() > 0) {
            TextView tvDesc = (TextView) findViewById(R.id.tv_desc);
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(mesag);
            // 重绘
            tvDesc.invalidate();
        } else {
            findViewById(R.id.tv_desc).setVisibility(View.GONE);
        }
    }

    public static LoadingDialog show(Activity context, String msg, boolean cancelable,
                                     OnCancelListener cancelListener) {
        loadingDialog = new LoadingDialog(context, R.style.loadingDialog);
        loadingDialog.setTitle("");
        loadingDialog.setContentView(R.layout.layout_loading_dialog);
        loadingDialog.setCancelable(cancelable);
        loadingDialog.setMessage(msg);
        if (cancelListener != null)
            loadingDialog.setOnCancelListener(cancelListener);
        // 设置属性
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;
        // 设置背景层透明效果
//        attributes.dimAmount = 0.2f;
        window.setAttributes(attributes);
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 加载数据对话框
     */
    private static Dialog mLoadingDialog;

    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showDialogForLoading(Activity context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showDialogForLoading(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText("加载中...");

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static void hideDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
