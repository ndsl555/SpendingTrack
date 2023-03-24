package com.example.inventory

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import com.example.inventory.data.ItemRoomDatabase
import com.example.inventory.databinding.FragmentBarcodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code39Writer


/**
 * A simple [Fragment] subclass.
 * Use the [BarcodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarcodeFragment : Fragment() {

    private var _binding: FragmentBarcodeBinding?=null
    private val binding get() = _binding!!

    private val viewModel: BudgetViewModel by activityViewModels {
        BudgetViewModelFactory(
            (activity?.application as InventoryApplication).database.BudgetDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    // Check whether this app has android write settings permission.
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasWriteSettingsPermission(context: Context): Boolean {
        var ret = true
        // Get the result from below code.
        ret = Settings.System.canWrite(context)
        return ret
    }

    // Start can modify system settings panel to let user change the write
    // settings permission.
    private fun changeWriteSettingsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        context.startActivity(intent)
    }

    // This function only take effect in real physical android device,
    // it can not take effect in android emulator.
    private fun changeScreenBrightness(context: Context, screenBrightnessValue: Int) {
        // Change the screen brightness change mode to manual.
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
        // Apply the screen brightness value to the system, this will change
        // the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*ItemRoomDatabase.getDatabase(requireContext()).itemDao().getItems().asLiveData().observeForever {
            println(it.last().itemPrice)
        }*/
        // Inflate the layout for this fragment
        _binding =FragmentBarcodeBinding.inflate(inflater, container, false)
        //displayBitmap("ABC123")
        binding.setting.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.codedialog,null)
            val myDialog = Dialog(requireView().context)
            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            val okbtn = dialogBinding.findViewById<Button>(R.id.button)
            val et = dialogBinding.findViewById<EditText>(R.id.editTextbudget)
            okbtn.setOnClickListener {
                val num = et.text.toString()
                if (num.isNullOrBlank()){
                    Toast.makeText(this.requireContext(),"尚未輸入載具碼", Toast.LENGTH_SHORT).show()
                }
                else{
                    viewModel.addNewItem(num)
                    println(num)
                    myDialog.dismiss()
                }
                //sharedViewModel.setbudget(num)

            }
        }
        binding.light.setOnCheckedChangeListener { _, isChecked ->
            var brightnessValue=255
            val context = this.requireContext()
            // Check whether has the write settings permission or not.
            val settingsCanWrite = hasWriteSettingsPermission(context)
            if (isChecked) {
                if (!settingsCanWrite) {
                    changeWriteSettingsPermission(context)
                } else {
                    changeScreenBrightness(context, brightnessValue)
                }
            } else {
                changeScreenBrightness(context, brightnessValue-180)
            }
        }

        viewModel.allbudItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                if(!it.isNullOrEmpty()){
                displayBitmap('/'+it.last().itemBarcode)
                    binding.codeNum.setText('/'+it.last().itemBarcode)
                }
                else{
                    binding.codeNum.setText("/ABC123")
                    displayBitmap("ABC123")
                }
            }
        }
        return binding.root
    }


    private fun displayBitmap(value: String) {
        val widthPixels = resources.getDimensionPixelSize(R.dimen.width_barcode)
        val heightPixels = resources.getDimensionPixelSize(R.dimen.height_barcode)
        binding.imageBarcode.setImageBitmap(
            createBarcodeBitmap(
                barcodeValue = value,
                barcodeColor = Color.BLACK,
                backgroundColor = Color.GRAY,
                widthPixels = widthPixels,
                heightPixels = heightPixels
            )
        )
    }

    private fun createBarcodeBitmap(
        barcodeValue: String,
        @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int
    ): Bitmap {
        val bitMatrix = Code39Writer().encode(
            barcodeValue,
            BarcodeFormat.CODE_39,
            widthPixels,
            heightPixels
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap
    }


}