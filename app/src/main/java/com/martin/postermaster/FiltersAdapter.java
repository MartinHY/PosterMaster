package com.martin.postermaster;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2016/7/11 0011.
 */
public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.MyViewHolder> {


    private RequestManager manager;
    private LayoutInflater mInflater;
    private List<float[]> filters;

    public FiltersAdapter(RequestManager manager, LayoutInflater mInflater, List<float[]> filters) {
        this.manager = manager;
        this.mInflater = mInflater;
        this.filters = filters;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder;
        viewHolder = new MyViewHolder(
                mInflater.inflate(R.layout.list_item, parent, false));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return filters.size() + 1;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position != 0) {
            ColorFilter.imageViewColorFilter(holder.imageView, filters.get(position - 1));
        } else {
            holder.imageView.setColorFilter(null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectListener != null) {
                    if (position != 0) {
                        onItemSelectListener.selected(filters.get(position - 1));
                    } else {
                        onItemSelectListener.selected(null);
                    }
                }
            }
        });
    }

    OnItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public interface OnItemSelectListener {

        void selected(float[] filter);

    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img);
        }
    }

}


