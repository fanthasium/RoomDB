package com.example.memojjang.adapter


import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.memojjang.R
import com.example.memojjang.activity.MainActivity
import com.example.memojjang.activity.MemoActivity
import com.example.memojjang.data.FolderData
import com.example.memojjang.data.MemoData
import com.example.memojjang.databinding.ItemMainBinding
import java.lang.ref.WeakReference


class FolderRcyAdapter(val onDeleteClick: (todo: FolderData) -> Unit) :
    RecyclerView.Adapter<FolderRcyAdapter.ViewHolder>() {

    private var foldList = ArrayList<FolderData>()
    private var memoList = ArrayList<MemoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding: ItemMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_main, parent, false)


        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 추가
        val itemList = foldList[position]
        holder.mBinding.folderData = itemList


        // 삭제
        holder.mBinding.delete.setOnClickListener {
            onDeleteClick.invoke(itemList)
        }
        // onclick 수정

        holder.mBinding.cardView.setOnClickListener{
            
        }

        holder.mBinding.cardView.setOnClickListener {
            val intent  = Intent(holder.itemView.context,MemoActivity::class.java)
             /*   intent.putExtra("memoData",memoList)*/
            startActivity(holder.itemView.context,intent,null)
            //해당 memo로 이동
        }
    }

    //유저리스트가 변경 되었을때, 업데이트해준다
    @SuppressLint("NotifyDataSetChanged")
    fun setData(folderData: List<FolderData>) {
        this.foldList = folderData as ArrayList<FolderData>
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int {
        return foldList.size
    }

    class ViewHolder(val mBinding: ItemMainBinding) : RecyclerView.ViewHolder(mBinding.root) {


        private val view = WeakReference(itemView)

//        var index = 0

        init {
            view.get()?.let {
                it.setOnClickListener {
                    if (view.get()?.scrollX != 0) {
                        view.get()?.scrollTo(0, 0)
                    }
                }
            }

        }

    }


}