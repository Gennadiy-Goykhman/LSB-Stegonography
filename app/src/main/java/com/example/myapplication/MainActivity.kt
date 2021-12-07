package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.myapplication.adapters.ModesPagerAdapter
import com.example.myapplication.databinding.ActivityCoderBinding
import com.example.myapplication.ui.main.ChooserDialog
import com.example.myapplication.ui.main.PhotoDialog
import com.example.myapplication.ui.main.TextDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), DialogInterface.OnClickListener{
    private lateinit var binding: ActivityCoderBinding
    private var operation_state: String = START_POSITION
    private lateinit var pdialog: ProgressDialog;

    private var inter_type: String=""
    private lateinit var temp_photo: Bitmap
    private lateinit var temp_string: String
    private lateinit var dialogMain: DialogFragment
    private var resultImg: Bitmap? = null
    private lateinit var dataObject: ObjectDataContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCoderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sectionsPagerAdapter = ModesPagerAdapter(supportFragmentManager)
        sectionsPagerAdapter.titleList.add(getText(R.string.tab_text_1).toString())
        sectionsPagerAdapter.titleList.add(getText(R.string.tab_text_2).toString())
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val permissions: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        }
        binding.fab.setOnClickListener { view ->
            dialogMain = ChooserDialog()
            operation_state= TYPE_POSITION
            dialogMain.show(supportFragmentManager,"chooserDialog")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(!(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                    Toast.makeText(baseContext,"Не были даны разрешение", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode== RESULT_OK && data!=null){
                if(requestCode== PICK_IMG_CODE_SECONDARY){
                   // (binding.viewPager.adapter as ModesPagerAdapter).executeTwoImagesOperation(data.data!!)

                    (dialogMain as PhotoDialog).setImage(data.data!!)
                }else{
                    (binding.viewPager.adapter as ModesPagerAdapter).setMainImage(data.data!!,requestCode)
                    Log.d("URI_ACT","${data.data!!}")
                    Log.d("URI_ACT_CODE","${requestCode}")
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object{
        const val PICK_IMG_CODE_SECONDARY=1111
        const val PERMISSION_CODE=1234
        const val PICK_IMG_CODE_FOR_INT=1001
        const val PICK_IMG_CODE_FOR_OUT=1002
        const val START_POSITION="start"
        const val TYPE_POSITION="type"
        const val DATA_POSITION="data"
        const val RESULT_POSITION="result"
    }

    fun setInterType(mode: String){
        inter_type=mode
    }

    fun setTempType(picture: Bitmap){
        temp_photo=picture
    }
    fun setTempType(text: String){
        temp_string=text
    }

    override fun onClick(dialog: DialogInterface?, which: Int): Unit = runBlocking {
        when(which){
            DialogInterface.BUTTON_POSITIVE ->{
                when(operation_state) {
                    TYPE_POSITION -> {
                        dialogMain = if (inter_type == ChooserDialog.PHOTO_INT) PhotoDialog() else TextDialog(inter_type)
                        operation_state = DATA_POSITION
                        dialogMain.show(supportFragmentManager, "DataDialog")
                    }
                    DATA_POSITION -> {
                       // val dialog = ChooserDialog()
                        operation_state = RESULT_POSITION
                        val oper_type: String=if( binding.viewPager.currentItem==0)"integrate" else "output"
                        dataObject=if(inter_type == ChooserDialog.PHOTO_INT)
                            ObjectDataContainer(oper_type,inter_type,
                                (binding.viewPager.adapter as ModesPagerAdapter).getMainImage(oper_type),codeBitmap = temp_photo)
                        else
                            ObjectDataContainer(oper_type,inter_type,
                                (binding.viewPager.adapter as ModesPagerAdapter).getMainImage(oper_type),codeText =temp_string)
                       Log.d("COLLECTED_DATA","${dataObject}")
                        val ResultJob=launch {
                            getResult(dataObject)
                        }
                    // dialog.show(supportFragmentManager, "chooserDialog")
                    }
                    RESULT_POSITION -> {
                        operation_state = START_POSITION

                    }
                }
            }
            DialogInterface.BUTTON_NEGATIVE ->{
                operation_state = START_POSITION
            }
        }
    }


    private suspend fun getResult(dataObject: ObjectDataContainer) {
            resultImg=Integrator.analyze(dataObject)
        (binding.viewPager.adapter as ModesPagerAdapter).encodeFragment.binding.interImage.setImageBitmap(resultImg)
    }
}