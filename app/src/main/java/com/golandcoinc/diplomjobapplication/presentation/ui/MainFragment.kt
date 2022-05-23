package com.golandcoinc.diplomjobapplication.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.golandcoinc.diplomjobapplication.R
import com.golandcoinc.diplomjobapplication.databinding.FragmentMainBinding
import com.golandcoinc.diplomjobapplication.utils.NavigateUtil
import com.golandcoinc.diplomjobapplication.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw NullPointerException("FragmentMainBinding is null")

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getRecommendedSpeed()

        with(binding) {

            mainViewModel.recommendedSpeed.observe(viewLifecycleOwner) {
                tvRecommendSpeed.text = it.toString()
            }

            btnFillFuel.setOnClickListener {
                val fuel = tilFuelInput.editText?.text.toString()
                if (mainViewModel.fillFuelTank(fuel)) {
                    tilFuelInput.editText?.text?.clear()
                    Toast.makeText(
                        requireContext(),
                        "${getString(R.string.fuel_set_successful)} $fuel л",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_invalid_input),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnStats.setOnClickListener {
                NavigateUtil.navigateTo(parentFragmentManager, StatsFragment.newInstance())
            }

            btnStartTravel.setOnClickListener {
                mainViewModel.startTrip()
                NavigateUtil.navigateTo(
                    parentFragmentManager,
                    WorkingFragment.newInstance()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}