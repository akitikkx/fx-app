package com.ahmedtikiwa.fxapp.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ahmedtikiwa.fxapp.R
import com.ahmedtikiwa.fxapp.databinding.FragmentConverterBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConverterFragment : Fragment() {

    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var converterViewModelFactory: ConverterViewModel.ConverterViewModelFactory

    private val viewModel by viewModels<ConverterViewModel> {
        converterViewModelFactory.create(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currenciesList.observe(viewLifecycleOwner, { currencyList ->
            if (currencyList.isNotEmpty()) {
                val currencyCodes = mutableListOf<String>()
                currencyList.forEach {
                    currencyCodes.add(it.code)
                }
                val dropdownAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.layout_dropdown_list_item,
                    currencyCodes
                )
                binding.autocompleteBaseCurrencyMenu.setAdapter(dropdownAdapter)
                binding.autocompleteToCurrencyMenu.setAdapter(dropdownAdapter)
            }
        })

        viewModel.rateRequested.observe(viewLifecycleOwner, {
            if (it) {
                val baseCurrency = binding.autocompleteBaseCurrencyMenu.text.toString()
                if (baseCurrency.isBlank()) {
                    binding.textInputBaseCurrencyMenu.error =
                        getString(R.string.error_base_currency_required)
                }

                val toCurrency = binding.autocompleteToCurrencyMenu.text.toString()
                if (toCurrency.isBlank()) {
                    binding.textInputToCurrencyMenu.error =
                        getString(R.string.error_to_currency_required)
                }

                if (baseCurrency.isNotBlank() && toCurrency.isNotBlank()) {
                    binding.textInputBaseCurrencyMenu.error = null
                    binding.textInputToCurrencyMenu.error = null
                    viewModel.onRateQueryReceived(baseCurrency, toCurrency)
                    viewModel.onRateRequestCompleted()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}