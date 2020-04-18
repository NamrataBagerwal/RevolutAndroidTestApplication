package com.androidtestapp.revolut.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidtestapp.revolut.AppConstants.CURRENCY_AMOUNT_ZERO
import com.androidtestapp.revolut.R
import com.androidtestapp.revolut.repository.remote_repository.dto.CurrencyConverter
import com.androidtestapp.revolut.ui.util.OnTextChangeListener
import com.bumptech.glide.Glide

class CurrencyConverterAdapter(
    val onCurrencyClickListener: (currencyCode: String, currencyAmount: Double) -> Unit,
    val onCurrencyAmountChangeListener: (currencyAmount: Double) -> Unit
) : RecyclerView.Adapter<CurrencyConverterAdapter.CurrencyConverterViewHolder>() {

    var currencyConverterList: List<CurrencyConverter> = listOf()
        set(value) {
            val oldCurrencyConverterList = currencyConverterList
            // To dispatch only difference in data to adapter
            val differenceResult =
                DiffUtil.calculateDiff(DiffUtilCallback(oldCurrencyConverterList, value))
            field = value
            differenceResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
        }

    private class DiffUtilCallback(
        private val oldList: List<CurrencyConverter>,
        private val newList: List<CurrencyConverter>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].currencyCode === newList[newItemPosition].currencyCode
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            val (currencyFlag1, currencyCode1, currencyName1, currencyAmount1) = oldList[oldPosition]
            val (currencyFlag2, currencyCode2, currencyName2, currencyAmount2) = newList[newPosition]

            return currencyFlag1 == currencyFlag2 && currencyCode1 == currencyCode2 && currencyName1 == currencyName2 && currencyAmount1 == currencyAmount2
        }

        @Nullable
        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
            return super.getChangePayload(oldPosition, newPosition)
        }
    }

    class CurrencyConverterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyFlag: ImageView = itemView.findViewById(R.id.currencyFlagImageView)
        val currencyCode: TextView = itemView.findViewById(R.id.currencyCodeTextView)
        val currencyName: TextView = itemView.findViewById(R.id.currencyNameTextView)
        val currencyAmount: EditText = itemView.findViewById(R.id.currencyAmountEditText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyConverterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_currency_converter_list_item, parent, false)
        return CurrencyConverterViewHolder(view)
    }

    override fun getItemCount(): Int = currencyConverterList.size

    override fun onBindViewHolder(holder: CurrencyConverterViewHolder, position: Int) {

        val currencyConverter: CurrencyConverter = currencyConverterList[position]

        Glide.with(holder.itemView.context)
            .load(currencyConverter.currencyFlag)
            .into(holder.currencyFlag)

        holder.currencyCode.text = currencyConverter.currencyCode

        holder.currencyName.text = currencyConverter.currencyName

//        holder.currencyAmount.addTextChangedListener(OnTextChangeListener { text ->
//            var currencyAmount = CURRENCY_AMOUNT_ZERO
//            if (!text.isNullOrBlank()) {
//                currencyAmount = text.toDouble()
//            }
//            onCurrencyAmountChangeListener(currencyAmount)
//        })

        holder.currencyAmount.setText(currencyConverter.convertedAmount.toString())
        holder.currencyAmount.setOnClickListener{
            onViewHolderItemClickListener(holder, currencyConverter)
        }

        holder.itemView.setOnClickListener {
            onViewHolderItemClickListener(holder, currencyConverter)
        }

    }

    private fun onViewHolderItemClickListener(
        holder: CurrencyConverterViewHolder,
        currencyConverter: CurrencyConverter
    ) {
        var currencyAmount = CURRENCY_AMOUNT_ZERO
        val text = holder.currencyAmount.text
        if (!text.isNullOrBlank()) {
            currencyAmount = text.toString().toDouble()
        }
        onCurrencyClickListener(currencyConverter.currencyCode, currencyAmount)
    }
}