package com.example.orgibly.words;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orgibly.words.dialogs.MessageDialog;

public class NewWordActivity extends Activity {

    private DataBaseManager db = new DataBaseManager(this);
    private EditText editTextNewWord;
    private EditText editTextTranslation;
    private RadioGroup isInQuizRG;
    private RadioButton addRB;
    private RadioButton doNotAddRB;
    private TextView textViewWTranslation;
    private EditText editTextWTranslation1;
    private EditText editTextWTranslation2;
    private EditText editTextWTranslation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        editTextNewWord = findViewById(R.id.editTextNewWord);
        editTextTranslation = findViewById(R.id.editTextTranslation);
        isInQuizRG = findViewById(R.id.radioGroupNewWord);
        addRB = findViewById(R.id.radioButtonNewWordAdd);
        addRB.setOnClickListener(onAddRBClickListener);
        doNotAddRB = findViewById(R.id.radioButtonNewWordDoNotAdd);
        doNotAddRB.setOnClickListener(onDoNotAddRBClickListener);
        textViewWTranslation = findViewById(R.id.textViewWrongTranslations);
        editTextWTranslation1 = findViewById(R.id.editTextWTranslation1);
        editTextWTranslation2 = findViewById(R.id.editTextWTranslation2);
        editTextWTranslation3 = findViewById(R.id.editTextWTranslation3);
    }

//  "MyWords" navigation button callback.
    public void startMyWordsActivityA(View view){
        Intent intent = new Intent(this,MyWordsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

//  "MyWords" navigation button callback.
    public void startQuizActivityA(View view){
        Intent intent = new Intent(this,QuizActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

//  Add a new word ("+") button callback.
    public void addWord(View view){
        if(checkInput()){
            boolean addToQuiz = isInQuizRG.getCheckedRadioButtonId()==R.id.radioButtonNewWordAdd;
            Word newWord = new Word(editTextNewWord.getText().toString(),
                    editTextTranslation.getText().toString(),
                    addToQuiz,
                    editTextWTranslation1.getText().toString(),
                    editTextWTranslation2.getText().toString(),
                    editTextWTranslation3.getText().toString());

            String msg = db.insertWord(newWord);
            if(msg.isEmpty()){//if everything went good with the database.
                clearEditTexts();
                Toast.makeText(this, "A New word has been added to your collection.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            showMessageDialog("To add a new word to your collection,\n" +
                    "Please fill all the fields.");
        }
    }

//-----------------------------------------------------------------------------------
    private void showMessageDialog(String msg){
        MessageDialog dialog = new MessageDialog(this);
        dialog.setMessage(msg);
        dialog.show();
    }

    private void clearEditTexts(){
        editTextNewWord.setText("");
        editTextTranslation.setText("");
        editTextWTranslation1.setText("");
        editTextWTranslation2.setText("");
        editTextWTranslation3.setText("");
    }

    private boolean checkInput(){
        if(addRB.isChecked()){
            return !(//returns NOT:
                editTextNewWord.getText().toString().matches("^ *$")||
                editTextTranslation.getText().toString().matches("^ *$")||
                editTextWTranslation1.getText().toString().matches("^ *$")||
                editTextWTranslation2.getText().toString().matches("^ *$")||
                editTextWTranslation3.getText().toString().matches("^ *$"));
        } else {
            return !(//returns NOT:
                editTextNewWord.getText().toString().matches("^ *$")||
                editTextTranslation.getText().toString().matches("^ *$"));
        }
    }

//----------------------------------------- Listeners ------------------------------------------
    private View.OnClickListener onAddRBClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editTextWTranslation1.setEnabled(true);
            editTextWTranslation2.setEnabled(true);
            editTextWTranslation3.setEnabled(true);
            textViewWTranslation.setAlpha(1f);
            editTextWTranslation1.setAlpha(1f);
            editTextWTranslation2.setAlpha(1f);
            editTextWTranslation3.setAlpha(1f);
        }
    };

    private View.OnClickListener onDoNotAddRBClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editTextWTranslation1.setEnabled(false);
            editTextWTranslation2.setEnabled(false);
            editTextWTranslation3.setEnabled(false);
            textViewWTranslation.setAlpha(0.4f);
            editTextWTranslation1.setAlpha(0.5f);
            editTextWTranslation2.setAlpha(0.5f);
            editTextWTranslation3.setAlpha(0.5f);

        }
    };
}

