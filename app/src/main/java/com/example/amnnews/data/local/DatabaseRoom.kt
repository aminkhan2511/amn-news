package com.example.amnnews.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.amnnews.data.remote.model.New

// Replace YourEntity with your actual Entity class and YourDao with your DAO interface.
@Database(entities = [New::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseRoom : RoomDatabase() {

    abstract fun getDaoInterfaceInstance(): DaoInterfaceRoom

    companion object {
        // Volatile keyword ensures that the value of INSTANCE is always up-to-date and the same to all execution threads.
        @Volatile
        private var INSTANCE: DatabaseRoom? = null

        fun getDatabase(context: Context): DatabaseRoom {
            // Return the existing INSTANCE or create a new one.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseRoom::class.java,
                    "news_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
