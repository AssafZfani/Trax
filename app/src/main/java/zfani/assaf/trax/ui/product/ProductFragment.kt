package zfani.assaf.trax.ui.product

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import zfani.assaf.trax.R
import zfani.assaf.trax.data.events.product.ProductEvent
import zfani.assaf.trax.databinding.FragmentProductBinding
import zfani.assaf.trax.utils.toast
import zfani.assaf.trax.utils.viewBinding

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private val binding by viewBinding(FragmentProductBinding::bind)
    private val viewModel: ProductViewModel by viewModels()
    private val args by navArgs<ProductFragmentArgs>()

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                viewModel.saveImageByteArray(it)
            }
        }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                viewModel.saveImageByteArray(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        subscribeUI()
    }

    private fun initUI() = with(binding) {
        val isEditMode = args.product != null
        editIb.isVisible = isEditMode
        editIb.setOnClickListener {
            viewModel.enableEditModeState()
        }
        if (isEditMode) {
            args.product?.let {
                productNameEt.setText(it.name)
                productCategorySpinner.setSelection(
                    resources.getStringArray(R.array.product_categories).indexOf(it.category)
                )
                with(it.image?.toByteArray()) {
                    viewModel.imageByteArray = this
                    Glide.with(root.context).load(this).into(productImageIv)
                }
            }
        } else {
            viewModel.enableEditModeState()
        }
        productImageIv.isVisible = isEditMode
        productImageBtn.apply {
            setText(if (isEditMode) R.string.update_image_text else R.string.add_image_text)
            setOnClickListener {
                showPicturePickerDialog()
            }
        }
        saveOrEditBtn.apply {
            setText(if (args.product == null) R.string.save_product_text else R.string.edit_product_text)
            setOnClickListener {
                val productName = productNameEt.text.toString()
                val productCategory = productCategorySpinner.selectedItem.toString()
                viewModel.addOrEditProduct(args.product, productName, productCategory)
            }
        }
        deleteBtn.isVisible = isEditMode
        deleteBtn.setOnClickListener {
            viewModel.deleteProduct(args.product)
        }
    }

    private fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            for (event in viewModel.productChannel) {
                when (event) {
                    ProductEvent.EmptyProductName -> toast(R.string.empty_product_name)
                    ProductEvent.EmptyProductCategory -> toast(R.string.empty_product_category)
                    ProductEvent.EmptyProductImage -> toast(R.string.empty_product_image)
                    ProductEvent.ProductInserted -> {
                        toast(R.string.product_inserted)
                        activity?.onBackPressed()
                    }
                    ProductEvent.ProductUpdated -> {
                        toast(R.string.product_updated)
                        activity?.onBackPressed()
                    }
                    ProductEvent.ProductDeleted -> {
                        toast(R.string.product_deleted)
                        activity?.onBackPressed()
                    }
                }
            }
        }
        viewModel.editModeLiveData.observe(viewLifecycleOwner) { isOnEditMode ->
            binding.apply {
                listOf<View>(
                    productNameEt,
                    productCategorySpinner,
                    productImageBtn,
                    saveOrEditBtn,
                    deleteBtn
                ).forEach {
                    it.isEnabled = isOnEditMode
                }
            }
        }
    }

    private fun showPicturePickerDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.picture_picker_title)
            .setMessage(R.string.picture_picker_message)
            .setPositiveButton(R.string.picture_picker_camera_btn) { _, _ ->
                takePicture.launch(null)
            }.setNegativeButton(R.string.picture_picker_library_btn) { _, _ ->
                pickImage.launch("image/*")
            }.setNeutralButton(R.string.picture_picker_cancel_btn) { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }
}