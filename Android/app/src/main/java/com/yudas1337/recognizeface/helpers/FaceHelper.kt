package com.yudas1337.recognizeface.helpers

import com.yudas1337.recognizeface.constants.FaceFolder
import java.io.File

class FaceHelper {
    companion object{

        private fun deleteFaceDirectory(facesDir: File){
            facesDir.delete()
        }

        private fun deleteProfileDirectory(profilesDir: File){
            profilesDir.delete()
        }

        fun initProfileDirectory(): Unit{
            val profilesDir = FaceFolder.profileDir

            if(!profilesDir.exists() && !profilesDir.isDirectory) profilesDir.mkdir()
        }

        fun initAttendanceFaceDirectory(): Unit {
            val facesDir = FaceFolder.facesDir

            deleteFaceDirectory(facesDir)

            if(!facesDir.exists() && !facesDir.isDirectory){
                facesDir.mkdir()
            }

            val roleDir = arrayOf(FaceFolder.EMPLOYEE_DIR_FACES_NAME, FaceFolder.STUDENTS_DIR_FACES_NAME)

            for(role in roleDir){
                val tmp = File(facesDir, role)
                if(!tmp.exists() && !tmp.isDirectory) tmp.mkdir()
            }
        }

    }

}