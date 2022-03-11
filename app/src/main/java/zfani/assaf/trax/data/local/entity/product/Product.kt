package zfani.assaf.trax.data.local.entity.product

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String? = null,
    var category: String? = null,
    var image: List<Byte>? = null,
    var isChecked: Boolean = false
) : Parcelable