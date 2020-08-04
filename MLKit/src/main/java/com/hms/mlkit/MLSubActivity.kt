package com.hms.mlkit

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.hms.availabletoalllbraries.BaseActivity
import com.hms.availabletoalllbraries.utils.ItemOffsetDecoration
import com.hms.mlkit.utils.MLMainAdapter
import com.hms.mlkit.utils.MLObject
import com.hms.mlkit.utils.MLSubAdapter
import kotlinx.android.synthetic.main.ml_main_layout.*

class MLSubActivity: BaseActivity(true) {

    var currPosition:Int=0
    var mlDataList:ArrayList<MLObject> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.ml_main_layout)

        rv_ml_list.layoutManager = GridLayoutManager(this, 2)
        rv_ml_list.addItemDecoration(ItemOffsetDecoration(this,getResources().getDimensionPixelSize(R.dimen.offsetItem)))

        if(intent!=null)
        {
            if(intent.extras!!.containsKey("position"))
            {
                currPosition=intent!!.extras!!.getInt("position")
            }
        }

        when(currPosition){
            0->mlDataList=getDummyText()
            1->mlDataList=getDummySpeachText()
            2->mlDataList=getDummyVisual()
            3->mlDataList=getDummyFaceBody()
        }
        rv_ml_list.adapter= MLSubAdapter(this,mlDataList)
    }


    fun getDummyText():ArrayList<MLObject>
    {

        val myMlData=ArrayList<MLObject>()
        myMlData.apply {
            add(MLObject(0,"Text Recognition"))
         //   add(MLObject(1,"Document Recognition"))
            add(MLObject(2,"Bank Recognition"))
            add(MLObject(3,"General Card"))
            add(MLObject(4,"Identity Card"))
        }

        return myMlData

    }

    fun getDummySpeachText():ArrayList<MLObject>
    {
        val myMlData=ArrayList<MLObject>()
        myMlData.apply {
            add(MLObject(5,"Translation"))
            add(MLObject(6,"ASR"))
            add(MLObject(7,"Text To Speech"))
            add(MLObject(8,"AFT"))
        }
        return myMlData
    }

    fun getDummyVisual():ArrayList<MLObject>
    {
        val myMlData=ArrayList<MLObject>()
        myMlData.apply {
            add(MLObject(9,"Image Classification"))
            add(MLObject(10,"ODT"))
            add(MLObject(11,"Landmark"))
            add(MLObject(12,"Image Segmentation"))
        }
        return myMlData
    }

    fun getDummyFaceBody():ArrayList<MLObject>
    {
        val myMlData=ArrayList<MLObject>()
        myMlData.apply {
            add(MLObject(13,"Face Recognition"))
            add(MLObject(14,"Skeleton Recognition"))
        }
        return myMlData
    }

}