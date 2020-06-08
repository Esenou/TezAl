package com.example.tezal.ui.authorization

import com.example.tezal.StartApplication
import com.example.tezal.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(val view:LoginContract.View):LoginContract.Presenter {

    override fun setDateUser(username: String?, password: String?) {
        val json = AuthModel(username,password)

        StartApplication.service.setDate(json).enqueue(
            object : Callback<JwtResponse> {
                override fun onFailure(call: Call<JwtResponse>?, t: Throwable?) {
                    t?.printStackTrace()
                }

                override fun onResponse(call: Call<JwtResponse>?, response: Response<JwtResponse>?) {

                    System.out.println("-------------------------------------------------")
                    System.out.println(response)
                    System.out.println("-------------------------------------------------")
                    if (response!!.isSuccessful && response != null) {
                        view.onSuccessesGet(response?.body()!!)
                    } else
                        view.onFailure()
                }

            }
        )
    }

    override fun setDateUserRegistration(
        personalCode: String?,
        lastName: String?,
        firstName: String?,
        patronymic: String?,
        clientSex: String?,
        locale: String?,
        nationality: String?
    ) {
        val json = ClientInfo(personalCode,lastName,firstName,patronymic,clientSex,locale,nationality)
        StartApplication.service.setDate(json).enqueue(
            object : Callback<Client> {
                override fun onFailure(call: Call<Client>?, t: Throwable?) {
                    t?.printStackTrace()
                }

                override fun onResponse(call: Call<Client>?, response: Response<Client>?) {

                    System.out.println("-------------------------------------------------")
                    System.out.println(response)
                    System.out.println("-------------------------------------------------")
                    if (response!!.isSuccessful && response != null) {
                        view.onSuccessesGetClientDetails(response?.body()!!)
                    } else
                        view.onFailure()
                }

            }
        )
    }

    override fun setDateUserSetPassword(
        client: ClientId?,
        phoneNumber: String?,
        password: String?,
        imei: String?,
        status: Boolean?,
        lastEnterDate: String?
    ) {
        val json = ClientSetPassword(client,imei,lastEnterDate,phoneNumber,password,status)
        System.out.println("-------------------------------------------------")
        System.out.println(client)
        System.out.println("-------------------------------------------------")
        StartApplication.service.setDatePassword(json).enqueue(
            object : Callback<ClientResponseAfterSetPassword> {
                override fun onFailure(call: Call<ClientResponseAfterSetPassword>?, t: Throwable?) {
                    t?.printStackTrace()
                }

                override fun onResponse(call: Call<ClientResponseAfterSetPassword>?, response: Response<ClientResponseAfterSetPassword>?) {

                    System.out.println("-------------------------------------------------")
                    System.out.println(response)
                    System.out.println("-------------------------------------------------")
                    if (response!!.isSuccessful && response != null) {
                        view.onSuccessesGetClientInformation(response?.body()!!)
                    } else
                        view.onFailure()
                }

            }
        )
    }
}