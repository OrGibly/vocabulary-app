package com.example.orgibly.words.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orgibly.words.R;
import com.example.orgibly.words.Word;
import com.example.orgibly.words.WordsListAdapter;

public class DeleteWordDialog extends Dialog {

    //----------------------------------------- CONSTANTS --------------------------------------
    private static final String WORD_DELETED_MSG = "Word has been deleted.";
    private static final String WORD_DELETE_ERROR_MSG = "Some problem occurred while deleting.";

    //-------------------------------------------------------------------------------
    private Word curWord;
    private WordsListAdapter adapter;
    private WordDetailsDialog fatherDialog;//needed for canceling after canceling this dialog.

    private Button yesBtn;
    private Button noBtn;

    DeleteWordDialog(Context context, Word word, WordsListAdapter adapter, WordDetailsDialog fatherDialog){
        super(context);
        curWord = word;
        this.adapter = adapter;
        this.fatherDialog = fatherDialog;
        View contentView = getLayoutInflater().inflate(R.layout.delete_word_dialog,null);
        yesBtn = contentView.findViewById(R.id.buttonYesDeleteWordDialog);
        yesBtn.setOnClickListener(onClickYesListener);
        noBtn = contentView.findViewById(R.id.buttonNoDeleteWordDialog);
        noBtn.setOnClickListener(onClickNoListener);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(contentView);
    }

    public void setMessage(String msg){
        ((TextView)findViewById(R.id.textViewDeleteWordDialogMsg)).setText(msg);
    }

    private View.OnClickListener onClickYesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(adapter.deleteWord(curWord)){
                cancel();
                fatherDialog.cancel();
                Toast.makeText(getContext(), WORD_DELETED_MSG,Toast.LENGTH_SHORT).show();
            } else {
                cancel();
                fatherDialog.cancel();
                Toast.makeText(getContext(), WORD_DELETE_ERROR_MSG,Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener onClickNoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cancel();
        }
    };

}
