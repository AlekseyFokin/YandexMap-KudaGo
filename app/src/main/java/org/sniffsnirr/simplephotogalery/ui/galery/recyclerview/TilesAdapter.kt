package org.sniffsnirr.simplephotogalery.ui.galery.recyclerview

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.sniffsnirr.simplephotogalery.R
import org.sniffsnirr.simplephotogalery.databinding.TileItemBinding
import org.sniffsnirr.simplephotogalery.entities.Tile
import org.sniffsnirr.simplephotogalery.ui.shot.ShotFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TilesAdapter() : RecyclerView.Adapter<TileViewHolder>() {
    private var tiles:List<Tile> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val binding = TileItemBinding.inflate(LayoutInflater.from(parent.context))
        return TileViewHolder(binding)
    }

    override fun getItemCount() = tiles.size

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        val item = tiles[position]
        val formatter = SimpleDateFormat(ShotFragment.DATE_FORMAT, Locale.US)
        val photoDate = Date(item.dateTime)
        holder.binding.labelTitle.text = formatter.format(photoDate)
        Glide
            .with(holder.binding.photoTile.context)
            .load(Uri.parse(item.photoPath))
            .placeholder(R.drawable.baseline_accessibility_24)
            .error(R.drawable.baseline_accessibility_24)
            .into(holder.binding.photoTile)
    }

   fun setData(data:List<Tile>){
        this.tiles=data
        notifyDataSetChanged()
    }
}

class TileViewHolder(val binding: TileItemBinding) : RecyclerView.ViewHolder(binding.root)