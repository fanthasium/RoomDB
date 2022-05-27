package com.example.memojjang.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.core.text.toHtml
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.memojjang.activity.MainActivity
import com.example.memojjang.activity.MemoActivity
import com.example.memojjang.data.MemoData
import com.example.memojjang.databinding.FragmentMemoBinding
import com.example.memojjang.viewmodel.FolderViewModel


class MemoFragment : Fragment() {


    private lateinit var mFolderViewModel: FolderViewModel
    lateinit var mBinding: FragmentMemoBinding
    private var checkClick: Boolean = true
    private var key : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMemoBinding.inflate(inflater, container, false)

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        setFragmentResultListener("memoData") { requestKey, bundle ->
            key = bundle.get("id") as Int
        }
        setFragmentResultListener("memoDataMemo") { requestKey, bundle ->
          val memoData = bundle.get("data") as String
            mBinding.editTxt.setText( HtmlCompat.fromHtml(memoData, HtmlCompat.FROM_HTML_MODE_LEGACY))
            Log.e("data", memoData)


        }
        setFragmentResultListener("position") { requestKey, bundle ->
            checkClick = bundle.get("bool") as Boolean

        }

        return mBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goto = Intent(requireContext(), MemoActivity::class.java)


        mBinding.btnOk.setOnClickListener {
            val text = mBinding.editTxt.text.toString()  //바깥에 위치하면 안됨
            Log.e("Text", text)
            val data = MemoData(key, folderMemo = text)
            Log.e("key2", "$key")
            if (!checkClick) {
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

            Log.e("span", "$span")

            span.setSpan(
                StyleSpan(Typeface.BOLD),
                selectPos, selectPos + end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )

            val html = Html.toHtml(span)   //html로 변환
            val c = MemoData(key, folderMemo = html)
            Log.e("gg", "$span")
            mFolderViewModel.insertMemo(c)
            mBinding.editTxt.setText(span, TextView.BufferType.SPANNABLE)
            // 이렇게 하니까 버튼만 누르면 오류가 나서 종류가 된다 if 문으로 만들 것
            // 이거 저장하면 다시 돌아온다
            //bullet span 글머리 기호도 있음
        }


        mBinding.underLineTxt.setOnClickListener {
            val txt = mBinding.editTxt.text
            val selectedStr = tx.text.substring(tx.selectionStart, tx.selectionEnd)
            val selectPos = tx.text.indexOf(selectedStr)
            val span2 = SpannableString(txt)
            val end = selectedStr.length

            span2.setSpan(
                UnderlineSpan(),
                selectPos, selectPos + end - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val html = Html.toHtml(span2)   //html로 변환
            val c = MemoData(key, folderMemo = html)
            mFolderViewModel.insertMemo(c)
            mBinding.editTxt.setText(span2, TextView.BufferType.SPANNABLE)

        }


    }

}
