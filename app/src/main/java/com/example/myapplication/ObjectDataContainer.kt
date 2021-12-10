package com.example.myapplication

import android.graphics.Bitmap

data class ObjectDataContainer(val operationType: String, val dataType: String,
                               val interBitmap: Bitmap, val codeText: String?, val codeBitmap: Bitmap?) {
    constructor( operationType: String, dataType: String,
                 interBitmap: Bitmap,  codeText: String?) : this(operationType=operationType, dataType=dataType,
        interBitmap=interBitmap, codeText=codeText, codeBitmap=null)
    constructor( operationType: String, dataType: String,
                 interBitmap: Bitmap,  codeBitmap: Bitmap?) : this(operationType=operationType, dataType=dataType,
        interBitmap=interBitmap, codeText=null, codeBitmap=codeBitmap)
    constructor( operationType: String, dataType: String,
                 interBitmap: Bitmap) : this(operationType=operationType, dataType=dataType,
        interBitmap=interBitmap, codeText=null, codeBitmap=null)
}