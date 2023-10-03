package com.viadroid.app.growingtree.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 通用RecyclerView Adapter
 */

public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter<BaseRvHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    protected final String TAG = getClass().getSimpleName();

    private Context mContext;
    private List<T> mList;

    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemLongClickListener<T> mOnItemLongClickListener;


    public BaseRvAdapter(List<T> list) {
        mList = list;
    }

    @Override
    public BaseRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new BaseRvHolder(inflateView(parent, getLayoutId()));
    }

    @Override
    public void onBindViewHolder(BaseRvHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);
        onBindViewHolder(holder, getItem(position), position);
    }

    public abstract int getLayoutId();

    public abstract void onBindViewHolder(BaseRvHolder holder, T item, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    public List<T> getList() {
        return mList;
    }

    public Context getContext() {
        return mContext;
    }

    public View inflateView(ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(mContext).inflate(layoutId, parent, false);
    }

    /**
     * 添加数据
     */
    public void addItem(T item, int position) {
        mList.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 移除数据
     */
    public void removeItem(T item) {
        int position = mList.indexOf(item);
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onClick(View v) {
        if (null != mOnItemClickListener) {
            int position = (int) v.getTag();
            mOnItemClickListener.onItemClick(v, getItem(position), position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (null != mOnItemLongClickListener) {
            int position = (int) v.getTag();
            mOnItemLongClickListener.onItemLongClick(v, getItem(position), position);
        }
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.mOnItemLongClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T data, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View view, T data, int position);
    }


}
