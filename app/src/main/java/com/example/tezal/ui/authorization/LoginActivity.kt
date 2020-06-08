package com.example.tezal.ui.authorization

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tezal.MainActivity
import com.example.tezal.R
import com.example.tezal.model.Client
import com.example.tezal.model.ClientId
import com.example.tezal.model.ClientResponseAfterSetPassword
import com.example.tezal.model.JwtResponse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_registration.*
import kotlinx.android.synthetic.main.dialog_registration.view.*
import kotlinx.android.synthetic.main.dialog_set_password.*


class LoginActivity() : AppCompatActivity(), LoginContract.View {


    var username:String? = null;
    var password:String? = null;





    lateinit var presenter: LoginPresenter
    lateinit var  sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences("SP_INFO_CLIENT", Context.MODE_PRIVATE)



        btnLogin.setOnClickListener{
            username = txtUsername.text.toString()
            password = txtPassword.text.toString()
            postQueryForAuthorization()

        }

        btnRegistration.setOnClickListener{

            // inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_registration,null)

            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Форма регистрации")

            // show dialog
            val mAlertDialog = mBuilder.show()

            //login button click of custom layout
            mDialogView.btnRegistrationOk.setOnClickListener {
                Toast.makeText(this,"Добавлено Успешно!!!", Toast.LENGTH_LONG).show()
                val personalCode = mAlertDialog.etLogin.text.toString()
                val lastName = mAlertDialog.etLastName.text.toString()
                val firstName = mAlertDialog.etFirstName.text.toString()
                val patronymic = mAlertDialog.etPatronymic.text.toString()
                val clientSex = mDialogView.spSex.selectedItem.toString()
                val locale = mAlertDialog.etLocal.text.toString()
                val nationality = mAlertDialog.etNationality.text.toString()

                presenter = LoginPresenter(this)
                presenter.setDateUserRegistration(personalCode,lastName,firstName,patronymic,clientSex,locale,nationality)
                mAlertDialog.dismiss()
                setPasswordWindow()
            }

            mDialogView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        val getId = sharedPreferences.getLong("clientId",0)
        println("----------------------------------------")
        println("esesesesesesesesese"+getId)
        println("----------------------------------------")
    }

    private fun postQueryForAuthorization() {
        presenter = LoginPresenter(this)
        presenter.setDateUser(username,password)
    }

    fun setPasswordWindow(){
        // inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_set_password,null)

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Форма регистрации")

        // show dialog
        val mAlertDialog = mBuilder.show()
        mAlertDialog.btnRegistrationSetPassword.setOnClickListener {
            Toast.makeText(this,"Добавлено Успешно!!!", Toast.LENGTH_LONG).show()
            val password = mAlertDialog.etPassword.text.toString()
            val phoneNumber = mAlertDialog.etPhone.text.toString()
            presenter = LoginPresenter(this)

            val getId = sharedPreferences.getLong("id",0)
            val id : ClientId = ClientId()
            id.id= getId

            presenter.setDateUserSetPassword(id,phoneNumber,password,"imei",true,"2020-10-20")
            mAlertDialog.dismiss()
        }
    }


    override fun onSuccessesGet(obj: JwtResponse) {

        val editor = sharedPreferences.edit()
        editor.putLong("clientId", obj.clientId!!)
        editor.putLong("deviceId",obj.deviceId!!)
        editor.putString("token",obj.token!!)
        editor.apply()

        Toast.makeText(this,"Добавлено Успешно!!!", Toast.LENGTH_LONG).show()
        permissionNextActivity()
    }

    override fun onSuccessesGetClientDetails(obj: Client) {
        //add shared Preferences
         //id = obj.id
        val editor = sharedPreferences.edit()
        editor.putLong("id", obj.id)
        editor.apply()
        System.out.println("----------------------------------------")
        System.out.println(obj.id)
        System.out.println("----------------------------------------")
        Toast.makeText(this,"Добавлено Успешно!!!", Toast.LENGTH_LONG).show()
    }

    override fun onSuccessesGetClientInformation(obj: ClientResponseAfterSetPassword) {
        System.out.println("----------------------------------------")
        System.out.println(obj)
        System.out.println("----------------------------------------")
        Toast.makeText(this,"Добавлено Успешно!!!", Toast.LENGTH_LONG).show()
        password = obj.password
        username = obj.phoneNumber
        postQueryForAuthorization()
        // permissionNextActivity()

    }

    override fun onFailure() {
        Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
    }

    fun permissionNextActivity(){
        val intent=Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



}