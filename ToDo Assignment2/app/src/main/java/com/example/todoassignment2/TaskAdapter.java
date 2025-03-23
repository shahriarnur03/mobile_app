package com.example.todoassignment2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private DBHelper dbHelper;

    public TaskAdapter(List<Task> taskList, DBHelper dbHelper) {
        this.taskList = taskList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTaskTitle.setText(task.getTitle());

        if (task.isCompleted()) {
            holder.taskContainer.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.holo_green_light));
        } else {
            holder.taskContainer.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.white));
        }

        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deleteTask(task.getId());
            taskList.remove(position);
            notifyItemRemoved(position);
        });

        holder.btnComplete.setOnClickListener(v -> {
            task.setCompleted(true);
            dbHelper.updateTask(task);
            notifyItemChanged(position);
        });

        holder.btnEdit.setOnClickListener(v -> {
            holder.tvTaskTitle.setVisibility(View.GONE);
            holder.etEditTask.setVisibility(View.VISIBLE);
            holder.etEditTask.setText(task.getTitle());

            holder.etEditTask.setOnFocusChangeListener((v1, hasFocus) -> {
                if (!hasFocus) {
                    String editedTitle = holder.etEditTask.getText().toString();
                    if (!editedTitle.isEmpty()) {
                        task.setTitle(editedTitle);
                        dbHelper.updateTask(task);
                        notifyItemChanged(position);
                    }
                    holder.tvTaskTitle.setVisibility(View.VISIBLE);
                    holder.etEditTask.setVisibility(View.GONE);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTaskTitle;
        EditText etEditTask;
        ImageButton btnDelete;
        ImageButton btnEdit;
        ImageButton btnComplete;
        LinearLayout taskContainer;

        public TaskViewHolder(View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            etEditTask = itemView.findViewById(R.id.etEditTask);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            taskContainer = itemView.findViewById(R.id.taskContainer);
        }
    }
}
