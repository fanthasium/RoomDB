package com.example.memojjang.activity


import android.content.Context
import android.graphics.Canvas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memojjang.R
import com.example.memojjang.adapter.FolderRcyAdapter
import com.example.memojjang.adapter.MemoRcyAdapter
import com.example.memojjang.data.FolderData

import com.example.memojjang.databinding.ActivityMainBinding
import com.example.memojjang.dialog.DialogMkFolder
import com.example.memojjang.fragment.MemoFragment
import com.example.memojjang.viewmodel.FolderViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView

    /*        val folderViewModel2 : FolderViewModel by viewModels()*/
    private lateinit var folderViewModel: FolderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding.createFolder.setOnClickListener {
            DialogMkFolder(this).show(supportFragmentManager, "folder")
        }

        recyclerView = mBinding.rcyView


        val adapter = FolderRcyAdapter(onDeleteClick = {
            folderViewModel.deleteFolder(it)
            Log.e("log","$it")

        })

        // 리사이클러 연동
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 뷰모델 연결 은 <초기화> 상황에 따라 방법이 많으니 공부해보자
        folderViewModel = ViewModelProvider(this)[FolderViewModel::class.java]

        //관찰하고 있다가 변하면 데이터 넣음
        folderViewModel.readFolderData.observe(this) { user ->
            adapter.setData(user)
        }
        // swipe&slide
        setItemTouchHelper()

        // Memo
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



    // 터치 헬퍼 메소드
    private fun setItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private val limitScrollX = dipToPx(65f, this@MainActivity)
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

