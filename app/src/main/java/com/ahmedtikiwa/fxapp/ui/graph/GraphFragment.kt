package com.ahmedtikiwa.fxapp.ui.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ahmedtikiwa.fxapp.databinding.FragmentGraphBinding
import com.ahmedtikiwa.fxapp.domain.History
import com.ahmedtikiwa.fxapp.util.DateUtil
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GraphFragment : Fragment() {

    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<GraphViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraphBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.eurUsdHistory.observe(viewLifecycleOwner, {
                addGraphData(binding.eurUsdGraph, it)
            })

            viewModel.gbpUsdHistory.observe(viewLifecycleOwner, {
                addGraphData(binding.gpbUsdGraph, it)
            })

            viewModel.usdJpyHistory.observe(viewLifecycleOwner, {
                addGraphData(binding.usdJpyGraph, it)
            })
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

}