package com.example.memojjang.activity


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memojjang.R
import com.example.memojjang.adapter.MemoRcyAdapter
import com.example.memojjang.data.MemoData

import com.example.memojjang.databinding.ActivityMemoBinding
import com.example.memojjang.fragment.MemoFragment
import com.example.memojjang.viewmodel.FolderViewModel
import kotlinx.coroutines.flow.observeOn

class MemoActivity: AppCompatActivity() {
   lateinit var mFolderViewModel : FolderViewModel
   lateinit var mBinding : ActivityMemoBinding

   private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_memo)

        recyclerView = mBinding.rcyView

        val adapter = MemoRcyAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        mFolderViewModel.readmemo.observe(this){ user ->
            adapter.setData(user)
        }

        mBinding.createMemo.setOnClickListener{
            mBinding.activityContainer.visibility = View.VISIBLE
            setFragment(MemoFragment())
        }

        mBinding.searchEditTxt.setOnClickListener{
            mBinding.searchEditTxt.addTextChangedListener( object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    TODO("Not yet implemented")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    TODO("Not yet implemented")
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

            }
        }

        fun filter(text : String) {
            val filteredList = ArrayList<MemoData>()

    }

      fun setFragment(fragment: Fragment) {
          val transaction = supportFragmentManager.beginTransaction()
          transaction.replace(R.id.activityContainer, fragment)
          transaction.addToBackStack(null)
          transaction.commit()
      }

    // adapter데이터를 .. 전달
    fun setData(memo: String) {
       val transaction = supportFragmentManager
        transaction.setFragmentResult("memoData", bundleOf("data" to memo))
    }
    fun boolean(pos: Int, bool : Boolean) {
        val transaction = supportFragmentManager
        transaction.setFragmentResult("position", bundleOf("pos" to pos))

        transaction.setFragmentResult("position", bundleOf("bool" to bool))
    }



}