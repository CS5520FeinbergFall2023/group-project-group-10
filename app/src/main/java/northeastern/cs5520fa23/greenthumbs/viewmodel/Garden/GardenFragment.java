package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.GrowingChart.Plant;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenStats.GardenStatsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GardenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GardenFragment extends Fragment implements GardenAdapter.PlantDragCallback, GardenPlotAdapter.PlotPlantDragCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<GardenMenuItem> plantsItemList;
    private RecyclerView gardenMenuRV;
    private GardenAdapter gardenAdapter;
    //GridLayout gardenPlot;
    private RecyclerView gardenPlotView;
    private GardenPlotAdapter gardenPlotAdapter;
    private Button goToStatsButton;
    private List<GardenPlotPlant> gardenPlotPlants;
    private FirebaseUser currUser;
    private FirebaseDatabase db;
    private ImageView harvestIcon;
    private GardenPlotPlant plantMoving;
    private ImageView deleteIcon;
    private HashMap<String, Integer> plantIds;
    ImageView testLettuce; // placeholder for menu options

    public GardenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GardenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GardenFragment newInstance(String param1, String param2) {
        GardenFragment fragment = new GardenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void populateMenu() {
        GardenMenuItem tomato = new GardenMenuItem("Tomato", R.drawable.tomato);
        GardenMenuItem eggplant = new GardenMenuItem("Eggplant", R.drawable.eggplant);
        GardenMenuItem cucumber = new GardenMenuItem("Cucumber", R.drawable.cucumber);
        GardenMenuItem carrot = new GardenMenuItem("Carrot", R.drawable.carrot);
        GardenMenuItem lettuce = new GardenMenuItem("Lettuce", R.drawable.lettuce);
        GardenMenuItem broccoli = new GardenMenuItem("Broccoli", R.drawable.broccoli);
        GardenMenuItem onion = new GardenMenuItem("Onion", R.drawable.onion);
        GardenMenuItem peas = new GardenMenuItem("Peas", R.drawable.peas);
        GardenMenuItem pepper = new GardenMenuItem("Pepper", R.drawable.pepper);
        GardenMenuItem potato = new GardenMenuItem("Potato", R.drawable.potato);
        plantsItemList.add(tomato);
        plantsItemList.add(eggplant);
        plantsItemList.add(cucumber);
        plantsItemList.add(carrot);
        plantsItemList.add(lettuce);
        plantsItemList.add(broccoli);
        plantsItemList.add(onion);
        plantsItemList.add(peas);
        plantsItemList.add(pepper);
        plantsItemList.add(potato);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garden, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.plantsItemList = new ArrayList<>();
        this.gardenPlotPlants= new ArrayList<>();
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.db = FirebaseDatabase.getInstance();
        this.plantIds = new HashMap<>();
        this.plantIds.put("tomato", R.drawable.tomato);
        this.plantIds.put("eggplant", R.drawable.eggplant);
        this.plantIds.put("cucumber", R.drawable.cucumber);
        this.plantIds.put("carrot", R.drawable.carrot);
        this.plantIds.put("lettuce", R.drawable.lettuce);
        this.plantIds.put("broccoli", R.drawable.broccoli);
        this.plantIds.put("onion", R.drawable.onion);
        this.plantIds.put("peas", R.drawable.peas);
        this.plantIds.put("pepper", R.drawable.pepper);
        this.plantIds.put("potato", R.drawable.potato);
        this.plantIds.put("default", R.drawable.baseline_nature_24);
        this.deleteIcon = view.findViewById(R.id.garden_delete_icon);
        this.harvestIcon = view.findViewById(R.id.garden_harvest_icon);
        setHarvestListener();
        setDeleteListener();
        populateMenu();
        this.goToStatsButton = view.findViewById(R.id.garden_go_to_stats_button);
        this.goToStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), GardenStatsActivity.class);
                getActivity().startActivity(i);
            }
        });
        this.gardenMenuRV = view.findViewById(R.id.garden_menu_rv);
        this.gardenAdapter = new GardenAdapter(plantsItemList, getContext(), this);
        gardenAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        this.gardenMenuRV.setHasFixedSize(true);
        this.gardenMenuRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.gardenMenuRV.setAdapter(gardenAdapter);
        this.gardenPlotView = view.findViewById(R.id.garden_plot_rv_grid);
        this.gardenPlotView.setHasFixedSize(true);
        this.gardenPlotView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        this.gardenPlotAdapter = new GardenPlotAdapter(gardenPlotPlants, this.getContext(), this);
        this.gardenPlotView.setAdapter(gardenPlotAdapter);

        populateGarden();

    }

    private void populateGarden() {
        Query gardenRef = db.getReference("users").child(currUser.getUid()).child("plants").child("garden").orderByKey();
        gardenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // handle incorrect sizing
                gardenPlotPlants.clear();
                gardenPlotAdapter.notifyDataSetChanged();
                if (snapshot.exists()) {
                    HashMap <Integer, GardenPlotPlant> plantPositions = new HashMap<>();
                    for (DataSnapshot plantSnapshot: snapshot.getChildren()) {
                        GardenPlotPlant plant = plantSnapshot.getValue(GardenPlotPlant.class);
                        plantPositions.put(plant.getPosition(), plant); // put the plant in the plot with their position
                    }

                    // Go through each potential plot position
                    for (int i = 0; i < 16; i++) {
                        Integer j = i;
                        if (plantPositions.containsKey(j)) { // if there is a plant skip because it's already added
                            //gardenPlotPlants.add(plantPositions.get(j));
                        } else {
                            GardenPlotPlant plant = new GardenPlotPlant(); // otherwise add an empty plant with the position
                            plant.setPosition(j);
                            plantPositions.put(j, plant);
                        }

                    }
                    for (int i = 0; i < 16; i++) {
                        gardenPlotPlants.add(plantPositions.get(i)); // add all the plants from the positions list to the garden plot list
                        gardenPlotAdapter.notifyDataSetChanged();
                    }


                } else { // if there were no plants they are all empty
                    for (int i = 0; i < 16; i++) {
                        GardenPlotPlant plant = new GardenPlotPlant();
                        plant.setPosition(i);
                        gardenPlotPlants.add(plant);
                        gardenPlotAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean dragPlant(String plantName, int resId, ImageView plantImage) {

        String plantNameMenu = "menu_" + plantName;
        ClipData.Item item = new ClipData.Item(String.valueOf(resId));
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData dragData = new ClipData(plantNameMenu, mimeTypes, item);
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(plantImage);
        plantImage.startDragAndDrop(dragData, myShadow, null, 0);

        return true;
    }

    @Override
    public boolean dragPlotPlant(GardenPlotPlant plant, ImageView plotImage) {

        plantMoving = plant;
        ClipData.Item item = new ClipData.Item(String.valueOf(plantIds.get(plant.getPlant_type().toLowerCase())));
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData dragData = new ClipData(plant.getPlant_type(), mimeTypes, item);
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(plotImage);
        plotImage.startDragAndDrop(dragData, myShadow, null, 0);

        return true;
    }

    private void setHarvestListener() {
        // when dragged onto the harvest icon
        harvestIcon.setOnDragListener((view, event) -> {

            switch(event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:

                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        ((ImageView)view).setColorFilter(Color.parseColor("#4B7DFA"));
                        view.invalidate();
                        return true; // view can accept the data
                    }

                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    ((ImageView)view).setColorFilter(Color.parseColor("#49A36A"));
                    view.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION: // doesn't matter
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    ((ImageView)view).setColorFilter(Color.parseColor("#4B7DFA"));
                    view.invalidate();
                    return true;

                case DragEvent.ACTION_DROP:

                    ClipData.Item item = event.getClipData().getItemAt(0);
                    CharSequence dragData = item.getText();

                    if (plantMoving != null) {
                        gardenPlotAdapter.harvestPlant(plantMoving);
                        plantMoving = null;
                    }
                    ((ImageView)view).clearColorFilter();

                    view.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    ((ImageView)view).clearColorFilter();
                    view.invalidate();

                    // potentially ad this back in later
                    //Drawable d = context.getResources().getDrawable(R.drawable.rounded_corners_orange);
                    //v.setBackground(d);
                    return true;
                default:
                    break;
            }
            return false;
        });
    }

    private void setDeleteListener() {
        // if it's dragged onto the delete icon
        deleteIcon.setOnDragListener((view, event) -> {

            switch(event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:

                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        ((ImageView)view).setColorFilter(Color.parseColor("#4B7DFA"));
                        view.invalidate();
                        return true;

                    }
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    ((ImageView)view).setColorFilter(Color.parseColor("#49A36A"));
                    view.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION: // doesn't matter
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    ((ImageView)view).setColorFilter(Color.parseColor("#4B7DFA"));
                    view.invalidate();
                    return true;

                case DragEvent.ACTION_DROP:

                    ClipData.Item item = event.getClipData().getItemAt(0);
                    CharSequence dragData = item.getText();

                    if (plantMoving != null) {
                        gardenPlotAdapter.deletePlant(plantMoving);
                        plantMoving = null;
                    }
                    ((ImageView)view).clearColorFilter();

                    view.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    ((ImageView)view).clearColorFilter();
                    view.invalidate();

                    return true;
                default:
                    break;
            }
            return false;
        });
    }
}