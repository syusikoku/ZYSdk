package com.zhiyangstudio.commonlib.helper;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zhiyangstudio.commonlib.inter.IViewHolder;

/**
 * Created by zhiyang on 2018/4/10.
 */

public class ViewHolderHelper implements IViewHolder {
    private final SparseArray<View> mViewCache;
    private final View mConvertView;

    public ViewHolderHelper(View itemView) {
        this.mViewCache = new SparseArray<>();
        this.mConvertView = itemView;
    }

    public void setText(int viewId, CharSequence value) {
        if (TextUtils.isEmpty(value)) return;
        TextView view = findView(viewId);
        view.setText(value);

    }

    @Override
    public <T> T findView(int viewID) {
        View view = mViewCache.get(viewID);
        if (view == null) {
            view = mConvertView.findViewById(viewID);
            mViewCache.put(viewID, view);
        }
        return (T) view;
    }

    @Override
    public void setText(int viewID, String txt) {
        TextView textView = findView(viewID);
        if (textView != null) {
            textView.setText(txt);
        }
    }

    public void setText(int viewId, @StringRes int strId) {
        TextView view = findView(viewId);
        view.setText(strId);

    }

    @Override
    public void setImageResource(int viewId, @DrawableRes int imageResId) {
        ImageView view = findView(viewId);
        view.setImageResource(imageResId);

    }

    @Override
    public void setBackgroundColor(int viewId, int color) {
        View view = findView(viewId);
        view.setBackgroundColor(color);

    }

    @Override
    public void setBackgroundRes(int viewId, @DrawableRes int backgroundRes) {
        View view = findView(viewId);
        view.setBackgroundResource(backgroundRes);

    }

    @Override
    public void setTextColor(int viewId, int textColor) {
        TextView view = findView(viewId);
        view.setTextColor(textColor);

    }

    @Override
    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = findView(viewId);
        view.setImageDrawable(drawable);

    }

    @Override
    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = findView(viewId);
        view.setImageBitmap(bitmap);
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    @Override
    public void setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((View) findView(viewId)).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            ((View) findView(viewId)).startAnimation(alpha);
        }
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     */
    @Override
    public void setVisible(int viewId, boolean visible) {
        View view = findView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Add links into a TextView.
     */
    @Override
    public void linkify(int viewId) {
        TextView view = findView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    @Override
    public void setTypeface(int viewId, Typeface typeface) {
        TextView view = findView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    @Override
    public void setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = findView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }

    @Override
    public void setProgress(int viewId, int progress) {
        ProgressBar view = findView(viewId);
        view.setProgress(progress);
    }

    /**
     * Sets the progress and max of a ProgressBar.
     */
    @Override
    public void setProgress(int viewId, int progress, int max) {
        ProgressBar view = findView(viewId);
        view.setMax(max);
        view.setProgress(progress);
    }

    @Override
    public void setMax(int viewId, int max) {
        ProgressBar view = findView(viewId);
        view.setMax(max);
    }

    @Override
    public void setRating(int viewId, float rating) {
        RatingBar view = findView(viewId);
        view.setRating(rating);
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     */
    @Override
    public void setRating(int viewId, float rating, int max) {
        RatingBar view = findView(viewId);
        view.setMax(max);
        view.setRating(rating);
    }

    /**
     * Sets the on click listener of the view.
     */
    @Override
    public void setOnItemClickListener(int viewId, View.OnClickListener listener) {
        View view = findView(viewId);
        view.setOnClickListener(listener);
    }

    /**
     * Sets the on touch listener of the view.
     */
    @Override
    public void setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = findView(viewId);
        view.setOnTouchListener(listener);
    }

    /**
     * Sets the on long click listener of the view.
     */
    @Override
    public void setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = findView(viewId);
        view.setOnLongClickListener(listener);
    }

    /**
     * Sets the listview or gridview's item click listener of the view
     */
    @Override
    public void setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = findView(viewId);
        view.setOnItemClickListener(listener);
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     */
    @Override
    public void setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = findView(viewId);
        view.setOnItemLongClickListener(listener);
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     */
    @Override
    public void setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = findView(viewId);
        view.setOnItemSelectedListener(listener);
    }

    /**
     * Sets the on checked change listener of the view.
     */
    @Override
    public void setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = findView(viewId);
        view.setOnCheckedChangeListener(listener);
    }

    /**
     * Sets the tag of the view.
     */
    @Override
    public void setTag(int viewId, Object tag) {
        View view = findView(viewId);
        view.setTag(tag);
    }

    /**
     * Sets the tag of the view.
     */
    @Override
    public void setTag(int viewId, int key, Object tag) {
        View view = findView(viewId);
        view.setTag(key, tag);
    }

    /**
     * Sets the checked status of a checkable.
     */
    @Override
    public void setChecked(int viewId, boolean checked) {
        View view = findView(viewId);
        // View unable cast to Checkable
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
    }
}
