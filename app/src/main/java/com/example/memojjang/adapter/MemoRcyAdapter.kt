package com.example.memojjang.adapter

import android.annotation.SuppressLint
import android.content.Context

import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memojjang.R
import com.example.memojjang.activity.MemoActivity

import com.example.memojjang.data.MemoData
import com.example.memojjang.databinding.ItemMemoBinding
import com.example.memojjang.fragment.MemoFragment

class MemoRcyAdapter(val context : Context) : RecyclerView.Adapter<MemoRcyAdapter.ViewHolder>(){

    private var memoList = ArrayList<MemoData>()


    class ViewHolder(var mBinding: ItemMemoBinding) : RecyclerView.ViewHolder(mBinding.root)  {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding: ItemMemoBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_memo, parent, false)

        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

      val itemList = memoList[position]
        holder.mBinding.memoData = itemList

        holder.mBinding.cardViewMemo.setOnClickListener{
            val goto = context as MemoActivity
            goto.setFragment(MemoFragment())
            goto.setData(position)


        }

    }

    override fun getItemCount(): Int {
      return memoList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(memoData : List<MemoData>) {
        this.memoList = memoData as ArrayList<MemoData>
        notifyDataSetChanged()
    }
}