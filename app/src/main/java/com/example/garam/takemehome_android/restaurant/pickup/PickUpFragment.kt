package com.example.garam.takemehome_android.restaurant.pickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.restaurant.RestaurantSharedViewModel

class PickUpFragment : Fragment() {

    private lateinit var sharedViewModel : RestaurantSharedViewModel
    private lateinit var root : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_pick_up, container, false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(RestaurantSharedViewModel::class.java)

        val restaurantId = sharedViewModel.getId()
        sharedViewModel.setId(restaurantId!!)

        return root
    }
}