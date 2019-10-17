package com.github.android.sample.jetpack.architecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_saved_state.*

/*
Activit 被系统杀死后，saved_vm_tv 依然显示正确的值
savedState 交给了 SavedStateHandle 与 SavedStateVMFactory 操作
 */
class SavedStateActivity : AppCompatActivity() {

    private lateinit var savedStateViewModel: SavedStateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_state)

        // 引用一个 SavedStateVMFactory
        savedStateViewModel = ViewModelProviders.of(this, SavedStateVMFactory(this))
                .get(SavedStateViewModel::class.java)

        savedStateViewModel.getName().observe(this, Observer<String> {
            saved_vm_tv.text = it
        })

        // saved
        save_bt.setOnClickListener {
            savedStateViewModel.saveNewName(name_et.text.toString())
        }
    }

    // 使用 key 来保存数据
    class SavedStateViewModel(private val state: SavedStateHandle) : ViewModel() {
        private val NAME_KEY = "name"

        // Expose an immutable LiveData
        fun getName(): LiveData<String> {
            // getLiveData obtains an object that is associated with the key wrapped in a LiveData
            // so it can be observed for changes.
            return state.getLiveData(NAME_KEY)
        }

        fun saveNewName(newName: String) {
            // Sets a new value for the object associated to the key. There's no need to set it
            // as a LiveData.
            state.set(NAME_KEY, newName)
        }
    }
}
