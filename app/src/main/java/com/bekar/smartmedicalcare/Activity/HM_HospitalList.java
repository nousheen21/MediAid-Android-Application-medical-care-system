package com.bekar.smartmedicalcare.Activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bekar.smartmedicalcare.R;

import java.util.List;

public class HM_HospitalList extends ArrayAdapter<HM_Hospital> {
    private Activity context;
    private List<HM_Hospital> hospitalList;

    public HM_HospitalList(Activity context, List<HM_Hospital> hospitalList){
        super(context, R.layout.hm_hospital_list,hospitalList);
        this.context = context;
        this.hospitalList = hospitalList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.hm_hospital_list,null,true);

        TextView viewHospitalName = (TextView) listViewItem.findViewById(R.id.listViewHospitalName);
        TextView viewHospitalAddress = (TextView) listViewItem.findViewById(R.id.listViewHospitalAddress);

        HM_Hospital hmHospital = hospitalList.get(position);

        viewHospitalName.setText(hmHospital.getHospitalName());
        viewHospitalAddress.setText(hmHospital.getHospitalAddress());


        return listViewItem;
    }
}
