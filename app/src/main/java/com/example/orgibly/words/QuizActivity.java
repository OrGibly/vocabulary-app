package com.example.orgibly.words;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.orgibly.words.dialogs.MessageDialog;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends Activity {

    private DataBaseManager db = new DataBaseManager(this);
    QuizStrategy quizStrategy = MyQuizStrategy.getInstance();
    private ArrayList<Word> wordsForQuiz;
    private int curWordIndex = -1;
    private int correctAnswersCounter = 0;

    private TextView questionCounter;
    private TextView word;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private TextView grade;
    private Button next;
    private View endQuizContainer;
    private TextView endQuizTextView;
    private Button restart;

    private static final int MAX_QUIZ_SIZE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        questionCounter = findViewById(R.id.textViewQuestionsCounter);
        word = findViewById(R.id.textViewWordQuestion);
        answer1 = findViewById(R.id.buttonAns1);
        answer2 = findViewById(R.id.buttonAns2);
        answer3 = findViewById(R.id.buttonAns3);
        answer4 = findViewById(R.id.buttonAns4);
        grade = findViewById(R.id.textViewGrade);
        next = findViewById(R.id.buttonNext);
        endQuizContainer = findViewById(R.id.linearLayoutEndQuiz);
        endQuizTextView = findViewById(R.id.textViewEndQuiz);
        restart = findViewById(R.id.buttonRestart);

        wordsForQuiz = quizStrategy.getWordsForQuiz(db,MAX_QUIZ_SIZE);
        if(wordsForQuiz.size()<1){
            MessageDialog dialog= new MessageDialog(this);
            dialog.setMessage("No words in your collection.\n" +
                    "Please add at least one new word.");
            dialog.setOnCancelListener(onCancelListener);
            dialog.show();
            return;
        }
        next(null);
    }

    //  "MyWords" button callback.
    public void startAddWordActivityQ(View view){
        Intent intent = new Intent(this,NewWordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    //  "MyWords" button callback.
    public void startMyWordsActivityQ(View view){
        Intent intent = new Intent(this,MyWordsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    //  Answers callback.
    public void answer(View view){
        Button[] answers = {answer1,answer2,answer3,answer4};

        Word curWord = wordsForQuiz.get(curWordIndex);
        String correctAnswer = curWord.getTranslation();
        for(Button answer:answers){
            answer.setEnabled(false);
            if(answer.getText().equals(correctAnswer)) {
                answer.setBackgroundResource(R.drawable.btn_answer_correct);
                if (answer == view) {//if user's answer is the correct answer:
                    db.increaseWrongAnswers(curWord.getId(),-1);
                    correctAnswersCounter++;
                }
            } else {
                if (answer == view){
                    answer.setBackgroundResource(R.drawable.btn_answer_wrong);
                    db.increaseWrongAnswers(curWord.getId(),1);
                }
            }
        }
        grade.setText("Grade: "+correctAnswersCounter+"/"+(curWordIndex+1));
        next.setEnabled(true);

        //if its last question end quiz.
        if(curWordIndex == wordsForQuiz.size()-1){
            next.setVisibility(View.GONE);
            endQuizContainer.setVisibility(View.VISIBLE);
        }
    }

    //  "Next"/"Again" button callback.
    public void next(View view){
        curWordIndex++;
        next.setEnabled(false);
        Word curWord = wordsForQuiz.get(curWordIndex);
        ArrayList<String> answers = new ArrayList<>(4);//ArrayList for shuffle method.
        answers.add(curWord.getTranslation());
        answers.add(curWord.getW_translation1());
        answers.add(curWord.getW_translation2());
        answers.add(curWord.getW_translation3());
        Collections.shuffle(answers);
        questionCounter.setText("word "+(curWordIndex+1)+"/"+wordsForQuiz.size()+":");
        word.setText(curWord.getWord());

        Button[] btnAnswers = {answer1,answer2,answer3,answer4};
        for(int i=0;i<btnAnswers.length;i++){
            btnAnswers[i].setText(answers.get(i));
            btnAnswers[i].setBackgroundResource(R.drawable.btn_answer);
            btnAnswers[i].setEnabled(true);
        }
    }

    //  "Restart" button callback.
    public void restart(View view){
        next.setVisibility(View.VISIBLE);
        endQuizContainer.setVisibility(View.GONE);
        wordsForQuiz = quizStrategy.getWordsForQuiz(db,MAX_QUIZ_SIZE);
        curWordIndex=-1;
        correctAnswersCounter=0;
        grade.setText("Grade: 0/0");
        next.setText("Next");
        next(null);
    }

//---------------------------------------------------------------------
    DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            startAddWordActivityQ(null);
        }
    };

}
