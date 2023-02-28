package com.codewithme.primefitness.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.codewithme.primefitness.databinding.FragmentAppUpdateFeeBinding
import com.codewithme.primefitness.global.DB
import com.codewithme.primefitness.global.MyFunction
import java.lang.Exception


class FragmentAppUpdateFee : Fragment() {

    private lateinit var binding: FragmentAppUpdateFeeBinding
    var db : DB?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppUpdateFeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Fee"
        db = activity?.let { DB(it) }
        binding.btnAddMemberShip.setOnClickListener {
            if(validate()){
                saveData()
            }
        }
        fillData()
    }

    private fun validate():Boolean{
        if(binding.edtOneMonth.text.toString().isEmpty()){
            showToast("Enter One Month Fees")
        } else if(binding.edtThreeMonth.text.toString().isEmpty()){
            showToast("Enter Three Month Fees")
        }
        return true
    }

    private fun saveData(){

        try{
            val sqlQuery = "INSERT OR REPLACE INTO FEE(ID, ONE_MONTH, THREE_MONTH) VALUES ('1', '"+binding.edtOneMonth.text.toString().trim()+"', '"+binding.edtThreeMonth.text.toString().trim()+"')"
            db?.executeQuery(sqlQuery)
            showToast("Membership Data Saved Successfully")
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun fillData(){
        try {
            val sqlQuery = "SELECT * FROM FEE WHERE ID = '1'"
            db?.fireQuery(sqlQuery)?.use {
                if(it.count>0){
                    it.moveToFirst()
                    binding.edtOneMonth.setText(MyFunction.getvalue(it, "ONE_MONTH"))
                    binding.edtThreeMonth.setText(MyFunction.getvalue(it, "THREE_MONTH"))
                }
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun showToast(value:String){
        Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
    }
}