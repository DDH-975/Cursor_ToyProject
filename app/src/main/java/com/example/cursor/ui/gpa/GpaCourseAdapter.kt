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
    private val onSelectionChanged: (GpaCourseItem) -> Unit,
    private val onSubjectNameChanged: (courseId: String, subjectName: String) -> Unit
) : ListAdapter<GpaCourseItem, GpaCourseAdapter.CourseViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemGpaCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(
            binding,
            creditOptions,
            gradeOptions,
            onSelectionChanged,
            onSubjectNameChanged
        )
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CourseViewHolder(
        private val binding: ItemGpaCourseBinding,
        private val creditOptions: Array<String>,
        private val gradeOptions: Array<String>,
        private val onSelectionChanged: (GpaCourseItem) -> Unit,
        private val onSubjectNameChanged: (courseId: String, subjectName: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var boundItem: GpaCourseItem? = null
        private var isBinding = false

        init {
            val context = binding.root.context

            binding.spinnerCredit.adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                creditOptions
            )
            binding.spinnerGrade.adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                gradeOptions
            )

            binding.spinnerCredit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    if (isBinding) return
                    notifySelectionChange { it.copy(creditIndex = position) }
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
                    if (isBinding) return
                    notifySelectionChange { it.copy(gradeIndex = position) }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }

            binding.etSubjectName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
                override fun afterTextChanged(s: Editable?) {
                    if (isBinding) return
                    val current = boundItem ?: return
                    val subjectName = s?.toString().orEmpty()
                    boundItem = current.copy(subjectName = subjectName)
                    onSubjectNameChanged(current.id, subjectName)
                }
            })
        }

        fun bind(item: GpaCourseItem) {
            isBinding = true
            boundItem = item

            val editText = binding.etSubjectName
            val currentText = editText.text?.toString().orEmpty()
            if (currentText != item.subjectName) {
                val selection = editText.selectionStart.coerceAtLeast(0)
                editText.setText(item.subjectName)
                val newSelection = selection.coerceIn(0, item.subjectName.length)
                editText.setSelection(newSelection)
            }

            val creditIndex = item.creditIndex.coerceIn(0, creditOptions.lastIndex)
            if (binding.spinnerCredit.selectedItemPosition != creditIndex) {
                binding.spinnerCredit.setSelection(creditIndex, false)
            }

            val gradeIndex = item.gradeIndex.coerceIn(0, gradeOptions.lastIndex)
            if (binding.spinnerGrade.selectedItemPosition != gradeIndex) {
                binding.spinnerGrade.setSelection(gradeIndex, false)
            }

            isBinding = false
        }

        private fun notifySelectionChange(transform: (GpaCourseItem) -> GpaCourseItem) {
            val current = boundItem ?: return
            val updated = transform(current)
            if (updated.creditIndex == current.creditIndex &&
                updated.gradeIndex == current.gradeIndex
            ) {
                return
            }
            boundItem = updated
            onSelectionChanged(updated)
        }
    }

    private class CourseDiffCallback : DiffUtil.ItemCallback<GpaCourseItem>() {
        override fun areItemsTheSame(oldItem: GpaCourseItem, newItem: GpaCourseItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GpaCourseItem, newItem: GpaCourseItem): Boolean =
            oldItem.creditIndex == newItem.creditIndex &&
                oldItem.gradeIndex == newItem.gradeIndex
    }
}
