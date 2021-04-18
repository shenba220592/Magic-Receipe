package com.modefin.magicrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modefin.magicrecipe.R
import com.modefin.magicrecipe.model.FoodList
import kotlinx.android.synthetic.main.food_item_layout.view.*


class FoodItemAdapter(var onWidgetClickListener: OnWidgetClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM = 0
    private val LOADING = 1

    private var foodItemList: MutableList<FoodList> = ArrayList()
    private var isLoadingAdded: Boolean = false

    fun setFoodItems(foodItemList: MutableList<FoodList>) {
        this.foodItemList = foodItemList
    }

    //click action for list
    interface OnWidgetClickListener {

        fun onClickWidgetItem(parent: FoodList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            ITEM -> {
                val item = inflater.inflate(R.layout.food_item_layout, parent, false)
                FoodItemVH(item)
            }
            LOADING -> {
                val item = inflater.inflate(R.layout.item_progress, parent, false)
                LoadingVH(item)
            }
            else -> throw IllegalArgumentException("Wrong view type")
        }
    }

    override fun getItemCount(): Int {
        return foodItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == foodItemList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                val foodItem = foodItemList[position]
                (holder as FoodItemVH).setFoodItem(foodItem)

            }
            LOADING -> {
            }
        }
    }

    fun add(r: FoodList) {
        foodItemList.add(r)
        notifyItemInserted(foodItemList.size - 1)
    }

    fun addAll(foodItem: List<FoodList>) {
        for (result in foodItem) {
            add(result)
        }
    }

    fun remove(r: FoodList?) {
        val position = foodItemList.indexOf(r)
        if (position > -1) {
            foodItemList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(FoodList())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = foodItemList.size - 1
        val result = getItem(position)

        if (result != null) {
            foodItemList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): FoodList? {
        return foodItemList.get(position)
    }


    inner class FoodItemVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        fun setFoodItem(foodItemModel: FoodList) {
            itemView. setOnClickListener(View.OnClickListener {
              onWidgetClickListener.onClickWidgetItem(foodItemModel)
            })

            // val foodItemModel = list[position]
            itemView.tv_food_name.text = foodItemModel.title?.trim()
            itemView.tv_food_desc.text = foodItemModel.ingredients?.trim()
            Glide.with(itemView.img_food.context)
                .load(foodItemModel.thumbnail)
                .into(itemView.img_food)


        }


    }


    inner class LoadingVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {




    }

}