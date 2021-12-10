package com.example.myapplication.ui.main

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogTextBinding
import com.example.myapplication.databinding.DialogTextresultBinding

class ResultTextDialog : DialogFragment() {
    private lateinit var binding: DialogTextresultBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding= DialogTextresultBinding.inflate(layoutInflater)

        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Результат ")
                .setPositiveButton("Далее", activity as DialogInterface.OnClickListener)
                // .setNegativeButton("Нет", activity as DialogInterface.OnClickListener)
                .setNeutralButton("Закрыть", activity as DialogInterface.OnClickListener)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    public fun setText(text: String){
        binding.multiAutoCompleteTextView.setText(text)
    }
}
