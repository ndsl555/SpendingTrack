package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Budget
import com.example.inventory.data.BudgetDao
import kotlinx.coroutines.launch

class BudgetViewModel(private val budgetDao: BudgetDao) :ViewModel(){

    val allbudItems: LiveData<List<Budget>> = budgetDao.getLastBudget().asLiveData()


    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    fun insertbudItem(budget: Budget) {
        viewModelScope.launch {
            budgetDao.insertbud(budget)
        }
    }

    fun addNewItem(itemName: String) {
        val newItem = getNewItemEntry(itemName)
        insertbudItem(newItem)
    }


    private fun getNewItemEntry(itemName: String): Budget {
        return Budget(
            itemBarcode = itemName
        )
    }

}

class BudgetViewModelFactory(private val budgetDao: BudgetDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(budgetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}