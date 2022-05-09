package com.example.memojjang.data

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FolderData::class, MemoData::class], version = 1, exportSchema = false)
abstract class FolderDataBase : RoomDatabase() {

    abstract fun folderDao() : FolderDao

    companion object{
        /* @Volatile = 접근가능한 변수의 값을 cache를 통해 사용하지 않고
        thread가 직접 main memory에 접근 하게하여 동기화. */

      val MIGRATION_1_2 : Migration = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE folder")
                database.execSQL("DROP TABLE memo")

            }
        }

        val MIGRATION_2_3 : Migration = object : Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE folder")
                database.execSQL("DROP TABLE memo")

                database.execSQL("CREATE TABLE folderName(id INTEGER PRIMARY KEY NOT NULL, folderName TEXT)")
                database.execSQL("CREATE TABLE memoName (id INTEGER PRIMARY KEY NOT NULL, folderMemo TEXT)")
            }
        }



        @Volatile  //다른 thread에서 접근 가능하게 만드는 것
        private var INSTANCE : FolderDataBase? = null

        // 싱글톤으로 생성 (자주 생성 시 성능 손해). 이미 존재할 경우 생성하지 않고 바로 반환

        fun getDatabase(context: Context) : FolderDataBase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){ //synchronized는 새로운 데이터베이스를 instance시킴.
                   val instance = Room.databaseBuilder(
                        context.applicationContext,
                        FolderDataBase::class.java,
                        "folder_database"
                   )    /*.fallbackToDestructiveMigration()
                       .addMigrations(MIGRATION_2_3)
                        .allowMainThreadQueries()*/
                        .build()
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }





