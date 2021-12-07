package com.example.myapplication.adapters

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myapplication.MainActivity.Companion.PICK_IMG_CODE_FOR_OUT
import com.example.myapplication.MainActivity.Companion.PICK_IMG_CODE_FOR_INT
import com.example.myapplication.ui.main.DecodeFragment
import com.example.myapplication.ui.main.EncodeFragment
import java.lang.Exception

class ModesPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    public  val encodeFragment: EncodeFragment = EncodeFragment(fm)
    private  val decodeFragment: DecodeFragment = DecodeFragment(fm)
    public val titleList: ArrayList<String> = ArrayList()


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0-> return encodeFragment
            1-> return decodeFragment
            else -> {
                Log.e("FR_ADAPTER","Position is noy included")
                throw Exception()}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0-> return titleList.get(0)
            1-> return titleList.get(1)
            else -> {
                Log.e("FR_ADAPTER","Position is noy included")
                throw Exception()}
        }
    }

    fun setMainImage(imageURI: Uri, requestCode: Int){
        when(requestCode){
            PICK_IMG_CODE_FOR_INT-> {
                encodeFragment.setImage(imageURI)
                Log.d("URI_ADP_1","${imageURI}")
            }
            PICK_IMG_CODE_FOR_OUT-> {
                decodeFragment.setImage(imageURI)
                Log.d("URI_ADP_2","${imageURI}")
            }
        }
        Log.d("URI_ADP","${imageURI}")
    }

    fun getMainImage(mode: String): Bitmap{
        return if (mode=="integrate") encodeFragment.getImage() else decodeFragment.getImage()
    }



}
