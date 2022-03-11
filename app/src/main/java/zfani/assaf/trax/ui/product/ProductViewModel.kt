package zfani.assaf.trax.ui.product

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import zfani.assaf.trax.data.events.product.ProductEvent
import zfani.assaf.trax.data.local.entity.product.Product
import zfani.assaf.trax.data.repository.product.ProductRepository
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val application: Application,
    private val productRepository: ProductRepository
) : ViewModel() {

    val productChannel = Channel<ProductEvent>()
    var imageByteArray: ByteArray? = null
    val editModeLiveData = MutableLiveData(false)

    fun saveImageByteArray(bitmap: Bitmap?) {
        bitmap?.let {
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            imageByteArray = stream.toByteArray()
            it.recycle()
        }
    }

    fun saveImageByteArray(uri: Uri?) {
        uri?.let {
            val descriptor = application.contentResolver?.openFileDescriptor(it, "r")
            val bitmap = BitmapFactory.decodeFileDescriptor(descriptor?.fileDescriptor, null, null)
            with(ByteArrayOutputStream()) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, this)
                imageByteArray = toByteArray()
            }
        }
    }

    fun addOrEditProduct(product: Product?, productName: String?, productCategory: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            productChannel.send(when {
                productName.isNullOrEmpty() -> ProductEvent.EmptyProductName
                productCategory.isNullOrEmpty() -> ProductEvent.EmptyProductCategory
                imageByteArray == null || imageByteArray!!.isEmpty() -> ProductEvent.EmptyProductImage
                product == null -> productRepository.insertProduct(
                    Product(
                        name = productName,
                        category = productCategory,
                        image = imageByteArray?.toList()
                    )
                )
                else -> {
                    product.apply {
                        name = productName
                        category = productCategory
                        image = imageByteArray?.toList()
                    }
                    productRepository.updateProduct(product)
                }
            })
        }

    fun deleteProduct(product: Product?) = viewModelScope.launch(Dispatchers.IO) {
        product?.let {
            productChannel.send(productRepository.deleteProduct(it))
        }
    }

    fun enableEditModeState() {
        editModeLiveData.postValue(true)
    }
}