package com.example.garam.takemehome_android.ui.update

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.ui.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*


class UpdateFragment : Fragment() {

    private lateinit var updateViewModel: UpdateViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateViewModel = ViewModelProviders.of(this).get(UpdateViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_update,container,false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val rangeButton = root.findViewById<Button>(R.id.rangeConfirm)

        rangeButton.setOnClickListener {
            val range = editRange.text.toString()
            val manager: InputMethodManager? = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            manager?.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )

            if (range == "") {
                Toast.makeText(root.context,"500에서 2000의 숫자를 입력하세요",Toast.LENGTH_LONG).show()
            }
            else if ( range.toInt() < 500 || range.toInt() > 2000 ) {
               Toast.makeText(root.context,"500에서 2000의 숫자만 입력할 수 있습니다",Toast.LENGTH_LONG).show()
            }
            else {
                sharedViewModel.setRange(range.toInt())
                Toast.makeText(root.context,"성공적으로 수정되었습니다",Toast.LENGTH_LONG).show()
            }
        }
        return root
    }
}