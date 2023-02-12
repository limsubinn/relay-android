package com.example.relay.running.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.relay.databinding.DialogRunningBinding

class CustomDialog (context: Context) : Dialog(context) {

    private lateinit var binding: DialogRunningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = DialogRunningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        imgClose.setOnClickListener {
            dismiss()
        }
    }

}