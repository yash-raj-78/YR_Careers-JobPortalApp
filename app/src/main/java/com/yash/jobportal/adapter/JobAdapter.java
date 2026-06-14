package com.yash.jobportal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.jobportal.R;
import com.yash.jobportal.activites.JobDetailsActivity;
import com.yash.jobportal.model.JobModel;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<JobModel> jobList;

    public JobAdapter(List<JobModel> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false);

        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {

        JobModel job = jobList.get(position);

        holder.tvTitle.setText(job.getTitle());
        holder.tvCompany.setText(job.getCompany());
        holder.tvLocation.setText(job.getLocation());
        holder.tvSalary.setText(job.getSalary());

        holder.imgDelete.setVisibility(View.GONE);

        String company = job.getCompany();

        if (company.equalsIgnoreCase("Google")) {

            holder.imgCompany.setImageResource(R.drawable.google);

        } else if (company.equalsIgnoreCase("Infosys")) {

            holder.imgCompany.setImageResource(R.drawable.infosys);

        } else if (company.equalsIgnoreCase("TCS")) {

            holder.imgCompany.setImageResource(R.drawable.tcs);

        } else if (company.equalsIgnoreCase("Microsoft")) {

            holder.imgCompany.setImageResource(R.drawable.microsoft);

        } else if (company.equalsIgnoreCase("Samsung")) {

            holder.imgCompany.setImageResource(R.drawable.samsung);

        } else if (company.equalsIgnoreCase("Accenture")) {

            holder.imgCompany.setImageResource(R.drawable.accenture);

        } else if (company.equalsIgnoreCase("Wipro")) {

            holder.imgCompany.setImageResource(R.drawable.wipro);

        } else {

            holder.imgCompany.setImageResource(R.drawable.company);

        }

        holder.itemView.setOnClickListener(v -> {

            Context context = v.getContext();

            Intent intent = new Intent(context, JobDetailsActivity.class);

            intent.putExtra("jobId", job.getJobId());
            intent.putExtra("title", job.getTitle());
            intent.putExtra("company", job.getCompany());
            intent.putExtra("location", job.getLocation());
            intent.putExtra("salary", job.getSalary());
            intent.putExtra("experience", job.getExperience());
            intent.putExtra("skills", job.getSkills());
            intent.putExtra("description", job.getDescription());

            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCompany, tvLocation, tvSalary;
        ImageView imgCompany, imgDelete;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSalary = itemView.findViewById(R.id.tvSalary);

            imgCompany = itemView.findViewById(R.id.imgCompany);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}