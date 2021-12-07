package com.example.myapplication.ui.main

import android.R.attr.data
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.hardware.camera2.params.MeteringRectangle
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.MainActivity
import com.example.myapplication.MainActivity.Companion.PICK_IMG_CODE_FOR_INT
import com.example.myapplication.databinding.FragmentEncodeBinding


class EncodeFragment(var fm: FragmentManager): Fragment() {
    public lateinit var binding:FragmentEncodeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentEncodeBinding.inflate(inflater,container,false)
        binding.interImage.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent()
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            (activity as MainActivity).startActivityForResult(Intent.createChooser(intent, "Выберите картинку"), PICK_IMG_CODE_FOR_INT)
        })
        return binding.root
    }

    fun setImage(imageURI: Uri){
      //  test_func(imageURI)
        binding.interImage.setImageURI(imageURI)
        binding.cardButton.setCardBackgroundColor(Color.WHITE)
        Log.d("URI_ENC","${imageURI}")
    }

    fun getImage(): Bitmap {
        return binding.interImage.drawable.toBitmap()
    }

    fun test_func(imageURI: Uri){
        val matrix: Matrix = Matrix()
        val bitmap = MediaStore.Images.Media.getBitmap(activity?.getContentResolver(), imageURI)
       matrix.postScale(0.05f, 0.05f)
        val bitmap1=Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.width,matrix,false)
        bitmap1.density=DisplayMetrics.DENSITY_XXXHIGH
        bitmap1.config=Bitmap.Config.ARGB_8888
        binding.interImage.setImageBitmap(bitmap1)

    }


}