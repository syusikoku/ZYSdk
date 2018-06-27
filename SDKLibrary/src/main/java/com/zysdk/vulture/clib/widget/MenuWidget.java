package com.zysdk.vulture.clib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.glide.GlideUtils;
import com.zysdk.vulture.clib.utils.DisplayUtils;
import com.zysdk.vulture.clib.utils.EmptyUtils;
import com.zysdk.vulture.clib.utils.ResourceUtils;
import com.zysdk.vulture.clib.utils.UiUtils;

/**
 * Created by zzg on 2018/5/26.
 * 个人中心，设置中心，通用的菜单条目
 * 菜单结构:
 * // *               主标题
 * // *  菜单图标                      扩展标题 | 更多图标 | 更多文字描述
 * // *               子标题
 */

public class MenuWidget extends RelativeLayout {

    private String mTitle, extMsgStr, tvMoreStr, mSubTitle;
    private TextView mTvTitle, tvExtMsg, tvMore;
    private TextView mTvSubTitle;
    private boolean isShowLeftIcon, showCheckbox, showExtMsg, showTvMore, hideSubTitle,
            isShowDivider2;
    private ImageView mLeftIcon, mRightIcon;
    private View vDivider2;
    private int titleColor;
    private int iconRes;
    private CheckBox checkBox;
    private int cbBgRes;
    private boolean showMoreIcon;

    public MenuWidget(Context context) {
        super(context);
        initProperties(context, null);
    }

    private void initProperties(Context context, AttributeSet attrs) {
        initContent();
        initAttrs(context, attrs);
        initView();
        preAttr();
    }

    private void initContent() {
        UiUtils.inflateViewY(R.layout.layout_menu_widget, this);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuWidget);
        mTitle = typedArray.getString(R.styleable.MenuWidget_Title);
        mSubTitle = typedArray.getString(R.styleable.MenuWidget_SubTitle);
        extMsgStr = typedArray.getString(R.styleable.MenuWidget_extMsgStr);
        tvMoreStr = typedArray.getString(R.styleable.MenuWidget_tvMoreStr);
        isShowLeftIcon = typedArray.getBoolean(R.styleable.MenuWidget_showLeftIcon, false);
        isShowDivider2 = typedArray.getBoolean(R.styleable.MenuWidget_showDivider2, false);
        hideSubTitle = typedArray.getBoolean(R.styleable.MenuWidget_hideSubTitle, false);
        showCheckbox = typedArray.getBoolean(R.styleable.MenuWidget_showCheckbox, false);
        showExtMsg = typedArray.getBoolean(R.styleable.MenuWidget_showExtMsg, false);
        showTvMore = typedArray.getBoolean(R.styleable.MenuWidget_showTvMore, false);
        showMoreIcon = typedArray.getBoolean(R.styleable.MenuWidget_showMoreIcon, true);
        titleColor = typedArray.getColor(R.styleable.MenuWidget_TitleTextColor, ResourceUtils
                .getColor(R.color.black));
        iconRes = typedArray.getResourceId(R.styleable.MenuWidget_leftIconRes, 0);
        cbBgRes = typedArray.getResourceId(R.styleable.MenuWidget_cbBgRes, 0);

        typedArray.recycle();
    }

    private void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        mTvSubTitle = findViewById(R.id.tv_sub_title);
        mLeftIcon = findViewById(R.id.ic_left);
        mRightIcon = findViewById(R.id.ic_right);
        vDivider2 = findViewById(R.id.v_divider2);
        tvExtMsg = findViewById(R.id.tv_ext_msg);
        checkBox = findViewById(R.id.cb_more);
        tvMore = findViewById(R.id.tv_more);
    }

    /**
     * 处理属性
     */
    private void preAttr() {
        if (EmptyUtils.isNotEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }
        if (EmptyUtils.isNotEmpty(mSubTitle)) {
            mTvSubTitle.setText(mSubTitle);
        }
        if (isShowLeftIcon) {
            mLeftIcon.setVisibility(View.VISIBLE);
        }
        if (hideSubTitle) {
            mTvSubTitle.setVisibility(GONE);
        }
        if (isShowDivider2) {
            vDivider2.setVisibility(View.VISIBLE);
        }
        if (titleColor != 0) {
            mTvTitle.setTextColor(titleColor);
        }
        if (iconRes != 0) {
            setLeftIcon(iconRes);
        }
        if (cbBgRes != 0) {
            setCheckboxBgRes(cbBgRes);
        }
        setTvExtMsg(extMsgStr);
        setTvMoreMsg(tvMoreStr);
        showCheckbox(showCheckbox);
        showTvMore(showTvMore);
        showMoreIcon(showMoreIcon);
        showTvExtMsg(showExtMsg);
    }

    private void showMoreIcon(boolean hasShow) {
        if (hasShow) {
            mRightIcon.setVisibility(View.VISIBLE);
        } else {
            mRightIcon.setVisibility(View.GONE);
        }
    }

    private void setTvMoreMsg(String str) {
        if (EmptyUtils.isNotEmpty(str)) {
            tvMore.setText(str);
        }
    }

    private void setTvExtMsg(String str) {
        if (EmptyUtils.isNotEmpty(str)) {
            tvExtMsg.setText(str);
        }
    }

    private void setCheckboxBgRes(int res) {
        if (res > 0)
            checkBox.setBackgroundResource(res);
    }

    public MenuWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperties(context, attrs);
    }

    public void setTitle(String title) {
        if (EmptyUtils.isNotEmpty(title))
            mTvTitle.setText(title);
    }

    public void setTitle(int titleResid) {
        if (titleResid > 0)
            mTvTitle.setText(UiUtils.getStr(titleResid));
    }

    public void setSubTitle(String title) {
        if (EmptyUtils.isNotEmpty(title))
            mTvTitle.setText(title);
    }

    public void setSubTitle(int titleResid) {
        if (titleResid > 0)
            mTvTitle.setText(UiUtils.getStr(titleResid));
    }

    public void setLeftIcon(int resId) {
        if (resId > 0)
            mLeftIcon.setBackgroundResource(resId);
    }

    public void setLeftIconSrc(int resId) {
        if (resId > 0)
            mLeftIcon.setImageResource(resId);
    }

    public void setLeftIconBgColor(int resId) {
        if (resId > 0)
            mLeftIcon.setBackgroundColor(resId);
    }

    public void setLeftIconBgColor(Drawable resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (resId != null)
                mLeftIcon.setBackground(resId);
        }
    }

    public void setRightIcon(int resId) {
        if (resId > 0)
            mRightIcon.setBackgroundResource(resId);
    }

    public void setRightIconSrc(int resId) {
        if (resId > 0)
            mRightIcon.setImageResource(resId);
    }

    public void setRightIconBgColor(int resId) {
        if (resId > 0)
            mRightIcon.setBackgroundColor(resId);
    }

    public void setRightIconBgColor(Drawable resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRightIcon.setBackground(resId);
        }
    }

    public void setLeftIcon(String iconUrl) {
        if (EmptyUtils.isNotEmpty(iconUrl))
            GlideUtils.loadPic(getContext(), iconUrl, mLeftIcon);
    }

    public void setLeftIconSize(int sW, int sH) {
        if (sW > 0 && sH > 0) {
            LayoutParams lP = (LayoutParams) mLeftIcon.getLayoutParams();
            lP.width = sW;
            lP.height = sH;
            mLeftIcon.setLayoutParams(lP);

            LinearLayout.LayoutParams rLp = (LinearLayout.LayoutParams) vDivider2.getLayoutParams();
            rLp.leftMargin = sW + DisplayUtils.dp2px(12);
            vDivider2.setLayoutParams(rLp);
        }
    }

    public void hideDivider2() {
        if (vDivider2 != null && vDivider2.getVisibility() != View.GONE) {
            vDivider2.setVisibility(View.GONE);
        }
    }

    public void setExtMsg(String str) {
        if (EmptyUtils.isNotEmpty(str)) {
            tvExtMsg.setText(str);
            tvExtMsg.setVisibility(View.VISIBLE);
        }
    }

    public void setMoreStr(String str) {
        if (EmptyUtils.isNotEmpty(str)) {
            tvMore.setText(str);
            tvMore.setVisibility(View.VISIBLE);
        }
    }

    public void showCheckbox(boolean hasShow) {
        if (hasShow) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }
    }

    public void showTvMore(boolean hasShow) {
        if (hasShow) {
            tvMore.setVisibility(View.VISIBLE);
        } else {
            tvMore.setVisibility(View.GONE);
        }
    }

    public void showTvExtMsg(boolean hasShow) {
        if (hasShow) {
            tvExtMsg.setVisibility(View.VISIBLE);
        } else {
            tvExtMsg.setVisibility(View.GONE);
        }
    }
}
