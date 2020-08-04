package com.hms.mlkit.utils

import android.content.Context
import androidx.lifecycle.ViewModel

class MLViewModel: ViewModel() {


    var mainAdapter: MLViewModel?=null

    fun init()
    {
        



    }

    fun setAdapter(context: Context):MLMainAdapter{
        return MLMainAdapter(context,getDummyData())
    }





    fun getDummyData():ArrayList<MLObject>
    {

        val myMlData=ArrayList<MLObject>()

        var mlData: MLObject= MLObject(1,"Text")
        myMlData.apply {
            add(MLObject(1,"Text"))
            add(MLObject(1,"Speach & Language"))
            add(MLObject(1,"Vision"))
            add(MLObject(1,"Face body"))
        }

        return myMlData

    }



}