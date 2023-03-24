/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.inventory

import android.app.DatePickerDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.data.Item
import com.example.inventory.databinding.FragmentAddItemBinding
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import java.util.*


/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {
    var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database
                .itemDao()
        )
    }

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    lateinit var item: Item
    lateinit var yy:String
    lateinit var mm: String
    lateinit var dd:String

    val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calendar.set(year, month, day)
        yy=year.toString()
        mm=(month+1).toString()
        dd=day.toString()
        binding.itemYmd.setText(yy+'/'+mm+'/'+dd)
        //format("yyyy / MM / dd", date_edit)
    }
    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemColorcode.text.toString(),
            binding.itemYmd.text.toString().split("/")[0],
            binding.itemYmd.text.toString().split("/")[1],
            binding.itemYmd.text.toString().split("/")[2]
            )
    }

    /**
     * Binds views with the passed in [item] information.
     */
    private fun bind(item: Item) {
        val price = "%.2f".format(item.itemPrice)
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText(price, TextView.BufferType.SPANNABLE)
            itemColorcode.setText(item.itemColorcode, TextView.BufferType.SPANNABLE)
            itemYmd.setText(item.itemYear+'/'+item.itemMonth+'/'+item.itemDay)
            saveAction.setOnClickListener { updateItem() }
            chooseAction.setOnClickListener {
                ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener { selectedColor ->
                    println(
                        "onColorSelected: 0x" + Integer.toHexString(
                            selectedColor
                        )
                    )
                }
                .setPositiveButton("ok"
                ) { dialog, selectedColor, allColors ->
                    binding.itemColorcode.setText(
                        "#"+Integer.toHexString(
                            selectedColor).substring(2)
                    )
                    //changeBackgroundColor(selectedColor)
                }
                .setNegativeButton(
                    "cancel",
                    DialogInterface.OnClickListener { dialog, which -> })
                .build()
                .show() }
            chooseDate.setOnClickListener {
                DatePickerDialog(requireContext(),
                    dateListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemColorcode.text.toString(),
                binding.itemYmd.text.toString().split("/")[0],
                binding.itemYmd.text.toString().split("/")[1],
                binding.itemYmd.text.toString().split("/")[2]
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.itemName.text.toString(),
                this.binding.itemPrice.text.toString(),
                this.binding.itemColorcode.text.toString(),
                binding.itemYmd.text.toString().split("/")[0],
                binding.itemYmd.text.toString().split("/")[1],
                binding.itemYmd.text.toString().split("/")[2]
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * Called when the view is created.
     * The itemId Navigation argument determines the edit item  or add new item.
     * If the itemId is positive, this method retrieves the information from the database and
     * allows the user to update it.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                if (binding.itemName.text.toString().isBlank() || binding.itemPrice.text.toString().isBlank()
                    || binding.itemColorcode.text.toString().isBlank()||binding.itemYmd.text.toString().isBlank()) {
                    Toast.makeText(this.requireContext(),"尚有空白欄位", Toast.LENGTH_SHORT).show()
                }
                else{
                    addNewItem()
                }
            }
            binding.chooseAction.setOnClickListener {
                ColorPickerDialogBuilder
                    .with(context)
                    .setTitle("Choose color")
                    .initialColor(Color.BLUE)
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(12)
                    .setOnColorSelectedListener { selectedColor ->
                        println(
                            "onColorSelected: 0x" + Integer.toHexString(
                                selectedColor
                            )
                        )
                    }
                    .setPositiveButton("ok"
                    ) { dialog, selectedColor, allColors ->
                        binding.itemColorcode.setText(
                            "#"+Integer.toHexString(
                                selectedColor).substring(2)
                        )
                        //changeBackgroundColor(selectedColor)
                    }
                    .setNegativeButton(
                        "cancel",
                        DialogInterface.OnClickListener { dialog, which -> })
                    .build()
                    .show()
            }
            binding.chooseDate.setOnClickListener {
                DatePickerDialog(requireContext(),
                    dateListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
