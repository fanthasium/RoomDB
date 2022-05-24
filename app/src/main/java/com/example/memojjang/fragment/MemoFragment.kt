package com.example.memojjang.fragment

import android.R
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.memojjang.activity.MemoActivity
import com.example.memojjang.data.MemoData
import com.example.memojjang.databinding.FragmentMemoBinding
import com.example.memojjang.viewmodel.FolderViewModel


class MemoFragment : Fragment() {


    private lateinit var mFolderViewModel: FolderViewModel
    lateinit var mBinding: FragmentMemoBinding
    private var pos: Int = 0
    private var bool: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMemoBinding.inflate(inflater, container, false)

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        setFragmentResultListener("memoData") { requestKey, bundle ->
            val memoData = bundle.get("data") as String
            mBinding.editTxt.setText(memoData)
        }

        setFragmentResultListener("position") { requestKey, bundle ->
            pos = bundle.getInt("pos")
            bool = bundle.get("bool") as Boolean
            Log.e("dc", "$pos")
        }

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goto = Intent(requireContext(), MemoActivity::class.java)


        mBinding.btnOk.setOnClickListener {
            val text = mBinding.editTxt.text.toString()  //바깥에 위치하면 안됨

            val data = MemoData(folderMemo = text)

            //position 값을 받아오는게 중요

            if (!bool) {
                val update = MemoData(pos + 1, text)
                mFolderViewModel.updateMemo(update)
                Toast.makeText(context, "well good update", Toast.LENGTH_SHORT).show()
                startActivity(goto)

            } else {
                mFolderViewModel.insertMemo(data)
                Toast.makeText(context, "well good insert", Toast.LENGTH_SHORT).show()
                startActivity(goto)
            }
        }
        mBinding.btnFolder.setOnClickListener {
            startActivity(goto)
        }

        val tx: EditText = mBinding.editTxt



        //스팬 넣어주기
        mBinding.boldTxt.setOnClickListener {
            val txt = mBinding.editTxt.text
            val selectedStr = tx.text.substring(tx.selectionStart, tx.selectionEnd)
            val selectPos = tx.text.indexOf(selectedStr)
            val span = SpannableString(txt)
            val end = selectedStr.length
            Log.e("startttx", txt.toString())
            Log.e("start","$selectedStr")
            Log.e("start","$selectPos")


            span.setSpan(
                StyleSpan(Typeface.BOLD),
                selectPos,selectPos + end ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                )

            mBinding.editTxt.setText(span, TextView.BufferType.SPANNABLE)
                // 이렇게 하니까 버튼만 누르면 오류가 나서 종류가 된다 if 문으로 만들 것
            // 이거 저장하면 다시 돌아온다
        }

        mBinding.underLineTxt.setOnClickListener {
            val txt = mBinding.editTxt.text
            val selectedStr = tx.text.substring(tx.selectionStart, tx.selectionEnd)
            val selectPos = tx.text.indexOf(selectedStr)
            val span2 = SpannableString(txt)
            val end = selectedStr.length

            span2.setSpan(UnderlineSpan(),
                selectPos,selectPos + end -1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            mBinding.editTxt.setText(span2, TextView.BufferType.SPANNABLE)

        }






        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

    }


}
