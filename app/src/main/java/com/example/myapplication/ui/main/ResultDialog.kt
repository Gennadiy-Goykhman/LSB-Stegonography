package com.example.myapplication.ui.main

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.DialogPhotoBinding
import com.example.myapplication.databinding.DialogResultBinding

class ResultDialog(): DialogFragment() {
    private lateinit var binding: DialogResultBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogResultBinding.inflate(layoutInflater)
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Результат")
                .setPositiveButton("Готово", activity as DialogInterface.OnClickListener)
                // .setNegativeButton("Нет", activity as DialogInterface.OnClickListener)
                .setNeutralButton("Закрыть", activity as DialogInterface.OnClickListener)
                .setNegativeButton("Поделиться", activity as DialogInterface.OnClickListener)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setImage(image: Bitmap){
        binding.interImage.setImageBitmap(image)
        binding.cardButton.setCardBackgroundColor(Color.WHITE)
    }
}