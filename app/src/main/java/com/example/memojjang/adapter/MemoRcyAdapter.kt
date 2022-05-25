package com.example.memojjang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memojjang.R
import com.example.memojjang.activity.MemoActivity

import com.example.memojjang.data.MemoData
import com.example.memojjang.databinding.ItemMemoBinding
import com.example.memojjang.fragment.MemoFragment

 class MemoRcyAdapter(
    private val context: Context,
    var filterList: ArrayList<MemoData>,
    val onDeleteClick: (todo: MemoData) -> Unit,
    ) :
    RecyclerView.Adapter<MemoRcyAdapter.ViewHolder>() {

    private var memoList = ArrayList<MemoData>()
    var boole = true   //memoActivity에서 adapter연결지어주고 변수 사용해 클릭시 값 false로\



    class ViewHolder(var mBinding: ItemMemoBinding) : RecyclerView.ViewHolder(mBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding: ItemMemoBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_memo, parent, false)

        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val memoList = memoList[position]
        val goto = context as MemoActivity
        val bool = false


        if (!boole) {
            val filterList = filterList[position]
            holder.mBinding.memoData = filterList
            Log.e("FilterListAdapt", "$filterList")

        } else {
            holder.mBinding.memoData = memoList
            holder.mBinding.cardViewMemo.setOnClickListener {
                goto.setFragment(MemoFragment())
                memoList.folderMemo?.let { it -> goto.setData(it) } // memoe데이터 전송
                goto.boolean(position, bool)     // 리스트 클릭 인지 시켜주기위함
            }
        }
        // 삭제
        holder.mBinding.delete.setOnClickListener {
            onDeleteClick.invoke(memoList)
        }

    }

    override fun getItemCount(): Int {

        return if (!boole) filterList.size else memoList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(memoData: List<MemoData>) {
        this.memoList = memoData as ArrayList<MemoData>
        notifyDataSetChanged()
    }

    //유저리스트가 변경 되었을때, 업데이트해준다
    @SuppressLint("NotifyDataSetChanged")
    fun filter(filter: ArrayList<MemoData>) {
        this.filterList = filter
        notifyDataSetChanged()
    }


}