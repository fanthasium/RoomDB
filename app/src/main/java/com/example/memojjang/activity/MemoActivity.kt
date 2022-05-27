package com.example.memojjang.activity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.memojjang.R
import com.example.memojjang.adapter.MemoRcyAdapter
import com.example.memojjang.data.MemoData
import com.example.memojjang.databinding.ActivityMemoBinding
import com.example.memojjang.fragment.MemoFragment
import com.example.memojjang.viewmodel.FolderViewModel


class MemoActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMemoBinding
    private lateinit var mFolderViewModel: FolderViewModel

    private var memoList = ArrayList<MemoData>()
    private var filterList = ArrayList<MemoData>()

    private lateinit var recyclerView: RecyclerView


    val adapter = MemoRcyAdapter(this, filterList = filterList, onDeleteClick = {
        mFolderViewModel.deleteMemo(it)
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_memo)


        recyclerView = mBinding.rcyView


        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,2)

        mFolderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        mFolderViewModel.readmemo.observe(this) { user ->
            adapter.setData(user)
        }

        mBinding.createMemo.setOnClickListener {
            mBinding.activityContainer.visibility = View.VISIBLE
            setFragment(MemoFragment())
        }

        setItemTouchHelper()

        mBinding.searchEditTxt.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList.clear()
                adapter.boole = false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(text: Editable?) {

                if (text.toString().isEmpty()) {
                    adapter.boole = true
                    adapter.notifyDataSetChanged()
                } else {
                    filter(text.toString())
                }
            }

        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filter(text: String) {
        memoList = mFolderViewModel.readmemo.value as ArrayList<MemoData>
        for (memo in memoList) {
            if (memo.folderMemo.toString().contains(text)) {
                memo.folderMemo?.let { filterList.add(memo) }
                adapter.filter(filterList)

            }
        }

        recyclerView.adapter.apply {
            applicationContext
            filterList
        }
        adapter.notifyDataSetChanged()
    }


    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activityContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // adapter데이터를 .. 전달
    fun setData(id: Int) {
        val transaction = supportFragmentManager
        transaction.setFragmentResult("memoData", bundleOf("id" to id))

    }
    fun setDataMemo(memo: String) {
        val transaction = supportFragmentManager
        transaction.setFragmentResult("memoDataMemo", bundleOf("data" to memo))
    }

    fun boolean(bool: Boolean) {
        val transaction = supportFragmentManager
        transaction.setFragmentResult("position", bundleOf("bool" to bool))
    }

    private fun setItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private val limitScrollX = dipToPx(65.0f, this@MemoActivity)
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    if (dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true
                    }
                    if (isCurrentlyActive) {
                        //swipe with finger

                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    } else {

                        //swipe with auto animation
                        if (firstInActive) {
                            firstInActive = false
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX
                            initXWhenInActive = dX
                        }
                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo(
                                (currentScrollXWhenInActive * dX / initXWhenInActive).toInt(),
                                0
                            )
                        }

                    }
                }

            }


            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                if (viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }


        }).apply {
            attachToRecyclerView(recyclerView)
        }
    }

    private fun dipToPx(dipValue: Float, context: Context): Int {
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }


}

