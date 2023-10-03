package com.viadroid.app.growingtree.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 通用RecyclerView ViewHolder
 */

public class BaseRvHolder extends RecyclerView.ViewHolder {


    private SparseArray<View> mViews;

    private Context mContext;

    public BaseRvHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mViews = new SparseArray<>();
    }

    public Context getContext() {
        return mContext;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (null == view) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public TextView getTextView(int viewId) {
        return getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return getView(viewId);
    }

    public TextView setText(int viewId, String text) {
        getTextView(viewId).setText(text);
        return getView(viewId);
    }

    public ImageView setImageResource(int viewId, int resId) {
        getImageView(viewId).setImageResource(resId);
        return getImageView(viewId);
    }

    public ImageView setImageDrawable(int viewId, Drawable drawable) {
        getImageView(viewId).setImageDrawable(drawable);
        return getImageView(viewId);
    }
}
