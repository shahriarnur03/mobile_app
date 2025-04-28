package com.example.learn_1;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<Task> tasklist;
    public TaskAdapter(ArrayList<Task> tasklist) {
        this.tasklist = tasklist;
    }

    @NonNull


    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent , false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = tasklist.get(position);
        holder.title_tv.setText(task.getTitle());

        holder.delete_btn.setOnClickListener( v -> {
            tasklist.remove(position);
            notifyItemRemoved(position);
        });

    }

    @Override
    public int getItemCount() {
        return tasklist.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv;
        ImageButton edit_btn, delete_btn, done_btn;
         public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
             title_tv = itemView.findViewById(R.id.title_tv);
             edit_btn = itemView.findViewById(R.id.edit_btn);
             edit_btn = itemView.findViewById(R.id.edit_btn);
        }
    }
}
