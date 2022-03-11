package zfani.assaf.trax.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun imageToJson(value: List<Byte>?): String =
        if (value == null) "" else Gson().toJson(value)

    @TypeConverter
    fun jsonToImage(value: String): ArrayList<Byte>? =
        object : TypeToken<ArrayList<Byte>>() {}.type.let { Gson().fromJson(value, it) }
}
