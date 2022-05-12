package com.example.memojjang.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.memojjang.activity.MemoActivity
import com.example.memojjang.adapter.MemoRcyAdapter
import com.example.memojjang.data.MemoData
import com.example.memojjang.databinding.FragmentMemoBinding
import com.example.memojjang.viewmodel.FolderViewModel


class MemoFragment : Fragment() {


    private lateinit var mFolderViewModel : FolderViewModel
    lateinit var mBinding: FragmentMemoBinding
    private var pos : Int = 0
    private var bool : Boolean = true




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMemoBinding.inflate(inflater, container, false)

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        setFragmentResultListener("memoData"){requestKey, bundle ->
           val memoData = bundle.get("data") as String
            mBinding.editTxt.setText(memoData)
        }

        setFragmentResultListener("position"){requestKey, bundle ->
             pos = bundle.getInt("pos")
             bool = bundle.get("bool") as Boolean
        }

        return mBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goto = Intent(requireContext(),MemoActivity::class.java)

        mBinding.btnOk.setOnClickListener {

            val text = mBinding.editTxt.text.toString()  //바깥에 위치하면 안됨
            val data = MemoData(folderMemo = text)

      //position 값을 받아오는게 중요
            if (!bool) {
               val update = MemoData(pos + 1,text)
                mFolderViewModel.updateMemo(update)
                Toast.makeText(context, "well good update", Toast.LENGTH_SHORT).show()
                startActivity(goto)
            }
            else {
                mFolderViewModel.insertMemo(data)
                Toast.makeText(context, "well good insert", Toast.LENGTH_SHORT).show()
                startActivity(goto)
                }

        }

        mBinding.btnFolder.setOnClickListener {
            startActivity(goto)
        }

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

    }



}
