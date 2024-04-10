package com.test.recipes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.test.recipes.R;

import java.util.List;

public class SpinAdapter extends BaseAdapter {
    Context context;
    List<String> items;
    LayoutInflater inflter;

    public SpinAdapter(Context applicationContext, List<String> items) {
        this.context = applicationContext;
        this.items = items;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        try{
            return items.get(i);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if(view == null){
            view = inflter.inflate(R.layout.layout_spiner_item, viewGroup, false);
            mHolder = new ViewHolder();
            mHolder.item_view = view.findViewById(R.id.text);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }
        try{
            final String item = (String) getItem(i);
            mHolder.item_view.setText(item);
        }catch (Exception e){

        }
        return view;
    }
    class ViewHolder{
        TextView item_view;
    }
}