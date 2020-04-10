package com.androidtestapp.revolut.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidtestapp.revolut.R
import com.androidtestapp.revolut.repository.remotedatastore.dto.CurrencyConverter
import com.androidtestapp.revolut.ui.util.OnTextChangeListener
import com.bumptech.glide.Glide

class CurrencyConverterAdapter(
   private val onCurrencyClickListener: (currencyCode: String) -> Unit
): RecyclerView.Adapter<CurrencyConverterAdapter.CurrencyConverterViewHolder>() {

    companion object{
        private const val BASE_CURRENCY_POSITION = 0
    }

    var currencyConverterList: List<CurrencyConverter> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class CurrencyConverterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val currencyFlag: ImageView = itemView.findViewById(R.id.currencyFlagImageView)
        val currencyCode: TextView = itemView.findViewById(R.id.currencyCodeTextView)
        val currencyName: TextView = itemView.findViewById(R.id.currencyNameTextView)
        val currencyAmount: EditText = itemView.findViewById(R.id.currencyAmountEditText)

    }

//    init {
//        setHasStableIds(true)
//    }Mo

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyConverterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_currency_converter_list_item, parent, false)
        return CurrencyConverterViewHolder(view)
    }

    override fun getItemCount(): Int = currencyConverterList.size

    override fun onBindViewHolder(holder: CurrencyConverterViewHolder, position: Int) {
        val currencyConverter: CurrencyConverter = currencyConverterList[position]

        Log.i(CurrencyConverterAdapter::class.simpleName, currencyConverter.currencyFlag)
        Glide.with(holder.itemView.context)
            .load(currencyConverter.currencyFlag)
            .into(holder.currencyFlag)

        holder.currencyCode.text = currencyConverter.currencyCode

        holder.currencyName.text = currencyConverter.currencyName

        holder.currencyAmount.isClickable = false
        holder.currencyAmount.isEnabled = false

        holder.currencyAmount.setText(currencyConverter.convertedAmount.toString())

        holder.itemView.setOnClickListener{
            onCurrencyClickListener(currencyConverter.currencyCode)
        }

    }
}