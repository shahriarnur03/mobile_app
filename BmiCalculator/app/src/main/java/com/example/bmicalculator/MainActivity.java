package com.example.bmicalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button calculate;
    private EditText heightInput;
    private EditText weightInput;
    private TextView bmi_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        calculate = findViewById(R.id.calculate);
        heightInput = findViewById(R.id.height_input);
        weightInput = findViewById(R.id.weight_input);
        bmi_result = findViewById(R.id.bmi_result);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String height = heightInput.getText().toString();
                String Weight = weightInput.getText().toString();

                if(height.isEmpty() || Weight.isEmpty()){
                    bmi_result.setText("Please Enter Height and Weight");
                    return;
                }
                double height_d = Double.parseDouble(height);
                double Weight_d = Double.parseDouble(Weight);

//                bmi_result.setText(String.format("Your BMI is %.2f", Weight_d / (height_d * height_d)));
                double bmi = (Weight_d*100*100) / (height_d * height_d);
                bmi_result.setText(String.format("Your BMI is %.2f", bmi));
//                bmi_result.setText("Your BMI is"+ bmi);

            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}