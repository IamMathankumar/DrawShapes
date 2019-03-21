/*
 * Created by Mathankumar K On 1/9/19 11:54 AM
 * Copyright (c) Aximsoft 2019.
 * All rights reserved.
 */

package com.aximsoft.triangle;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DialogAddText extends Dialog implements
        View.OnClickListener {
    private Context c;
    private EditText editText;
    private AddTextListener addTextListener;
    private String text;

    DialogAddText(Context a, AddTextListener addTextListener, String text) {
        super(a);
        c = a;
        this.text = text;
        this.addTextListener = addTextListener;
        // TODO Auto-generated constructor stub
    }

    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.draw_dialog_add_text);
        Button yes = findViewById(R.id.buttonAdd);
        Button no = findViewById(R.id.buttonCancel);
        editText = findViewById(R.id.editText);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
        if (!text.isEmpty()) {
            update = true;
            editText.setText(text);
            editText.setSelection(editText.getText().length());
            yes.setText(R.string.tri_update);
        } else {
            update = false;
            yes.setText(R.string.tri_add);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (null != charSequence)
                    addTextListener.onTextChange(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // dialog dismiss without button press
                cancelled();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAdd) {
            if (editText.getText().toString().trim().isEmpty())
                Toast.makeText(c, "Type text", Toast.LENGTH_SHORT).show();
            else {
                addTextListener.onAdd(editText.getText().toString().trim());
                dismiss();
            }
        } else if (v.getId() == R.id.buttonCancel) {
            cancelled();
        }
    }


    private void cancelled() {
        if (!update)
            addTextListener.onCancel("");
        else
            addTextListener.onCancel(text);
        dismiss();
    }

    interface AddTextListener {
        void onTextChange(String text);

        void onAdd(String text);

        void onCancel(String text);
    }
}