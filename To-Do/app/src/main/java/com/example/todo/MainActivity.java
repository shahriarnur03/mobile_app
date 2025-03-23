package com.example.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<String> taskList;
    private EditText etNewList;
    private ImageButton btnAddList;
    private TextView categoryTextView;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        etNewList = findViewById(R.id.etNewList);
        btnAddList = findViewById(R.id.btnAddList);

        // Initialize task list
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        // Add button click listener
        btnAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTask = etNewList.getText().toString().trim();
                if (!newTask.isEmpty()) {
                    taskList.add(newTask);
                    adapter.notifyItemInserted(taskList.size() - 1);
                    etNewList.setText("");
                    count+= 1;
//                    categoryTextView.setText(count + "Categories");
                }
            }
        });
    }
}
