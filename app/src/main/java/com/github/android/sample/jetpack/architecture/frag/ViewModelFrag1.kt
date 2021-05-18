package com.github.android.sample.jetpack.architecture.frag

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.better.base.isNull
import com.github.android.sample.R
import kotlinx.android.synthetic.main.fragment_view_model_frag1.*

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ViewModelFrag1 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_model_frag1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The ViewModelStore provides a new ViewModel or one previously created.
        val chronometerViewModel = ViewModelProviders.of(this).get(ChronometerViewModel::class.java)
        // Get the chronometer reference
        val chronometer = view.findViewById<Chronometer>(R.id.chronometer)

        chronometerViewModel.startTime.let {
            if (it.isNull()) {
                // If the start date is not defined, it's a new ViewModel so set it.
                val startTime = SystemClock.elapsedRealtime()
                chronometerViewModel.startTime = startTime
                chronometerViewModel.helloWorld = "better"
                chronometer.base = startTime
            } else {
                // Otherwise the ViewModel has been retained, set the chronometer's base to the original
                // starting time.
                chronometer.base = chronometerViewModel.startTime ?: 0
            }
        }

        hello_textview.text = chronometerViewModel.helloWorld
        chronometer.start()

    }

    // ViewModel
    class ChronometerViewModel(var startTime: Long? = null, var helloWorld: String = "") : ViewModel()

}