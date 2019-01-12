package com.example.orgibly.words;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.orgibly.words.dialogs.WordDetailsDialog;

//This activity should not have access to the database,
//any action on database is through its adapter.
public class MyWordsActivity extends Activity{

    //indicates if the user wants to hide the word or its translation.
    //use its set method.
    private int hideMode;
    //Constants for hide mode.
    public final int HIDE_TRANSLATION = 0; //Right TextView
    public final int HIDE_WORD = 1; //Left TextView

//------------------------------------------
    ListView myWords;
    WordsListAdapter wordsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_words);
        myWords = findViewById(R.id.listViewMyWords);
        wordsListAdapter = new WordsListAdapter(this);
        myWords.setAdapter(wordsListAdapter);
        setHideMode(HIDE_TRANSLATION);
    }

    //  "MyWords" button callback.
    public void startAddWordActivityM(View view){
        Intent intent = new Intent(this,NewWordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    //  "Quiz" button callback.
    public void startQuizActivityM(View view){
        Intent intent = new Intent(this,QuizActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    //Items' visibility buttons callback.
    public void setItemVisibility(View view){
        //Item's id = word's id (set in the adapter).
        int wordId =((View)view.getParent()).getId();
        wordsListAdapter.switchVisibility(wordId);
        TextView textView;
        if(hideMode==HIDE_WORD){
            textView = ((View)view.getParent()).findViewById(R.id.textViewWord);
        } else if(hideMode==HIDE_TRANSLATION){
            textView = ((View)view.getParent()).findViewById(R.id.textViewTrans);
        } else {
            return;
        }

        //Hide the TextView correspondingly.
        if (textView.getVisibility() == View.VISIBLE){
            textView.setVisibility(View.INVISIBLE);
            view.setBackgroundResource(R.drawable.btn_visibility_invisible);
        }
        else {
            textView.setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.drawable.btn_visibility_visible);
        }
    }

    public void setAllItemsVisibility(View view){
        if ((view).getId()==R.id.buttonSetVisible){
            wordsListAdapter.setAllVisibility(true);
        } else if (view.getId()==R.id.buttonSetInvisible){
            wordsListAdapter.setAllVisibility(false);
        }
    }

    //item's long click callback.
    public void showWordDetailsDialog(View item){
        Word word = wordsListAdapter.getWordById(item.getId());//item's id = its word's id.
        if(word==null)return;
        WordDetailsDialog dialog = new WordDetailsDialog(this, word, wordsListAdapter);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();
    }

//------------------------------------------------------------------
    public int getHideMode() {return hideMode;}

    private void setHideMode(int hideMode) {
        this.hideMode = hideMode;
        wordsListAdapter.notifyDataSetChanged();
    }
}
