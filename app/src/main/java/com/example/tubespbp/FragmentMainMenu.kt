package com.example.tubespbp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tubespbp.databinding.FragmentMainmenuBinding

class FragmentMainMenu : Fragment(R.layout.fragment_mainmenu) {
    private var _binding : FragmentMainmenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {

        _binding = FragmentMainmenuBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.logout.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(rootView.context)
            builder.setMessage("Do you want to exit?")
            builder.setPositiveButton("OK", object:DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    val intent = Intent ( requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            })
                .setNegativeButton("NO",DialogInterface.OnClickListener{
                    dialog, id -> dialog.cancel()
                })
            builder.show()

        }
        binding.readbtn.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.layout_fragment, FragmentNews())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.scanbtn.setOnClickListener{
            val intent = Intent(getActivity(), QRActivity::class.java)
            startActivity(intent)
        }
        binding.rsbtn.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.layout_fragment, FragmentRS())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.vaccinebtn.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.layout_fragment, FragmentVaccination())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return rootView
    }

}