package com.hms.mlkit.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hms.mlkit.MLSubActivity
import com.hms.mlkit.R
import com.hms.mlkit.databinding.PCardItemBinding

class MLMainAdapter(var context: Context, var mlList: ArrayList<MLObject>): RecyclerView.Adapter<MLMainAdapter.MainMLViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMLViewHolder {
        return MainMLViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.p_card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mlList.size
    }

    override fun onBindViewHolder(holder: MainMLViewHolder, position: Int) {
        holder.dataBinding.ml=mlList.get(position)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val detailsIntent=Intent(context,MLSubActivity::class.java)
            detailsIntent.putExtra("position",position)
            context.startActivity(detailsIntent)
        })
    }


    class MainMLViewHolder(binding: PCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var dataBinding = binding
    }


}