package com.cs.testapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs.testapp.data.DataModel
import com.cs.testapp.ui.details.DetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
open class DetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var dataModel: DataModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        detailsViewModel = DetailsViewModel(Application())
    }

    @Test
    fun `fetchEvery10ThChar SHOULD return a list of every ten characters`() =
        runTest {
            // Given
            dataModel = DataModel(pageData = MOCKED_WEB_DATA)
            val result = EVERY_TEN_CHAR

            // When
            detailsViewModel.fetchEvery10ThChar(dataModel)

            // Then
            Assert.assertEquals(dataModel.result10thCharArray, result)
        }

    @Test
    fun `fetchWordCount SHOULD return a list of every ten characters`() =
        runTest {
            // Given
            dataModel = DataModel(pageData = MOCKED_WEB_DATA)
            val result = RESULT_WORD_COUNT

            // When
            detailsViewModel.fetchWordCount(dataModel)

            // Then
            Assert.assertEquals(dataModel.resultWordCount, result)
        }

    companion object {
        const val MOCKED_WEB_DATA =
            "<p>This is <sup>superscripted</sup> text.</p> <p>This is <sup>superscripted</sup> text.</p> <p>This is <sup>superscripted</sup> text.</p>"
        const val EVERY_TEN_CHAR = "s, e, <, t, T, u, r, p, p,  , u, e, e, "
        const val RESULT_WORD_COUNT = "This = 3\n" +
                "is = 3\n" +
                "p = 6\n" +
                "sup = 6\n" +
                "superscripted = 3\n" +
                "text = 3\n"
    }
}
