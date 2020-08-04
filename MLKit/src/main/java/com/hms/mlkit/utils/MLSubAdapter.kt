package com.hms.mlkit.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hms.mlkit.R
import com.hms.mlkit.databinding.PCardItemBinding
import com.mlkit.sampletext.activity.*
import com.mlkit.sampletext.util.Constant
import java.lang.Exception

class MLSubAdapter(var context: Context, var mlList: ArrayList<MLObject>): RecyclerView.Adapter<MLSubAdapter.SubMLViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubMLViewHolder {
        return SubMLViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.p_card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mlList.size
    }

    override fun onBindViewHolder(holder: SubMLViewHolder, position: Int) {
        var mlData=mlList.get(position)
        holder.dataBinding.ml=mlData

        holder.itemView.setOnClickListener(View.OnClickListener {
            when(mlData.id)
            {
                0->{
                    try {
                        context.startActivity(Intent(context, TextRecognitionActivity::class.java))
                    } catch (ex:Exception){
                        Log.e("MLText",ex.message)
                    }
                }
                1->{
                    try {
                    context.startActivity(Intent(context,RemoteTextDetectionActivity::class.java).apply { putExtra(
                        Constant.MODEL_TYPE, Constant.CLOUD_DOCUMENT_TEXT_DETECTION) })
                    } catch (ex:Exception){
                        Log.e("MLText",ex.message)
                    }
                }
                2->{
                    context.startActivity(Intent(context,BankCardRecognitionActivity::class.java))
                }
                3->{
                    context.startActivity(Intent(context,GeneralCardRecognitionActivity::class.java))
                }
                4->{
                    context.startActivity(Intent(context,IDCardRecognitionActivity::class.java))
                }
                5->{
                    context.startActivity(Intent(context,TranslateActivity::class.java))
                }
                6->{
                    context.startActivity(Intent(context,AsrAudioActivity::class.java))
                }
                7->{
                    context.startActivity(Intent(context,TtsAudioActivity::class.java))
                }
                8->{
                    context.startActivity(Intent(context,AudioFileTranscriptionActivity::class.java))
                }
                9->{
                    context.startActivity(Intent(context,ImageClassificationActivity::class.java))
                }
                10->{
                    context.startActivity(Intent(context,ImageClassificationActivity::class.java))
                }
                11->{
                    context.startActivity(Intent(context,RemoteDetectionActivity::class.java).apply {
                        putExtra(Constant.MODEL_TYPE, Constant.CLOUD_LANDMARK_DETECTION)
                    })
                }
                12->{
                    context.startActivity(Intent(context,ImageSegmentationActivity::class.java))
                }
                13->{
                    context.startActivity(Intent(context,FaceDetectionActivity::class.java))
                }
                14->{
                    context.startActivity(Intent(context,HumanSkeletonActivity::class.java))
                }




            }
        })

    }


    class SubMLViewHolder(binding: PCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var dataBinding = binding
    }


}