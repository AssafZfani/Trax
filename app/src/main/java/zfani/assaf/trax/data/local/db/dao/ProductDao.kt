package zfani.assaf.trax.data.local.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import zfani.assaf.trax.data.local.entity.product.Product

@Dao
interface ProductDao {

    @Query("Select * From product_table")
    fun getAllProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("Delete From product_table Where isChecked=1")
    suspend fun deleteCheckedProducts()
}