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
            val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
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

            dialog.setCancelable(false)
            dialog.show()
        }

        fun doSync(context: Context, confirmClickListener: () -> Unit){
            val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
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

            dialog.setCancelable(false)
            dialog.show()
        }

        fun doSyncWithPictures(context: Context, confirmClickListener: () -> Unit, cancelClickListener: () -> Unit, confirmText: String, cancelText: String){
            val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Lakukan Sinkronisasi?")
                .setContentText("Pilih Jenis Sinkronisasi")
                .setConfirmText(confirmText)
                .setCancelText(cancelText)
                .setConfirmClickListener { sDialog ->
                    confirmClickListener.invoke()
                    sDialog.dismissWithAnimation()
                }
                .setCancelClickListener { sDialog ->
                    cancelClickListener.invoke()
                    sDialog.cancel()
                }
            dialog.show()
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

        fun errorDialog(context: Context, titleText: String = "Gagal", contentText: String) {
            SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .show()
        }

        fun errorDialogWithButton(context: Context, titleText: String, contentText: String, positiveButton: String = "OK", negativeButton: String = "Batal", confirmClickListener: () -> Unit, cancelClickListener: () -> Unit){
            val dialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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

            dialog.setCancelable(false)
            dialog.show()
        }
        fun warningDialogWithButton(context: Context, titleText: String, contentText: String, positiveButton: String = "OK", negativeButton: String = "Batal", confirmClickListener: () -> Unit, cancelClickListener: () -> Unit){
           val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
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

            dialog.setCancelable(false)
            dialog.show()
        }

        fun runVoiceAndToast(voiceHelper: VoiceHelper, context: Context, text: String){
            voiceHelper.runVoice(text)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

}