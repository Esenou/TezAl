package com.example.tezal.ui.authorization

import com.example.tezal.model.*

interface LoginContract {
    interface View{
        fun onSuccessesGet(obj: JwtResponse)
        fun onSuccessesGetClientDetails(obj: Client)
        fun onSuccessesGetClientInformation(obj: ClientResponseAfterSetPassword)
        fun onFailure();
    }

    interface Presenter{
        fun setDateUser(username:String?,password:String?)
        fun setDateUserRegistration(personalCode:String?,lastName:String?, firstName:String?,patronymic:String?,clientSex:String?,locale:String?,nationality:String?)
        fun setDateUserSetPassword(
            client: ClientId?,
            phoneNumber:String?, password:String?, imei:String?, status:Boolean?, lastEnterDate: String?
        )
    }
}