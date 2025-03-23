package com.example.todoassignment2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private EditText etNewList;
    private ImageButton btnAddList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        etNewList = findViewById(R.id.etNewList);
        btnAddList = findViewById(R.id.btnAddList);

        dbHelper = new DBHelper(this);
        taskList = dbHelper.getAllTasks();

        adapter = new TaskAdapter(taskList, dbHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAddList.setOnClickListener(v -> {
            String newTaskTitle = etNewList.getText().toString().trim();
            if (!newTaskTitle.isEmpty()) {
                long newTaskId = dbHelper.addTask(newTaskTitle);
                Task newTask = new Task((int) newTaskId, newTaskTitle, false);
                taskList.add(newTask);
                adapter.notifyItemInserted(taskList.size() - 1);
                etNewList.setText("");
            }
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
    }
}
