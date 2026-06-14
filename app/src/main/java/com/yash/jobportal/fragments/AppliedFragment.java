package com.yash.jobportal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yash.jobportal.R;
import com.yash.jobportal.adapter.AppliedJobAdapter;
import com.yash.jobportal.model.JobModel;

import java.util.ArrayList;

public class AppliedFragment extends Fragment {

    RecyclerView recyclerApplied;
    ArrayList<JobModel> appliedList;
    AppliedJobAdapter adapter;
    TextView txtNoAppliedJobs;

    FirebaseFirestore db;
    FirebaseAuth auth;

    public AppliedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_applied,
                container,
                false);

        recyclerApplied = view.findViewById(R.id.recyclerApplied);
        txtNoAppliedJobs = view.findViewById(R.id.txtNoAppliedJobs);

        recyclerApplied.setLayoutManager(
                new LinearLayoutManager(getContext()));

        appliedList = new ArrayList<>();
        adapter = new AppliedJobAdapter(appliedList);
        recyclerApplied.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadAppliedJobs();

        return view;
    }

    private void loadAppliedJobs() {

        if (auth.getCurrentUser() == null) {
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        db.collection("Applications")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    appliedList.clear();

                    // No applied jobs
                    if (queryDocumentSnapshots.isEmpty()) {

                        txtNoAppliedJobs.setVisibility(View.VISIBLE);
                        recyclerApplied.setVisibility(View.GONE);

                        return;
                    }

                    txtNoAppliedJobs.setVisibility(View.GONE);
                    recyclerApplied.setVisibility(View.VISIBLE);

                    for (QueryDocumentSnapshot doc
                            : queryDocumentSnapshots) {

                        String jobId = doc.getString("jobId");

                        db.collection("Jobs")
                                .document(jobId)
                                .get()
                                .addOnSuccessListener(jobDoc -> {

                                    JobModel job =
                                            jobDoc.toObject(JobModel.class);

                                    if (job != null) {

                                        job.setJobId(jobDoc.getId());

                                        appliedList.add(job);

                                        adapter.notifyDataSetChanged();
                                    }

                                });
                    }

                });

    }
}