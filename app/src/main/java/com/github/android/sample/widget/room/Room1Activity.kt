package com.github.android.sample.widget.room

import android.arch.persistence.room.Room
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.e
import com.better.base.setTitleFromIntent
import com.better.base.toast
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_room1.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class Room1Activity : ToolbarActivity() {


    lateinit var mobilePhoneDao: MobilePhoneDao

    val db: AppDatabase by lazy {
        val a = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "roomTest.db")
                .allowMainThreadQueries().build()
        mobilePhoneDao = a.mobilePhoneDao()
        a
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room1)
        setTitleFromIntent(intent)

        db.isOpen

        // init db
        btn_init.onClick {
            e("" + db.isOpen)
        }

        // add
        btn_add.onClick {
            addPhone()
        }

        // getAll
        btn_getAll.onClick {
            val list = mobilePhoneDao.getAll()
            list?.forEach {
                e(it.toString())
            }
        }

        // search
        btn_search.onClick {
            search()
        }
    }

    private inline fun addPhone() {
        val phone = MobilePhone(
                name = et_name.text.toString().trim(),
                price = et_price.text.toString()?.toDouble(),
                width = et_width.text.toString()?.toInt(),
                height = et_height.text.toString()?.toInt()
        )
        mobilePhoneDao.add(phone)
    }

    private inline fun search() {
        et_search_name.text.apply {
            if (this.trim().isEmpty()) {
                toast("Please input search key")
            } else {
                mobilePhoneDao.query(this.toString())?.let { it ->
                    toast(it.toString())
                } ?: toast("not found ${this}")
            }
        }
    }
}
