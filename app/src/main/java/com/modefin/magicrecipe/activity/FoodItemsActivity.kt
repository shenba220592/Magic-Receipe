package com.modefin.magicrecipe.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.modefin.magicrecipe.R
import com.modefin.magicrecipe.adapter.FoodItemAdapter
import com.modefin.magicrecipe.listener.PaginationScrollListener
import com.modefin.magicrecipe.model.FoodList
import com.modefin.magicrecipe.repository.NetworkRepository
import com.modefin.magicrecipe.utils.CommonUtils
import com.modefin.magicrecipe.viewmodel.FoodItemViewModel
import com.modefin.magicrecipe.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class FoodItemsActivity : AppCompatActivity(),FoodItemAdapter.OnWidgetClickListener {
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: FoodItemViewModel
    private lateinit var adapter: FoodItemAdapter
    private lateinit var foodList: ArrayList<FoodList>
    private var isLoading = false
    private var isLastPage = false
    private var currentPage: Int = 1
    private val totalPages: Int = 50 //As API is not giving total pages,assumed as 50


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intitializeRepo()
        setupUI()


        setUpListener()
        callFoodItemsObserver()

    }

    private fun setUpListener() {

        ed_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(
                charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0) {
                    var searchString = ""
                    try {
                        searchString = ed_search.text.toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    //set with intial level while text is changing in search
                    currentPage=1
                    isLoading = false
                    isLastPage=false

                    //load first page at initial time
                    loadFirstPage(currentPage, searchString)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }



    private fun loadFirstPage(currentPage: Int, searchString: String) {
        if (searchString.length >= 4)

            callfoodItemsAPI(searchString, currentPage)
    }

    private fun loadNextPage(currentPage: Int, searchString: String) {


        callfoodItemsAPI(searchString, currentPage)
    }

    private fun setupUI() {


        val linearlayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearlayoutManager

        //ScrollListener to listen recycler scroll
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearlayoutManager) {

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }


            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                //load next items
                loadNextPage(currentPage, ed_search.text.toString()) //loadingNextPage

            }


        })

        recyclerView.setHasFixedSize(true)

        //set adapter
        adapter = FoodItemAdapter(this)
        recyclerView.adapter = adapter


    }


    private fun callfoodItemsAPI(ingredeients: String, pageNo: Int) {

        // food search API
        if(CommonUtils.isNetworkAvailable(this)) {
            viewModel.getFoodItems(ingredeients, pageNo)
            if (currentPage == 1) progressBar.visibility = View.VISIBLE
        }else{
            ed_search.hideKeyboard()
            Snackbar.make(this@FoodItemsActivity.findViewById(android.R.id.content), CommonUtils.NETWORK_ERROR, 6000).show()
        }

    }

    private fun callFoodItemsObserver() {

     //observer
        viewModel.foodListData.observe(this, Observer { foodListResponse ->

            progressBar.visibility = View.GONE
            foodListResponse?.let {
                if (foodListResponse.title.equals("Recipe Puppy", true)) {



                    if (foodListResponse?.results!!.size != 0) {


                        foodList = foodListResponse.results
                        foodList?.let {
                            if (it.size != 0) {
                                recyclerView.visibility = View.VISIBLE
                                tv_empty.visibility = View.GONE

                                setFoodItems(it)

                            }
                        }
                    } else {

                        tv_empty.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }


                }
            }


        })
    }


    private fun setFoodItems(foodList: ArrayList<FoodList>) {
        if (currentPage == 1) {
            adapter.setFoodItems(foodList)
            adapter.addLoadingFooter()
            adapter.notifyDataSetChanged()
            if (currentPage == totalPages) {
                adapter.removeLoadingFooter()
                isLastPage = true
            }
        } else {

                if (!foodList.isEmpty()) {
                    adapter.removeLoadingFooter()
                    isLoading = false
                    adapter.addAll(foodList)
                    adapter.addLoadingFooter()
                    if (currentPage == totalPages) {
                        adapter.removeLoadingFooter()
                        isLastPage = true
                    }
                } else {
                    adapter.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }

        }
    }

    private fun intitializeRepo() {
        val repository = NetworkRepository()
        factory = ViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(FoodItemViewModel::class.java)

    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    // click listener for list item
    override fun onClickWidgetItem(parent: FoodList) {

        val intent = Intent (this, SelectedFoodDetailsActivity::class.java)
            .apply {
                putExtra("href", parent.href)

            }
        startActivity(intent)
    }

}