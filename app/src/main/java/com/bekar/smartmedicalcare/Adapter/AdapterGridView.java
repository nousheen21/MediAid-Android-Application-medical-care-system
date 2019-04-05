package com.bekar.smartmedicalcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bekar.smartmedicalcare.ModelClass.GridViewItem;
import com.bekar.smartmedicalcare.R;
import com.bekar.smartmedicalcare.databinding.DashbordItemBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;

public class AdapterGridView extends BaseAdapter{
    private  List<GridViewItem> itemList;
    private LayoutInflater inflater;

    private DashbordItemBinding binding;

    public AdapterGridView(Context context,List<GridViewItem> itemList) {
        this.itemList = itemList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.dashbord_item,parent,false);
            binding= DataBindingUtil.bind(convertView);
        }

        GridViewItem item= (GridViewItem) getItem(position);
        convertView.setTag(position);
        binding.ivIcon.setImageResource(item.getImage());
        binding.tvTitle.setText(item.getTitle());

        return convertView;
    }
}
