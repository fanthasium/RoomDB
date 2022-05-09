package com.example.memojjang.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// DB 내의 Table, 즉 DB에 저장할 데이터 형식으로 class의 변수들이 컬럼(column)이 되어 table이 된다.
@Entity(tableName = "folder")
data class FolderData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "folderName")
    val folderName: String?,

    ) {

}
