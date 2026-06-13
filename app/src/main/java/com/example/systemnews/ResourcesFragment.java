package com.example.systemnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ResourcesFragment: Displays a list of resources with sorting functionality.
 * Users can view details and sort items based on ratings.
 */
public class ResourcesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ResourceAdapter adapter;
    private List<Resource> resourceList;
    private Spinner spinnerSort;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View view = inflater.inflate(R.layout.fragment_resources, container, false);

        // Initialize RecyclerView and set its layout manager
        recyclerView = view.findViewById(R.id.rv_resources);
        // Use 1 column for Portrait and 2 columns for Landscape
        if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        // Setup the sorting Spinner with options
        spinnerSort = view.findViewById(R.id.spinner_sort);
        String[] sortOptions = {"Sort", "Highest Rated", "Lowest Rated", "Average Rated"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(spinnerAdapter);

        // Initialize the data source with sample data
        resourceList = new ArrayList<>();
        resourceList.add(new Resource(1, "FIFA Training Centre", "https://www.fifatrainingcentre.com", "Official FIFA platform for football development", "Coaching", 5.0f, "2026-01-10 10:00:00"));
        resourceList.add(new Resource(2, "MyFitnessPal Nutrition", "https://www.myfitnesspal.com", "Comprehensive nutrition and calorie tracking", "Nutrition", 4.7f, "2026-01-12 11:30:00"));
        resourceList.add(new Resource(3, "NSCA Strength & Conditioning", "https://www.nsca.com", "Evidence-based strength and conditioning resources", "Fitness", 4.1f, "2026-01-14 09:00:00"));
        resourceList.add(new Resource(4, "Sports Injury Clinic", "https://www.sportsinjuryclinic.net", "Practical guides covering sports injuries", "Sports Science", 1.0f, "2026-01-16 14:00:00"));

        // Connect the adapter to the RecyclerView
        adapter = new ResourceAdapter(resourceList);
        recyclerView.setAdapter(adapter);

        // Handle sorting selection
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) { // Sort by Highest Rated
                    Collections.sort(resourceList, (r1, r2) -> Float.compare(r2.getAverageRating(), r1.getAverageRating()));
                } else if (position == 2) { // Sort by Lowest Rated
                    Collections.sort(resourceList, (r1, r2) -> Float.compare(r1.getAverageRating(), r2.getAverageRating()));
                } else if (position == 3) { // Sort by Average Rated (relative to a median of 3.0)
                    Collections.sort(resourceList, (r1, r2) -> Float.compare(Math.abs(r2.getAverageRating() - 3.0f), Math.abs(r1.getAverageRating() - 3.0f)));
                }
                // Notify the adapter that the data set has changed to refresh the UI
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }
}
