package com.s1g1.memonotes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.s1g1.memonotes.databinding.FragmentSettingsBinding
import com.s1g1.memonotes.viewmodel.SettingsViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings){

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    private val svm: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        binding.switchDarkTheme.isChecked = svm.getSavedTheme()

        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            svm.toggleDarkMode(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}