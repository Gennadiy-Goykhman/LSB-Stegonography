package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.*
import com.example.myapplication.ui.main.ChooserDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Integrator {
    companion object{
        suspend fun analyze(data: ObjectDataContainer): Bitmap {
            when(data.operationType){
                "integrate" -> return integrate(data)!!
                else -> return output(data)!!
            }
        }
        private fun output(data: ObjectDataContainer): Bitmap? {
         /*   when(data.dataType){
                ChooserDialog.PHOTO_INT -> return outPhoto(data.interBitmap)
                ChooserDialog.TEXT_INT -> return outText(data.interBitmap)
                ChooserDialog.BIN_INT -> return outBin(data.interBitmap)
                else -> throw Exception()
            } */
            return null
        }
        private suspend fun integrate(data: ObjectDataContainer): Bitmap? {
                when(data.dataType){
                    ChooserDialog.PHOTO_INT ->return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intPhoto(data.interBitmap,data.codeBitmap)
                    } else {
                        data.codeBitmap
                    }
                    ChooserDialog.TEXT_INT -> return data.codeBitmap//intText(data.interBitmap,data.codeText)
                    ChooserDialog.BIN_INT -> return data.codeBitmap//intBin(data.interBitmap,data.codeText)
                    else -> throw Exception()
                }
            }


        @RequiresApi(Build.VERSION_CODES.O)
        private suspend fun intPhoto(mainBitmap: Bitmap, codeBitmap: Bitmap?): Bitmap? = runBlocking{
            var newBitmap= Bitmap.createBitmap(mainBitmap,0,0,600,600)
            var tempBitmap: Bitmap=Bitmap.createBitmap(codeBitmap!!,0,0,600,600)
            val resultBitmap =Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888)
                    val bitsList: ArrayList<Color> = ArrayList()
                    val partsList: ArrayList<Byte> = ArrayList()

                    val job1  = launch {
                        for (x in 0..newBitmap.width-1) {
                            for (y in 0..newBitmap.width-1) {
                                bitsList.add(newBitmap.getPixel(x, y).toColor())
                            }
                        }
                    }
                    val job2  = launch {
                        for (x in 0..tempBitmap.width-1) {
                            for (y in 0..tempBitmap.width-1) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                   // Log.d("PIXEL","${tempBitmap.getPixel(x,y).toColor().red()}")
                                }
                                var code: String = ((tempBitmap.getPixel(x,y).toColor().alpha()*255).toInt()+256).toString(2)
                                for(i in 1..8){
                                        partsList.add(code.get(i).toByte())
                                }
                                code=((tempBitmap.getPixel(x,y).toColor().red()*255).toInt()+256).toString(2)
                                for(i in 1..8){
                                    partsList.add(code.get(i).toByte())
                                }
                                code=((tempBitmap.getPixel(x,y).toColor().green()*255).toInt()+256).toString(2)
                                for(i in 1..8){
                                    partsList.add(code.get(i).toByte())
                                }
                                code=((tempBitmap.getPixel(x,y).toColor().blue()*255).toInt()+256).toString(2)
                                for(i in 1..8){
                                    partsList.add(code.get(i).toByte())
                                }
                            }
                        }
                    }

                    job1.join()
                    job2.join()
                    val resultList: ArrayList<Color> = ArrayList()
                    val job3 = launch {
                        val iterator = partsList.iterator()
                        bitsList.stream().forEach {
                            if(iterator.hasNext()){
                                val a = iterator.next()
                                resultList.add(Color.valueOf(
                                    (it.red()*255).toInt()+iterator.next().toFloat(),
                                    it.green()+iterator.next().toFloat(),
                                    it.blue()+iterator.next().toFloat(),
                                    it.alpha()+a.toFloat()
                                ))
                            }else{
                                resultList.add(it)
                            }

                        }
                    }
                    job3.join()
                    val job4 = launch {
                        for( i in 0..599){
                            for(j in 0..599){
                                resultBitmap.setPixel(i,j,resultList.get(j+600*i).toArgb())
                            }
                        }
                    }
                    job4.join()
                    return@runBlocking resultBitmap
                }
            }

        }
