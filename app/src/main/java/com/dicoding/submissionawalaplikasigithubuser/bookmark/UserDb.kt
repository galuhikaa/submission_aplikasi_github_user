package com.dicoding.submissionawalaplikasigithubuser.bookmark

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntityUser::class], version = 1, exportSchema = false)
abstract class UserDb : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var instance: UserDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDb::class.java,
            "user-database"
        ).build()
    }
}