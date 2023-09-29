package com.insecta.edu.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.insecta.edu.data.model.DetailLessonItem
import com.insecta.edu.data.model.LessonItem
import com.insecta.edu.databinding.ItemMateriBinding

class LessonAdapter(
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    private val itemList = mutableListOf<LessonItem>()

    inner class LessonViewHolder(private val binding: ItemMateriBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LessonItem) {
            binding.apply {
                tvTitle.text = item.lessonTitle
                Glide.with(root)
                    .load(item.lessonImages)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivImage)
                root.setOnClickListener {
                    itemClickListener.onItemClicked(item.lessonId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder(
            ItemMateriBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        itemList[position].let { holder.bind(it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<LessonItem>) {
        itemList.clear()
        itemList.addAll(data)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(lessonId: String)
    }
}