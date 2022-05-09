package com.example.memojjang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.memojjang.data.MemoData

import com.example.memojjang.databinding.FragmentMemoBinding
import com.example.memojjang.viewmodel.FolderViewModel
import org.w3c.dom.Text

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

        mBinding.btnFolder.setOnClickListener {

        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnOk.setOnClickListener {
            val text = mBinding.editTxt.text.toString()
            val data = MemoData(folderMemo = text)
            mFolderViewModel.insertMemo(data)
            Toast.makeText(context, "good", Toast.LENGTH_SHORT).show()
        }

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        mFolderViewModel.readFolderMemo.observe(viewLifecycleOwner) {
        }
    }



}
