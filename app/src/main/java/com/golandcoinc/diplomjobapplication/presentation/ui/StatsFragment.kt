package com.golandcoinc.diplomjobapplication.presentation.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.golandcoinc.diplomjobapplication.R
import com.golandcoinc.diplomjobapplication.databinding.FragmentStatsBinding
import com.golandcoinc.diplomjobapplication.presentation.adapters.StatsAdapter
import com.golandcoinc.diplomjobapplication.utils.NavigateUtil
import com.golandcoinc.diplomjobapplication.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding: FragmentStatsBinding
        get() = _binding ?: throw NullPointerException("FragmentStatsBinding is null")

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getStatsJournal()
        recyclerViewAdapterSettings()

        binding.btnDeleteStats.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.delete_stats_message))
                .setPositiveButton(getString(R.string.positive)) { _, _ ->
                    mainViewModel.deleteStatsJournal()
                    NavigateUtil.navigateTo(parentFragmentManager, WelcomeFragment.newInstance())
                }
                .setNegativeButton(getString(R.string.negative)) {_,_->}
                .create()
                .show()
        }
    }

    private fun recyclerViewAdapterSettings() {
        val statsAdapter = StatsAdapter()

        mainViewModel.statsJournalModel.observe(viewLifecycleOwner) {
            statsAdapter.list = it.statsList
            binding.rvStats.adapter = statsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatsFragment()
    }
}