package com.zhiyangstudio.commonlib.inter;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

/**
 * Created by zhiyang on 2018/4/10.
 */

public interface IViewHolder {
    <T> T findView(int viewID);

    void setText(int viewID, String txt);

    void setText(int viewId, @StringRes int strId);

    void setImageResource(int viewId, @DrawableRes int imageResId);

    void setBackgroundColor(int viewId, int color);

    void setBackgroundRes(int viewId, @DrawableRes int backgroundRes);

    void setTextColor(int viewId, int textColor);

    void setImageDrawable(int viewId, Drawable drawable);

    void setImageBitmap(int viewId, Bitmap bitmap);

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    void setAlpha(int viewId, float value);

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     */
    void setVisible(int viewId, boolean visible);

    /**
     * Add links into a TextView.
     */
    void linkify(int viewId);

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    void setTypeface(int viewId, Typeface typeface);

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    void setTypeface(Typeface typeface, int... viewIds);

    void setProgress(int viewId, int progress);

    /**
     * Sets the progress and max of a ProgressBar.
     */
    void setProgress(int viewId, int progress, int max);

    void setMax(int viewId, int max);

    void setRating(int viewId, float rating);

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     */
    void setRating(int viewId, float rating, int max);

    /**
     * Sets the on click listener of the view.
     */
    void setOnItemClickListener(int viewId, View.OnClickListener listener);

    /**
     * Sets the on touch listener of the view.
     */
    void setOnTouchListener(int viewId, View.OnTouchListener listener);

    /**
     * Sets the on long click listener of the view.
     */
    void setOnLongClickListener(int viewId, View.OnLongClickListener listener);

    /**
     * Sets the listview or gridview's item click listener of the view
     */
    void setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener);

    /**
     * Sets the listview or gridview's item long click listener of the view
     */
    void setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener);

    /**
     * Sets the listview or gridview's item selected click listener of the view
     */
    void setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener);

    /**
     * Sets the on checked change listener of the view.
     */
    void setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener);

    /**
     * Sets the tag of the view.
     */
    void setTag(int viewId, Object tag);

    /**
     * Sets the tag of the view.
     */
    void setTag(int viewId, int key, Object tag);

    /**
     * Sets the checked status of a checkable.
     */
    void setChecked(int viewId, boolean checked);
}
