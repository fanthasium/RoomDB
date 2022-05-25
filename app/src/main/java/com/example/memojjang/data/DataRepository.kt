package com.example.memojjang.data

import androidx.lifecycle.LiveData
import  kotlinx.coroutines.flow.Flow

// 앱에서 사용하는 데이터와 그 데이터 통신을 하는 역할
// 역할은 데이터들을 접근하는 코드들을 모아 둘 때 유용하다.
// 데이터 접근하는 코드? -> INSERT,QUERY,DELETE,UPDATE
class DataRepository (private val folderDao : FolderDao){

    val readAllData : LiveData<List<FolderData>> = folderDao.queryFolder()
    val readMemo : LiveData<List<MemoData>> = folderDao.memoLiveQuery()


    //리사이클러뷰 해당 아이템 추가
    suspend fun insertFolder(folderData : FolderData){
        folderDao.insertFolder(folderData)
    }

    suspend fun insertMemo(memoData: MemoData){
        folderDao.insertMemo(memoData)
    }

    //리사이클러뷰 해당 아이템 삭제
   suspend fun deleteFolder(folderData: FolderData){
        folderDao.deleteFolder(folderData)
    }
    suspend fun deleteFolder(memoData: MemoData){
        folderDao.deleteMemo(memoData)
    }

    // 아이템 업데이트
    suspend fun updateMemo(memoData: MemoData){
        folderDao.updateMemo((memoData))
    }



}