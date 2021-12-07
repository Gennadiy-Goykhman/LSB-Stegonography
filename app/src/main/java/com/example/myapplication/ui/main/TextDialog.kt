package com.example.myapplication.ui.main

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogChooserBinding
import com.example.myapplication.databinding.DialogTextBinding

class TextDialog(var mode: String): DialogFragment() {
    private lateinit var binding: DialogTextBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding= DialogTextBinding.inflate(layoutInflater)
        binding.multiAutoCompleteTextView.hint = if (mode == ChooserDialog.TEXT_INT) getString(R.string.text_int_hint)
        else getString(R.string.bin_int_hint)
        binding.multiAutoCompleteTextView.addTextChangedListener {
            (activity as MainActivity).setTempType(it.toString())
        }
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Введите нужную информацию ")
                .setPositiveButton("Далее", activity as DialogInterface.OnClickListener)
                // .setNegativeButton("Нет", activity as DialogInterface.OnClickListener)
                .setNeutralButton("Закрыть", activity as DialogInterface.OnClickListener)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}