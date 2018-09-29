package com.zysdk.vulture.clib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.utils.CommonUtils;
import com.zysdk.vulture.clib.utils.EmptyUtils;
import com.zysdk.vulture.clib.utils.ResourceUtils;

/**
 * 通用的自定义toolbar
 */
public class CommonToolbar extends Toolbar {
    private final Context mContext;
    ImageView ivHomeIcon, ivExt01, ivExt02;
    TextView tvExt, tvTitle;
    private View view;
    private LayoutInflater inflater;
    private String titleStr, emTitleStr;
    private int titleRes, emIcon01Res, emIcon02Res;
    private int titleTxtColorRes, emTitleTextColorRes;
    private int emTitleTxtRes;
    private int titleColor;
    private int emTitleColor;
    private boolean hasShowEmTtile;
    private boolean hasShowEmIcon01;
    private boolean hasShowEmIcon02;
    private int emTitleTextSize;
    private int emIcon01Size;
    private int emIcon02Size;
    private int emTitleTextSizeIV;
    private int emIcon01SizeIV;
    private int emIcon02SizeIV;
    private int mHomeIconRes;
    private boolean hasShowHomeIcon;
    private int emHomeIconSize;
    private int emHomeIconSizeIV;

    public CommonToolbar(Context context) {
        this(context, null);
    }

    public CommonToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        createView();
        initView();
        initAttr(attrs);
        setContentInsetStartWithNavigation(0);
    }

    private void createView() {
        inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.layout_common_toolbar, this, false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            android.widget.Toolbar.LayoutParams lap = new android.widget.Toolbar
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(view, lap);
        } else {
            ViewGroup.LayoutParams lap = new ViewGroup.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(view, lap);
        }
    }

    private void initView() {
        tvTitle = view.findViewById(R.id.tv_title_toolbar);
        ivHomeIcon = view.findViewById(R.id.iv_icon_toolbar);
        tvExt = view.findViewById(R.id.tv_em_toolbar);
        ivExt01 = view.findViewById(R.id.iv_em_icon_01);
        ivExt02 = view.findViewById(R.id.iv_em_icon_02);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.CommonToolbar);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarTitle))
            titleStr = attributes.getString(R.styleable.CommonToolbar_ToolbarTitle);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmTitle))
            emTitleStr = attributes.getString(R.styleable.CommonToolbar_ToolbarEmTitle);

        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarTitleTextColor))
            titleColor = attributes.getColor(R.styleable.CommonToolbar_ToolbarTitleTextColor, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmTitleTextColor))
            emTitleColor = attributes.getColor(R.styleable.CommonToolbar_ToolbarEmTitleTextColor,
                    0);

        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarTitle))
            titleRes = attributes.getResourceId(R.styleable.CommonToolbar_ToolbarTitle, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarTitleTextColor))
            titleTxtColorRes = attributes.getResourceId(R.styleable
                    .CommonToolbar_ToolbarTitleTextColor, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmTitle))
            emTitleTxtRes = attributes.getResourceId(R.styleable.CommonToolbar_ToolbarEmTitle, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmTitleTextColor))
            emTitleTextColorRes = attributes.getResourceId(R.styleable
                    .CommonToolbar_ToolbarEmTitleTextColor, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmIcon01Res))
            emIcon01Res = attributes.getResourceId(R.styleable.CommonToolbar_ToolbarEmIcon01Res, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmIcon02Res))
            emIcon02Res = attributes.getResourceId(R.styleable.CommonToolbar_ToolbarEmIcon02Res, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarHomeIconRes))
            mHomeIconRes = attributes.getResourceId(R.styleable.CommonToolbar_ToolbarHomeIconRes,
                    0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarShowEmTitle))
            hasShowEmTtile = attributes.getBoolean(R.styleable.CommonToolbar_ToolbarShowEmTitle,
                    false);

        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarShowEmIcon01))
            hasShowEmIcon01 = attributes.getBoolean(R.styleable.CommonToolbar_ToolbarShowEmIcon01,
                    false);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarShowEmIcon02))
            hasShowEmIcon02 = attributes.getBoolean(R.styleable.CommonToolbar_ToolbarShowEmIcon02,
                    false);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarShowHomeIcon))
            hasShowHomeIcon = attributes.getBoolean(R.styleable.CommonToolbar_ToolbarShowHomeIcon,
                    false);

        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmTitleTextSize))
            emTitleTextSize = attributes.getDimensionPixelOffset(R.styleable
                    .CommonToolbar_ToolbarEmTitleTextSize, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmIcon01Size))
            emIcon01Size = attributes.getDimensionPixelOffset(R.styleable
                    .CommonToolbar_ToolbarEmIcon01Size, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmIcon02Size))
            emIcon02Size = attributes.getDimensionPixelOffset(R.styleable
                    .CommonToolbar_ToolbarEmIcon02Size, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarShowHomeIconSize))
            emHomeIconSize = attributes.getDimensionPixelOffset(R.styleable
                    .CommonToolbar_ToolbarShowHomeIconSize, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmTitleTextSize))
            emTitleTextSizeIV = attributes.getInteger(R.styleable
                    .CommonToolbar_ToolbarEmTitleTextSize, 0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmIcon01Size))
            emIcon01SizeIV = attributes.getInteger(R.styleable.CommonToolbar_ToolbarEmIcon01Size,
                    0);
        if (hasTarAttr(attributes, R.styleable
                .CommonToolbar_ToolbarEmIcon02Size))
            emIcon02SizeIV = attributes.getInteger(R.styleable.CommonToolbar_ToolbarEmIcon02Size,
                    0);

//        if (hasTarAttr(attributes, R.styleable
//                .CommonToolbar_ToolbarShowHomeIconSize))
//            emHomeIconSizeIV = attributes.getInteger(R.styleable
//                    .CommonToolbar_ToolbarShowHomeIconSize, 0);
        setDefData();
        attributes.recycle();
    }

    private boolean hasTarAttr(TypedArray attributes, int index) {
        return attributes.hasValue(index);
    }

    private void setDefData() {
        setTitle(titleStr);
        setTitle(titleRes);
        setEMTitle(emTitleStr);
        setEMTitle(emTitleTxtRes);

        setTitleTextColor(titleColor);
        setEMTitleTextColor(titleColor);
        setEMTitleTextColor(emTitleColor);
        setTitleTextColor(ResourceUtils.getColor(titleTxtColorRes));
        setEMTitleTextColor(ResourceUtils.getColor(emTitleTextColorRes));

        setEMIcon01ImgRes(emIcon01Res);
        setEMIcon02ImgRes(emIcon02Res);
        showHomeIcon(mHomeIconRes);

        changeEmTitleState(hasShowEmTtile);
        changeEmIcon01State(hasShowEmIcon01);
        changeEmIcon02State(hasShowEmIcon02);
        changeHomeIconSate(hasShowHomeIcon);

        setEMTitleTextSize(emTitleTextSize);
        setEmIcon01Size(emIcon01Size);
        setEmIcon02Size(emIcon02Size);
        setHomeIconSize(emHomeIconSize);

        setEMTitleTextSize(ResourceUtils.getDimensionPixelOffset(emTitleTextSizeIV));
        setEmIcon01Size(ResourceUtils.getDimensionPixelOffset(emIcon01SizeIV));
        setEmIcon01Size(ResourceUtils.getDimensionPixelOffset(emIcon02SizeIV));
        setHomeIconSize(ResourceUtils.getDimensionPixelOffset(emHomeIconSizeIV));
    }

    private void setHomeIconSize(int size) {
        if (size == 0)
            return;

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivHomeIcon.getLayoutParams();
        if (lp != null) {
            lp.width = size;
            lp.height = size;
            ivHomeIcon.setLayoutParams(lp);
        }
    }

    private void changeHomeIconSate(boolean show) {
        CommonUtils.changeViewState(ivHomeIcon, show ? CommonUtils.ViewState.SHOW : CommonUtils
                .ViewState.GONE);
    }

    private void showHomeIcon(int res) {
        if (res == 0)
            return;
        ivHomeIcon.setImageResource(res);
    }

    private void setEmIcon02Size(int size) {
        if (size == 0)
            return;
    }

    private void setEmIcon01Size(int size) {
        if (size == 0)
            return;
    }

    @Override
    public void setTitle(int resId) {
        if (resId == 0)
            return;
        setTitle(ResourceUtils.getStr(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        if (EmptyUtils.isEmpty(title))
            return;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        if (color == 0)
            return;
        if (tvTitle != null) {
            tvTitle.setTextColor(color);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setEMTitle(int res) {
        if (res == 0)
            return;
        setEMTitle(ResourceUtils.getStr(res));
    }

    public void setEMTitle(String str) {
        if (EmptyUtils.isEmpty(str))
            return;
        if (tvExt != null) {
            tvExt.setText(str);
        }
        CommonUtils.changeViewState(tvExt, CommonUtils.ViewState.SHOW);
    }

    private void changeEmTitleState(boolean hasShow) {
        CommonUtils.changeViewState(tvExt, hasShow ? CommonUtils.ViewState.SHOW : CommonUtils
                .ViewState.GONE);
    }

    private void changeEmIcon01State(boolean hasShow) {
        CommonUtils.changeViewState(ivExt01, hasShow ? CommonUtils.ViewState.SHOW : CommonUtils
                .ViewState.GONE);
    }

    private void changeEmIcon02State(boolean hasShow) {
        CommonUtils.changeViewState(ivExt02, hasShow ? CommonUtils.ViewState.SHOW : CommonUtils
                .ViewState.GONE);
    }

    public void setTitleTextSize(int size) {
        if (size == 0)
            return;
        if (tvTitle != null) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
        CommonUtils.changeViewState(tvTitle, CommonUtils.ViewState.SHOW);
    }

    public void setEMTitleTextSize(int size) {
        if (size == 0)
            return;
        if (tvExt != null) {
            tvExt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
        CommonUtils.changeViewState(tvExt, CommonUtils.ViewState.SHOW);
    }

    public void setEMTitleTextColor(int color) {
        if (color == 0)
            return;
        if (tvExt != null) {
            tvExt.setTextColor(color);
        }
        CommonUtils.changeViewState(tvExt, CommonUtils.ViewState.SHOW);
    }

    public void setHomeIconImgRes(int res) {
        if (res == 0)
            return;
        if (ivHomeIcon != null) {
            ivHomeIcon.setImageResource(res);
        }
        CommonUtils.changeViewState(ivHomeIcon, CommonUtils.ViewState.SHOW);
    }

    public void setHomeIconBgRes(int res) {
        if (res == 0)
            return;
        if (ivHomeIcon != null) {
            ivHomeIcon.setBackgroundResource(res);
        }
        CommonUtils.changeViewState(ivHomeIcon, CommonUtils.ViewState.SHOW);
    }

    public void setHomeIconImgBmp(Bitmap res) {
        if (res == null)
            return;
        if (ivHomeIcon != null) {
            ivHomeIcon.setImageBitmap(res);
        }
        CommonUtils.changeViewState(ivHomeIcon, CommonUtils.ViewState.SHOW);
    }

    public void setHomeIconImgDraw(Drawable res) {
        if (res == null)
            return;
        if (ivHomeIcon != null) {
            ivHomeIcon.setImageDrawable(res);
        }
        CommonUtils.changeViewState(ivHomeIcon, CommonUtils.ViewState.SHOW);
    }

    public void setHomeIconBgColor(int res) {
        if (res == 0)
            return;
        if (ivHomeIcon != null) {
            ivHomeIcon.setBackgroundColor(res);
        }
        CommonUtils.changeViewState(ivHomeIcon, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon01ImgRes(int res) {
        if (res == 0)
            return;
        if (ivExt01 != null) {
            ivExt01.setImageResource(res);
        }
        CommonUtils.changeViewState(ivExt01, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon01BgRes(int res) {
        if (res == 0)
            return;
        if (ivExt01 != null) {
            ivExt01.setBackgroundResource(res);
        }
        CommonUtils.changeViewState(ivExt01, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon01ImgBmp(Bitmap res) {
        if (res == null)
            return;
        if (ivExt01 != null) {
            ivExt01.setImageBitmap(res);
        }
        CommonUtils.changeViewState(ivExt01, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon01ImgDraw(Drawable res) {
        if (res == null)
            return;
        if (ivExt01 != null) {
            ivExt01.setImageDrawable(res);
        }
        CommonUtils.changeViewState(ivExt01, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon01BgColor(int res) {
        if (res == 0)
            return;
        if (ivExt01 != null) {
            ivExt01.setBackgroundColor(res);
        }
        CommonUtils.changeViewState(ivExt01, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon02ImgRes(int res) {
        if (ivExt02 != null) {
            ivExt02.setImageResource(res);
        }
        CommonUtils.changeViewState(ivExt02, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon02BgRes(int res) {
        if (res == 0)
            return;
        if (ivExt02 != null) {
            ivExt02.setBackgroundResource(res);
        }
        CommonUtils.changeViewState(ivExt02, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon02ImgBmp(Bitmap res) {
        if (res == null)
            return;
        if (ivExt02 != null) {
            ivExt02.setImageBitmap(res);
        }
        CommonUtils.changeViewState(ivExt02, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon02ImgDraw(Drawable res) {
        if (res == null)
            return;
        if (ivExt02 != null) {
            ivExt02.setImageDrawable(res);
        }
        CommonUtils.changeViewState(ivExt02, CommonUtils.ViewState.SHOW);
    }

    public void setEMIcon02BgColor(int res) {
        if (res == 0)
            return;
        if (ivExt02 != null) {
            ivExt02.setBackgroundColor(res);
        }
        CommonUtils.changeViewState(ivExt02, CommonUtils.ViewState.SHOW);
    }

}
