package com.yash.jobportal.activites;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.jobportal.R;
import com.yash.jobportal.model.ApplicationModel;

import java.util.HashMap;

public class JobDetailsActivity extends AppCompatActivity {

    TextView txtJobTitle, txtCompany, txtLocation, txtSalary,
            txtExperience, txtSkills, txtDescription;

    ImageView imgCompany;

    Button btnApply, btnSave;

    String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        jobId = getIntent().getStringExtra("jobId");

        txtJobTitle = findViewById(R.id.txtJobTitle);
        txtCompany = findViewById(R.id.txtCompany);
        txtLocation = findViewById(R.id.txtLocation);
        txtSalary = findViewById(R.id.txtSalary);
        txtExperience = findViewById(R.id.txtExperience);
        txtSkills = findViewById(R.id.txtSkills);
        txtDescription = findViewById(R.id.txtDescription);

        imgCompany = findViewById(R.id.imgCompany);

        btnApply = findViewById(R.id.btnApply);
        btnSave = findViewById(R.id.btnSave);

        if (getIntent() != null) {

            String company = getIntent().getStringExtra("company");

            txtJobTitle.setText(getIntent().getStringExtra("title"));
            txtCompany.setText(company);
            txtLocation.setText(getIntent().getStringExtra("location"));
            txtSalary.setText(getIntent().getStringExtra("salary"));
            txtExperience.setText(getIntent().getStringExtra("experience"));
            txtSkills.setText(getIntent().getStringExtra("skills"));
            txtDescription.setText(getIntent().getStringExtra("description"));

            if (company != null && imgCompany != null) {
                if (company.equalsIgnoreCase("Google")) {
                    imgCompany.setImageResource(R.drawable.google);
                } else if (company.equalsIgnoreCase("Infosys")) {
                    imgCompany.setImageResource(R.drawable.infosys);
                } else if (company.equalsIgnoreCase("TCS")) {
                    imgCompany.setImageResource(R.drawable.tcs);
                } else if (company.equalsIgnoreCase("Microsoft")) {
                    imgCompany.setImageResource(R.drawable.microsoft);
                } else if (company.equalsIgnoreCase("Samsung")) {
                    imgCompany.setImageResource(R.drawable.samsung);
                } else if (company.equalsIgnoreCase("Accenture")) {
                    imgCompany.setImageResource(R.drawable.accenture);
                } else if (company.equalsIgnoreCase("Wipro")) {
                    imgCompany.setImageResource(R.drawable.wipro);
                } else {
                    imgCompany.setImageResource(R.drawable.company); // Default placeholder
                }
            }
        }

        // Already Applied Check
        if (auth.getCurrentUser() != null) {

            String userId = auth.getCurrentUser().getUid();
            String applicationId = userId + "_" + jobId;

            db.collection("Applications")
                    .document(applicationId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        if (documentSnapshot.exists()) {

                            btnApply.setText("Applied ✓");
                            btnApply.setEnabled(false);

                        }

                    });

            // Already Saved Check
            db.collection("SavedJobs")
                    .document(userId)
                    .collection("jobs")
                    .document(jobId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        if (documentSnapshot.exists()) {

                            btnSave.setText("Saved ❤️");
                            btnSave.setEnabled(false);

                        }

                    });
        }

        // Apply Job
        btnApply.setOnClickListener(v -> {

            if (auth.getCurrentUser() == null) {

                Toast.makeText(this,
                        "Please Login First",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            String userId = auth.getCurrentUser().getUid();
            String applicationId = userId + "_" + jobId;

            db.collection("Applications")
                    .document(applicationId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        if (documentSnapshot.exists()) {

                            Toast.makeText(this,
                                    "Already Applied 🚫",
                                    Toast.LENGTH_SHORT).show();

                            btnApply.setText("Applied ✓");
                            btnApply.setEnabled(false);

                        } else {

                            ApplicationModel model =
                                    new ApplicationModel(
                                            userId,
                                            jobId,
                                            "Applied");

                            db.collection("Applications")
                                    .document(applicationId)
                                    .set(model)
                                    .addOnSuccessListener(unused -> {

                                        Toast.makeText(this,
                                                "Applied Successfully ✅",
                                                Toast.LENGTH_SHORT).show();

                                        btnApply.setText("Applied ✓");
                                        btnApply.setEnabled(false);

                                    })
                                    .addOnFailureListener(e -> {

                                        Toast.makeText(this,
                                                e.getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                    });
                        }

                    });

        });

        // Save Job
        btnSave.setOnClickListener(v -> {

            if (auth.getCurrentUser() == null) {

                Toast.makeText(this,
                        "Please login first",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            String userId = auth.getCurrentUser().getUid();

            HashMap<String, Object> jobMap = new HashMap<>();
            jobMap.put("jobId", jobId);
            jobMap.put("title", getIntent().getStringExtra("title"));
            jobMap.put("company", getIntent().getStringExtra("company"));
            jobMap.put("location", getIntent().getStringExtra("location"));
            jobMap.put("salary", getIntent().getStringExtra("salary"));

            db.collection("SavedJobs")
                    .document(userId)
                    .collection("jobs")
                    .document(jobId)
                    .get()
                    .addOnSuccessListener(doc -> {

                        if (doc.exists()) {

                            Toast.makeText(this,
                                    "Already Saved ❤️",
                                    Toast.LENGTH_SHORT).show();

                            btnSave.setText("Saved ❤️");
                            btnSave.setEnabled(false);

                        } else {

                            db.collection("SavedJobs")
                                    .document(userId)
                                    .collection("jobs")
                                    .document(jobId)
                                    .set(jobMap)
                                    .addOnSuccessListener(unused -> {

                                        Toast.makeText(this,
                                                "Job Saved ❤️",
                                                Toast.LENGTH_SHORT).show();

                                        btnSave.setText("Saved ❤️");
                                        btnSave.setEnabled(false);

                                    });

                        }

                    });

        });

    }
}