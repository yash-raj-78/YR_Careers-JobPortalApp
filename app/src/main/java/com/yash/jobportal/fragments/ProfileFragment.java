package com.yash.jobportal.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.jobportal.R;
import com.yash.jobportal.activites.LoginActivity;
import com.yash.jobportal.model.UserModel;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressBar progressBar;

    TextView txtName, txtEmail, txtPhone;
    Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        btnLogout = view.findViewById(R.id.btnLogout);
        progressBar = view.findViewById(R.id.progressBar);

        if (auth.getCurrentUser() != null) {

            progressBar.setVisibility(View.VISIBLE);

            String uid = auth.getCurrentUser().getUid();

            db.collection("Users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        if (!isAdded()) return;

                        progressBar.setVisibility(View.GONE); // ✔ FIX HERE

                        if (documentSnapshot.exists()) {

                            UserModel user = documentSnapshot.toObject(UserModel.class);

                            if (user != null) {
                                txtName.setText(user.getName());
                                txtEmail.setText(user.getEmail());
                                txtPhone.setText(user.getPhone());
                            }
                        }
                    })
                    .addOnFailureListener(e -> {

                        if (!isAdded()) return;

                        progressBar.setVisibility(View.GONE);
                    });

        } else {

            progressBar.setVisibility(View.GONE);

            startActivity(new Intent(getActivity(), LoginActivity.class));
            if (getActivity() != null) {
                getActivity().finish();
            }
        }

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            getActivity().finish();
        });

        return view;
    }
}