package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Rule
import com.example.inventory.data.RuleDao
import kotlinx.coroutines.launch

class RuleViewModel(private val ruleDao: RuleDao) :ViewModel(){
    val allruleItems: LiveData<List<Rule>> = ruleDao.getLastRule().asLiveData()


    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    fun insertruleItem(rule: Rule ){
        viewModelScope.launch {
            ruleDao.insertrule(rule)
        }
    }

    fun addNewItem(itemName: Int) {
        val newItem = getNewItemEntry(itemName)
        insertruleItem(newItem)
    }


    private fun getNewItemEntry(itemName: Int): Rule {
        return Rule(
            itemRule = itemName
        )
    }

}
class RuleViewModelFactory(private val ruleDao: RuleDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RuleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RuleViewModel(ruleDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}