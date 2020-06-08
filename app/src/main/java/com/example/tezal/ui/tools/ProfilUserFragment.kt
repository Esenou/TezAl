package com.example.tezal.ui.tools

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.tezal.R
import com.example.tezal.model.ClientDevice
import com.example.tezal.model.ClientInfo
import com.example.tezal.model.ClientResponseAfterSetPassword
import kotlinx.android.synthetic.main.fragment_tools.*

class ProfilUserFragment : Fragment(),ProfilUserContract.View {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var presenter: ProfilUserPresenter
    var getId:Long = 0
    var IdClient:Int = 0
    //private var lastName: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_tools, container, false)
        val btnChane: Button = root.findViewById(R.id.btnChaneProfile) as Button
        btnChane.setOnClickListener {
            Toast.makeText(context!!,"Success chane profile",Toast.LENGTH_SHORT).show()
            val lastName = edtLastNameProfile.text.toString()
            val firstName = edtFirstNameProfile.text.toString()
            val patronymic = edtPatronymicProfile.text.toString()
            val clientSex = spSexProfile.selectedItem.toString()
            val local = edtLastNameProfile.text.toString()
            val nationality = edtNationalityProfile.text.toString()
            val client = ClientInfo("",lastName,firstName,patronymic,clientSex,local,nationality)
            init2(client)

            val password = edtPasswordProfile.text.toString()
            val phoneNumber = edtPhoneProfile.text.toString()
            val clientPassword = ClientDevice(getId.toInt(),"","",password,phoneNumber,true)
            println("id :"+ IdClient)
            println("password :"+password)
            println("phoneNumber :"+ phoneNumber)
            init3(IdClient,clientPassword)
        }

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         sharedPreferences = this.activity!!.getSharedPreferences("SP_INFO_CLIENT", Context.MODE_PRIVATE)
         getId = sharedPreferences?.getLong("clientId",0)
        init(getId)

    }
    fun init(id:Long){
        presenter = ProfilUserPresenter(this)
        presenter.setDateUserId(id)

    }
    fun init2(obj:ClientInfo){
        presenter = ProfilUserPresenter(this)
        presenter.setDateUserIdForClient(getId,obj)
    }
    fun init3(obj1: Int, obj: ClientDevice){
        presenter = ProfilUserPresenter(this)
        presenter.setDateUserPassword(obj1.toLong(),obj)
    }

    override fun onSuccessesGet(obj: ClientResponseAfterSetPassword) {
        Glide.with(this).load(obj.client.image).into(imgUserProfile)
       // Picasso.get().load("http://webrand.kg/admin/image/webimg/" + obj.client.image).into(imgUserProfile)
        txtNameProfile!!.setText(obj.client.firstName + " " + obj.client.lastName)
        edtFirstNameProfile!!.setText(obj.client.firstName)
        edtLastNameProfile!!.setText(obj.client.lastName)
        edtPatronymicProfile!!.setText(obj.client.patronymic)
        edtNationalityProfile!!.setText(obj.client.nationality)
        edtLocaleProfile!!.setText(obj.client.locale)
        edtPasswordProfile!!.setText(obj.password)
        edtPhoneProfile!!.setText(obj.phoneNumber)
        IdClient = obj.id



      //  Toast.makeText(context,"Yes "+ obj.id ,Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessesGetClientInfo(obj: ClientResponseAfterSetPassword) {

        Toast.makeText(context!!,"Success chane profile",Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessesGetClientChanePassword(obj: ClientResponseAfterSetPassword) {
        Toast.makeText(context!!,"Success chane password",Toast.LENGTH_SHORT).show()
    }

    override fun onFailure() {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}