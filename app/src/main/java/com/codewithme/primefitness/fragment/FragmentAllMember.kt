package com.codewithme.primefitness.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithme.primefitness.R
import com.codewithme.primefitness.databinding.FragmentAllMemberBinding
import com.codewithme.primefitness.adapter.AdapterLoadMember
import com.codewithme.primefitness.global.DB
import com.codewithme.primefitness.global.MyFunction
import com.codewithme.primefitness.model.AllMember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class FragmentAllMember : BaseFragment() {
    private val TAG = "FragmentAllMember"
    var db: DB?=null
    var adapter: AdapterLoadMember?=null
    var arrayList:ArrayList<AllMember> = ArrayList()
    private lateinit var binding: FragmentAllMemberBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Dashboard"
        db = activity?.let { DB(it) }

        binding.rdGroupMember.setOnCheckedChangeListener { radioGroup, id ->
            when(id){
                R.id.rdActiveMember -> {
                    loadData("A")
                }
                R.id.rdInActiveMember -> {
                    loadData("D")
                }
            }
        }

        binding.imgAddMember.setOnClickListener {
            loadFragment("")
        }

        binding.edtAllMemberSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                myFilter(s.toString())
            }


        })
    }

    override fun onResume() {
        super.onResume()
        loadData("A")
    }

    fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result = withContext(Dispatchers.IO){
            doInBackground()
        }
        onPostExecute(result)
    }

    @SuppressLint("SetTextI18n")
    private fun loadData(memberStatus:String){
        lifecycleScope.executeAsyncTask(onPreExecute = {
            showDialog("Processing....")
        }, doInBackground = {
            arrayList.clear()
            val sqlQuery = "SELECT * FROM MEMBER WHERE STATUS='$memberStatus'"
            db?.fireQuery(sqlQuery)?.use {
                if(it.count>0){
                    binding.memberCount.text = "Total Members: ${it.count}"
                  it.moveToFirst()
                    do {
                        val list = AllMember(id = MyFunction.getvalue(it, "ID"),
                            firstName = MyFunction.getvalue(it, "FIRST_NAME"),
                            lastName = MyFunction.getvalue(it, "LAST_NAME"),
                            age = MyFunction.getvalue(it, "AGE"),
                            gender = MyFunction.getvalue(it, "GENDER"),
                            weight = MyFunction.getvalue(it, "WEIGHT"),
                            mobile = MyFunction.getvalue(it, "MOBILE"),
                            address = MyFunction.getvalue(it, "ADDRESS"),
                            image = MyFunction.getvalue(it, "IMAGE_PATH"),
                            dateOfJoining = MyFunction.returnUserdateFormat(MyFunction.getvalue(it, "DATE_OF_JOINING")),
                            expiryDate = MyFunction.returnUserdateFormat(MyFunction.getvalue(it, "EXPIRE_ON")),
                            description = MyFunction.getvalue(it, "DESCRIPTION"))

                        arrayList.add(list)
                    } while (it.moveToNext())

                }
            }
        }, onPostExecute = {
            if(arrayList.size>0){
                binding.recyclerViewMember.visibility = View.VISIBLE
                binding.txtAllMemberNDF.visibility = View.GONE
                adapter = AdapterLoadMember(arrayList)
                binding.recyclerViewMember.layoutManager = LinearLayoutManager(activity)
                binding.recyclerViewMember.adapter = adapter


                adapter?.onClick {
                    loadFragment(it)
                }

            } else {
                binding.recyclerViewMember.visibility = View.GONE
                binding.txtAllMemberNDF.visibility = View.VISIBLE
            }
            CloseDialog()
        })
    }

    private fun loadFragment(id:String){
        val fragment = FragmentAddMember()
        val args = Bundle()
        args.putString("ID", id)
        fragment.arguments = args
        val fragmentManager:FragmentManager?= fragmentManager
        fragmentManager!!.beginTransaction().replace(R.id.frame_container, fragment, "FragmentAdd").commit()
    }

    private fun myFilter(searchValue:String){
        val temp:ArrayList<AllMember> = ArrayList()
        if(arrayList.size>0){
            for(list in arrayList){
                if(list.firstName.toLowerCase(Locale.ROOT).contains(searchValue.toLowerCase(Locale.ROOT)) || list.lastName.toLowerCase(
                        Locale.ROOT).contains(searchValue.toLowerCase(Locale.ROOT))){
                    temp.add(list)
                }
            }
        }
        adapter?.updateList(temp)
    }


}