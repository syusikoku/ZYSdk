package com.zhiyangstudio.commonlib.adapter.lgrcommon;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zhiyangstudio.commonlib.utils.EmptyUtils;

import java.lang.ref.WeakReference;

/**
 * Created by zhiyang on 2018/4/17.
 * listview gridview recyclerview共用的viewholder
 */
public class QuickViewHolder extends RecyclerView.ViewHolder {

    private int mLayoutId;
    private SparseArray<WeakReference<View>> mViews;

    public QuickViewHolder(View itemView) {
        this(itemView, -1);
    }

    public QuickViewHolder(View itemView, int layoutId) {
        super(itemView);
        mViews = new SparseArray<>();
        this.mLayoutId = layoutId;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public QuickViewHolder setOnClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
        return this;
    }

    public QuickViewHolder setOnLongClickListener(View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
        return this;
    }

    public QuickViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnLongClickListener(listener);
        }
        return this;
    }

    public <T extends View> T getView(int viewId) {
        WeakReference<View> vwr = mViews.get(viewId);
        View view = null;
        if (vwr == null) {
            view = itemView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<View>(view));
            }
        } else {
            view = vwr.get();
        }
        return (T) view;
    }

    public QuickViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    public QuickViewHolder setText(int viewId, CharSequence txt) {
        TextView textView = getView(viewId);
        if (textView != null && EmptyUtils.isNotEmpty(txt)) {
            textView.setText(txt);
        }
        return this;
    }

    public QuickViewHolder setTextColor(int viewId, int color) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setTextColor(color);
        }
        return this;
    }

    public QuickViewHolder setVisible(int viewId, int visible) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visible);
        }
        return this;
    }

    public QuickViewHolder setChecked(int viewId, boolean checked) {
        Checkable checkable = getView(viewId);
        if (checkable != null) {
            checkable.setChecked(checked);
        }
        return this;
    }

    public QuickViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        if (view != null) {
            view.setBackgroundResource(backgroundRes);
        }
        return this;
    }

    public QuickViewHolder setBackgroundRes(int backgroundRes) {
        itemView.setBackgroundResource(backgroundRes);
        return this;
    }

    public QuickViewHolder setImageRes(int viewId, int imgRes) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageResource(imgRes);
        }
        return this;
    }

    public QuickViewHolder setImageRes(int viewId, Bitmap bm) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageBitmap(bm);
        }
        return this;
    }

    public void setText(int viewID, String txt) {
        TextView textView = getView(viewID);
        if (textView != null) {
            textView.setText(txt);
        }
    }

    public QuickViewHolder setText(int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    public QuickViewHolder setImageResource(int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public QuickViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public QuickViewHolder setBackgroundColor(int color) {
        itemView.setBackgroundColor(color);
        return this;
    }

    public QuickViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }


    public QuickViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public QuickViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((View) getView(viewId)).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            ((View) getView(viewId)).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     */
    public QuickViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Add links into a TextView.
     */
    public QuickViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public QuickViewHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    public QuickViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }


    public QuickViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     */
    public QuickViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }


    public QuickViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }


    public QuickViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     */
    public QuickViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the on click listener of the view.
     */
    public QuickViewHolder setOnItemClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     */
    public QuickViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }


    /**
     * Sets the listview or gridview's item click listener of the view
     */
    public QuickViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     */
    public QuickViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     */
    public QuickViewHolder setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    /**
     * Sets the on checked change listener of the view.
     */
    public QuickViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    /**
     * Sets the tag of the view.
     */
    public QuickViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     */
    public QuickViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public View getItemView() {
        return itemView;
    }

    public void setVisible(boolean hasShow) {
        itemView.setVisibility(hasShow ? View.VISIBLE : View.GONE);
    }
}
