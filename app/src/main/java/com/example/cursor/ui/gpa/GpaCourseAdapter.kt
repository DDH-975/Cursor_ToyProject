package com.example.cursor.ui.gpa

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cursor.databinding.ItemGpaCourseBinding
import com.example.cursor.model.GpaCourseItem

class GpaCourseAdapter(
    private val creditOptions: Array<String>,
    private val gradeOptions: Array<String>,
    private val onCourseChanged: (GpaCourseItem) -> Unit
) : ListAdapter<GpaCourseItem, GpaCourseAdapter.CourseViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemGpaCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CourseViewHolder(
        private val binding: ItemGpaCourseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var boundItem: GpaCourseItem? = null
        private var subjectWatcher: TextWatcher? = null

        fun bind(item: GpaCourseItem) {
            boundItem = item
            clearListeners()

            binding.etSubjectName.setText(item.subjectName)
            setupSpinners(item)
            setupSubjectWatcher()
        }

        private fun setupSpinners(item: GpaCourseItem) {
            val context = binding.root.context

            val creditAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                creditOptions
            )
            binding.spinnerCredit.adapter = creditAdapter
            binding.spinnerCredit.setSelection(item.creditIndex.coerceIn(0, creditOptions.lastIndex))

            val gradeAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                gradeOptions
            )
            binding.spinnerGrade.adapter = gradeAdapter
            binding.spinnerGrade.setSelection(item.gradeIndex.coerceIn(0, gradeOptions.lastIndex))

            binding.spinnerCredit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    updateItem { it.copy(creditIndex = position) }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }

            binding.spinnerGrade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    updateItem { it.copy(gradeIndex = position) }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }

        private fun setupSubjectWatcher() {
            subjectWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
                override fun afterTextChanged(s: Editable?) {
                    updateItem { it.copy(subjectName = s?.toString().orEmpty()) }
                }
            }
            binding.etSubjectName.addTextChangedListener(subjectWatcher)
        }

        private fun updateItem(transform: (GpaCourseItem) -> GpaCourseItem) {
            val current = boundItem ?: return
            val updated = transform(current)
            if (updated != current) {
                boundItem = updated
                onCourseChanged(updated)
            }
        }

        private fun clearListeners() {
            subjectWatcher?.let { binding.etSubjectName.removeTextChangedListener(it) }
            subjectWatcher = null
            binding.spinnerCredit.onItemSelectedListener = null
            binding.spinnerGrade.onItemSelectedListener = null
        }
    }

    private class CourseDiffCallback : DiffUtil.ItemCallback<GpaCourseItem>() {
        override fun areItemsTheSame(oldItem: GpaCourseItem, newItem: GpaCourseItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GpaCourseItem, newItem: GpaCourseItem): Boolean =
            oldItem == newItem
    }
}
