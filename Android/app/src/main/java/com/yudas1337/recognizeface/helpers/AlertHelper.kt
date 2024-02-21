package com.yudas1337.recognizeface.helpers

import android.content.Context
import android.graphics.Color
import android.widget.Toast
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

        fun internetAvailable(context: Context, confirmClickListener: () -> Unit, cancelClickListener: () -> Unit) {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Terdeteksi Internet")
                .setContentText("Ingin melakukan sinkronisasi?")
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

        fun doSync(context: Context, confirmClickListener: () -> Unit){
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Lakukan Sinkronisasi?")
                .setContentText("Klik Ok untuk menjalankan")
                .setConfirmText("Ok")
                .setCancelText("Batal")
                .setConfirmClickListener { sDialog ->
                    confirmClickListener.invoke()
                    sDialog.dismissWithAnimation()
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
        }

        fun progressDialog(context: Context, titleText: String): SweetAlertDialog {
            val pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = titleText
            pDialog.setCancelable(false)

            return pDialog
        }

        fun successDialog(context: Context, titleText: String = "Berhasil", contentText: String) {
            SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .show()
        }

        fun errorDialogWithButton(context: Context, titleText: String, contentText: String, positiveButton: String = "OK", negativeButton: String = "Batal", confirmClickListener: () -> Unit, cancelClickListener: () -> Unit){
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .setConfirmText(positiveButton)
                .setCancelText(negativeButton)
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

        fun selectDirectoryDialog(context: Context, titleText: String, contentText: String, confirmText: String, confirmClickListener: () -> Unit){
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .setConfirmText(confirmText)
                .showCancelButton(false)
                .setConfirmClickListener { sDialog ->
                    confirmClickListener.invoke()
                    sDialog.dismissWithAnimation()
                }
                .show()
        }

        fun warningDialogWithButton(context: Context, titleText: String, contentText: String, positiveButton: String = "OK", negativeButton: String = "Batal", confirmClickListener: () -> Unit, cancelClickListener: () -> Unit){
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .setConfirmText(positiveButton)
                .setCancelText(negativeButton)
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


        fun runVoiceAndToast(voiceHelper: VoiceHelper, context: Context, text: String){
            voiceHelper.runVoice(text)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

}