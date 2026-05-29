package com.example.cursor.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.cursor.R
import com.example.cursor.databinding.ActivityMainBinding
import com.example.cursor.ui.bmi.BmiFragment
import com.example.cursor.ui.calculator.CalculatorFragment
import com.example.cursor.ui.dutch.DutchPayFragment
import com.example.cursor.ui.gpa.GpaFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            binding.bottomNavigation.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        setupBottomNavigation()
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_calculator
            showFragment(R.id.nav_calculator)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                showFragment(item.itemId)
                true
            }
        )
    }

    private fun showFragment(menuItemId: Int) {
        val tag = fragmentTag(menuItemId)
        val existing = supportFragmentManager.findFragmentByTag(tag)
        if (existing != null && existing.isVisible) return

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            supportFragmentManager.fragments.forEach { fragment ->
                if (fragment.isAdded) hide(fragment)
            }
            val fragment = existing ?: createFragment(menuItemId)
            if (existing == null) {
                add(R.id.fragmentContainer, fragment, tag)
            } else {
                show(fragment)
            }
        }
    }

    private fun createFragment(menuItemId: Int): Fragment = when (menuItemId) {
        R.id.nav_gpa -> GpaFragment()
        R.id.nav_bmi -> BmiFragment()
        R.id.nav_dutch -> DutchPayFragment()
        else -> CalculatorFragment()
    }

    private fun fragmentTag(menuItemId: Int): String = when (menuItemId) {
        R.id.nav_gpa -> TAG_GPA
        R.id.nav_bmi -> TAG_BMI
        R.id.nav_dutch -> TAG_DUTCH
        else -> TAG_CALCULATOR
    }

    companion object {
        private const val TAG_CALCULATOR = "calculator"
        private const val TAG_GPA = "gpa"
        private const val TAG_BMI = "bmi"
        private const val TAG_DUTCH = "dutch"
    }
}
