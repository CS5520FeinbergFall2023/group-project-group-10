package northeastern.cs5520fa23.greenthumbs.viewmodel.Garden;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenStats.GardenStatsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GardenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GardenFragment extends Fragment implements GardenAdapter.PlantDragCallback {

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


        //gardenPlot = view.findViewById(R.id.garden_plot);
        //testLettuce = view.findViewById(R.id.testLettuce);
        this.plantsItemList = new ArrayList<>();
        this.gardenPlotPlants= new ArrayList<>();
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
        this.db = FirebaseDatabase.getInstance();
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
        this.gardenMenuRV.setHasFixedSize(true);
        this.gardenMenuRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.gardenMenuRV.setAdapter(gardenAdapter);
        this.gardenPlotView = view.findViewById(R.id.garden_plot_rv_grid);
        this.gardenPlotView.setHasFixedSize(true);
        this.gardenPlotView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        this.gardenPlotAdapter = new GardenPlotAdapter(gardenPlotPlants, this.getContext());
        this.gardenPlotView.setAdapter(gardenPlotAdapter);


        // TODO: dialog asking user for ?x? plot


        //setMenuDragListeners(); // for menu
        //setReceivingListeners(view); // for garden plot
        //setGridListeners();
        populateGarden();

        // TODO: connect whats in garden plot to a data structure for DB, and reload on launch
    }

    private void populateGarden() {
        Query gardenRef = db.getReference("users").child(currUser.getUid()).child("plants").child("garden").orderByKey();
        //Query query = gardenRef.orderByChild("position");
        gardenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // handle incorrect sizing
                if (snapshot.exists()) {
                    HashMap <Integer, GardenPlotPlant> plantPositions = new HashMap<>();
                    for (DataSnapshot plantSnapshot: snapshot.getChildren()) {
                        GardenPlotPlant plant = plantSnapshot.getValue(GardenPlotPlant.class);
                        plantPositions.put(plant.getPosition(), plant);
                    }

                    for (int i = 0; i < 16; i++) {
                        Integer j = i;
                        if (plantPositions.containsKey(j)) {
                            //gardenPlotPlants.add(plantPositions.get(j));
                        } else {
                            GardenPlotPlant plant = new GardenPlotPlant();
                            plant.setPosition(j);
                            plantPositions.put(j, plant);
                        }
                        //gardenPlotPlants.add(gardenPlotPlants.get(j));
                        //gardenPlotAdapter.notifyDataSetChanged();
                    }
                    for (int i = 0; i < 16; i++) {
                        gardenPlotPlants.add(plantPositions.get(i));
                        gardenPlotAdapter.notifyDataSetChanged();
                    }


                } else {
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
        //ImageView
        ClipData.Item item = new ClipData.Item(String.valueOf(resId));
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        // Create a new ClipData using "Lettuce" as a label.
        ClipData dragData = new ClipData(plantName, mimeTypes, item);
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(plantImage);
        // Start the drag.
        plantImage.startDragAndDrop(dragData, myShadow, null, 0);


//                img.setVisibility(View.INVISIBLE);
        return true;
    }

    /*
    private void setMenuDragListeners() {
        // TODo: replace testLettuce with function parameter
        testLettuce.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // create data we want to send in drag event
                // TODO: use tags for imageviews to get resourceID, no function to get resourceID.
                ClipData.Item item = new ClipData.Item(String.valueOf(R.drawable.ic_lettuce));
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                // Create a new ClipData using "Lettuce" as a label.
                ClipData dragData = new ClipData("Lettuce", mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(testLettuce);
                // Start the drag.
                v.startDragAndDrop(dragData, myShadow, null, 0);

//                img.setVisibility(View.INVISIBLE);
                return true; // Indicate that the long-click is handled.
            }
        });
    }

     */

    /*
    private void setGridListeners() {
        int rows = gardenPlot.getRowCount();
        int cols = gardenPlot.getColumnCount();
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j++) {
                // all one list want
                // 0,0 -> 0 but
                // 1,0 -> 3
                // i * cols + j
                // 1,1 would be 1 * 3 + 1 = 4
                // 1, 2 would be 1 * 3 + 2 = 5
                // 0, 1 would be 1 * 0 + 1 = 1
                int idx = (i * cols) + j;
                View plot = gardenPlot.getChildAt(idx);
                plot.setOnDragListener( (v, e) -> {

                    // Handle each of the expected events.
                    switch(e.getAction()) {

                        case DragEvent.ACTION_DRAG_STARTED:

                            // Determine whether this View can accept the dragged data.
                            if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                                // As an example, apply a blue color tint to the View to
                                // indicate that it can accept data.
                                ((ImageView)v).setColorFilter(Color.BLUE);

                                // Invalidate the view to force a redraw in the new tint.
                                v.invalidate();

                                // Return true to indicate that the View can accept the dragged
                                // data.
                                return true;
                            }
                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            // Apply a green tint to the View.
                            ((ImageView)v).setColorFilter(Color.GREEN);

                            // Invalidate the view to force a redraw in the new tint.
                            v.invalidate();

                            // Return true. The value is ignored.
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:

                            // Ignore the event.
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                            // Reset the color tint to blue.
                            ((ImageView)v).setColorFilter(Color.BLUE);

                            // Invalidate the view to force a redraw in the new tint.
                            v.invalidate();

                            // Return true. The value is ignored.
                            return true;

                        case DragEvent.ACTION_DROP:

                            // Get the item containing the dragged data.
                            ClipData.Item item = e.getClipData().getItemAt(0);
                            CharSequence dragData = item.getText();

                            // Set imageView in garden plot to display new image based on clipboard data
                            int w = ((ImageView) v).getWidth();
                            int h = ((ImageView) v).getHeight();
                            Picasso.get().load(Integer.parseInt((String) dragData)).resize(w,h).into((ImageView) v);
                            Drawable d = getResources().getDrawable(R.drawable.rounded_corners_orange);
                            v.setBackground(d);
                            //((ImageView) v).setImageResource(Integer.parseInt((String) dragData));
                            ((ImageView)v).clearColorFilter();

                            v.invalidate();
                            // Return true. DragEvent.getResult() returns true.
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            // Turn off color tinting.
                            ((ImageView)v).clearColorFilter();


                            // Invalidate the view to force a redraw.
                            v.invalidate();


                            // Return true. The value is ignored.
                            return true;
                        // An unknown action type is received.
                        default:
                            break;
                    }
                    return false;
                });
            }
        }

    }

     */
    /*
    private void setReceivingListeners(View view) {

        // TODO: Replace blankview with function parameter
        View acceptingView = view.findViewById(R.id.testEmptySpot);
        acceptingView.setOnDragListener( (v, e) -> {

            // Handle each of the expected events.
            switch(e.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determine whether this View can accept the dragged data.
                    if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        // As an example, apply a blue color tint to the View to
                        // indicate that it can accept data.
                        ((ImageView)v).setColorFilter(Color.BLUE);

                        // Invalidate the view to force a redraw in the new tint.
                        v.invalidate();

                        // Return true to indicate that the View can accept the dragged
                        // data.
                        return true;
                    }
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    // Apply a green tint to the View.
                    ((ImageView)v).setColorFilter(Color.GREEN);

                    // Invalidate the view to force a redraw in the new tint.
                    v.invalidate();

                    // Return true. The value is ignored.
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    // Ignore the event.
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Reset the color tint to blue.
                    ((ImageView)v).setColorFilter(Color.BLUE);

                    // Invalidate the view to force a redraw in the new tint.
                    v.invalidate();

                    // Return true. The value is ignored.
                    return true;

                case DragEvent.ACTION_DROP:

                    // Get the item containing the dragged data.
                    ClipData.Item item = e.getClipData().getItemAt(0);
                    CharSequence dragData = item.getText();

                    // Set imageView in garden plot to display new image based on clipboard data
                    ((ImageView) v).setImageResource(Integer.parseInt((String) dragData));
                    ((ImageView)v).clearColorFilter();

                    v.invalidate();
                    // Return true. DragEvent.getResult() returns true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turn off color tinting.
                    ((ImageView)v).clearColorFilter();

                    // Invalidate the view to force a redraw.
                    v.invalidate();
                    Drawable d = getResources().getDrawable(R.drawable.rounded_corners_orange);
                    v.setBackground(d);

                    // Return true. The value is ignored.
                    return true;
                // An unknown action type is received.
                default:
                    break;
            }
            return false;
        });
    }

     */
}