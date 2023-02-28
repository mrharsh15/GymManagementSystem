package com.codewithme.primefitness.fragment

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codewithme.primefitness.R
import com.codewithme.primefitness.databinding.FragmentFeePendingBinding
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

class FragmentFeePending : BaseFragment() {

    private lateinit var binding: FragmentFeePendingBinding
    private var db: DB?=null
    var adapter: AdapterLoadMember?=null
    var arrayList:ArrayList<AllMember> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeePendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Fee Pending"
        db = activity?.let { DB(it) }

        binding.edtPendingSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                myFilter(s.toString())
            }


        })

    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        loadData()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(){
//        createNotificationChannel()
        lifecycleScope.executeAsyncTask(onPreExecute = {
            showDialog("Processing....")
        }, doInBackground = {
            arrayList.clear()
            val sqlQuery = "SELECT * FROM MEMBER WHERE STATUS='A' AND EXPIRE_ON<='"+ MyFunction.getThreeDaysAfterDate()+"' ORDER BY FIRST_NAME"  //three days before notification
            db?.fireQuery(sqlQuery)?.use {
                if(it.count>0){
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



                        //Notification
//                        val builder = activity?.let { it1 ->
//                            NotificationCompat.Builder(it1, R.string.channel_id.toString())
//                                .setSmallIcon(R.drawable.boy)
//                                .setContentTitle("Fee Pending")
//                                .setContentText(MyFunction.getvalue(it, "FIRST_NAME") + " " +MyFunction.getvalue(it, "LAST_NAME"))
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        }
//                        var notificationId = 1234
//                        with(activity?.let { it1 -> NotificationManagerCompat.from(it1) }) {
//                            // notificationId is a unique int for each notification that you must define
//                            builder?.let { it1 -> this?.notify(notificationId, it1.build()) }
//                        }
//                        notificationId += 1




                    } while (it.moveToNext())

                }
            }
        }, onPostExecute = {
            if(arrayList.size>0){
                binding.recyclerViewPending.visibility = View.VISIBLE
                binding.txtPendingNDF.visibility = View.GONE
                adapter = AdapterLoadMember(arrayList)
                binding.recyclerViewPending.layoutManager = LinearLayoutManager(activity)
                binding.recyclerViewPending.adapter = adapter


                adapter?.onClick {
                    loadFragment(it)
                }

            } else {
                binding.recyclerViewPending.visibility = View.GONE
                binding.txtPendingNDF.visibility = View.VISIBLE
            }
            CloseDialog()
        })
    }

    private fun loadFragment(id:String){
        val fragment = FragmentAddMember()
        val args = Bundle()
        args.putString("ID", id)
        fragment.arguments = args
        val fragmentManager: FragmentManager?= fragmentManager
        fragmentManager!!.beginTransaction().replace(R.id.frame_container, fragment, "FragmentAdd").commit()
    }

    private fun myFilter(searchValue:String){
        val temp:ArrayList<AllMember> = ArrayList()
        if(arrayList.size>0){
            for(list in arrayList){
                if(list.firstName.toLowerCase(Locale.ROOT).contains(searchValue.toLowerCase(Locale.ROOT)) || list.lastName.toLowerCase(Locale.ROOT).contains(searchValue.toLowerCase(Locale.ROOT))){
                    temp.add(list)
                }
            }
        }
        adapter?.updateList(temp)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(R.string.channel_id.toString(), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager //context. or activity.
            notificationManager.createNotificationChannel(channel)
        }
    }

}