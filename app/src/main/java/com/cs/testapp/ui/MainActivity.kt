package com.cs.testapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.cs.testapp.R
import com.cs.testapp.data.DataModel
import com.cs.testapp.databinding.ActivityMainBinding
import com.cs.testapp.protocol.CommunicationCallback
import com.cs.testapp.protocol.ProtocolAction
import com.cs.testapp.ui.details.DetailsFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CommunicationCallback {

    private val dataModel = DataModel()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.fragment)
    }

    override fun onFragmentEvent(action: ProtocolAction) {
        when (action) {
            is ProtocolAction.OpenRunRequestScreen -> {
                navController.navigate(HomeFragmentDirections.actionHomeToDetailsFragment(dataModel))
            }
        }
    }
}