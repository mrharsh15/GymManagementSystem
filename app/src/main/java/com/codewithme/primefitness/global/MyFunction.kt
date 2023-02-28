package com.codewithme.primefitness.global

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MyFunction {

    companion object{
        fun getvalue(cursor:Cursor, columnName: String):String{
            var value:String = ""
            try {
                val col = cursor.getColumnIndex(columnName)
                value = cursor.getString(col)
            } catch (e:Exception){
                e.printStackTrace()
                Log.d("MyFunction", "getValue ${e.printStackTrace()}")
                value = ""
            }
            return value
        }

        @SuppressLint("SimpleDateFormat")
        fun returnSQLdateFormat(date:String):String{
            try {
                if(date.trim().isNotEmpty()){
                    val dateFormat1 = SimpleDateFormat("dd/MM/yyyy")
                    val firstDate = dateFormat1.parse(date)
                    val dateFormat2 = SimpleDateFormat("yyyy-MM-dd")
                    return dateFormat2.format(firstDate)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            return ""
        }

        @SuppressLint("SimpleDateFormat")
        fun returnUserdateFormat(date:String):String{
            try {
                if(date.trim().isNotEmpty()){
                    val dateFormat1 = SimpleDateFormat("yyyy-MM-dd")
                    val firstDate = dateFormat1.parse(date)
                    val dateFormat2 = SimpleDateFormat("dd/MM/yyyy")
                    return dateFormat2.format(firstDate)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            return ""
        }

        fun getCurrentDate():String{
            var txtDate = ""
            try {
                txtDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            } catch (e:Exception){
                e.printStackTrace()
            }
            return txtDate
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getThreeDaysAfterDate():String{
            var requiredDate = ""
            var txtDate = ""
            try {
//                var format = DateFormat("yyyy-MM-dd")
                txtDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                requiredDate = LocalDate.parse(txtDate).plusDays(3).toString()
            } catch (e:Exception){
                e.printStackTrace()
            }
            return requiredDate
        }
    }
}
