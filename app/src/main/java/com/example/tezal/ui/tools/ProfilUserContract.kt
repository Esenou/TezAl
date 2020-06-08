package com.example.tezal.ui.tools

import app.superesenou.ru.example_neobis_translate.main.utils.IProgressBar
import com.example.tezal.model.Client
import com.example.tezal.model.ClientDevice
import com.example.tezal.model.ClientInfo
import com.example.tezal.model.ClientResponseAfterSetPassword

interface ProfilUserContract  {
    interface View: IProgressBar {
        fun onSuccessesGet(obj: ClientResponseAfterSetPassword)
        fun onSuccessesGetClientInfo(obj: ClientResponseAfterSetPassword)
        fun onSuccessesGetClientChanePassword(obj: ClientResponseAfterSetPassword)

        fun onFailure();
    }

    interface Presenter{
        fun setDateUserId(id: Long)
        fun setDateUserIdForClient(id: Long,obj:ClientInfo)
        fun setDateUserPassword(id: Long,obj:ClientDevice)


    }
}