package com.example.quizactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private Button mRestartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mRestartButton = findViewById(R.id.restart_button);

        // Obtener los datos pasados desde QuizActivity
        Intent intent = getIntent();
        int correctCount = intent.getIntExtra("correctCount", 0);
        int incorrectCount = intent.getIntExtra("incorrectCount", 0);
        int totalQuestions = correctCount + incorrectCount;

        // Calcular el porcentaje de aciertos
        double correctPercentage = (double) correctCount / totalQuestions * 100;

        // Actualizar los TextViews con los resultados
        TextView correctTextView = findViewById(R.id.correct_count_text_view);
        TextView incorrectTextView = findViewById(R.id.incorrect_count_text_view);
        TextView percentageTextView = findViewById(R.id.percentage_text_view);

        correctTextView.setText("Correctas: " + correctCount);
        incorrectTextView.setText("Incorrectas: " + incorrectCount);
        percentageTextView.setText("Porcentaje de aciertos: " + String.format("%.2f", correctPercentage) + "%");

        mRestartButton.setOnClickListener(v -> {
            Intent restartIntent = new Intent(ResultActivity.this, QuizActivity.class);
            startActivity(restartIntent);
            finish();
        });
    }
}
