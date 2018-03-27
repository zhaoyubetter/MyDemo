package com.github.android.sample.anim;


import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.android.sample.R;

import java.util.Random;

public class AnimateLayoutChangesActivity extends AppCompatActivity {

    private LinearLayout container;
    private LayoutTransition transition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_layout_changes);

        container = findViewById(R.id.container);


        // default
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.addView(getCustomView());
            }
        });

        findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (container.getChildCount() > 0) {
                    container.removeViewAt(0);
                }
            }
        });


        // LayoutTransition.APPEARING
        transition = new LayoutTransition();
        transition.setAnimator(LayoutTransition.APPEARING,
                ObjectAnimator.ofFloat(null, "rotationY", -90f, 90f, 0f));

        // LayoutTransition.DISAPPEARING
        transition.setAnimator(LayoutTransition.DISAPPEARING,
                ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f));

        // 设置给ViewGroup
        container.setLayoutTransition(transition);


        // 添加
        findViewById(R.id.btn_layout_transition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.addView(getCustomView(), 0);
            }
        });

        // 移除
        findViewById(R.id.btn_layout_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (container.getChildCount() > 0) {
                    container.removeViewAt(0);
                }
            }
        });

        // LayoutTransition.CHANGE_APPEARING和LayoutTransition.CHANGE_DISAPPEARING必须使用
        // PropertyValuesHolder所构造的动画才会有效果，不然无效

        // LayoutTransition.CHANGE_APPEARING，添加元素调用addView()方法时，index值，不能是最后一个
        findViewById(R.id.btn_change_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 必须的left与top
                PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 20, 0);
                PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 20, 0);
                PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f, 1f);
                transition.setAnimator(LayoutTransition.CHANGE_APPEARING,
                        ObjectAnimator.ofPropertyValuesHolder(container, pvhLeft, pvhTop, scaleX));
                //设置单个item间的动画间隔
                transition.setStagger(LayoutTransition.CHANGE_APPEARING, 800);
                container.setLayoutTransition(transition);
                container.addView(getCustomView(), 0);
                transition.addTransitionListener(new LayoutTransition.TransitionListener() {
                    @Override
                    public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                    }

                    @Override
                    public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

                    }
                });
            }
        });

        // LayoutTransition.CHANGE_DISAPPEARING
        findViewById(R.id.btn_change_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyValuesHolder outLeft = PropertyValuesHolder.ofInt("left", 0, 0);
                PropertyValuesHolder outTop = PropertyValuesHolder.ofInt("top", 0, 0);
                PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotationY", 0f, 90f, 0f);
                transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
                        ObjectAnimator.ofPropertyValuesHolder(container, outLeft, outTop, rotation));
                if (container.getChildCount() > 0) {
                    container.removeViewAt(0);
                }
            }
        });
    }

    private Button getCustomView() {
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setText("abc" + new Random().nextInt(20));
        return btn;
    }
}


/*
class AnimateLayoutChangesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animate_layout_changes)

        // 设置 toolbar
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }

        // btn_add
        btn_add.setOnClickListener {
            container.addView(getCustomView())
        }

        // btn_remove
        btn_remove.setOnClickListener {
            if (container.childCount > 0) {
                container.removeViewAt(container.childCount - 1)
            }
        }

        // layoutTransaction
        btn_layout_transition.setOnClickListener {
            val transition = LayoutTransition()
            val objAnimator = ObjectAnimator.ofFloat(container, "rotation", 0f, -90f, 90f, 0f)
            transition.setAnimator(LayoutTransition.APPEARING, objAnimator)
            container.layoutTransition = transition
        }
    }

    fun getCustomView(): View {
        return Button(baseContext).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            text = "abc${Random().nextInt(20)}"
        }
    }

}*/
