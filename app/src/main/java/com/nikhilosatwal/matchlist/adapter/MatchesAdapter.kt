package com.nikhilosatwal.matchlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhilosatwal.matchlist.databinding.MatchItemBinding
import com.nikhilosatwal.matchlist.models.Matches

class MatchesAdapter(var mContext: Context, private val mList : ArrayList<Matches>) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {

    class ViewHolder(binding : MatchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val matchTextView = binding.textView
        val matchTime = binding.matchTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MatchItemBinding = MatchItemBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.matchTextView.text = mList[position].eventName
        holder.matchTime.text = mList[position].openDate
    }
}