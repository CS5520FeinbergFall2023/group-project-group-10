package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenStats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart.Plant;

public class GardenStatsActivity extends AppCompatActivity {
    private AnyChartView chartView;
    private HashMap<String, Integer> plantTotals;
    private FirebaseDatabase db;
    private FirebaseUser currUser;
    private TextView noPlantText;
    private TextView plantTotalsText;
    private RecyclerView plantTotalsRecyclerView;
    private StatsAdapter statsAdapter;
    private List<PlantTotal> plantTotalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_stats);
        this.noPlantText = findViewById(R.id.plants_stats_no_plants);
        this.noPlantText.setVisibility(View.GONE);
        this.plantTotalList = new ArrayList<>();
        this.plantTotalsText = findViewById(R.id.plant_stats_totals_title);
        this.plantTotalsRecyclerView = findViewById(R.id.stats_recycler_view);
        this.statsAdapter = new StatsAdapter(plantTotalList, this);
        this.plantTotalsRecyclerView.setHasFixedSize(true);
        this.plantTotalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.plantTotalsRecyclerView.setAdapter(statsAdapter);
        this.db = FirebaseDatabase.getInstance();
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.chartView = findViewById(R.id.plant_pie_chart);
        this.plantTotals = new HashMap<>();
        getPlantTotals();


        if (plantTotals.isEmpty()) {
            chartView.setVisibility(View.GONE);
            plantTotalsRecyclerView.setVisibility(View.GONE);
            plantTotalsText.setVisibility(View.GONE);
            noPlantText.setVisibility(View.VISIBLE);
        }

    }

    private void getPlantTotals() {
        DatabaseReference plantRef = db.getReference("users").child(currUser.getUid()).child("plants").child("growing");
        plantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot plantsSnapshot: snapshot.getChildren()) {
                    //HashMap<String, Object> children = plantsSnapshot.getChildren();
                    Log.d("plant charts totals", plantsSnapshot.toString());
                    Integer numChildren = (int) plantsSnapshot.getChildrenCount();
                    String plantName = plantsSnapshot.getKey();
                    Log.d("plant charts key", plantName);
                    PlantTotal totalItem = new PlantTotal(plantName, numChildren);
                    plantTotalList.add(totalItem);
                    statsAdapter.notifyDataSetChanged();

                    plantTotals.put(plantName, numChildren);
                }
                if (plantTotals.isEmpty()) {
                    chartView.setVisibility(View.GONE);
                    plantTotalsRecyclerView.setVisibility(View.GONE);
                    plantTotalsText.setVisibility(View.GONE);
                    noPlantText.setVisibility(View.VISIBLE);
                } else {
                    Pie plantPieChart = AnyChart.pie();
                    List<DataEntry> plantStats = new ArrayList<>();
                    for (String plant : plantTotals.keySet()) {
                        plantStats.add(new ValueDataEntry(plant, plantTotals.get(plant)));

                    }
                    plantPieChart.data(plantStats);
                    plantPieChart.background().fill("#FFFBE6");
                    chartView.setChart(plantPieChart);
                    chartView.setVisibility(View.VISIBLE);
                    plantTotalsRecyclerView.setVisibility(View.VISIBLE);
                    plantTotalsText.setVisibility(View.VISIBLE);
                    noPlantText.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}