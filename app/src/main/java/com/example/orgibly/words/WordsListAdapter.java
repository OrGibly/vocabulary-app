package com.example.orgibly.words;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsListAdapter extends BaseAdapter {

    private DataBaseManager db;
    private ArrayList<Word> wordsList;
    private MyWordsActivity activity;

    public WordsListAdapter(MyWordsActivity activity){
        this.activity = activity;
        db = new DataBaseManager(activity);
        wordsList = db.getAllWords(false);
    }

//------------------------------------ getView ------------------------------------
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = (activity.getLayoutInflater().inflate(R.layout.item_word, null));
        }
        TextView wordTextView = view.findViewById(R.id.textViewWord);
        wordTextView.setText(wordsList.get(i).getWord());
        TextView translationTextView = view.findViewById(R.id.textViewTrans);
        translationTextView.setText(wordsList.get(i).getTranslation());
        Button visibilityButton = view.findViewById(R.id.buttonVisibility);

        wordTextView.setBackgroundResource(R.drawable.item_textview_background);
        translationTextView.setBackgroundResource(R.drawable.item_textview_background);

        //Hide or uncover the word or the translation of each word
        //depend on its visibility attribute.
        int hideMode = activity.getHideMode();
        boolean isVisible = wordsList.get(i).isVisible();
        if(isVisible){
            if(hideMode==activity.HIDE_WORD){
                wordTextView.setVisibility(View.VISIBLE);
            }else if(hideMode==activity.HIDE_TRANSLATION){
                translationTextView.setVisibility(View.VISIBLE);
            }
            visibilityButton.setBackgroundResource(R.drawable.btn_visibility_visible);
        }else {
            if(hideMode==activity.HIDE_WORD){
                wordTextView.setVisibility(View.INVISIBLE);
            }else if(hideMode==activity.HIDE_TRANSLATION){
                translationTextView.setVisibility(View.INVISIBLE);
            }
            visibilityButton.setBackgroundResource(R.drawable.btn_visibility_invisible);
        }

        view.setOnLongClickListener(onLongClickListener);
        //set word's id to its item's id
        view.setId(wordsList.get(i).getId());
        return view;
    }

//---------------------------------------------------------------------------------------
    @Override
    public int getCount() {
        return wordsList.size();
    }

    @Override
    public Object getItem(int i) {
        return wordsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return wordsList.get(i).getId();
    }

//---------------------------------------------------------------------------------------
    public Word getWordById(int id){
        for(Word word:wordsList){
            if(word.getId()==id)return word;
        }
        return null;
    }

    public void switchVisibility(int wordId){
        if(db.switchVisibility(wordId).equals(DataBaseManager.EMPTY_MSG)){
            getWordById(wordId).switchVisibility();
        }
    }

    public void setAllVisibility(boolean visibility){
        db.setAllVisibility(visibility);
        wordsList = db.getAllWords(false);
        notifyDataSetChanged();
    }

    public boolean updateWord(Word word){
        if(db.updateWord(word).equals(DataBaseManager.EMPTY_MSG)){
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean deleteWord(Word word){
        if(db.deleteWord(word).equals(DataBaseManager.EMPTY_MSG)){
            wordsList = db.getAllWords(false);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }
    //----------------------------------------------------------------------------------------
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            activity.showWordDetailsDialog(view);
            return true;
        }
    };
}
