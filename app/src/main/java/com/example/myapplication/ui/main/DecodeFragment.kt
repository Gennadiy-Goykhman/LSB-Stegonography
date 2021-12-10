package com.example.myapplication.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.MainActivity
import com.example.myapplication.MainActivity.Companion.PICK_IMG_CODE_FOR_OUT
import com.example.myapplication.databinding.FragmentDecodeBinding
import java.net.URI

class DecodeFragment(fm: FragmentManager): Fragment() {
    private lateinit var binding: FragmentDecodeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentDecodeBinding.inflate(inflater,container,false)
        binding.interImage.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.setType("image/*");
            (activity as MainActivity).startActivityForResult(intent, PICK_IMG_CODE_FOR_OUT)
        })
        return binding.root
    }

    fun setImage(imageURI: Uri){
        binding.interImage.setImageURI(imageURI)
        binding.cardButton.setCardBackgroundColor(Color.WHITE)
    }

    fun getImage(): Bitmap {
        return binding.interImage.drawable.toBitmap()
    }

}