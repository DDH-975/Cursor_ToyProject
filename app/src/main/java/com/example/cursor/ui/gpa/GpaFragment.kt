package com.example.cursor.ui.gpa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cursor.R
import com.example.cursor.databinding.FragmentGpaBinding

class GpaFragment : Fragment() {

    private var _binding: FragmentGpaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GpaViewModel by viewModels()

    private lateinit var courseAdapter: GpaCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGpaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        courseAdapter = GpaCourseAdapter(
            creditOptions = resources.getStringArray(R.array.gpa_credits),
            gradeOptions = resources.getStringArray(R.array.gpa_grades),
            onCourseChanged = viewModel::updateCourse
        )
        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }
    }

    private fun setupObservers() {
        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            courseAdapter.submitList(courses)
        }
        viewModel.gpaResult.observe(viewLifecycleOwner) { gpa ->
            if (gpa != null) {
                binding.tvGpaResult.text = getString(R.string.gpa_result_format, gpa)
            }
        }
        viewModel.gpaError.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                binding.tvGpaResult.text = getString(R.string.gpa_error_no_courses)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnAddCourse.setOnClickListener { viewModel.addCourse() }
        binding.btnCalculateGpa.setOnClickListener { viewModel.calculateGpa() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
