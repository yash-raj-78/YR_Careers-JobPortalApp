package com.yash.jobportal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yash.jobportal.R;
import com.yash.jobportal.activites.JobDetailsActivity;
import com.yash.jobportal.model.JobModel;

import java.util.List;

public class SavedJobAdapter extends RecyclerView.Adapter<SavedJobAdapter.ViewHolder> {

    private List<JobModel> list;
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDelete(JobModel job);
    }

    public SavedJobAdapter(List<JobModel> list,
                           OnDeleteClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCompany, tvLocation, tvSalary;
        ImageView imgCompany, imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSalary = itemView.findViewById(R.id.tvSalary);

            imgCompany = itemView.findViewById(R.id.imgCompany);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        JobModel job = list.get(position);

        holder.tvTitle.setText(job.getTitle());
        holder.tvCompany.setText(job.getCompany());
        holder.tvLocation.setText(job.getLocation());
        holder.tvSalary.setText(job.getSalary());

        holder.imgDelete.setVisibility(View.VISIBLE);

        holder.imgDelete.setOnClickListener(v -> {
            listener.onDelete(job);
        });

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

        } else {

            holder.imgCompany.setImageResource(R.drawable.company);
        }

        holder.itemView.setOnClickListener(v -> {

            Context context = v.getContext();

            Intent intent = new Intent(context,
                    JobDetailsActivity.class);

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
        return list.size();
    }
}