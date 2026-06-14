package com.yash.jobportal.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yash.jobportal.R;
import com.yash.jobportal.adapter.JobAdapter;
import com.yash.jobportal.model.JobModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<JobModel> list;
    JobAdapter adapter;
    FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        list = new ArrayList<>();

        adapter = new JobAdapter(list);

        db = FirebaseFirestore.getInstance();

        loadJobs();

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadJobs() {

        db.collection("Jobs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    list.clear();

                    for (QueryDocumentSnapshot document
                            : queryDocumentSnapshots) {

                        JobModel job =
                                document.toObject(JobModel.class);

                        job.setJobId(document.getId());

                        list.add(job);
                    }

                    adapter.notifyDataSetChanged();

                });

    }
}