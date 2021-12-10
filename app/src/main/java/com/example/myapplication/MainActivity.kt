package com.example.myapplication

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback
import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback
import com.ayush.imagesteganographylibrary.Text.ImageSteganography
import com.example.myapplication.adapters.ModesPagerAdapter
import com.example.myapplication.databinding.ActivityCoderBinding
import com.example.myapplication.ui.main.ResultTextDialog
import com.example.myapplication.ui.main.ChooserDialog
import com.example.myapplication.ui.main.PhotoDialog
import com.example.myapplication.ui.main.ResultDialog
import com.example.myapplication.ui.main.TextDialog
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), DialogInterface.OnClickListener, TextEncodingCallback, TextDecodingCallback{
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
        const val SECRET_KEY="The General Physics major contains all the theory of the other majors and provides students with the foundations of Physics. Students with this major will be expected to learn a little about each major in the Department, as well as, some other more theoretical subjects, like Astronomy and Cosmology"
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
                        val oper_type: String=if( binding.viewPager.currentItem==0)"integrate" else "output"
                        if(oper_type == "integrate"){
                            dialogMain = if (inter_type == ChooserDialog.PHOTO_INT) PhotoDialog() else TextDialog(inter_type)
                            operation_state = DATA_POSITION
                            dialogMain.show(supportFragmentManager, "DataDialog")
                        }else{
                            operation_state= DATA_POSITION
                            temp_photo= Bitmap.createBitmap(10,10,Bitmap.Config.ARGB_8888)
                            temp_string=""
                            onClick(dialog, which)
                        }
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
                        when(dataObject.operationType){
                            "integrate" -> dialogMain = ResultDialog()
                            "output"    -> dialogMain=if(dataObject.dataType==ChooserDialog.PHOTO_INT)ResultDialog() else ResultTextDialog()
                        }
                        dialogMain.show(supportFragmentManager, "resultDialog")
                        val ResultJob=launch {
                            getResult(dataObject,this@MainActivity)
                        }
                    //
                    }
                    RESULT_POSITION -> {
                        operation_state = START_POSITION
                    }
                }
            }
            DialogInterface.BUTTON_NEUTRAL->{
                operation_state = START_POSITION
            }
            DialogInterface.BUTTON_NEGATIVE->{
                val intent = Intent(Intent.ACTION_SEND).setType("image/*")
                val bytes = ByteArrayOutputStream()
                resultImg?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(contentResolver, resultImg, "tempimage", null)
                val uri = Uri.parse(path)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(intent)
            }

        }
    }


    private suspend fun getResult(dataObject: ObjectDataContainer, activity: MainActivity) {
        Integrator.analyze(dataObject, activity)
        //(binding.viewPager.adapter as ModesPagerAdapter).encodeFragment.binding.interImage.setImageBitmap(resultImg)
    }

    override fun onStartTextEncoding() {
        //
    }

    override fun onCompleteTextEncoding(p0: ImageSteganography?) {
        // dialogMain.show(supportFragmentManager,"resultFragment")
        Log.d("COMPLETE_OPER", "decoding...")
        when (dataObject.operationType) {
            "integrate" -> {
                resultImg = p0?.encoded_image
                p0?.encoded_image?.let { (dialogMain as ResultDialog).setImage(it) }
            }
            "output" -> {
               if (!p0?.isDecoded()!!)
                    Toast.makeText(baseContext, "No message found", Toast.LENGTH_LONG).show()
                else {
                    //If result.isSecretKeyWrong() is true, it means that secret key provided 				is wrong. */
                   if (!p0?.isSecretKeyWrong()!!) {
                        //set the message to the UI component.
                        Toast.makeText(baseContext, "Decoded", Toast.LENGTH_LONG).show()
                        (dialogMain as ResultTextDialog).setText(p0!!.message)
                    } else {
                        Toast.makeText(baseContext, "Wrong secret key ", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
        }
    }
