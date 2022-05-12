package com.example.memojjang.viewmodel

import android.app.Application
import androidx.lifecycle.*

import com.example.memojjang.data.DataRepository
import com.example.memojjang.data.FolderData
import com.example.memojjang.data.FolderDataBase
import com.example.memojjang.data.MemoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FolderViewModel(application: Application) : AndroidViewModel(application) {

    val readFolderData: LiveData<List<FolderData>>  //DB
    val readFolderMemo: LiveData<List<MemoData>>    //DB
    private val repository: DataRepository

    init {
        val folderDao = FolderDataBase.getDatabase(application).folderDao()
        repository = DataRepository(folderDao)
        readFolderData = repository.readAllData
        readFolderMemo = repository.readAllMemo

    }

    fun insertFolder(folderData: FolderData) {            // 파라미터에 만든 데이터클래스가 들어감
        viewModelScope.launch(Dispatchers.IO) {           //코루틴 활성화 dispatcherIO는 백그라운드에서 실행
            repository.insertFolder(folderData)           //repository에 insertFolder 함수 불러오기
        }
    }

   fun deleteFolder(folderData: FolderData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFolder(folderData)
        }
    }

    fun insertMemo(memoData: MemoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertMemo(memoData)
        }
    }
    fun updateMemo(memoData: MemoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateMemo(memoData)
        }
    }

}