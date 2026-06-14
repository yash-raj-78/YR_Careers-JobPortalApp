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
import com.yash.jobportal.adapter.SavedJobAdapter;
import com.yash.jobportal.model.JobModel;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    RecyclerView recyclerSaved;
    ArrayList<JobModel> savedList;
    SavedJobAdapter adapter;
    TextView txtNoSavedJobs;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        recyclerSaved = view.findViewById(R.id.recyclerSaved);
        txtNoSavedJobs = view.findViewById(R.id.txtNoSavedJobs);

        recyclerSaved.setLayoutManager(
                new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        savedList = new ArrayList<>();

        adapter = new SavedJobAdapter(savedList, job -> {

            if (auth.getCurrentUser() == null) {
                return;
            }

            String userId = auth.getCurrentUser().getUid();

            db.collection("SavedJobs")
                    .document(userId)
                    .collection("jobs")
                    .document(job.getJobId())
                    .delete()
                    .addOnSuccessListener(unused -> {

                        savedList.remove(job);

                        adapter.notifyDataSetChanged();

                        if (savedList.isEmpty()) {

                            txtNoSavedJobs.setVisibility(View.VISIBLE);
                            recyclerSaved.setVisibility(View.GONE);

                        }

                    });

        });

        recyclerSaved.setAdapter(adapter);

        loadSavedJobs();

        return view;
    }

    private void loadSavedJobs() {

        if (auth.getCurrentUser() == null) {
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        db.collection("SavedJobs")
                .document(userId)
                .collection("jobs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    savedList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        JobModel job = doc.toObject(JobModel.class);

                        // Important
                        job.setJobId(doc.getId());

                        savedList.add(job);
                    }

                    adapter.notifyDataSetChanged();

                    if (savedList.isEmpty()) {

                        txtNoSavedJobs.setVisibility(View.VISIBLE);
                        recyclerSaved.setVisibility(View.GONE);

                    } else {

                        txtNoSavedJobs.setVisibility(View.GONE);
                        recyclerSaved.setVisibility(View.VISIBLE);

                    }

                });
    }
}