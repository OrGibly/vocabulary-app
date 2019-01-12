package com.example.orgibly.words.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.orgibly.words.R;

public class MessageDialog extends Dialog {
    private View contentView;
    public MessageDialog(Context context){
        super(context);
        contentView = getLayoutInflater().inflate(R.layout.message_dialog,null);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(contentView);
    }

    public void setMessage(String msg){
        ((TextView)contentView.findViewById(R.id.textViewMessageDialogMsg)).setText(msg);
    }
}
