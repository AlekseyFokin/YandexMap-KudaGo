package org.sniffsnirr.simplephotogalery.ui.galery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.simplephotogalery.databinding.FragmentGaleryBinding
import org.sniffsnirr.simplephotogalery.ui.galery.recyclerview.TilesAdapter
import org.sniffsnirr.simplephotogalery.ui.CommonViewModel

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGaleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommonViewModel by activityViewModels()
    val tilesAdapter=TilesAdapter()

      override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGaleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
      return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.galery.adapter=tilesAdapter
        viewModel.tiles.onEach { tilesAdapter.setData(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}