package com.open.charview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HorizontalChartView chView = findViewById(R.id.ch_view);
        chView.setData(getCharts());
    }

    public List<Chart> getCharts() {
        List<Chart> list = new ArrayList<>();
        Chart chart = new Chart();
        chart.setLeftName("A");
        chart.setRightName(10f);
        Chart chart1 = new Chart();
        chart1.setLeftName("B");
        chart1.setRightName(6f);
        Chart chart2 = new Chart();
        chart2.setLeftName("C");
        chart2.setRightName(4.0f);
        Chart chart3 = new Chart();
        chart3.setLeftName("D");
        chart3.setRightName(3f);

        list.add(chart3);
        list.add(chart2);
        list.add(chart1);
        list.add(chart);
        return list;
    }
}
