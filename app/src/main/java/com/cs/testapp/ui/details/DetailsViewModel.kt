package com.cs.testapp.ui.details

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cs.testapp.R
import com.cs.testapp.data.DataModel
import com.cs.testapp.data.EMPTY_STRING
import com.cs.testapp.utils.Constant.MAIN_CONTAINER
import com.cs.testapp.utils.Constant.URL
import com.cs.testapp.utils.Constant.regex
import com.cs.testapp.utils.NetworkHelper
import com.cs.testapp.utils.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.TreeMap
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    val outcome by lazy { MutableLiveData<Outcome<DetailsActions>>() }

    private val context: Context by lazy { getApplication() }
    private val networkHelper = NetworkHelper(context)

    fun fetchBlogData(dataModel: DataModel) {
        outcome.postValue(Outcome.Loading())
        val fetchDataJob = viewModelScope.launch(Dispatchers.IO) {
            if (dataModel.pageData.isEmpty()) {
                getDataFromWebSite(dataModel)
            } else {
                outcome.postValue(Outcome.Success())
            }
        }
        runBlocking {
            fetchDataJob.join()
            launch {
                fetchEvery10ThChar(dataModel)
            }
            launch {
                fetchWordCount(dataModel)
            }
        }
    }

    private fun getDataFromWebSite(dataModel: DataModel) {
        if (networkHelper.isNetworkAvailable()) {
            val builder: StringBuilder = StringBuilder()
            try {
                val url: String = URL
                val doc: Document = Jsoup.connect(url).get()
                val body =
                    doc.select(MAIN_CONTAINER).toString().replace("\n", "").substring(0, 1000)
                builder.append(body.split(" ").toTypedArray().filter { it.isNotEmpty() })
                dataModel.pageData = builder.toString()
                outcome.postValue(Outcome.Success())
            } catch (e: Exception) {
                builder.append(e.message)
                dataModel.pageData = builder.toString()
                outcome.postValue(Outcome.Error(e.cause))
            }
        } else {
            outcome.postValue(Outcome.Error(Throwable("No internet connection")))
        }
    }

    @VisibleForTesting(otherwise = PRIVATE)
    fun fetchEvery10ThChar(dataModel: DataModel) {
        val strText = dataModel.pageData
        var result = EMPTY_STRING
        for (i in strText.indices) {
            if (i != 0 && i % 10 == 0) {
                result = "$result${strText[i - 1]}, "
            }
        }
        dataModel.result10thCharArray = result
    }

    @VisibleForTesting(otherwise = PRIVATE)
    fun fetchWordCount(dataModel: DataModel) {
        val wordCount = countFrequency(regex.replace(dataModel.pageData, " "))
        var count = EMPTY_STRING
        wordCount.forEach {
            count = "$count${it.key} = ${it.value}\n"
        }
        dataModel.resultWordCount = count
    }

    private fun countFrequency(str: String): MutableMap<String, Int> {
        val map: MutableMap<String, Int> = TreeMap()

        // Splitting to find the word
        val arr = str.split(" ").toTypedArray()

        // Loop to iterate over the words
        for (i in arr.indices) {
            // Condition to check if the
            // array element is present
            // the hash-map
            if (arr[i] != "") {
                //condition to remove extra empty strings
                if (map.containsKey(arr[i])) {
                    map[arr[i]] = map[arr[i]]!! + 1
                } else {
                    map[arr[i]] = 1
                }
            }
        }
        return map
    }
}
