package com.muqit.KharredoAdminPanel.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.muqit.KharredoAdminPanel.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;
    private ArrayList barEnteries;
    private Spinner spinner;
    private String SelectedYear = "2020";

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dashboard, viewGroup, false);
        this.barChart = (BarChart) inflate.findViewById(R.id.barchart);
        barChart.getDescription().setText("Sales Data");
        List<String> spinnerArray =  new ArrayList<String>();
        for(int i = 2015; i<=2065;i++)
        {
            spinnerArray.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) inflate.findViewById(R.id.Year_spinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(spinnerArray.indexOf("2020"));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedYear = spinnerArray.get(position);
                GetEnteries(SelectedYear);
                barChart.notifyDataSetChanged();
                barChart.invalidate();
                Toast.makeText(getActivity(),SelectedYear,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //this.barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        return inflate;
    }

    /* access modifiers changed from: 0000 */
    public void GetEnteries(String year) {
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Jan");
        xAxisLabel.add("Feb");
        xAxisLabel.add("Mar");
        xAxisLabel.add("Apr");
        xAxisLabel.add("May");
        xAxisLabel.add("Jun");
        xAxisLabel.add("Jul");
        xAxisLabel.add("Aug");
        xAxisLabel.add("Sep");
        xAxisLabel.add("Oct");
        xAxisLabel.add("Nov");
        xAxisLabel.add("Dec");
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xAxisLabel.get((int) value);
            }
        });
       // final ArrayList<String> yAxisLabel = new ArrayList<String>();
        //for(int i = 0; i< 3000; i = i + 250)
        //{
          //  xAxisLabel.add(String.valueOf(i));
        //}

        ArrayList arrayList = new ArrayList();
        this.barEnteries = arrayList;
        barEnteries.clear();
        if(year.equals("2020"))
        {
            this.barEnteries.add(new BarEntry(4, 2250));
            this.barEnteries.add(new BarEntry(7, 1300));
        }
        else
        {
            this.barEnteries.add(new BarEntry(0, 0));
        }
        this.barDataSet = new BarDataSet(this.barEnteries, "Sales");
        BarData barData2 = new BarData(this.barDataSet);
        this.barData = barData2;
        this.barChart.setData(barData2);
        this.barDataSet.setValueTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.barDataSet.setValueTextSize(16.0f);
       // this.barEnteries.add(new BarEntry(6.0f, 5.0f));
    }
}
