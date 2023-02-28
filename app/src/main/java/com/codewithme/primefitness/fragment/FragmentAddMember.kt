package com.codewithme.primefitness.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codewithme.primefitness.R
import com.codewithme.primefitness.databinding.FragmentAddMemberBinding
import com.codewithme.primefitness.databinding.RenewDialogBinding
import com.codewithme.primefitness.global.CaptureImage
import com.codewithme.primefitness.global.DB
import com.codewithme.primefitness.global.MyFunction
import com.github.florent37.runtimepermission.RuntimePermission
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FragmentAddMember : Fragment() {
    var db: DB?=null
    private lateinit var binding: FragmentAddMemberBinding
    private lateinit var bindingDialog: RenewDialogBinding
    private var captureImage: CaptureImage?=null
    private var captureImageID1: CaptureImage?=null
    private var captureImageID2: CaptureImage?=null
//    private val REQUEST_CAMERA = 1234
//    private val REQUEST_GALLERY = 5464
    private var actualImagePath = ""
    private var actualImagePathID1 = ""
    private var actualImagePathID2 = ""
    private var gender = "Male"
    private var ID = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Add New Member"
        db = activity?.let { DB(it) }
        captureImage = CaptureImage(activity)
        captureImageID1 = CaptureImage(activity)
        captureImageID2 = CaptureImage(activity)

        ID = arguments!!.getString("ID").toString()

        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener{ view1, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.edtJoining.setText(sdf.format(cal.time))

        }

        val cal2 = Calendar.getInstance()
        val dateSetListener2 = DatePickerDialog.OnDateSetListener{ view2, year2, monthOfYear2, dayOfMonth2 ->
            cal2.set(Calendar.YEAR, year2)
            cal2.set(Calendar.MONTH, monthOfYear2)
            cal2.set(Calendar.DAY_OF_MONTH, dayOfMonth2)

            val myFormat2 = "dd/MM/yyyy"
            val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
            binding.edtExpire.setText(sdf2.format(cal2.time))
//            binding.edtExpire.setText(sdf.format(cal.time))

        }



//        binding.spMembership.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val value = binding.spMembership.selectedItem.toString().trim()
//
//                if(value == "Select"){
//                    binding.edtExpire.setText("")
//                    calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
//                }else{
//                    if (binding.edtJoining.text.toString().trim().isNotEmpty()){
//                        if(value == "1 Month"){
//                            calculateExpireDate(1, binding.edtJoining, binding.edtExpire)
//                            calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
//                        } else if (value == "3 Month"){
//                            calculateExpireDate(3, binding.edtJoining, binding.edtExpire)
//                            calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
//                        }
//
//                    }else{
//                        showToast("Select Joining Date first!")
//                        binding.spMembership.setSelection(0)
//                    }
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//        }

//        binding.edtDiscount.addTextChangedListener(object: TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if(s!=null){
//                    calculateTotal(binding.spMembership, binding.edtDiscount, binding.edtAmount)
//                }
//            }
//        })

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, id ->
            when(id){
                R.id.rdMale -> {
                    gender = "Male"
                }
                R.id.rdFemale -> {
                    gender = "Female"
                }
            }
        }


        binding.btnAddMemberSave.setOnClickListener {

//            File("/data/data/com.example.primefitness/databases/PrimeFitness.DB").copyTo(File("/mnt/sdcard/Download/PrimeFitness.DB"))

            if(validate()){
                saveData()
            }
        }

        binding.imgPicDate.setOnClickListener {
            activity?.let { it1 -> DatePickerDialog(
                it1, dateSetListener, cal.get(Calendar.YEAR), cal.get(
                    Calendar.MONTH
                ), cal.get(Calendar.DAY_OF_MONTH)
            ).show() }
        }

        binding.imgPicDateExpiry.setOnClickListener {
            activity?.let { it2 -> DatePickerDialog(
                it2, dateSetListener2, cal2.get(Calendar.YEAR), cal2.get(
                    Calendar.MONTH
                ), cal2.get(Calendar.DAY_OF_MONTH)
            ).show() }
        }

        binding.imgTakeImage.setOnClickListener {
//            getImage()
            selectImage()
        }

        binding.imgTakeIDImage1.setOnClickListener {
//            getImageID1()
            selectImageID1()
        }

        binding.imgTakeIDImage2.setOnClickListener {
//            getImageID2()
            selectImageID2()
        }
//        getFee()

        binding.btnActiveInactive.setOnClickListener {
            try {
                if(getStatus() == "A"){
                    val sqlQuery = "UPDATE MEMBER SET STATUS='D' WHERE ID='$ID'"
                    db?.fireQuery(sqlQuery)
                    showToast("Member is Inactive now")

                } else {
                    val sqlQuery = "UPDATE MEMBER SET STATUS='A' WHERE ID='$ID'"
                    db?.fireQuery(sqlQuery)
                    showToast("Member is Active now")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        if(ID.trim().isNotEmpty()){
            if(getStatus() == "A"){
                binding.btnActiveInactive.text = "Inactive"
                binding.btnActiveInactive.visibility = View.VISIBLE
            } else {
                binding.btnActiveInactive.text = "Active"
                binding.btnActiveInactive.visibility = View.VISIBLE
            }
            loadData()
        } else {
            binding.btnActiveInactive.visibility = View.GONE
        }

//        binding.btnRenewalSave.setOnClickListener { //change nahi kiya hai renew vala
//            if(ID.trim().isNotEmpty()){
//                openRenewableDialog()
//            }
//        }

        binding.btnDelete.setOnClickListener {
            if(ID.trim().isNotEmpty()){
                deleteEntry()
            }
        }

    }

    private fun selectImage(){
        val items:Array<CharSequence>
        try {
            items = arrayOf("Take Photo", "Choose Image", "Cancel")
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setCancelable(false)
            builder.setTitle("Select Image")
            builder.setItems(items) { dialoginterface, i ->
                if (items[i] == "Take Photo"){
                    RuntimePermission.askPermission(this)
                        .request(android.Manifest.permission.CAMERA)
                        .onAccepted {
                            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(takePicture, 0) //zero can be replaced with any action code (called requestCode)
                        }
                        .onDenied {
                            android.app.AlertDialog.Builder(activity)
                                .setMessage("Please accept the permission to capture!")
                                .setPositiveButton("Yes") { dialoginterface, i ->
                                    it.askAgain()
                                }
                                .setNegativeButton("No") { dialoginterface, i ->
                                    dialoginterface.dismiss()
                                }
                                .show()
                        }
                        .ask()
                }else if(items[i] == "Choose Image"){

                    RuntimePermission.askPermission(this)
                        .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onAccepted {
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(pickPhoto, 1) //one can be replaced with any action code

                        }
                        .onDenied {
                            android.app.AlertDialog.Builder(activity)
                                .setMessage("Please accept the permission to select!")
                                .setPositiveButton("Yes") { dialoginterface, i ->
                                    it.askAgain()
                                }
                                .setNegativeButton("No") { dialoginterface, i ->
                                    dialoginterface.dismiss()
                                }
                                .show()
                        }
                        .ask()
                }else{
                    dialoginterface.dismiss()
                }
            }
            builder.show()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun selectImageID1(){
        val items:Array<CharSequence>
        try {
            items = arrayOf("Take Photo", "Choose Image", "Cancel")
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setCancelable(false)
            builder.setTitle("Select Image")
            builder.setItems(items) { dialoginterface, i ->
                if (items[i] == "Take Photo"){
                    RuntimePermission.askPermission(this)
                        .request(android.Manifest.permission.CAMERA)
                        .onAccepted {
                            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(takePicture, 2) //zero can be replaced with any action code (called requestCode)
                        }
                        .onDenied {
                            android.app.AlertDialog.Builder(activity)
                                .setMessage("Please accept the permission to capture!")
                                .setPositiveButton("Yes") { dialoginterface, i ->
                                    it.askAgain()
                                }
                                .setNegativeButton("No") { dialoginterface, i ->
                                    dialoginterface.dismiss()
                                }
                                .show()
                        }
                        .ask()
                }else if(items[i] == "Choose Image"){

                    RuntimePermission.askPermission(this)
                        .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onAccepted {
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(pickPhoto, 3) //one can be replaced with any action code

                        }
                        .onDenied {
                            android.app.AlertDialog.Builder(activity)
                                .setMessage("Please accept the permission to select!")
                                .setPositiveButton("Yes") { dialoginterface, i ->
                                    it.askAgain()
                                }
                                .setNegativeButton("No") { dialoginterface, i ->
                                    dialoginterface.dismiss()
                                }
                                .show()
                        }
                        .ask()
                }else{
                    dialoginterface.dismiss()
                }
            }
            builder.show()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun selectImageID2(){
        val items:Array<CharSequence>
        try {
            items = arrayOf("Take Photo", "Choose Image", "Cancel")
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setCancelable(false)
            builder.setTitle("Select Image")
            builder.setItems(items) { dialoginterface, i ->
                if (items[i] == "Take Photo"){
                    RuntimePermission.askPermission(this)
                        .request(android.Manifest.permission.CAMERA)
                        .onAccepted {
                            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(takePicture, 4) //zero can be replaced with any action code (called requestCode)
                        }
                        .onDenied {
                            android.app.AlertDialog.Builder(activity)
                                .setMessage("Please accept the permission to capture!")
                                .setPositiveButton("Yes") { dialoginterface, i ->
                                    it.askAgain()
                                }
                                .setNegativeButton("No") { dialoginterface, i ->
                                    dialoginterface.dismiss()
                                }
                                .show()
                        }
                        .ask()
                }else if(items[i] == "Choose Image"){

                    RuntimePermission.askPermission(this)
                        .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onAccepted {
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(pickPhoto, 5) //one can be replaced with any action code

                        }
                        .onDenied {
                            android.app.AlertDialog.Builder(activity)
                                .setMessage("Please accept the permission to select!")
                                .setPositiveButton("Yes") { dialoginterface, i ->
                                    it.askAgain()
                                }
                                .setNegativeButton("No") { dialoginterface, i ->
                                    dialoginterface.dismiss()
                                }
                                .show()
                        }
                        .ask()
                }else{
                    dialoginterface.dismiss()
                }
            }
            builder.show()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    //        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
//            captureImage(captureImage?.getRightAngleImage(captureImage?.imagePath).toString())
//            captureImageID1(captureImageID1?.getRightAngleImage(captureImageID1?.imagePath).toString())
//            captureImageID2(captureImageID2?.getRightAngleImage(captureImageID2?.imagePath).toString())
//        } else if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
//            captureImage(captureImage?.getRightAngleImage(captureImage?.getPath(data?.data, context)).toString())
//            captureImageID1(captureImageID1?.getRightAngleImage(captureImageID1?.getPath(data?.data, context)).toString())
//            captureImageID2(captureImageID2?.getRightAngleImage(captureImageID2?.getPath(data?.data, context)).toString())
//        }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                binding.imgPic.setImageURI(selectedImage)
                captureImage(captureImage?.getRightAngleImage(captureImage?.imagePath).toString())
            }
            1 -> if (resultCode == RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                binding.imgPic.setImageURI(selectedImage)
                captureImage(
                    captureImage?.getRightAngleImage(
                        captureImage?.getPath(
                            imageReturnedIntent?.data,
                            context
                        )
                    ).toString()
                )
            }
            2 -> if (resultCode == RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                binding.imgIDPic1.setImageURI(selectedImage)
                captureImageID1(
                    captureImageID1?.getRightAngleImage(captureImageID1?.imagePath).toString()
                )
            }
            3 -> if (resultCode == RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                binding.imgIDPic1.setImageURI(selectedImage)
                captureImageID1(
                    captureImageID1?.getRightAngleImage(
                        captureImageID1?.getPath(
                            imageReturnedIntent?.data,
                            context
                        )
                    ).toString()
                )
            }
            4 -> if (resultCode == RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                binding.imgIDPic2.setImageURI(selectedImage)
                captureImageID2(
                    captureImageID2?.getRightAngleImage(captureImageID2?.imagePath).toString()
                )
            }
            5 -> if (resultCode == RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                binding.imgIDPic2.setImageURI(selectedImage)
                captureImageID2(
                    captureImageID2?.getRightAngleImage(
                        captureImageID2?.getPath(
                            imageReturnedIntent?.data,
                            context
                        )
                    ).toString()
                )
            }
        }
    }

    private fun getStatus():String{
        var status = ""
        try {
            val sqlQuery = "SELECT STATUS FROM MEMBER WHERE ID='$ID'"
            db?.fireQuery(sqlQuery)?.use {
                if(it.count>0){
                    status = MyFunction.getvalue(it, "STATUS")
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
        return status
    }

//    private fun calculateTotal(spMember: Spinner, edtDis:EditText, edtAmt:EditText){
//        val month = spMember.selectedItem.toString().trim()
//        var discount = edtDis.text.toString().trim()
//        if(edtDis.text.toString().isEmpty()){
//            discount = "0"
//        }
//
//        if(month == "Select"){
//            edtAmt.setText("")
//        }else if(month=="1 Month"){
//            if(discount.trim().isEmpty()){
//                discount="0"
//            }
//            if(oneMonth!!.trim().isNotEmpty()){
//                val discountAmount = ((oneMonth!!.toDouble() * discount.toDouble())/100)
//                val total = oneMonth!!.toDouble() - discountAmount
//                edtAmt.setText(String.format("%.2f", total))
//            }
//        }else if(month=="3 Month"){
//            if(discount.trim().isEmpty()){
//                discount="0"
//            }
//            if(threeMonth!!.trim().isNotEmpty()){
//                val discountAmount = ((threeMonth!!.toDouble() * discount.toDouble())/100)
//                val total = threeMonth!!.toDouble() - discountAmount
//                edtAmt.setText(String.format("%.2f", total))
//            }
//        }
//
//    }

//    private fun calculateTotal(edtAmount:EditText){
//        val month = spMember.selectedItem.toString().trim()
//        var discount = edtDis.text.toString().trim()
//        if(edtDis.text.toString().isEmpty()){
//            discount = "0"
//        }

//        if(month == "Select"){
//            edtAmt.setText("")
//        }else if(month=="1 Month"){
//            if(discount.trim().isEmpty()){
//                discount="0"
//            }
//            if(oneMonth!!.trim().isNotEmpty()){
//                val discountAmount = ((oneMonth!!.toDouble() * discount.toDouble())/100)
//                val total = oneMonth!!.toDouble() - discountAmount
//                edtAmt.setText(String.format("%.2f", total))
//            }
//        }else if(month=="3 Month"){
//            if(discount.trim().isEmpty()){
//                discount="0"
//            }
//            if(threeMonth!!.trim().isNotEmpty()){
//                val discountAmount = ((threeMonth!!.toDouble() * discount.toDouble())/100)
//                val total = threeMonth!!.toDouble() - discountAmount
//                edtAmt.setText(String.format("%.2f", total))
//            }
//        }

//        if(edtAmount.text.toString().isEmpty()){
//            edtAmount.setText(String.format("%.2f", 0))
//        }
//        else{
//            edtAmount.setText(String.format("%.2f", edtAmount.text.toString().toDouble()))
//            edtAmount.text.toString().toDouble()
//        }
//    }

//    private fun getFee(){
//        try {
//            val sqlQuery = "SELECT * FROM FEE WHERE ID = '1'"
//            db?.fireQuery(sqlQuery)?.use {
//                if(it.count>0){
//                oneMonth = MyFunction.getvalue(it, "ONE_MONTH")
//                threeMonth = MyFunction.getvalue(it, "THREE_MONTH")}
//            }
//        } catch (e:Exception){
//            e.printStackTrace()
//        }
//    }

//    @SuppressLint("SimpleDateFormat")
//    private fun calculateExpireDate(month:Int, edtJoining:EditText,edtExpiry: EditText){
//        val dtStart = edtJoining.text.toString().trim()
//        if(dtStart.isNotEmpty()){
//            val format = SimpleDateFormat("dd/MM/yyyy")
//            val date1 = format.parse(dtStart)
//            val cal = Calendar.getInstance()
//            cal.time = date1
//            cal.add(Calendar.MONTH, month)
//
//            val myFormat = "dd/MM/yyyy"
//            val sdf = SimpleDateFormat(myFormat, Locale.US)
//            edtExpiry.setText(sdf.format(cal.time))
//        }
//    }

    private fun showToast(value: String){
        Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
    }

//    private fun getImage(){
//        val items:Array<CharSequence>
//        try {
//            items = arrayOf("Take Photo", "Choose Image", "Cancel")
//            val builder = android.app.AlertDialog.Builder(activity)
//            builder.setCancelable(false)
//            builder.setTitle("Select Image")
//            builder.setItems(items) { dialoginterface, i ->
//                if (items[i] == "Take Photo"){
//                    RuntimePermission.askPermission(this)
//                        .request(android.Manifest.permission.CAMERA)
//                        .onAccepted {
//                            takePicture()
//                        }
//                        .onDenied {
//                            android.app.AlertDialog.Builder(activity)
//                                .setMessage("Please accept the permission to capture!")
//                                .setPositiveButton("Yes") { dialoginterface, i ->
//                                    it.askAgain()
//                                }
//                                .setNegativeButton("No") { dialoginterface, i ->
//                                    dialoginterface.dismiss()
//                                }
//                                .show()
//                        }
//                        .ask()
//                }else if(items[i] == "Choose Image"){
//
//                    RuntimePermission.askPermission(this)
//                        .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .onAccepted {
//                            takeFromGallery()
//                        }
//                        .onDenied {
//                            android.app.AlertDialog.Builder(activity)
//                                .setMessage("Please accept the permission to select!")
//                                .setPositiveButton("Yes") { dialoginterface, i ->
//                                    it.askAgain()
//                                }
//                                .setNegativeButton("No") { dialoginterface, i ->
//                                    dialoginterface.dismiss()
//                                }
//                                .show()
//                        }
//                        .ask()
//                }else{
//                    dialoginterface.dismiss()
//                }
//            }
//            builder.show()
//        } catch (e: Exception){
//            e.printStackTrace()
//        }
//    }

//    private fun takePicture(){
//        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImage?.setImageUri())
//        takePicIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivityForResult(takePicIntent, REQUEST_CAMERA)
//    }
//
//    private fun takeFromGallery(){
//        val intent = Intent()
//        intent.type = "image/jpg"
//        intent.action = Intent.ACTION_PICK
//        startActivityForResult(intent, REQUEST_GALLERY)
//    }

//    private fun getImageID1(){
//        val items:Array<CharSequence>
//        try {
//            items = arrayOf("Take Photo", "Choose Image", "Cancel")
//            val builder = android.app.AlertDialog.Builder(activity)
//            builder.setCancelable(false)
//            builder.setTitle("Select Image")
//            builder.setItems(items) { dialoginterface, i ->
//                if (items[i] == "Take Photo"){
//                    RuntimePermission.askPermission(this)
//                        .request(android.Manifest.permission.CAMERA)
//                        .onAccepted {
//                            takePictureID1()
//                        }
//                        .onDenied {
//                            android.app.AlertDialog.Builder(activity)
//                                .setMessage("Please accept the permission to capture!")
//                                .setPositiveButton("Yes") { dialoginterface, i ->
//                                    it.askAgain()
//                                }
//                                .setNegativeButton("No") { dialoginterface, i ->
//                                    dialoginterface.dismiss()
//                                }
//                                .show()
//                        }
//                        .ask()
//                }else if(items[i] == "Choose Image"){
//
//                    RuntimePermission.askPermission(this)
//                        .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .onAccepted {
//                            takeFromGalleryID1()
//                        }
//                        .onDenied {
//                            android.app.AlertDialog.Builder(activity)
//                                .setMessage("Please accept the permission to select!")
//                                .setPositiveButton("Yes") { dialoginterface, i ->
//                                    it.askAgain()
//                                }
//                                .setNegativeButton("No") { dialoginterface, i ->
//                                    dialoginterface.dismiss()
//                                }
//                                .show()
//                        }
//                        .ask()
//                }else{
//                    dialoginterface.dismiss()
//                }
//            }
//            builder.show()
//        } catch (e: Exception){
//            e.printStackTrace()
//        }
//    }

//    private fun takePictureID1(){
//        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageID1?.setImageUri())
//        takePicIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivityForResult(takePicIntent, REQUEST_CAMERA)
//    }
//
//    private fun takeFromGalleryID1(){
//        val intent = Intent()
//        intent.type = "image/jpg"
//        intent.action = Intent.ACTION_PICK
//        startActivityForResult(intent, REQUEST_GALLERY)
//    }

//    private fun getImageID2(){
//        val items:Array<CharSequence>
//        try {
//            items = arrayOf("Take Photo", "Choose Image", "Cancel")
//            val builder = android.app.AlertDialog.Builder(activity)
//            builder.setCancelable(false)
//            builder.setTitle("Select Image")
//            builder.setItems(items) { dialoginterface, i ->
//                if (items[i] == "Take Photo"){
//                    RuntimePermission.askPermission(this)
//                        .request(android.Manifest.permission.CAMERA)
//                        .onAccepted {
//                            takePictureID2()
//                        }
//                        .onDenied {
//                            android.app.AlertDialog.Builder(activity)
//                                .setMessage("Please accept the permission to capture!")
//                                .setPositiveButton("Yes") { dialoginterface, i ->
//                                    it.askAgain()
//                                }
//                                .setNegativeButton("No") { dialoginterface, i ->
//                                    dialoginterface.dismiss()
//                                }
//                                .show()
//                        }
//                        .ask()
//                }else if(items[i] == "Choose Image"){
//
//                    RuntimePermission.askPermission(this)
//                        .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .onAccepted {
//                            takeFromGalleryID2()
//                        }
//                        .onDenied {
//                            android.app.AlertDialog.Builder(activity)
//                                .setMessage("Please accept the permission to select!")
//                                .setPositiveButton("Yes") { dialoginterface, i ->
//                                    it.askAgain()
//                                }
//                                .setNegativeButton("No") { dialoginterface, i ->
//                                    dialoginterface.dismiss()
//                                }
//                                .show()
//                        }
//                        .ask()
//                }else{
//                    dialoginterface.dismiss()
//                }
//            }
//            builder.show()
//        } catch (e: Exception){
//            e.printStackTrace()
//        }
//    }

//    private fun takePictureID2(){
//        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageID2?.setImageUri())
//        takePicIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivityForResult(takePicIntent, REQUEST_CAMERA)
//    }
//
//    private fun takeFromGalleryID2(){
//        val intent = Intent()
//        intent.type = "image/jpg"
//        intent.action = Intent.ACTION_PICK
//        startActivityForResult(intent, REQUEST_GALLERY)
//    }


//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
//            captureImage(captureImage?.getRightAngleImage(captureImage?.imagePath).toString())
//        } else if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
//            captureImage(captureImage?.getRightAngleImage(captureImage?.getPath(data?.data, context)).toString())
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
//            captureImage(captureImage?.getRightAngleImage(captureImage?.imagePath).toString())
//            captureImageID1(captureImageID1?.getRightAngleImage(captureImageID1?.imagePath).toString())
//            captureImageID2(captureImageID2?.getRightAngleImage(captureImageID2?.imagePath).toString())
//        } else if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
//            captureImage(captureImage?.getRightAngleImage(captureImage?.getPath(data?.data, context)).toString())
//            captureImageID1(captureImageID1?.getRightAngleImage(captureImageID1?.getPath(data?.data, context)).toString())
//            captureImageID2(captureImageID2?.getRightAngleImage(captureImageID2?.getPath(data?.data, context)).toString())
//        }
//    }


    private fun captureImage(path: String){
        Log.d("FragmentAdd", "imagePath : $path")
        getImagePath(captureImage?.decodeFile(path))
    }

    private fun getImagePath(bitmap: Bitmap?){
        val tempUri: Uri? = captureImage?.getImageUri(activity, bitmap)
        actualImagePath = captureImage?.getRealPathFromURI(tempUri, activity).toString()
        Log.d("FragmentAdd", "ActualImagePath : ${actualImagePath}")
        context?.let { Picasso.with(it).load(tempUri).into(binding.imgPic) }

//        activity?.let {
//            Glide.with(it)
//                .load(actualImagePath)
//                .into(binding.imgPic)
//        }
    }

    private fun captureImageID1(path: String){
        Log.d("FragmentAdd", "imagePath : $path")
        getImagePathID1(captureImageID1?.decodeFile(path))
    }

    private fun getImagePathID1(bitmap: Bitmap?){
        val tempUri: Uri? = captureImageID1?.getImageUri(activity, bitmap)
        actualImagePathID1 = captureImageID1?.getRealPathFromURI(tempUri, activity).toString()
        Log.d("FragmentAdd", "ActualImagePath : ${actualImagePathID1}")

        context?.let { Picasso.with(it).load(tempUri).into(binding.imgIDPic1) }

//        activity?.let {
//            Glide.with(it)
//                .load(actualImagePathID1)
//                .into(binding.imgIDPic1)
//        }
    }

    private fun captureImageID2(path: String){
        Log.d("FragmentAdd", "imagePath : $path")
        getImagePathID2(captureImageID2?.decodeFile(path))
    }

    private fun getImagePathID2(bitmap: Bitmap?){
        val tempUri: Uri? = captureImageID2?.getImageUri(activity, bitmap)
        actualImagePathID2 = captureImageID2?.getRealPathFromURI(tempUri, activity).toString()
        Log.d("FragmentAdd", "ActualImagePath : ${actualImagePathID2}")

        context?.let { Picasso.with(it).load(tempUri).into(binding.imgIDPic2) }

//        activity?.let {
//            Glide.with(it)
//                .load(actualImagePathID2)
//                .into(binding.imgIDPic2)
//        }
    }

//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
//            captureImage(captureImage?.getRightAngleImage(captureImage?.imagePath).toString())
////            captureImageID1(captureImageID1?.getRightAngleImage(captureImageID1?.imagePath).toString())
////            captureImageID2(captureImageID2?.getRightAngleImage(captureImageID2?.imagePath).toString())
//        } else if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
//            captureImage(
//                captureImage?.getRightAngleImage(
//                    captureImage?.getPath(
//                        data?.data,
//                        context
//                    )
//                ).toString()
//            )
////            captureImageID1(captureImageID1?.getRightAngleImage(captureImageID1?.getPath(data?.data, context)).toString())
////            captureImageID2(captureImageID2?.getRightAngleImage(captureImageID2?.getPath(data?.data, context)).toString())
//        }
//    }

    private fun validate():Boolean{
        if(binding.edtFirstName.text.toString().trim().isEmpty()){
            showToast("Enter First Name")
            return false
//        } else if(binding.edtLastName.text.toString().trim().isEmpty()){
//            showToast("Enter Last Name")
//            return false
//        } else if(binding.edtAge.text.toString().trim().isEmpty()){
//            showToast("Enter Age")
//            return false
        }else if(binding.edtMobile.text.toString().trim().isEmpty()){
            showToast("Enter Mobile Number")
            return false
        }
        return true
    }

    private fun saveData(){
        try {
            var myIncrementId = ""
            if(ID.trim().isEmpty()){
                myIncrementId = getIncrementedId()
            } else {
                myIncrementId = ID
            }
//            val sqlQuery = "INSERT OR REPLACE INTO MEMBER(ID, FIRST_NAME, LAST_NAME, GENDER, AGE, WEIGHT, MOBILE, ADDRESS, DATE_OF_JOINING, MEMBERSHIP, EXPIRE_ON, DISCOUNT, TOTAL, IMAGE_PATH, STATUS) VALUES " +
//                    "('"+myIncrementId+"',"+DatabaseUtils.sqlEscapeString(binding.edtFirstName.text.toString().trim())+"," +
//                    ""+DatabaseUtils.sqlEscapeString(binding.edtLastName.text.toString().trim())+", '"+gender+"', " +
//                    "'"+binding.edtAge.text.toString().trim()+"', '"+binding.edtWeight.text.toString().trim()+"'," +
//                    "'"+binding.edtMobile.text.toString().trim()+"',"+DatabaseUtils.sqlEscapeString(binding.edtAddress.text.toString().trim())+"," +
//                    "'"+MyFunction.returnSQLdateFormat(binding.edtJoining.text.toString().trim())+"', '"+binding.spMembership.selectedItem.toString().trim()+"'," +
//                    "'"+MyFunction.returnSQLdateFormat(binding.edtExpire.text.toString().trim())+"', '"+binding.edtDiscount.text.toString().trim()+"'," +
//                    "'"+binding.edtAmount.text.toString().trim()+"', '"+actualImagePath+"', 'A')"
            val sqlQuery = "INSERT OR REPLACE INTO MEMBER(ID, FIRST_NAME, LAST_NAME, GENDER, AGE, WEIGHT, MOBILE, ADDRESS, DATE_OF_JOINING, EXPIRE_ON, TOTAL, IMAGE_PATH, STATUS, IMAGE_PATH_ID_1, IMAGE_PATH_ID_2, DESCRIPTION) VALUES " +
                    "('"+myIncrementId+"',"+DatabaseUtils.sqlEscapeString(
                binding.edtFirstName.text.toString().trim()
            )+"," +
                    ""+DatabaseUtils.sqlEscapeString(binding.edtLastName.text.toString().trim())+", '"+gender+"', " +
                    "'"+binding.edtAge.text.toString().trim()+"', '"+binding.edtWeight.text.toString().trim()+"'," +
                    "'"+binding.edtMobile.text.toString().trim()+"',"+DatabaseUtils.sqlEscapeString(
                binding.edtAddress.text.toString().trim()
            )+"," +
                    "'"+ MyFunction.returnSQLdateFormat(binding.edtJoining.text.toString().trim())+"'," +
                    "'"+ MyFunction.returnSQLdateFormat(binding.edtExpire.text.toString().trim())+"', " +
                    "'"+binding.edtAmount.text.toString().trim()+"', '"+actualImagePath+"', 'A', '"+actualImagePathID1+"', '"+actualImagePathID2+"', "+DatabaseUtils.sqlEscapeString(
                binding.edtDescription.text.toString().trim()
            )+")"
            db?.executeQuery(sqlQuery)
            showToast("Data Saved Successfully!")
            if(ID.trim().isEmpty()){
                clearData()
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun getIncrementedId():String{
        var incrementId = ""
        try {
            val sqlQuery = "SELECT IFNULL(MAX(ID)+1, '1') AS ID FROM MEMBER"
            db?.fireQuery(sqlQuery)?.use {
                if(it.count>0){
                incrementId = MyFunction.getvalue(it, "ID")}
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
        return incrementId
    }

    private fun clearData(){
        binding.edtFirstName.setText("")
        binding.edtLastName.setText("")
        binding.edtAge.setText("")
        binding.edtWeight.setText("")
        binding.edtMobile.setText("")
        binding.edtAddress.setText("")
        binding.edtJoining.setText("")
        binding.edtExpire.setText("")
        binding.edtAmount.setText("")
        binding.edtDescription.setText("")
        actualImagePathID1=""
        actualImagePathID2=""
        actualImagePath=""

        Glide.with(this)
            .load(R.drawable.boy)
            .into(binding.imgPic)

        Glide.with(this)
            .load(R.drawable.document)
            .into(binding.imgIDPic1)

        Glide.with(this)
            .load(R.drawable.document)
            .into(binding.imgIDPic2)

    }


    @SuppressLint("SimpleDateFormat")
    private fun loadData(){
        try {
            val sqlQuery = "SELECT * FROM MEMBER WHERE ID='$ID'"
            db?.fireQuery(sqlQuery)?.use {
                if(it.count>0){
                    val firstName = MyFunction.getvalue(it, "FIRST_NAME")
                    val lastName = MyFunction.getvalue(it, "LAST_NAME")
                    val age = MyFunction.getvalue(it, "AGE")
                    val gender = MyFunction.getvalue(it, "GENDER")
                    val weight = MyFunction.getvalue(it, "WEIGHT")
                    val mobileNo = MyFunction.getvalue(it, "MOBILE")
                    val address = MyFunction.getvalue(it, "ADDRESS")
                    val dateOfJoin = MyFunction.getvalue(it, "DATE_OF_JOINING")
                    val expiry = MyFunction.getvalue(it, "EXPIRE_ON")
                    val total = MyFunction.getvalue(it, "TOTAL")
                    val description = MyFunction.getvalue(it, "DESCRIPTION")
                    actualImagePath = MyFunction.getvalue(it, "IMAGE_PATH")
                    actualImagePathID1 = MyFunction.getvalue(it, "IMAGE_PATH_ID_1")
                    actualImagePathID2 = MyFunction.getvalue(it, "IMAGE_PATH_ID_2")

                    binding.edtFirstName.setText(firstName)
                    binding.edtLastName.setText(lastName)
                    binding.edtAge.setText(age)
                    binding.edtWeight.setText(weight)
                    binding.edtMobile.setText(mobileNo)
                    binding.edtAddress.setText(address)
                    binding.edtJoining.setText(MyFunction.returnUserdateFormat(dateOfJoin))
                    binding.edtExpire.setText(MyFunction.returnUserdateFormat(expiry))
                    binding.edtDescription.setText(description)

                    if(actualImagePath.isNotEmpty()){
//                        Glide.with(this)
//                            .load(actualImagePath)
//                            .into(binding.imgPic)
                        val uri = Uri.fromFile(File(actualImagePath))
                        context?.let { it1 -> Picasso.with(it1).load(uri).into(binding.imgPic) }
                    }
                    else {
                        if(gender=="Male"){
                            Glide.with(this)
                                .load(R.drawable.boy)
                                .into(binding.imgPic)
                        } else{
                            Glide.with(this)
                                .load(R.drawable.girl)
                                .into(binding.imgPic)
                        }
                    }

                    if(actualImagePathID1.isNotEmpty()){
//                        Glide.with(this)
//                            .load(actualImagePathID1)
//                            .into(binding.imgIDPic1)
                        val uri = Uri.fromFile(File(actualImagePathID1))
                        context?.let { it1 -> Picasso.with(it1).load(uri).into(binding.imgIDPic1) }
                    }

                    else {
                            Glide.with(this)
                                .load(R.drawable.document)
                                .into(binding.imgIDPic1)
                        }

                    if(actualImagePathID2.isNotEmpty()){
//                        Glide.with(this)
//                            .load(actualImagePathID2)
//                            .into(binding.imgIDPic2)
                        val uri = Uri.fromFile(File(actualImagePathID2))
                        context?.let { it1 -> Picasso.with(it1).load(uri).into(binding.imgIDPic2) }
                    }
                    else {
                        Glide.with(this)
                            .load(R.drawable.document)
                            .into(binding.imgIDPic2)
                        }

//                    if(membership.trim().isNotEmpty()){
//                        when(membership){
//                            "1 Month" -> {
//                                binding.spMembership.setSelection(1)
//                            }
//                            "3 Month" -> {
//                                binding.spMembership.setSelection(2)
//                            }
//                            else -> {
//                                binding.spMembership.setSelection(0)
//                            }
//                        }
//                    }

                    if (gender == "Male"){
                        binding.radioGroup.check(R.id.rdMale)
                    } else {
                        binding.radioGroup.check(R.id.rdFemale)
                    }


                    binding.edtAmount.setText(total)
//                    binding.edtDiscount.setText(discount)
//
//                    val sdf = SimpleDateFormat("yyyy-MM-dd")
//                    val eDate = sdf.parse(expiry)
//                    if(eDate!!.after(Date())){  // expiry date greater than current date
//                        binding.btnRenewalSave.visibility=View.GONE
//                    } else {
//                        if(getStatus() == "A"){
//                            binding.btnRenewalSave.visibility=View.VISIBLE
//                        } else {
//                            binding.btnRenewalSave.visibility=View.GONE
//                        }
//                    }
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun deleteEntry(){
        try {
            val sqlQuery = "DELETE FROM MEMBER WHERE ID='$ID'"
            db?.executeQuery(sqlQuery)
            showToast("Member deleted successfully!")
            clearData()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

//    @SuppressLint("UseRequireInsteadOfGet")
//    private fun openRenewableDialog(){
//        bindingDialog = RenewDialogBinding.inflate(LayoutInflater.from(activity))
//        val dialog = Dialog(activity!!, R.style.AlertCustomDialog)
//        dialog.setContentView(bindingDialog.root)
//        dialog.setCancelable(false)
//        dialog.show()
//
//        bindingDialog.edtDialogJoining.setText(binding.edtExpire.text.toString().trim())
//
//        bindingDialog.imgDialogRenewBack.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        val cal = Calendar.getInstance()
//        val dateSetListener = DatePickerDialog.OnDateSetListener{view1, year, monthOfYear, dayOfMonth ->
//            cal.set(Calendar.YEAR, year)
//            cal.set(Calendar.MONTH, monthOfYear)
//            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//            val myFormat = "dd/MM/yyyy"
//            val sdf = SimpleDateFormat(myFormat, Locale.US)
//            bindingDialog.edtDialogJoining.setText(sdf.format(cal.time))
//
//        }
//
//        bindingDialog.imgDialogPicDate.setOnClickListener {
//        binding.imgPicDate.setOnClickListener {
//            activity?.let { it1 -> DatePickerDialog(it1, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
//        }
//    }
//
//        bindingDialog.spDialogMembership.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val value = bindingDialog.spDialogMembership.selectedItem.toString().trim()
//
//                if(value == "Select"){
//                    bindingDialog.edtDialogExpire.setText("")
//                    calculateTotal(bindingDialog.spDialogMembership, bindingDialog.edtDialogDiscount, bindingDialog.edtDialogAmount)
//                }else{
//                    if (bindingDialog.edtDialogJoining.text.toString().trim().isNotEmpty()){
//                        if(value == "1 Month"){
//                            calculateExpireDate(1, bindingDialog.edtDialogJoining, bindingDialog.edtDialogExpire)
//                            calculateTotal(bindingDialog.spDialogMembership, bindingDialog.edtDialogDiscount, bindingDialog.edtDialogAmount)
//                        } else if (value == "3 Month"){
//                            calculateExpireDate(3, bindingDialog.edtDialogJoining, bindingDialog.edtDialogExpire)
//                            calculateTotal(bindingDialog.spDialogMembership, bindingDialog.edtDialogDiscount, bindingDialog.edtDialogAmount)
//                        }
//
//                    }else{
//                        showToast("Select Joining Date first!")
//                        bindingDialog.spDialogMembership.setSelection(0)
//                    }
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//
//        }
//
//        bindingDialog.edtDialogDiscount.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if(s!=null){
//                    calculateTotal(bindingDialog.spDialogMembership, bindingDialog.edtDialogDiscount, bindingDialog.edtDialogAmount)
//                }
//            }
//
//        })
//
//        bindingDialog.btnDialogRenewSave.setOnClickListener {
//            if(bindingDialog.spDialogMembership.selectedItem.toString().trim() != "Select"){
//                try {
//                    val sqlQuery = "UPDATE MEMBER SET DATE_OF_JOINING='"+MyFunction.returnSQLdateFormat(bindingDialog.edtDialogJoining.text.toString().trim())+"'," +
//                            "MEMBERSHIP='"+bindingDialog.spDialogMembership.selectedItem.toString().trim()+"'," +
//                            "EXPIRE_ON='"+MyFunction.returnSQLdateFormat(bindingDialog.edtDialogExpire.text.toString().trim())+"'," +
//                            "DISCOUNT='"+bindingDialog.edtDialogDiscount.text.toString().trim()+"'," +
//                            "TOTAL='"+bindingDialog.edtDialogAmount.text.toString().trim()+"' WHERE ID= '"+ID+"'"
//                    db?.executeQuery(sqlQuery)
//                    showToast("Members data saved successfully")
//                    dialog.dismiss()
//                    loadData()
//                } catch (e:Exception){
//                    e.printStackTrace()
//                }
//            } else {
//                showToast("Select Membership")
//            }
//        }
//}

//    //import android.provider.<span id="IL_AD11" class="IL_AD">MediaStore</span>;
//
//    //import android.provider.<span id="IL_AD11" class="IL_AD">MediaStore</span>;
//    @SuppressLint("NewApi")
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    object ImageFilePath {
//        /**
//         * Method for return file path of Gallery image
//         *
//         * @param context
//         * @param uri
//         * @return path of the selected image file from gallery
//         */
//        var nopath = "Select Video Only"
//        @TargetApi(Build.VERSION_CODES.KITKAT)
//        @SuppressLint("NewApi")
//        fun getPath(context: Context, uri: Uri): String? {
//
//            // check here to KITKAT or new version
//            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//
//            // DocumentProvider
//            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//
//                // ExternalStorageProvider
//                if (isExternalStorageDocument(uri)) {
//                    val docId = DocumentsContract.getDocumentId(uri)
//                    val split = docId.split(":").toTypedArray()
//                    val type = split[0]
//                    if ("primary".equals(type, ignoreCase = true)) {
//                        return (Environment.getExternalStorageDirectory().toString() + "/"
//                                + split[1])
//                    }
//                } else if (isDownloadsDocument(uri)) {
//                    val id = DocumentsContract.getDocumentId(uri)
//                    val contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"),
//                        java.lang.Long.valueOf(id)
//                    )
//                    return getDataColumn(context, contentUri, null, null)
//                } else if (isMediaDocument(uri)) {
//                    val docId = DocumentsContract.getDocumentId(uri)
//                    val split = docId.split(":").toTypedArray()
//                    val type = split[0]
//                    var contentUri: Uri? = null
//                    if ("image" == type) {
//                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                    } else if ("video" == type) {
//                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                    } else if ("audio" == type) {
//                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//                    }
//                    val selection = "_id=?"
//                    val selectionArgs = arrayOf(split[1])
//                    return contentUri?.let {
//                        getDataColumn(
//                            context, it, selection,
//                            selectionArgs
//                        )
//                    }
//                }
//            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
//
//                // Return the remote address
//                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
//                    context,
//                    uri,
//                    null,
//                    null
//                )
//            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//                return uri.path
//            }
//            return nopath
//        }
//
//        /**
//         * Get the value of the data column for this Uri. This is <span id="IL_AD2" class="IL_AD">useful</span> for MediaStore Uris, and other file-based
//         * ContentProviders.
//         *
//         * @param context
//         * The context.
//         * @param uri
//         * The Uri to query.
//         * @param selection
//         * (Optional) Filter used in the query.
//         * @param selectionArgs
//         * (Optional) Selection arguments used in the query.
//         * @return The value of the _data column, which is typically a file path.
//         */
//        fun getDataColumn(
//            context: Context, uri: Uri?,
//            selection: String?, selectionArgs: Array<String>?
//        ): String {
//            var cursor: Cursor? = null
//            val column = "_data"
//            val projection = arrayOf(column)
//            try {
//                cursor = uri?.let {
//                    context.contentResolver.query(
//                        it, projection,
//                        selection, selectionArgs, null
//                    )
//                }
//                if (cursor != null && cursor.moveToFirst()) {
//                    val index: Int = cursor.getColumnIndexOrThrow(column)
//                    return cursor.getString(index)
//                }
//            } finally {
//                if (cursor != null) cursor.close()
//            }
//            return nopath
//        }
//
//        /**
//         * @param uri
//         * The Uri to check.
//         * @return Whether the Uri authority is ExternalStorageProvider.
//         */
//        fun isExternalStorageDocument(uri: Uri): Boolean {
//            return "com.android.externalstorage.documents" == uri
//                .authority
//        }
//
//        /**
//         * @param uri
//         * The Uri to check.
//         * @return Whether the Uri authority is DownloadsProvider.
//         */
//        fun isDownloadsDocument(uri: Uri): Boolean {
//            return "com.android.providers.downloads.documents" == uri
//                .authority
//        }
//
//        /**
//         * @param uri
//         * The Uri to check.
//         * @return Whether the Uri authority is MediaProvider.
//         */
//        fun isMediaDocument(uri: Uri): Boolean {
//            return "com.android.providers.media.documents" == uri
//                .authority
//        }
//
//        /**
//         * @param uri
//         * The Uri to check.
//         * @return Whether the Uri authority is Google Photos.
//         */
//        fun isGooglePhotosUri(uri: Uri): Boolean {
//            return "com.google.android.apps.photos.content" == uri
//                .authority
//        }
//    }

}
