package com.github.android.sample.annotationProcesser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.android.sample.R;
import com.github.better.annotation.BindView;
import com.github.better.app.annotationapi.BetterKnife;

public class AnnotationProcesserTestActivity extends AppCompatActivity {

//    @BindView(R.id.btn1)
    Button btn1;
//    @BindView(R.id.btn2)
    Button btn2;
//    @BindView(R.id.txt1)
    TextView txt1;
//    @BindView(R.id.txt2)
    TextView txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_processer_test);
        BetterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BetterKnife.unBind(this);
    }
}
