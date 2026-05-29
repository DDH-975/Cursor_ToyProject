package com.example.cursor.ui.bmi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cursor.R
import com.example.cursor.databinding.FragmentBmiBinding

class BmiFragment : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BmiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.btnCalculateBmi.setOnClickListener {
            viewModel.calculate(
                binding.etHeight.text?.toString().orEmpty(),
                binding.etWeight.text?.toString().orEmpty()
            )
        }
    }

    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) { result ->
            if (result == null) return@observe
            binding.cardBmiResult.visibility = View.VISIBLE
            binding.tvBmiError.visibility = View.GONE
            binding.tvBmiValue.text = getString(R.string.bmi_result_format, result.bmi)
            binding.tvBmiStatus.text = getString(result.category.labelRes)
            binding.tvBmiStatus.setTextColor(
                ContextCompat.getColor(requireContext(), result.category.colorRes)
            )
        }
        viewModel.error.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                binding.cardBmiResult.visibility = View.GONE
                binding.tvBmiError.visibility = View.VISIBLE
                binding.tvBmiError.text = getString(R.string.bmi_error_invalid_input)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
