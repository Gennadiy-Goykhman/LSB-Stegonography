package com.example.myapplication.ui.main

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.DialogCompat
import androidx.fragment.app.DialogFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogChooserBinding

class ChooserDialog(): DialogFragment() {
    private lateinit var binding: DialogChooserBinding
    private var mode: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding= DialogChooserBinding.inflate(layoutInflater)
        binding.radiogr.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            run {
                when (checkedId) {
                    R.id.radioButton1 -> mode= PHOTO_INT
                    R.id.radioButton2 -> mode= TEXT_INT
                    R.id.radioButton3 -> mode= BIN_INT
                }
                Log.d("MODE_D","${checkedId}")
                Log.d("MODE_D_TEXT", mode)
                (activity as MainActivity)?.setInterType(mode)
            }
        })
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Выберите, что будет встроено ")
                .setPositiveButton("Далее", activity as DialogInterface.OnClickListener)
               // .setNegativeButton("Нет", activity as DialogInterface.OnClickListener)
               .setNeutralButton("Закрыть", activity as DialogInterface.OnClickListener)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object{
        const val PHOTO_INT="photo"
        const val TEXT_INT="text"
        const val BIN_INT="bin"
    }
}