package com.github.android.sample.anim




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
