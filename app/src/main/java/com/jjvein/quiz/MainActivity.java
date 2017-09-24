package com.jjvein.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private static final String KEY_INDEX = "index";
    private static final String RESULT_INDEX = "result";
    private static final String LOG_KEY = "quiz";
    private int mCurrentIndex = 0;
    private byte[] mQuizResult;

    private Question[] mQuestionBank = new Question[] {

        new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mQuizResult = savedInstanceState.getByteArray(RESULT_INDEX);
        } else {
            mQuizResult = new byte[mQuestionBank.length];
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestion();
            }
        });
        mQuestionTextView.setText(question);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(view, true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(view, false);
            }
        });
        updateQuestion();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putByteArray(RESULT_INDEX, mQuizResult);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        Log.d(LOG_KEY, mCurrentIndex + "" + mQuizResult[mCurrentIndex]);
        if (mQuizResult[mCurrentIndex] != 0) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(View view, boolean userPressedTrue) {
        view.setEnabled(false);
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mQuizResult[mCurrentIndex] = 1;
        } else {
            messageResId = R.string.incorrect_toast;
            mQuizResult[mCurrentIndex] = -1;
        }
        Log.d(LOG_KEY, Arrays.toString(mQuizResult));

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
