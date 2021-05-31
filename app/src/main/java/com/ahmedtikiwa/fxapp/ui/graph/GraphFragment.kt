package com.ahmedtikiwa.fxapp.ui.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedtikiwa.fxapp.R
import com.ahmedtikiwa.fxapp.databinding.FragmentGraphBinding
import com.ahmedtikiwa.fxapp.domain.History
import com.ahmedtikiwa.fxapp.util.DateUtil
import com.google.android.material.snackbar.Snackbar
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GraphFragment : Fragment() {

    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GraphViewModel>()

    private lateinit var historyPagingAdapter: HistoryPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraphBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        historyPagingAdapter = HistoryPagingAdapter()
        binding.recyclerviewHistory.apply {
            adapter = historyPagingAdapter.withLoadStateHeaderAndFooter(
                header = HistoryLoadStateAdapter { historyPagingAdapter.retry() },
                footer = HistoryLoadStateAdapter { historyPagingAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.eurUsdHistory.observe(viewLifecycleOwner, {
            addGraphData(binding.eurUsdGraph, it)
        })

        viewModel.gbpUsdHistory.observe(viewLifecycleOwner, {
            addGraphData(binding.gpbUsdGraph, it)
        })

        viewModel.usdJpyHistory.observe(viewLifecycleOwner, {
            addGraphData(binding.usdJpyGraph, it)
        })

        lifecycleScope.launch {
            viewModel.history.collectLatest {
                historyPagingAdapter.submitData(lifecycle, it)
            }

            historyPagingAdapter.loadStateFlow.collectLatest { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.loading_general),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else if (loadState.refresh is LoadState.Error) {
                    val error = (loadState.refresh as LoadState.Error).error.message
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.load_state_error_default, error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun addGraphData(graphView: GraphView, data: List<History>) {
        if (data.isNotEmpty()) {
            val series: LineGraphSeries<DataPoint>
            val dataPoints = mutableListOf<DataPoint>()

            for (item in data) {
                dataPoints.add(
                    DataPoint(
                        DateUtil.getDateFromString(item.date),
                        item.price
                    )
                )
            }

            series = LineGraphSeries(
                dataPoints.toTypedArray()
            )

            graphView.addSeries(series)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}