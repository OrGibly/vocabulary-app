package com.example.orgibly.words.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.orgibly.words.R;
import com.example.orgibly.words.Word;
import com.example.orgibly.words.WordsListAdapter;

//This dialog should not have access to the database,
//any action on database is through its adapter(WordsListAdapter).
public class WordDetailsDialog extends Dialog{

    //----------------------------------------- CONSTANTS --------------------------------------
    private static final String CHANGES_SAVED_MSG = "Changes have been saved." ;
    private static final String SAVE_CHANGES_ERROR_MSG = "Some problem occurred while saving changes.";
    private static final String MISSING_FIELDS_MSG = "To save changes,\nPlease fill all the fields.";
    private static final String DELETE_MSG = "Are you sure that you want to delete this word?";

    //-------------------------------------------------------------------------------

    private Word curWord;
    private EditText wordET;
    private EditText translationET;
    private EditText wrongTranslation1ET;
    private EditText wrongTranslation2ET;
    private EditText wrongTranslation3ET;
    private LinearLayout wrongTranslationC1;
    private LinearLayout wrongTranslationC2;
    private LinearLayout wrongTranslationC3;
    private WordsListAdapter adapter;
    private Button saveBtn;
    private Button cancelBtn;
    private Button deleteBtn;
    private RadioGroup isInQuizRG;
    private RadioButton addRB;
    private RadioButton doNotAddRB;

    public WordDetailsDialog(Context context, Word word, WordsListAdapter adapter){
        super(context);
        curWord = word;
        this.adapter = adapter;
        //setting the content view.
        View view = getLayoutInflater().inflate(R.layout.word_details_dialog,null);
        wordET = view.findViewById(R.id.editTextDialogWord);
        translationET = view.findViewById(R.id.editTextDialogTranslation);
        isInQuizRG = view.findViewById(R.id.radioGroupDialog);
        addRB = view.findViewById(R.id.radioButtonDialogAdd);
        addRB.setOnClickListener(onAddRBClickListener);
        doNotAddRB = view.findViewById(R.id.radioButtonDialogDoNotAdd);
        doNotAddRB.setOnClickListener(onDoNotAddRBClickListener);
        wrongTranslation1ET = view.findViewById(R.id.editTextDialogWTranslation1);
        wrongTranslation2ET = view.findViewById(R.id.editTextDialogWTranslation2);
        wrongTranslation3ET = view.findViewById(R.id.editTextDialogWTranslation3);
        wrongTranslationC1 = view.findViewById(R.id.wrongTranslationContainer1);
        wrongTranslationC2 = view.findViewById(R.id.wrongTranslationContainer2);
        wrongTranslationC3 = view.findViewById(R.id.wrongTranslationContainer3);
        saveBtn = view.findViewById(R.id.buttonDialogSave);
        cancelBtn = view.findViewById(R.id.buttonDialogCancel);
        saveBtn.setOnClickListener(onClickSaveListener);
        cancelBtn.setOnClickListener(onClickCancelListener);
        deleteBtn = view.findViewById(R.id.buttonDialogDelete);
        deleteBtn.setOnClickListener(onClickDeleteListener);

        wordET.setText(curWord.getWord());
        translationET.setText(curWord.getTranslation());
        if(word.isInQuiz()){
            addRB.setChecked(true);
            onAddRBClickListener.onClick(addRB);
        } else {
            doNotAddRB.setChecked(true);
            onDoNotAddRBClickListener.onClick(doNotAddRB);
        }
        wrongTranslation1ET.setText(curWord.getW_translation1());
        wrongTranslation2ET.setText(curWord.getW_translation2());
        wrongTranslation3ET.setText(curWord.getW_translation3());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(view);
    }

    private boolean updateWord(){
        curWord.setWord(wordET.getText().toString());
        curWord.setTranslation(translationET.getText().toString());
        curWord.setW_translation1(wrongTranslation1ET.getText().toString());
        curWord.setW_translation2(wrongTranslation2ET.getText().toString());
        curWord.setW_translation3(wrongTranslation3ET.getText().toString());
        curWord.setInQuiz(isInQuizRG.getCheckedRadioButtonId()==R.id.radioButtonDialogAdd);
        //returns 'true' if word has been saved successfully.
        return this.adapter.updateWord(curWord);
    }

    private boolean checkInput(){
        if(addRB.isChecked()){
            return !(//returns NOT:
                wordET.getText().toString().matches("^ *$")||
                translationET.getText().toString().matches("^ *$")||
                wrongTranslation1ET.getText().toString().matches("^ *$")||
                wrongTranslation2ET.getText().toString().matches("^ *$")||
                wrongTranslation3ET.getText().toString().matches("^ *$")
            );
        } else {
            return !(//returns NOT:
                wordET.getText().toString().matches("^ *$")||
                translationET.getText().toString().matches("^ *$")
            );
        }

    }

    private void showMessageDialog(String msg){
        MessageDialog dialog = new MessageDialog(getContext());
        dialog.setMessage(msg);
        dialog.show();
    }

    private void showDeleteWordDialog(String msg){
        DeleteWordDialog dialog = new DeleteWordDialog(getContext(),curWord,adapter,this);
        dialog.setMessage(msg);
        dialog.show();
    }

//------------------------------------- listeners -------------------------------
    private View.OnClickListener onAddRBClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            wrongTranslation1ET.setEnabled(true);
            wrongTranslation2ET.setEnabled(true);
            wrongTranslation3ET.setEnabled(true);
            wrongTranslationC1.setAlpha(1f);
            wrongTranslationC2.setAlpha(1f);
            wrongTranslationC3.setAlpha(1f);
        }
    };

    private View.OnClickListener onDoNotAddRBClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            wrongTranslation1ET.setEnabled(false);
            wrongTranslation2ET.setEnabled(false);
            wrongTranslation3ET.setEnabled(false);
            wrongTranslationC1.setAlpha(0.5f);
            wrongTranslationC2.setAlpha(0.5f);
            wrongTranslationC3.setAlpha(0.5f);
        }
    };

    private View.OnClickListener onClickSaveListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(checkInput()){
                if(updateWord()){
                    cancel();
                    Toast.makeText(getContext(),CHANGES_SAVED_MSG,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),SAVE_CHANGES_ERROR_MSG,Toast.LENGTH_SHORT).show();
                }
            } else {
                showMessageDialog(MISSING_FIELDS_MSG);
            }
        }
    };

    private View.OnClickListener onClickDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDeleteWordDialog(DELETE_MSG);
        }
    };

    private View.OnClickListener onClickCancelListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cancel();
        }
    };
}
