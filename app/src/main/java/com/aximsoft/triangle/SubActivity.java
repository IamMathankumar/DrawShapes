package com.aximsoft.triangle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

@SuppressLint("ClickableViewAccessibility")
public class SubActivity extends AppCompatActivity {
    AnnotationView parentView;
    Button b_line, b_circle, b_freeDraw, b_square, b_text, b_triAngle, b_undo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_activity_sub);
        parentView = findViewById(R.id.parentView);
        b_freeDraw =  findViewById(R.id.b_freeDraw);
        b_circle =  findViewById(R.id.b_circle);
        b_line =  findViewById(R.id.b_line);
        b_square =  findViewById(R.id.b_square);
        b_text =  findViewById(R.id.b_text);
        b_triAngle =   findViewById(R.id.b_triAngle);
        b_undo =   findViewById(R.id.b_undo);


        b_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddLine();
            }
        });
        b_freeDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddFreeDraw();
            }
        });
        b_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddCircle();

            }
        });
        b_square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddSquare();

            }
        });
        b_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddText();
            }
        });
        b_triAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddTriAngle();
            }
        });
        b_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.undo();
            }
        });

    }


}

