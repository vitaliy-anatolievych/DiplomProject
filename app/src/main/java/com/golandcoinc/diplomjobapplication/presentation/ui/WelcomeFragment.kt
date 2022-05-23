package com.golandcoinc.diplomjobapplication.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.golandcoinc.diplomjobapplication.R
import com.golandcoinc.diplomjobapplication.databinding.FragmentWelcomeBinding
import com.golandcoinc.diplomjobapplication.utils.NavigateUtil
import com.golandcoinc.diplomjobapplication.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.NullPointerException

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw NullPointerException("FragmentWelcomeBinding is null")

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            tlInputSpeed.editText?.addTextChangedListener {
                tlInputSpeed.isErrorEnabled = false
            }

            btnStartApp.setOnClickListener {
                val speed = tlInputSpeed.editText?.text.toString()
                if (mainViewModel.setSpeed(speed = speed)) {
                    NavigateUtil.navigateTo(parentFragmentManager, MainFragment.newInstance())
                } else {
                    tlInputSpeed.error = getString(R.string.error_invalid_input)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = WelcomeFragment()
    }
}