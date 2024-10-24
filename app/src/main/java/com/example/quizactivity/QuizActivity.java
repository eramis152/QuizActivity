package com.example.quizactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private static final String KEY_INDEX = "index";
    private static final String KEY_CORRECT_COUNT = "correctCount";
    private static final String KEY_INCORRECT_COUNT = "incorrectCount";
    private static final String KEY_BUTTONS_DISABLED = "buttonsDisabled";
    private static final String KEY_QUESTIONS_ANSWERED = "questionsAnswered";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private TextView mCorrectCountTextView;
    private TextView mIncorrectCountTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int mCorrectCount = 0;
    private int mIncorrectCount = 0;
    private boolean mButtonsDisabled = false;
    private List<Integer> mQuestionsAnswered = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Referencias a los widgets
        mQuestionTextView = findViewById(R.id.question_text_view);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);

        // Restaurar el estado si existe
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCorrectCount = savedInstanceState.getInt(KEY_CORRECT_COUNT, 0);
            mIncorrectCount = savedInstanceState.getInt(KEY_INCORRECT_COUNT, 0);
            mButtonsDisabled = savedInstanceState.getBoolean(KEY_BUTTONS_DISABLED, false);
            mQuestionsAnswered = savedInstanceState.getIntegerArrayList(KEY_QUESTIONS_ANSWERED);
            if (mButtonsDisabled) {
                disableAnswerButtons();
            }
        }

        // Actualizar la pregunta actual
        updateQuestion();

        // Configuración de los listeners para los botones
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                disableAnswerButtons();  // Deshabilitar los botones al responder
                mButtonsDisabled = true;
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                disableAnswerButtons();  // Deshabilitar los botones al responder
                mButtonsDisabled = true;
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Avanzar a la siguiente pregunta sin repetir
                if (mQuestionsAnswered.size() < mQuestionBank.length) {
                    getNextQuestion();
                    updateQuestion();
                    enableAnswerButtons();  // Habilitar nuevamente los botones para la nueva pregunta
                    mButtonsDisabled = false;
                } else {
                    // Ir a la nueva pantalla de resultados
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correctCount", mCorrectCount);
                    intent.putExtra("incorrectCount", mIncorrectCount);
                    startActivity(intent);
                    finish(); // Finaliza esta actividad para que no se pueda volver atrás
                }
            }
        });
    }

    // Guardar el estado cuando la actividad se va a destruir (rotar/minimizar)
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_CORRECT_COUNT, mCorrectCount);
        outState.putInt(KEY_INCORRECT_COUNT, mIncorrectCount);
        outState.putBoolean(KEY_BUTTONS_DISABLED, mButtonsDisabled);
        outState.putIntegerArrayList(KEY_QUESTIONS_ANSWERED, (ArrayList<Integer>) mQuestionsAnswered);
    }

    private void updateQuestion() {
        if (mQuestionsAnswered.size() < mQuestionBank.length) {
            int question = mQuestionBank[mCurrentIndex].getTextResId();
            mQuestionTextView.setText(question);
        }
    }

    private void getNextQuestion() {
        // Evitar repetir preguntas ya respondidas
        do {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        } while (mQuestionsAnswered.contains(mCurrentIndex) && mQuestionsAnswered.size() < mQuestionBank.length);

        // Marcar la pregunta como respondida
        mQuestionsAnswered.add(mCurrentIndex);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mCorrectCount++;
        } else {
            messageResId = R.string.incorrect_toast;
            mIncorrectCount++;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }


    // Método para deshabilitar los botones de respuesta
    private void disableAnswerButtons() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    // Método para habilitar los botones de respuesta cuando se cambia la pregunta
    private void enableAnswerButtons() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }
}
