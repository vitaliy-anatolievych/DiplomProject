package com.golandcoinc.diplomjobapplication.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.golandcoinc.diplomjobapplication.databinding.FragmentWorkingBinding
import com.golandcoinc.diplomjobapplication.utils.AppPowerManager
import com.golandcoinc.diplomjobapplication.utils.NavigateUtil
import com.golandcoinc.diplomjobapplication.utils.SpeedStatus
import com.golandcoinc.diplomjobapplication.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorkingFragment : Fragment() {
    private var _binding: FragmentWorkingBinding? = null
    private val binding: FragmentWorkingBinding
        get() = _binding ?: throw NullPointerException("FragmentWorkingBinding is null")

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.listenSpeed()
        mainViewModel.getRecommendedSpeed()

        mainViewModel.recommendedSpeed.observe(viewLifecycleOwner) {
            binding.viewSpeedDetector.setSpeed(it)
        }

        mainViewModel.speedStatus.observe(viewLifecycleOwner) {
            when (it) {
                SpeedStatus.FASTER -> {
                    binding.viewSpeedDetector.activateArrowUp()
                }
                SpeedStatus.NORMAL -> {
                    binding.viewSpeedDetector.hideArrow()
                }
                SpeedStatus.LOWER -> {
                    binding.viewSpeedDetector.activateArrowDown()
                }
            }
        }

        val powerManager = AppPowerManager(requireContext())

        mainViewModel.currentSpeed.observe(viewLifecycleOwner) {
            mainViewModel.listenSpeedStatus(it)
            binding.tvCurrentSpeed.text = it.toString()
            powerManager.stayWake()
        }

        binding.btnFinishTrip.setOnClickListener {
            mainViewModel.stopTrip()
            powerManager.releaseWake()
            NavigateUtil.navigateTo(parentFragmentManager, MainFragment.newInstance())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = WorkingFragment()
    }
}