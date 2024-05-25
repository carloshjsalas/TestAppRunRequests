package com.cs.testapp.ui.details

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.cs.testapp.R
import com.cs.testapp.data.DataModel
import com.cs.testapp.databinding.FragmentDetailsBinding
import com.cs.testapp.protocol.CommunicationCallback
import com.cs.testapp.ui.MainActivity
import com.cs.testapp.utils.Outcome
import com.cs.testapp.utils.gone
import com.cs.testapp.utils.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var communication: CommunicationCallback

    private val viewModel by viewModels<DetailsViewModel>()

    private lateinit var dataModel: DataModel

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        communication = (requireActivity() as MainActivity)
        val safeArgs: DetailsFragmentArgs by navArgs()
        dataModel = safeArgs.dataModel
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            (it as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let { layout ->
                    val behavior = BottomSheetBehavior.from(layout)
                    behavior.skipCollapsed = true
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    val layoutParams = layout.layoutParams
                    layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
                    layout.layoutParams = layoutParams
                }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMainViewModelObserver()
        getWebContent()
    }

    private fun setupMainViewModelObserver() {
        binding.apply {
            viewModel.outcome.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Outcome.Loading -> {
                        progressBar.visible()
                    }

                    is Outcome.Success -> {
                        progressBar.gone()
                    }

                    is Outcome.Error -> {
                        progressBar.gone()
                        Toast.makeText(context, result.error?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getWebContent() {
        lifecycleScope.launch {
            viewModel.fetchBlogData(dataModel)
            binding.resultEvery10thChar.text =
                "${context?.getString(R.string.str_every_10th_Char, dataModel.result10thCharArray)}"
            binding.resultWordCount.text =
                "${context?.getString(R.string.str_word_count, dataModel.resultWordCount)}"
        }
    }
}
