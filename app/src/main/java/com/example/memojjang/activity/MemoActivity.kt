package com.example.memojjang.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.memojjang.R
import com.example.memojjang.adapter.MemoRcyAdapter
import com.example.memojjang.databinding.ActivityMemoBinding
import com.example.memojjang.databinding.ItemMemoBinding
import com.example.memojjang.fragment.MemoFragment
import com.example.memojjang.viewmodel.FolderViewModel

class MemoActivity: AppCompatActivity() {
   lateinit var mFolderViewModel : FolderViewModel
   lateinit var mBinding : ActivityMemoBinding

   lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_memo)

      val adapter = MemoRcyAdapter()

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        mFolderViewModel.readFolderMemo.observe(this){ user ->
            adapter.setData(user)
        }

        mBinding.createMemo.setOnClickListener {
            mBinding.container.visibility = View.VISIBLE
            setFragment(MemoFragment())
        }
    }

    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}