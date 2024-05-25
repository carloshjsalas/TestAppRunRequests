package com.cs.testapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val EMPTY_STRING = ""

@Parcelize
data class DataModel(
    var pageData: String = EMPTY_STRING,
    var result10thCharArray: String = EMPTY_STRING,
    var resultWordCount: String = EMPTY_STRING
) : Parcelable
