package com.yudas1337.recognizeface.helpers

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog

class AlertHelper {

    companion object{
        fun internetNotAvailable(context: Context){
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Internet tidak tersedia")
                .setContentText("Sinkronisasi dapat dijalankan jika internet tersedia")
                .setConfirmText("Ok")
                .show()
        }

        fun serializedFaces(context: Context, confirmClickListener: () -> Unit, cancelClickListener: () -> Unit){
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Serialized Data")
                .setContentText("Existing image data was found on this device. Would you like to load it?")
                .setConfirmText("RESCAN")
                .setCancelText("LOAD")
                .setConfirmClickListener { sDialog ->
                    confirmClickListener.invoke()
                    sDialog.dismissWithAnimation()
                }
                .setCancelClickListener { sDialog ->
                    cancelClickListener.invoke()
                    sDialog.cancel()
                }
                .show()
        }

        fun internetAvailable(context: Context, confirmClickListener: () -> Unit, cancelClickListener: () -> Unit) {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Terdeteksi Internet")
                .setContentText("Apa anda ingin melakukan sinkronisasi?")
                .setConfirmText("Sinkronisasi")
                .setCancelText("Batal")
                .setConfirmClickListener { sDialog ->
                    confirmClickListener.invoke()
                    sDialog.dismissWithAnimation()
                }
                .setCancelClickListener { sDialog ->
                    cancelClickListener.invoke()
                    sDialog.cancel()
                }
                .show()
        }
    }

}