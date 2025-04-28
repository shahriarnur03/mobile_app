package com.example.learn_1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private TextView toolbar_text;
    private ImageButton todo_btn, count_btn, weather_btn, add_btn, delete_btn;
    private EditText edit_tx;
    private ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frame_layout);
        toolbar_text = findViewById(R.id.toolbar_text);

        todo_btn = findViewById(R.id.todo_btn);
        count_btn = findViewById(R.id.count_btn);
        weather_btn = findViewById(R.id.weather_btn);

        showTodoLayout();

        todo_btn.setOnClickListener(v -> {
            View todoView = showTodoLayout();
            toolbar_text.setText(R.string.todo);

            RecyclerView recyclerView = todoView.findViewById(R.id.recycle_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            edit_tx = todoView.findViewById(R.id.edit_tx);
            add_btn = todoView.findViewById(R.id.add_btn);

            TaskAdapter adapter = new TaskAdapter(taskList);
            recyclerView.setAdapter(adapter);


            add_btn.setOnClickListener(vi -> {
                String newTask = edit_tx.getText().toString();
                if (!newTask.isEmpty()) {
                    Task newTaskObj = new Task(newTask, false);
                    taskList.add(newTaskObj);
                    adapter.notifyItemInserted(taskList.size() - 1);
                    edit_tx.setText("");
                }
            });
        });

        count_btn.setOnClickListener(v -> {
            showCountLayout();
            toolbar_text.setText(R.string.count);
        });

        weather_btn.setOnClickListener(v -> {
            showWeatherLayout();
            toolbar_text.setText(R.string.weather);
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
    }

    private View showTodoLayout() {
        frameLayout.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_todo, frameLayout, false);
        frameLayout.addView(view);
        return view;
    }

    private void showCountLayout() {
        frameLayout.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_count, frameLayout, false);
        frameLayout.addView(view);
    }

    private void showWeatherLayout() {
        frameLayout.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_weather, frameLayout, false);
        frameLayout.addView(view);
    }
}
