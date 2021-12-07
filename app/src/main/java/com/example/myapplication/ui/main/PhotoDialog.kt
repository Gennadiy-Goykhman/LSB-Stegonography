package com.example.myapplication.ui.main

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.DialogPhotoBinding

class PhotoDialog: DialogFragment() {
    private lateinit var binding: DialogPhotoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPhotoBinding.inflate(layoutInflater)
        binding.cardButton.setOnClickListener {
            val intent: Intent = Intent()
            intent.action= Intent.ACTION_PICK
            intent.setType("image/*");
            (activity as MainActivity).startActivityForResult(intent, MainActivity.PICK_IMG_CODE_SECONDARY)
        }
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Нажмите и выберите фото для встраивания")
                .setPositiveButton("Далее", activity as DialogInterface.OnClickListener)
                // .setNegativeButton("Нет", activity as DialogInterface.OnClickListener)
                .setNeutralButton("Закрыть", activity as DialogInterface.OnClickListener)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setImage(imageURI: Uri){
        binding.interImage.setImageURI(imageURI)
        binding.cardButton.setCardBackgroundColor(Color.WHITE)
        (activity as MainActivity)?.setTempType(binding.interImage.drawable.toBitmap())
    }

}