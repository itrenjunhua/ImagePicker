package com.renj.selecttest.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-21   21:34
 * <p>
 * 描述：ListView/GridView 控件 Adapter 的封装，减少重复代码的编写。泛型表示单个条目的数据类型
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected List<T> dataList = new ArrayList<T>();
    protected Context context;

    public BaseListAdapter(@NonNull Context context) {
        this.context = context;
    }

    public BaseListAdapter(@NonNull Fragment fragment) {
        this.context = fragment.getActivity();
    }

    public BaseListAdapter(@NonNull android.support.v4.app.Fragment fragment) {
        this.context = fragment.getActivity();
    }

    public BaseListAdapter(@NonNull Context context, @NonNull List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public BaseListAdapter(@NonNull Fragment fragment, @NonNull List<T> dataList) {
        this.context = fragment.getActivity();
        this.dataList = dataList;
    }

    public BaseListAdapter(@NonNull android.support.v4.app.Fragment fragment, @NonNull List<T> dataList) {
        this.context = fragment.getActivity();
        this.dataList = dataList;
    }

    /**
     * 设置数据/重置数据
     *
     * @param dataList 新的数据/重置的数据
     */
    public void setDatas(@NonNull List<T> dataList) {
        if (this.dataList != null) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    /**
     * 加载更多数据时调用设置更多数据
     *
     * @param dataList 需要设置更多的数据
     */
    public void loadMore(@NonNull List<T> dataList) {
        if (this.dataList != null) {
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseListViewHolder<T> listViewHolder;
        if (convertView == null) {
            listViewHolder = getViewHolder(context, parent, position);
        } else {
            listViewHolder = (BaseListViewHolder<T>) convertView.getTag();
        }
        listViewHolder.setData(position, dataList.get(position));
        return listViewHolder.getItemRootView();
    }

    /**
     * 获取ViewHolder，强制ListView/GridView都需要使用ViewHolder，该ViewHolder必须继承至 {@link BaseListViewHolder}
     *
     * @param context  上下文
     * @param parent   ViewGroup
     * @param position 当前位置
     * @return
     */
    protected abstract BaseListViewHolder getViewHolder(Context context, ViewGroup parent, int position);

    public static abstract class BaseListViewHolder<T> {
        protected View mItemRootView;

        public BaseListViewHolder(Context context, ViewGroup parent) {
            View itemView = LayoutInflater.from(context).inflate(getItemViewLayoutId(), parent, false);
            bindView(itemView);
        }

        public BaseListViewHolder(View itemView) {
            bindView(itemView);
        }

        private void bindView(View itemView) {
            if (itemView != null) {
                mItemRootView = itemView;
                mItemRootView.setTag(this);
                ButterKnife.bind(this, mItemRootView);
            }
        }

        private View getItemRootView() {
            return mItemRootView;
        }

        /**
         * 如果使用 {@link #BaseListViewHolder(Context, ViewGroup)} 构造方法子类必须重写该方法，获取条目布局资源ID
         *
         * @return
         */
        public int getItemViewLayoutId() {
            return 0;
        }

        /**
         * 设置条目的数据
         *
         * @param position 当前的位置
         * @param data     当前位置的数据
         */
        public abstract void setData(int position, T data);
    }
}
