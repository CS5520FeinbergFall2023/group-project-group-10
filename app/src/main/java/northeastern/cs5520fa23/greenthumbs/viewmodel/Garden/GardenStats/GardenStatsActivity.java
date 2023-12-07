package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenStats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;

public class GardenStatsActivity extends AppCompatActivity {
    private AnyChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_stats);
        this.chartView = findViewById(R.id.plant_pie_chart);
        Pie plantPieChart = AnyChart.pie();
        List<DataEntry> plantStats = new ArrayList<>();
        plantStats.add(new ValueDataEntry("Tomato", 3));
        plantPieChart.data(plantStats);
        chartView.setChart(plantPieChart);

    }
}