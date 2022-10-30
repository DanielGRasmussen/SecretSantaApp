package com.example.secretsanta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_option.view.*

class OptionsAdapter(var options: MutableList<Option>, var Main: MainActivity) :
    RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {
    class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_option,
                parent,
                false)
        )
    }

    fun addOption(option: Option) {
        options.add(option)
        notifyItemInserted(options.size - 1)
        Main.saveOptions(Main.sharedPreference, options)
    }

    private fun delete(option: Option) {
        for ((i, item) in options.withIndex()) {
            if (item.title == option.title && item.relation == option.relation) {
                options.removeAt(i)
                notifyItemRemoved(i)
                Main.saveOptions(Main.sharedPreference, options)
                return
            }
        }
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val curOptions = options[position]
        holder.itemView.apply {
            tvName.text = curOptions.title
            tvRelation.text = curOptions.relation
            btnDelete.setOnClickListener{ delete(Option(tvName.text.toString(), tvRelation.text.toString())) }
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }
}