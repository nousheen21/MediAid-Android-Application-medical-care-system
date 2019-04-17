package com.bekar.smartmedicalcare.Activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bekar.smartmedicalcare.R;

import java.util.List;

public class MS_RequestedMedicineList extends ArrayAdapter<MS_RequestMedicine> {
    private Activity context;
    private List<MS_RequestMedicine> medicineList;

    public MS_RequestedMedicineList(Activity context, List<MS_RequestMedicine> medicineList){
        super(context, R.layout.ms_requested_medicine_list,medicineList);
        this.context = context;
        this.medicineList = medicineList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.ms_requested_medicine_list,null,true);

        TextView viewMedName = (TextView) listViewItem.findViewById(R.id.viewMedicineName);
        TextView viewCompany = (TextView) listViewItem.findViewById(R.id.viewMedicineCompany);

        MS_RequestMedicine msMedicine = medicineList.get(position);

        viewMedName.setText(msMedicine.getReqMedicineName());
        viewCompany.setText("Company: " + msMedicine.getReqCompanyName());

        return listViewItem;
    }
}
