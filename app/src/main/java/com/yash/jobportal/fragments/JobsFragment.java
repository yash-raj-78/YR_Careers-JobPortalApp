package com.yash.jobportal.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yash.jobportal.R;
import com.yash.jobportal.adapter.JobAdapter;
import com.yash.jobportal.model.JobModel;

import java.util.ArrayList;

public class JobsFragment extends Fragment {

    RecyclerView recyclerViewJobs;
    ArrayList<JobModel> jobList;
    ArrayList<JobModel> fullList;
    JobAdapter adapter;
    FirebaseFirestore db;
    SearchView searchView;

    public JobsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobs, container, false);

        // Views
        recyclerViewJobs = view.findViewById(R.id.recyclerViewJobs);
        searchView = view.findViewById(R.id.searchView);

        // Recycler setup
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(getContext()));

        jobList = new ArrayList<>();
        fullList = new ArrayList<>();

        adapter = new JobAdapter(jobList);
        recyclerViewJobs.setAdapter(adapter);

        // Firestore
        db = FirebaseFirestore.getInstance();

        loadJobs();
        setupSearch();

        return view;
    }

    // ================= LOAD JOBS =================
    private void loadJobs() {

        db.collection("Jobs")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        jobList.clear();
                        fullList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            JobModel job = document.toObject(JobModel.class);
                            job.setJobId(document.getId());

                            jobList.add(job);
                        }

                        fullList.addAll(jobList);
                        adapter.notifyDataSetChanged();

                    } else {
                        System.out.println("Error: " +
                                task.getException().getMessage());
                    }
                });
    }

    // ================= SEARCH =================
    private void setupSearch() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterJobs(newText);
                return true;
            }
        });
    }

    // ================= FILTER (FULL SAFE VERSION) =================
    private void filterJobs(String text) {

        ArrayList<JobModel> filteredList = new ArrayList<>();

        for (JobModel job : fullList) {

            String searchText = text.toLowerCase();

            String title = job.getTitle() != null ? job.getTitle().toLowerCase() : "";
            String company = job.getCompany() != null ? job.getCompany().toLowerCase() : "";
            String location = job.getLocation() != null ? job.getLocation().toLowerCase() : "";

            if (title.contains(searchText)
                    || company.contains(searchText)
                    || location.contains(searchText)) {

                filteredList.add(job);
            }
        }

        jobList.clear();
        jobList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}