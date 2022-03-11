package zfani.assaf.trax.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import zfani.assaf.trax.data.local.db.dao.ProductDao
import zfani.assaf.trax.data.local.entity.product.Product

@Database(
    entities = [Product::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class TraxDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}