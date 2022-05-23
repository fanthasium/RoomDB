package com.example.memojjang.data


import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {  // 데이터베이스에 접근하여 수행할 작업을 메소드 형태로 정의 (SQL 쿼리 지정 가능)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(vararg folder: FolderData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)   // {} 추가되면 쿼리 작동을 안하네?..
    suspend fun insertMemo(vararg memo : MemoData)



    @Delete
    suspend fun deleteFolder(vararg folder: FolderData)

    @Delete
    suspend fun deleteMemo(vararg memo : MemoData)



    @Update        //대신에 onConflict = OnConflictStrategy.REPLACE insert에서 쓰면 데이터 덮어짐
    fun updateName(folder: FolderData)

    @Update
    suspend fun updateMemo(memo : MemoData)



    // 조회 쿼리
    @Query("SELECT * FROM folder ORDER BY id ASC")
    fun queryFolder(): LiveData<List<FolderData>>

    @Query("SELECT * FROM memo ORDER BY id ASC")   //stack 구조는 DASC
    fun memoLiveQuery(): LiveData<List<MemoData>>
/*
    @Query("SELECT * FROM memo WHERE folderMemo LIKE : searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<MemoData>>*/
}