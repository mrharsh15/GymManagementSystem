package com.codewithme.primefitness.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codewithme.primefitness.databinding.FragmentImportExportDatabaseBinding
import com.codewithme.primefitness.global.DB
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel


class FragmentImportExportDatabase : Fragment() {
    private lateinit var binding: FragmentImportExportDatabaseBinding
    private var db: DB?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImportExportDatabaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Import Export Database"
        db = activity?.let { DB(it) }

        binding.btnImportDatabase.setOnClickListener {
            try {
                import()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.btnExportDatabase.setOnClickListener {
            try {
                export()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun showToast(value: String){
        Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
    }

    fun import(): Boolean {
        try {
            val sd: File? = context?.getExternalFilesDir(null)
            val backupDBPath = "PrimeFitness.DB"
            val currentDB: File? = context?.getDatabasePath("PrimeFitness.DB")
            val backupDB = File(sd, backupDBPath)
            if (backupDB.exists()) {
                val src: FileChannel = FileInputStream(backupDB).channel
                val dst: FileChannel = FileOutputStream(currentDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                println("Import successful")
                showToast("Import Successful!")
                return true
            } else {
                println("Can't find currentDBPath")
            }
        } catch (e: java.lang.Exception) {
            Log.w("Settings Backup", e)
        }
        return false
    }

    fun export(): Boolean {
        try {
            val sd = context?.getExternalFilesDir(null)
            val backupDBPath = "PrimeFitness.DB"
            val currentDB = context?.getDatabasePath("PrimeFitness.DB")
            val backupDB = File(sd, backupDBPath)
            if (currentDB?.exists() == true) {
                val src = FileInputStream(currentDB).channel
                val dst = FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                println("Export successful")
                showToast("Export successful!")
                return true
            } else {
                println("Can't find currentDBPath")
            }
        } catch (e: java.lang.Exception) {
            Log.w("Settings Backup", e)
        }
        return false
    }

//    @RequiresApi(Build.VERSION_CODES.Q)
//    private fun export() {
//        val sourcePath =
//            context?.getDatabasePath("PrimeFitness.DB")?.absolutePath
//        val source = File(sourcePath)
//        val destinationPath =
//            Environment.getExternalStorageDirectory().absolutePath + "/PrimeFitness.DB"
//        val destination = File(destinationPath)
//        val fis = FileInputStream(source)
//        val fos = FileOutputStream(destination)
//        try {
//            FileUtils.copy(fis, fos)
//            showToast("Export done!")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

//    @SuppressLint("SdCardPath")
//    private fun exportDB() {
////        showToast(context?.getDatabasePath("PrimeFitness.DB").toString())
////        showToast(Environment.getExternalStorageDirectory().toString())
////        val DatabaseName = "PrimeFitness.DB"
//        val sd = Environment.getExternalStorageDirectory()
////        val data = context?.getDatabasePath("PrimeFitness.DB")
//        var source: FileChannel? = null
//        var destination: FileChannel? = null
////        val currentDBPath = "PrimeFitness.DB"
////        val currentDBPath = context?.getDatabasePath("PrimeFitness.DB").toString()
//        val backupDBPath = "BackupPrimeFitness.DB"
//        val currentDB = File(context?.getDatabasePath("PrimeFitness.DB").toString())
//        val backupDB = File(sd, backupDBPath)
//        try {
//
//            val file = File(
//                Environment.getExternalStorageDirectory().toString() + "/BackupPrimeFitness.DB"
//            )
////            file.parentFile.mkdirs() // Will create parent directories if not exists
//            file.createNewFile()
//            val s = FileOutputStream(file, false)
//
//            source = FileInputStream(currentDB).channel
//            destination = FileOutputStream(backupDB).channel
//            destination.transferFrom(source, 0, source.size())
//            source.close()
//            destination.close()
//            showToast("Your Database is Exported !!")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    @SuppressLint("SdCardPath")
//    private fun importDB() {
//        val dir = Environment.getExternalStorageDirectory().absolutePath
//        val sd = File(dir)
//        val data = Environment.getDataDirectory()
//        var source: FileChannel? = null
//        var destination: FileChannel? = null
//        val backupDBPath = "/data/data/com.example.primefitness/databases/BackupPrimeFitness.DB"
//        val currentDBPath = "PrimeFitness.DB"
//        val currentDB = File(sd, currentDBPath)
//        val backupDB = File(data, backupDBPath)
//        try {
//            source = FileInputStream(currentDB).channel
//            destination = FileOutputStream(backupDB).channel
//            destination.transferFrom(source, 0, source.size())
//            source.close()
//            destination.close()
//            showToast("Your Database is Imported !!")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }


//    class ImportExportDB : Activity(){
//        fun createDirectory(){
//            val direct =
//                File(Environment.getExternalStorageDirectory().toString() + "/Exam Creator")
//
//            if (!direct.exists()) {
//                if (direct.mkdir()) {
//                    //directory is created;
//                }
//            }
//        }
//        @SuppressLint("SdCardPath")
//        fun importDB(){
//            createDirectory()
//            try {
//                val sd = Environment.getExternalStorageDirectory()
//                val data = Environment.getDataDirectory()
//                if (sd.canWrite()) {
//                    val  currentDBPath = "/data/" + "com.example.primefitness" + "/databases/" + "PrimeFitness.DB";
//                    val backupDBPath  = "/PrimeFitness/PrimeFitness.DB";
//                    val  backupDB = File(data, currentDBPath)
//                    val currentDB  = File(sd, backupDBPath)
//                    val src : FileChannel = FileInputStream(currentDB).channel
//                    val dst : FileChannel = FileOutputStream(backupDB).channel
//                    dst.transferFrom(src, 0, src.size());
//                    src.close()
//                    dst.close()
//                    Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
//                }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            }
//        }
//
//        @SuppressLint("SdCardPath")
//        fun exportDB(){
//            createDirectory()
//            try {
//                val sd = Environment.getExternalStorageDirectory()
//                val data = Environment.getDataDirectory()
//                if (sd.canWrite()) {
//                    val currentDBPath = ("/data/" + "com.example.primefitness" + "/databases/" + "PrimeFitness.DB")
//                    val backupDBPath = "/PrimeFitness/PrimeFitness.DB"
//                    val currentDB = File(data, currentDBPath)
//                    val backupDB = File(sd, backupDBPath)
//                    val src = FileInputStream(currentDB).channel
//                    val dst = FileOutputStream(backupDB).channel
//                    dst.transferFrom(src, 0, src.size())
//                    src.close()
//                    dst.close()
//                    Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
//                }
//            } catch (e: Exception) {
//                Toast.makeText(baseContext, e.toString(), Toast.LENGTH_LONG)
//                    .show()
//            }
//        }
//    }
}