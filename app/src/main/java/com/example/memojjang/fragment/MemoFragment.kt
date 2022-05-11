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
import com.example.memojjang.activity.MemoActivity
import com.example.memojjang.data.MemoData
import com.example.memojjang.databinding.FragmentMemoBinding
import com.example.memojjang.viewmodel.FolderViewModel


class MemoFragment : Fragment() {


    private lateinit var mFolderViewModel : FolderViewModel
    lateinit var mBinding: FragmentMemoBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMemoBinding.inflate(inflater, container, false)

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        setFragmentResultListener("key"){requestKey, bundle ->
            val data:Int = bundle.getInt("data")
          val c : List<MemoData>? = mFolderViewModel.readFolderMemo.value
            Log.e("data","$c")
            mBinding.editTxt.setText(c?.get(data).toString())
            Log.e("asd","$data")
        }


        return mBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mBinding.btnOk.setOnClickListener {
            val text = mBinding.editTxt.text.toString()
            val data = MemoData(folderMemo = text)
            mFolderViewModel.insertMemo(data)
            Toast.makeText(context, "well good save", Toast.LENGTH_SHORT).show()

        }

        mBinding.btnFolder.setOnClickListener {
          val goto = Intent(requireContext(),MemoActivity::class.java)
            startActivity(goto)
        }

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        mFolderViewModel.readFolderMemo.observe(viewLifecycleOwner) {

        }
    }



}
