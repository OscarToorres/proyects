package com.git.alquilalotodo.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.git.alquilalotodo.R
import com.git.alquilalotodo.databinding.ActivityMainBinding
import com.git.alquilalotodo.databinding.FragmentFavoritesBinding
import com.git.alquilalotodo.databinding.FragmentUnregisteredBinding
import com.git.alquilalotodo.ui.activities.GetStartedActivity

class UnregisteredFragment : Fragment() {

    private var _binding : FragmentUnregisteredBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_unregistered, container, false)
        _binding = FragmentUnregisteredBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.unregisteredBtn.setOnClickListener{
            startActivity(Intent(requireActivity(),GetStartedActivity::class.java))
            requireActivity().finish()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UnregisteredFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}