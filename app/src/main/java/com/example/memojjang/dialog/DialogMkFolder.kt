package com.example.memojjang.dialog


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

import com.example.memojjang.data.FolderData
import com.example.memojjang.databinding.DialogFolderBinding
import com.example.memojjang.viewmodel.FolderViewModel


class DialogMkFolder(context: Context) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }


    private lateinit var mFolderViewModel : FolderViewModel
    private lateinit var mBinding: DialogFolderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogFolderBinding.inflate(inflater, container, false)

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //다이얼로그 확인버튼
        mBinding.okBtn.setOnClickListener {
            val text = mBinding.nameEditTxt.text.toString()

            if (TextUtils.isEmpty(text)) {
                Toast.makeText(context, "폴더명을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            } else {

                 val data = FolderData(folderName = text)
                 mFolderViewModel.insertFolder(data)
                dismiss()
            }
        }
        //다이얼로그 취소버튼
        mBinding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}

