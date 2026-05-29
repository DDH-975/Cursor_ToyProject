package com.example.cursor.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cursor.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.expression.observe(viewLifecycleOwner) { expression ->
            binding.tvExpression.text = expression
        }
        viewModel.result.observe(viewLifecycleOwner) { result ->
            binding.tvResult.text = result
        }
    }

    private fun setupClickListeners() {
        binding.btn0.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(0)) }
        binding.btn1.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(1)) }
        binding.btn2.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(2)) }
        binding.btn3.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(3)) }
        binding.btn4.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(4)) }
        binding.btn5.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(5)) }
        binding.btn6.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(6)) }
        binding.btn7.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(7)) }
        binding.btn8.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(8)) }
        binding.btn9.setOnClickListener { viewModel.onInput(CalculatorInput.Digit(9)) }

        binding.btnAdd.setOnClickListener { viewModel.onInput(CalculatorInput.Operator('+')) }
        binding.btnSubtract.setOnClickListener { viewModel.onInput(CalculatorInput.Operator('-')) }
        binding.btnMultiply.setOnClickListener { viewModel.onInput(CalculatorInput.Operator('*')) }
        binding.btnDivide.setOnClickListener { viewModel.onInput(CalculatorInput.Operator('/')) }

        binding.btnClear.setOnClickListener { viewModel.onInput(CalculatorInput.Clear) }
        binding.btnBackspace.setOnClickListener { viewModel.onInput(CalculatorInput.Backspace) }
        binding.btnEquals.setOnClickListener { viewModel.onInput(CalculatorInput.Equals) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
