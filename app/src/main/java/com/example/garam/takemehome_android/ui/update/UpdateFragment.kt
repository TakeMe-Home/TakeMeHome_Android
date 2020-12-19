package com.example.garam.takemehome_android.ui.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.ui.SharedViewModel

class UpdateFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_update,container,false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        return root
    }
}