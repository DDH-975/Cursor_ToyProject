package com.example.cursor.ui.dutch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cursor.R
import com.example.cursor.databinding.FragmentDutchBinding

class DutchPayFragment : Fragment() {

    private var _binding: FragmentDutchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DutchPayViewModel by viewModels()

    private var isUpdatingPeopleField = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDutchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
        setupPeopleCountWatcher()
    }

    private fun setupObservers() {
        viewModel.peopleCount.observe(viewLifecycleOwner) { count ->
            isUpdatingPeopleField = true
            binding.etPeopleCount.setText(count.toString())
            isUpdatingPeopleField = false
        }
        viewModel.perPersonAmount.observe(viewLifecycleOwner) { amount ->
            if (amount == null) return@observe
            binding.cardDutchResult.visibility = View.VISIBLE
            binding.tvDutchError.visibility = View.GONE
            binding.tvPerPersonAmount.text = getString(R.string.dutch_per_person_format, amount)
        }
        viewModel.error.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                binding.cardDutchResult.visibility = View.GONE
                binding.tvDutchError.visibility = View.VISIBLE
                binding.tvDutchError.text = getString(R.string.dutch_error_invalid)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnIncreasePeople.setOnClickListener { viewModel.increasePeople() }
        binding.btnDecreasePeople.setOnClickListener { viewModel.decreasePeople() }
        binding.btnCalculateDutch.setOnClickListener {
            viewModel.calculate(
                binding.etTotalAmount.text?.toString().orEmpty(),
                binding.etPeopleCount.text?.toString().orEmpty()
            )
        }
    }

    private fun setupPeopleCountWatcher() {
        binding.etPeopleCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                if (isUpdatingPeopleField) return
                val count = s?.toString()?.toIntOrNull() ?: return
                viewModel.setPeopleCount(count)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
