/*
 * Created by Mathankumar K On 1/9/19 11:54 AM
 * Copyright (c) Aximsoft 2019.
 * All rights reserved.
 */

package com.aximsoft.triangle;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
        final int color = Color.GREEN;

        b_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddLine(color);
            }
        });
        b_freeDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddFreeDraw(color);
            }
        });
        b_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddCircle(color);

            }
        });
        b_square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddSquare(color);

            }
        });
        b_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddText(color);
            }
        });
        b_triAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentView.onAddTriAngle(color);
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

